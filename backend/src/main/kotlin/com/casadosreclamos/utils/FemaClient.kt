package com.casadosreclamos.utils

import com.casadosreclamos.dto.fema.*
import com.casadosreclamos.exception.fema.*
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import org.jose4j.base64url.Base64
import org.w3c.dom.Node
import java.io.File
import java.time.Instant
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.xml.soap.MessageFactory
import javax.xml.soap.SOAPMessage

@ApplicationScoped
@OptIn(ExperimentalStdlibApi::class)
class FemaClient {
    @Inject
    lateinit var logger: Logger

    @Inject
    @ConfigProperty(name = "fema.username")
    lateinit var authUser: String

    @Inject
    @ConfigProperty(name = "fema.password")
    lateinit var authPass: String

    @Inject
    @RestClient
    lateinit var expeditionsClient: FemaExpeditions

    var xmlBuilder = MessageFactory.newInstance()

    fun getPackageTypes(): Uni<List<PackageType>> {
        return Uni.createFrom().item(
            listOf(
                PackageType("Caixa", "Caixa"),
                PackageType("LTR", "Envelope"),
                PackageType("PLT", "Palete"),
                PackageType("Rolo", "Rolo"),
                PackageType("Saco", "Saco")
            )
        )
    }

    fun getLabelFormats(): Uni<List<LabelFormat>> {
        return Uni.createFrom().item(listOf("A4", "A6"))
    }

    fun getServices(source: Address, destination: Address): Uni<List<Service>> {
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<Servicos xmlns=\"http://tempuri.org/\">" +
                "<request>" +
                "<CodigoPostalOrigem>${source.postalCode}</CodigoPostalOrigem>" +
                "<PaisOrigem>${source.country}</PaisOrigem>" +
                "<CodigoPostalDestino>${destination.postalCode}</CodigoPostalDestino>" +
                "<PaisDestino>${destination.country}</PaisDestino>" +
                "<Utilizador>" +
                "<Username>$authUser</Username>" +
                "<Password>$authPass</Password>" +
                "</Utilizador>" +
                "</request>" +
                "</Servicos>" +
                "</soap:Body>" +
                "</soap:Envelope>"


        return expeditionsClient.getServices(body).onItem().transform { response ->
        val doc: SOAPMessage = xmlBuilder.createMessage(null, response.toByteArray().inputStream())

        val soapBody = doc.soapBody.extractContentAsDocument()

        val state = getXMLElement(soapBody, "ServicosResponse/ServicosResult/State")?.textContent
        val services = getXMLElement(soapBody, "ServicosResponse/ServicosResult/Servicos")?.childNodes

        if (state != "Ok") {
            logger.error("Received state $state from request: $response")

            throw GetServicesException()
        }

        val serviceList: MutableList<Service> = mutableListOf()
        for (i in 0..<services!!.length) {
            val serviceNode = services.item(i)

            val name = getXMLElement(serviceNode, "nome")?.textContent
            val id = getXMLElement(serviceNode, "servico")?.textContent

            if (name == null || id == null) {
                throw GetServicesException()
            }

            serviceList.add(Service(id, name))
        }

        return@transform serviceList
        }
    }

    fun openWaybill(waybill: WaybillDto): Uni<Label> {
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<Criar xmlns=\"http://tempuri.org/\">" +
                "<Pedido>" +
                "<Expedicao>" +
                "<Referencia>${waybill.reference}</Referencia>" +
                "<Servico>${waybill.service!!.id}</Servico>" +
                "<MoradaRemetente>" +
                "<Nome>${waybill.source!!.name}</Nome>" +
                "<Telefone>${waybill.source!!.phone}</Telefone>" +
                "<Morada1>${waybill.source!!.address!!.address}</Morada1>" +
                "<CodigoPostal>${waybill.source!!.address!!.postalCode}</CodigoPostal>" +
                "<Pais>${waybill.source!!.address!!.country}</Pais>" +
                "<Cidade>${waybill.source!!.address!!.city}</Cidade>" +
                "</MoradaRemetente>" +
                "<MoradaDestinatario>" +
                "<Nome>${waybill.destination!!.name}</Nome>" +
                "<Telefone>${waybill.destination!!.phone}</Telefone>" +
                "<Morada1>${waybill.destination!!.address!!.address}</Morada1>" +
                "<CodigoPostal>${waybill.destination!!.address!!.postalCode}</CodigoPostal>" +
                "<Cidade>${waybill.destination!!.address!!.city}</Cidade>" +
                "<Pais>${waybill.destination!!.address!!.country}</Pais>" +
                "</MoradaDestinatario>" +
                "<TipoPacote>${waybill.packaging!!.id}</TipoPacote>" +
                "<DescricaoVolumes>${waybill.description}</DescricaoVolumes>" +
                "<NCaixas>${waybill.items}</NCaixas>" +
                "<PesoTotal>${waybill.totalWeight}</PesoTotal>" +
                "<Comprimento>${waybill.dimensions!!.length}</Comprimento>" +
                "<Largura>${waybill.dimensions.width}</Largura>" +
                "<Altura>${waybill.dimensions.height}</Altura>" +
                "</Expedicao>" +
                "<Etiqueta>" +
                "<Formato>${waybill.labelFormat}</Formato>" +
                "</Etiqueta>" +
                "<Utilizador>" +
                "<Username>$authUser</Username>" +
                "<Password>$authPass</Password>" +
                "</Utilizador>" +
                "</Pedido>" +
                "</Criar>" +
                "</soap:Body>" +
                "</soap:Envelope>"

        return expeditionsClient.openWaybill(body).onItem().transform { response ->
        val doc: SOAPMessage = xmlBuilder.createMessage(null, response.toByteArray().inputStream())

        val soapBody = doc.soapBody.extractContentAsDocument()

        val state = getXMLElement(soapBody, "CriarResponse/CriarResult/State")?.textContent
        val id = getXMLElement(soapBody, "CriarResponse/CriarResult/Numero")?.textContent?.toLongOrNull()
        val pdf = getXMLElement(soapBody, "CriarResponse/CriarResult/Etiqueta/base64Label")?.textContent

        if (state != "Ok") {
            logger.error("Received state $state from request: $response")

            throw OpenWaybillException()
        }

        logger.info("Saving PDF file")
        val file = kotlin.io.path.createTempFile().toFile()
        file.writeBytes(Base64.decode(pdf))

        return@transform Label(id, file)
        }
    }

