package com.casadosreclamos

import com.casadosreclamos.dto.RegisterDto

val EMAIL_REGEX = Regex("^[\\w-.+]+@([\\w-]+\\.)+[\\w-]+$")
val PHONE_REGEX = Regex("^[\\d- +)(]+$")


fun getEmailRegisteredCommercial(credentials: RegisterDto): String {
    return """
            Foi efetuado um registo para o seu email na plataforma de pedidos para a Casa dos Reclamos com os seguintes dados

            Username: ${credentials.email}
            Password: ${credentials.password}

            Aconselha-se a mudança de password na plataforma.


            Este é um email automático, por favor não responda
           """.trimIndent()
}

fun getEmailPasswordRecovery(domain: String, user: String, token: String): String {
    return """
            Foi efetuado um pedido de substituição de password para a conta associada a este email

            Para proceder à alteração da password aceda a https://${domain}/forget?email=${user}&token=${token}


            Este é um email automático, por favor não responda
           """.trimIndent()
}

fun getEmailNewStandardRequest(user: String, client: String, type: String, brand: String): String {
    return """
           Foi efetuado um novo pedido à Casa dos Reclamos por um utilizador.

           Resumo do pedido:

           Comercial: $user
           Cliente: $client
           Marca: $brand
           Tipo de pedido: $type

           Para ver todos os detalhes aceda à plataforma.


           Este email é automático, por favor não responda
          """.trimIndent()
}

fun getEmailAcceptedRequest(user: String, client: String, type: String, brand: String): String {
    return """
            Um pedido foi aceite pela Casa dos Reclamos.

            Resumo do pedido:

            Comercial: $user
            Cliente: $client
            Marca: $brand
            Tipo de pedido: $type

            Para ver todos os detalhes do pedido aceda à plataforma.


            Este email é automático, por favor não responda
           """.trimIndent()
}

fun getEmailCancelledRequest(user: String, client: String, type: String, brand: String, reason: String): String {
    return """
            Um pedido foi cancelado!

            Resumo do pedido:

            Comercial: $user
            Cliente: $client
            Marca: $brand
            Tipo de pedido: $type

            Motivo: $reason

            Para ver todos os detalhes do pedido aceda à plataforma.


            Este email é automático, por favor não responda
           """.trimIndent()
}