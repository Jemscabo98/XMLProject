/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlproject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import static person.XMLReaderDOM.getLista;
import person.person;

/**
 *
 * @author Eduardo Montoya
 */
public class XMLProjectServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException, TransformerConfigurationException, TransformerException{
       
        DatagramSocket udpSocket = new DatagramSocket(1001);
        byte buffer[] = new byte[1024];
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("Waiting for client to receive...");
        udpSocket.receive(packet);
        
        buffer = packet.getData();
        
        byte[] aux = new String(buffer).trim().getBytes();
        
        ByteArrayInputStream myStream = new ByteArrayInputStream(aux);
        
        //Metodo para sacar el String de los bytes
        int size = myStream.available();
        char[] theChars = new char[size];
        byte[] bytes    = new byte[size];

        myStream.read(bytes, 0, size);
        for (int i = 0; i < size;)
            theChars[i] = (char)(bytes[i++]&0xff);
        
        //Guarda los chars en un String
        String data = new String(theChars);
                
        //Convierte el String en un documento xml
        Document doc = convertStringToDocument(data);
        //Dirección donde se va a guardar
        String filePath = "..\\XMLProject\\src\\file\\personReceive.xml";
        //Guarda el documento
        crearXML(doc, filePath);
        
        //Obtiene la lista de personas del archivo XML
        List<person> personas = getLista(filePath);
        
        //Se calcula la lista de personas de
        for (person persona : personas) {
            calcularIBM(persona);
            System.out.println(persona.toString());
        }
        
        
        
    }       
    
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse(new InputSource(new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    private static void crearXML(Document document, String url ) throws TransformerConfigurationException, TransformerException
    {
       TransformerFactory transformerFactory = TransformerFactory.newInstance();
       Transformer transformer = transformerFactory.newTransformer();
       DOMSource domSource = new DOMSource(document);
       StreamResult streamResult = new StreamResult(new File(url));
       transformer.transform(domSource, streamResult);
    }
     
    private static void calcularIBM(person persona)
    {
        persona.bmi = (Math.round((persona.getWeight()/Math.pow(persona.getHeight(), 2))*10.0)/10.0);
                
        if(persona.bmi < 18.5)
            persona.setMeaning("Thin");
        else if (persona.bmi < 24.9)
            persona.setMeaning("Healthy");
        else if (persona.bmi < 29.9)
            persona.setMeaning("Overweight");
        else             
            persona.setMeaning("Obese");
    }        
}
