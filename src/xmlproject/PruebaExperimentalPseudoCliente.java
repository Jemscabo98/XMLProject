/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlproject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import static xmlproject.XMLReaderDOM.getLista;
import person.person;
import static xmlproject.XMLProjectServer.calcularIBM;
import static xmlproject.XMLProjectServer.convertStringToDocument;
import static xmlproject.XMLProjectServer.crearXML;

/**
 *
 * @author Eduardo Montoya
 */
public class PruebaExperimentalPseudoCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException, TransformerException {
        DatagramSocket udpSocket = new DatagramSocket();
        // Creates a ByteArrayOutputStream with default size
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        File myFile = new File("..\\XMLProject\\src\\file\\person.xml");  
        
        out.write(Files.readAllBytes(myFile.toPath()));
        
        byte buffer[] = out.toByteArray();
                
        DatagramPacket packet = 
                new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 1001);
        
        udpSocket.send(packet);
        System.out.println("File Send");
        
        System.out.println("Waiting for an answer");
        
        // Corrobora que si llegue el paquete
        
        
        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        
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
        String filePath = "..\\XMLProject\\src\\file\\ClientReceived.xml";
        //Guarda el documento
        crearXML(doc, filePath);
        
        //Obtiene la lista de personas del archivo XML
        List<person> personas = getLista(filePath);
        
        //Se calcula la lista de personas de
        for (person persona : personas) {
            calcularIBM(persona);
            System.out.println(persona.getName());
            System.out.println(persona.getHeight());
            System.out.println(persona.getWeight());
            System.out.println(persona.getBmi());
            System.out.println(persona.getMeaning());
            System.out.println("");
        }
    }
    
}
