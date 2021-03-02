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
import static xmlproject.XMLReaderDOM.getList;
import person.Person;
import static xmlproject.XMLProjectServer.calcularIBM;
import static xmlproject.XMLProjectServer.convertStringToDocument;
import static xmlproject.XMLProjectServer.crearXML;


public class XMLProjectClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException, TransformerException {
        DatagramSocket udpSocket = new DatagramSocket();
        
        // Creates a ByteArrayOutputStream with default size
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        File myFile = new File("..\\XMLProject\\src\\files\\person.xml");  
        
        out.write(Files.readAllBytes(myFile.toPath()));
        
        byte buffer[] = out.toByteArray();
                
        DatagramPacket packet = 
                new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 1001);
        
        udpSocket.send(packet);
        System.out.println("File Send");
        
        System.out.println("Waiting for an answer");
        
        // Check that the package is received
        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        
        udpSocket.receive(packet);
        
        buffer = packet.getData();
        byte[] aux = new String(buffer).trim().getBytes();
        
        ByteArrayInputStream myStream = new ByteArrayInputStream(aux);
        
        //Method to get the String out of the bytes
        int size = myStream.available();
        char[] theChars = new char[size];
        byte[] bytes    = new byte[size];

        myStream.read(bytes, 0, size);
        for (int i = 0; i < size;)
            theChars[i] = (char)(bytes[i++]&0xff);
        
        //Save the chars in a String
        String data = new String(theChars);
        
        //Convert the String to an XML document
        Document doc = convertStringToDocument(data);
        
        //Path where the file is saved
        String filePath = "..\\XMLProject\\src\\files\\ClientReceived.xml";
        
        //Save the file
        crearXML(doc, filePath);
        
        //Gets the list of people from the XML file
        List<Person> people = getList(filePath);
        
        //The list of people is calculated
        for (Person persona : people) {
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