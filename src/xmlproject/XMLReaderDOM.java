/*
 * Clase que lee el archivo XML y obtiene los nombres de personas.
 * Gran parte de este codigo esta basado del trabajo del señor Pankaj.
 * https://www.journaldev.com/898/read-xml-file-java-dom-parser#java-dom-parser
 */
package xmlproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import person.person;

/**
 *
 * @author Eduardo Montoya
 */
public class XMLReaderDOM {
    
    /**
     * Gets the list of persons in the XML file
     * @param path Path to the file
     * @return Returns a list of every person in the XML file
     */
    public static List<person> getLista(String path)
    {
        List<person> personas = new ArrayList<person>();
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("person");
            //now XML is loaded as Document in memory, lets convert it to Object List
           
            for (int i = 0; i < nodeList.getLength(); i++) {
                personas.add(getPerson(nodeList.item(i)));
            }
            /*lets print Employee list information
            for (person emp : personas) {
                System.out.println(emp.toString());
            }*/
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }        
        
        return personas;
    }
    
    
    /**
     * Gets the each person out of the XML File
     * @param node Node where each person is ubicate
     * @return 
     */
    private static person getPerson(Node node){
        person per = new person();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            per.setName(getTagValue("name", element));
            per.setHeight(Double.parseDouble(getTagValue("height", element)));
            per.setWeight(Double.parseDouble(getTagValue("weight", element)));
        }

        return per;
    }

    /**
     * Get the tags inside every person
     * @param tag Atribute of the person
     * @param element Where it is ubicate
     * @return return the value of the atribute
     */
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}


