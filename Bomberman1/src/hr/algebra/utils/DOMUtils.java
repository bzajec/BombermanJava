/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import hr.algebra.sprites.SaveSprite;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Bruno
 */
public class DOMUtils {
    
    private static final String FILENAME_MAPA = "mapa.xml";
    
    public static void saveSprites(ObservableList<SaveSprite> saveSprite) {

        try {
            Document document = createDocument("sprites");
            saveSprite.forEach(c -> document.getDocumentElement().appendChild(createSpriteElement(c, document)));
            saveDocument(document, FILENAME_MAPA);
        } catch (ParserConfigurationException | TransformerException e) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static Document createDocument(String root) throws ParserConfigurationException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
        
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //transformer.transform(new DOMSource(document), new StreamResult(System.out));
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
        
    }

    private static Node createSpriteElement(SaveSprite sS, Document document) {
        
        Element element = document.createElement("sloj");
        element.setAttributeNode(createAttribute(document, "layer", sS.getSloj()));
        element.appendChild(createElement(document, "xOs", sS.getxOS()));
        element.appendChild(createElement(document, "yOs", sS.getyOS()));
        //element.appendChild(createDriverElement(sS.getDriver(), document));
        return element;
        
    }

    private static Node createElement(Document document, String yOs, int koordinata) {
        String temp = String.valueOf(koordinata) ;
        Element element = document.createElement(yOs);
        Text text = document.createTextNode(temp);
        element.appendChild(text);
        return element;
        
    }

    private static Attr createAttribute(Document document, String name, int value) {
        String temp = String.valueOf(value);
        Attr attr = document.createAttribute(name);
        attr.setValue(temp);
        return attr;
    }
    
    public static ObservableList<SaveSprite> loadSprites() {
        ObservableList<SaveSprite> sprites = FXCollections.observableArrayList();
        try {
            Document document = createDocument(new File(FILENAME_MAPA));
            NodeList nodes = document.getElementsByTagName("sloj");
            for (int i = 0; i < nodes.getLength(); i++) {
                // dangerous class cast exception
                sprites.add(processSpritesNode((Element) nodes.item(i)));
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return sprites;
    }
    
    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }
    private static SaveSprite processSpritesNode(Element element) {
        return new SaveSprite(
                //processLayerNode((Element) element.getElementsByTagName("driver").item(0)),
                Integer.valueOf(element.getElementsByTagName("yOs").item(0).getTextContent()),
                Integer.valueOf(element.getElementsByTagName("xOs").item(0).getTextContent()),
                Integer.valueOf(element.getAttribute("layer")));
    }
    
    
    
}
