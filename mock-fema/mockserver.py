from fastapi import FastAPI, Request, Response, HTTPException
import uvicorn
import base64
import os

class FemaMockServer:
    
    def __init__(self,port):
        self.port = port
        self.app = FastAPI(tittle = "Fema Mock Server")
        self.pdf = self.pdfToBase64(filename="pdftest.pdf")

        self.ACTIONS_HANDLER= {
           "http://tempuri.org/Criar":self.create,
           "http://tempuri.org/Anular":self.cancel,
           "http://tempuri.org/Etiqueta":self.label,
           "http://tempuri.org/FecharDia": self.endOfTheDay,
           "http://tempuri.org/Servicos":self.listServices}  
        
        self.app.add_api_route("/FemaMock", self.dispatcher, methods=["POST"])
    

    def pdfToBase64(self,filename="PdfTest.pdf"):
        dirPath = os.path.dirname(__file__)
        filePath = os.path.join(dirPath,filename)

        try:
            with open(filePath,"rb") as pdf:
                encodedFile = base64.b64encode(pdf.read()).decode('utf-8')
                return encodedFile
        except FileNotFoundError:
            print(f"File {filePath} not found.")
            raise HTTPException(status_code=404, detail="PDF file missing")


    def listServices(self, body=None):
        xmlResponse = (
            '<?xml version="1.0" encoding="utf-8"?>'
            '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
            'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
            'xmlns:xsd="http://www.w3.org/2001/XMLSchema">'
            '<soap:Body>'
            '<ServicosResponse xmlns="http://tempuri.org/">'
            '<ServicosResult>'
            '<State>Ok</State>'
            '<Servicos>'
            '<Servico><servico>CTT19</servico><nome>Fema Expresso 19 1Vol</nome></Servico>'
            '<Servico><servico>CTT19MV</servico><nome>Fema Expresso 19 Múltiplos</nome></Servico>'
            '</Servicos>'
            '</ServicosResult>'
            '</ServicosResponse>'
            '</soap:Body>'
            '</soap:Envelope>'
        )
        return xmlResponse
        

    def create(self,body = None):
        xmlResponse = (
            '<?xml version="1.0" encoding="utf-8"?>'
            '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">'
            '<soap:Body>'
            '<CriarResponse xmlns="http://tempuri.org/">'
            '<CriarResult>'
            '<State>Ok</State>'
            '<ID>53630ef8-bda6-4d79-b652-dd1ef745455c</ID>'
            '<Numero>17814447</Numero>'
            '<Etiqueta>'
            '<Formato>A4</Formato>'
            f'<base64Label>{self.pdf}</base64Label>'
            '</Etiqueta>'
            '<Etiquetas>'
            '<LabelReturn>'
            '<Formato>A4</Formato>'
            f'<base64Label>{self.pdf}</base64Label>'
            '</LabelReturn>'
            '</Etiquetas>'
            '</CriarResult>'
            '</CriarResponse>'
            '</soap:Body>'
            '</soap:Envelope>'
            )
        return xmlResponse

    
    def endOfTheDay(self,body = None):
        xmlResponse = (
            '<?xml version="1.0" encoding="utf-8"?>'
            '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">'
            '<soap:Body>'
            '<FecharDiaResponse xmlns="http://tempuri.org/">'
            '<FecharDiaResult>'
            '<State>Ok</State>'
            '<Report>'
            '<Formato>A4</Formato>'
            f'<base64Label>{self.pdf}</base64Label>'
            '</Report>'
            '</FecharDiaResult>'
            '</FecharDiaResponse>'
            '</soap:Body>'
            '</soap:Envelope>'
        )
        return xmlResponse

    
    def label(self,body = None):
        xml_response = (
            '<?xml version="1.0" encoding="utf-8"?>'
            '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">'
            '<soap:Body>'
            '<EtiquetaResponse xmlns="http://tempuri.org/">'
            '<EtiquetaResult>'
            '<State>Ok</State>'
            '<ID>00000000-0000-0000-0000-000000000000</ID>'
            '<Numero>17814447</Numero>'
            '<Etiqueta>'
            '<Formato>A4</Formato>'
            f'<base64Label>{self.pdf}</base64Label>'
            '</Etiqueta>'
            '<Etiquetas>'
            '<LabelReturn>'
            '<Formato>A4</Formato>'
            f'<base64Label>{self.pdf}</base64Label>'
            '</LabelReturn>'
            '</Etiquetas>'
            '</EtiquetaResult>'
            '</EtiquetaResponse>'
            '</soap:Body>'
            '</soap:Envelope>'
        )
        return xml_response

    
    def cancel(self, body=None):
        xml_response = (
            '<?xml version="1.0" encoding="utf-8"?>'    
            '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
            'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
            'xmlns:xsd="http://www.w3.org/2001/XMLSchema">'
            '<soap:Body>'
            '<AnularResponse xmlns="http://tempuri.org/">'
            '<AnularResult>'
            '<State>NOK</State>'
            '<Message>A carta de porte não está no estado aberta</Message>'
            '</AnularResult>'
            '</AnularResponse>'
            '</soap:Body>'
            '</soap:Envelope>'
        ) 
        return xml_response


    

    async def dispatcher(self,request: Request):
            
            soapAction = request.headers.get("SOAPAction")
            if not soapAction:
                raise HTTPException(status_code=400, detail="Header not found")

            soapAction = soapAction.strip('"')

            handlerFunction = self.ACTIONS_HANDLER.get(soapAction)

            if handlerFunction:
                body = await request.body()
                result_xml = handlerFunction(body)

                return Response(content = result_xml,media_type="application/xml")
            else:
                print(f"Unknown Action: '{soapAction}'")
                raise HTTPException(status_code=501,detail=f"Action '{soapAction}' not implemented")


server = FemaMockServer(port=9091)
app = server.app   

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=9091)
    