    fun cancelWaybill(waybillId: Long): Uni<Void> {
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<Anular xmlns=\"http://tempuri.org/\">" +
                "<Pedido>" +
                "<Numero>$waybillId</Numero>" +
                "<Utilizador>" +
                "<Username>$authUser</Username>" +
                "<Password>$authPass</Password>" +
                "</Utilizador>" +
                "</Pedido>" +
                "</Anular>" +
                "</soap:Body>" +
                "</soap:Envelope>"

        return expeditionsClient.cancelWaybill(body).onItem().transformToUni { response ->
        val doc: SOAPMessage = xmlBuilder.createMessage(null, response.toByteArray().inputStream())

        val soapBody = doc.soapBody.extractContentAsDocument()

        val state = getXMLElement(soapBody, "AnularResponse/AnularResult/State")?.textContent

        val message = getXMLElement(soapBody, "AnularResponse/AnularResult/Message")?.textContent

        if (state != "Ok") {
            logger.error("Couldn't cancel waybill: $message")

            throw CancelWaybillException(message!!)
        }

        return@transformToUni Uni.createFrom().voidItem()
        }
    }

    fun getWaybill(waybillId: Long, labelFormat: String): Uni<File> {
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<Etiqueta xmlns=\"http://tempuri.org/\">" +
                "<Etiqueta>" +
                "<Numero>$waybillId</Numero>" +
                "<Etiqueta>" +
                "<Formato>$labelFormat</Formato>" +
                "</Etiqueta>" +
                "<Utilizador>" +
                "<Username>$authUser</Username>" +
                "<Password>$authPass</Password>" +
                "</Utilizador>" +
                "</Etiqueta>" +
                "</Etiqueta>" +
                "</soap:Body>" +
                "</soap:Envelope>"

        return expeditionsClient.getWaybill(body).onItem().transform { response ->
        val doc: SOAPMessage = xmlBuilder.createMessage(null, response.toByteArray().inputStream())

        val soapBody = doc.soapBody.extractContentAsDocument()

        val state = getXMLElement(soapBody, "EtiquetaResponse/EtiquetaResult/State")?.textContent

        val pdf = getXMLElement(soapBody, "EtiquetaResponse/EtiquetaResult/Etiqueta/base64Label")?.textContent

        if (state != "Ok") {
            logger.error("Couldn't get waybill")

            throw GetWaybillException()
        }

        logger.info("Saving PDF file")
        val file = kotlin.io.path.createTempFile().toFile()
        file.writeBytes(Base64.decode(pdf))

        return@transform file
        }
    }

    fun closeDay(): Uni<File> {
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<FecharDia xmlns=\"http://tempuri.org/\">" +
                "<Pedido>" +
                "<Dia>${Instant.now()}</Dia>" +
                "<Utilizador>" +
                "<Username>$authUser</Username>" +
                "<Password>$authPass</Password>" +
                "</Utilizador>" +
                "</Pedido>" +
                "</FecharDia>" +
                "</soap:Body>" +
                "</soap:Envelope>"

        return expeditionsClient.closeDay(body).onItem().transform { response ->
        val doc: SOAPMessage = xmlBuilder.createMessage(null, response.toByteArray().inputStream())

        val soapBody = doc.soapBody.extractContentAsDocument()

        val state = getXMLElement(soapBody, "FecharDiaResponse/FecharDiaResult/State")?.textContent

        val pdf = getXMLElement(soapBody, "FecharDiaResponse/FecharDiaResult/Report/base64Label")?.textContent

        if (state != "Ok") {
            logger.error("Couldn't close day")

            throw CloseDayException()
        }

        logger.info("Saving PDF file")
        val file = kotlin.io.path.createTempFile().toFile()
        file.writeBytes(Base64.decode(pdf))

        return@transform file
        }
    }

    private fun getXMLElement(document: Node, path: String): Node? {
        val split = path.split("/")
        val curElem = split[0]
        val remaining = split.drop(1).joinToString("/")

        return if (curElem.isEmpty()) {
            null
        } else {
            val nodes = document.childNodes

            for (i in 0..<nodes.length) {
                val node = nodes.item(i)
                if (node.nodeName == curElem) {
                    if (remaining.isEmpty()) {
                        return node
                    } else {
                        return getXMLElement(node, remaining)
                    }
                }
            }

            return null
        }
    }
}