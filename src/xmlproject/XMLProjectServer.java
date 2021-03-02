package xmlproject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
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
import person.Person;
import static xmlproject.XMLReaderDOM.getList;

/**
 *
 * @author Eduardo Montoya
 */
public class XMLProjectServer {

    
    public static void main(String[] args) throws SocketException, IOException, TransformerConfigurationException, TransformerException{
       
        DatagramSocket udpSocket = new DatagramSocket(1001);
        byte buffer[] = new byte[1024];
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("Waiting for client to send file...");
        udpSocket.receive(packet);
        
        buffer = packet.getData();
        InetAddress direccion = packet.getAddress();
        int puerto = packet.getPort();
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
                        
        //Convert the String to an xml document
        Document doc = convertStringToDocument(data);
        
        //Path where the file is saved
        String filePath = "..\\XMLProject\\src\\files\\ServerReceived.xml";
        
        //Save the file
        crearXML(doc, filePath);
        
        System.out.println("File received");
        System.out.println("Processing File");
        
        //Get a list of people from the XML file
        List<Person> people = getList(filePath);
        
        data = "<person>\n";
        
        //The list of people is calculated
        for (Person person : people) {
            calcularIBM(person);
            data = data+person.toString()+"\n";
        }
        
        data = data+"</person>";
        
        //Convert the String to an xml document
        doc = convertStringToDocument(data);
        
        //Path where the file is saved
        filePath = "..\\XMLProject\\src\\files\\ServerSend.xml";
        
        //Save the file
        crearXML(doc, filePath);
        
        System.out.println("Sending File back");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File myFile = new File(filePath); 
        out.write(Files.readAllBytes(myFile.toPath()));
        
        byte buffer2[] = out.toByteArray();
        DatagramPacket packet2 = 
                new DatagramPacket(buffer2, buffer2.length, direccion, puerto);
                
        udpSocket.send(packet2);
        
        
        
    }       
    
    public static Document convertStringToDocument(String xmlStr) {
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
    
    public static void crearXML(Document document, String url ) throws TransformerConfigurationException, TransformerException
    {
       TransformerFactory transformerFactory = TransformerFactory.newInstance();
       Transformer transformer = transformerFactory.newTransformer();
       DOMSource domSource = new DOMSource(document);
       StreamResult streamResult = new StreamResult(new File(url));
       transformer.transform(domSource, streamResult);
    }
     
    public static void calcularIBM(Person person)
    {
        person.setBmi((Math.round((person.getWeight()/Math.pow(person.getHeight(), 2))*10.0)/10.0));
                
        if(person.getBmi() < 18.5)
            person.setMeaning("Thin");
        else if (person.getBmi() < 24.9)
            person.setMeaning("Healthy");
        else if (person.getBmi() < 29.9)
            person.setMeaning("Overweight");
        else             
            person.setMeaning("Obese");
    }        
}
