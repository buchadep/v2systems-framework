package uk.co.v2systems.framework.files;

import uk.co.v2systems.framework.utils.KeyValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;


/**
 * Created by I&T Lab User on 20/07/2015.
 */
public class CustomXml {

    static Document doc;
    String inputXmlFile;

//Open a new blank xml file
    public void openXml(){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        }catch (Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
        }
    }
//    Open an existing xml file and parse it
    public int openXml(String inputXmlFile){
        try{
            this.inputXmlFile=inputXmlFile;
            File fXmlFile = new File(inputXmlFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(fXmlFile);
            return 0;//success
        }catch (Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return -1;
        }
    }
// Add new xmlTag tagName, tagAttribute, tagValue and parentXmlTag if multiple tag matching parent tag add after first occurance
    public void addXmlTag(String tagName, List<KeyValuePair> attributes, String tagValue,String strParentXmlTag){
        addXmlTag(tagName,attributes,tagValue, strParentXmlTag,0);
    }
// Add new xmlTag tagName, tagAttribute, tagValue and parentXmlTag if multiple tag matching parent tag add after specified occurance
    public void addXmlTag(String tagName, List<KeyValuePair> attributes, String tagValue, String strParentXmlTag, int occurrence){
        Element parentXmlTag=null;
        NodeList nodeList = doc.getElementsByTagName(strParentXmlTag);
        if(nodeList!=null)
            parentXmlTag = (Element) nodeList.item(occurrence);
        addXmlTag(tagName,attributes,tagValue,parentXmlTag);
    }
// append(Add) new xmlTag tagName, tagAttribute, tagValue and parentXmlTag if multiple tag matching parent tag replace last occurance
    public void appendXmlTag(String tagName, List<KeyValuePair> attributes, String tagValue, String strParentXmlTag){
        Element parentXmlTag=null;
        NodeList nodeList = doc.getElementsByTagName(strParentXmlTag);
        if(nodeList!=null)
            parentXmlTag = (Element) nodeList.item(nodeList.getLength()-1);
        addXmlTag(tagName,attributes,tagValue,parentXmlTag);
    }
// Add xmltag (Main Logic)to existing xml under parent tag, xmlTag, list of tagattribute, tagValue, tagParent
    public void addXmlTag(String tagName, List<KeyValuePair> attributes, String tagValue, Element parentXmlTag){
        try {
            Element xmlElement = doc.createElement(tagName);
            xmlElement.setTextContent(tagValue);
            //initialise xmlElement
            if(attributes!=null){
                for (int i = 0; i < attributes.size(); i++) {
                    if (!(attributes.get(i).getKey().equalsIgnoreCase("") && attributes.get(i).getValue().equalsIgnoreCase("")))
                        xmlElement.setAttribute(attributes.get(i).getKey(), attributes.get(i).getValue());
                }
            }
            if (parentXmlTag != null)
                parentXmlTag.appendChild(xmlElement);
            else { //preventing having more than one root xml element
                if (doc.getFirstChild() == null)
                    doc.appendChild(xmlElement);
            }
        }catch(Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
        }
    }
//Clone xmlTag and add under the same parent
    public int cloneXmlTag(String xPath, int numOfCopies){
        try{
            //Expecting to clone one element at a time, despite getting NodeList, interested in only one of a kind
            NodeList nodeList = findElementsByXpath(xPath);
            int i = 0;
            for ( ;i < numOfCopies; i++) {
                //clone deep
                nodeList.item(0).getParentNode().appendChild(nodeList.item(0).cloneNode(true));
            }
            return i;
        }catch(Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return -1;
        }
    }
//findElements by xpath and return NodeList
    public NodeList findElementsByXpath(String xPath) {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xPath);
            NodeList nodeList= (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            return nodeList;
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return null;
        }
    }
//get count of Elements by xpath and return NodeList
    public int countElementsByXpath(String xPath) {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xPath);
            NodeList nodeList= (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            return nodeList.getLength();
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return 0;
        }
    }
//Update Element Value & returns: number of updates
    public int updateElementValue(NodeList nodeList, String newValue) {
        try {
            int i = 0;
            for ( ;i < nodeList.getLength(); i++) {
                //nodeList.item(i).setNodeValue(newValue);
                Element element=(Element)nodeList.item(i);
                element.setTextContent(newValue);
            }
            return i;
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return 0;
        }
    }
//Update Element attribute Value & returns: number of updates
    public int updateElementAttributeValue(NodeList nodeList, String attribute, String newAttributeValue) {
        try {
            int i = 0;
            for ( ;i < nodeList.getLength(); i++) {
                //nodeList.item(i).setNodeValue(newValue);
                Element element=(Element)nodeList.item(i);
                element.setAttribute(attribute, newAttributeValue);
            }
            return i;
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return 0;
        }
    }
//replacing matching substring with new substring in attribute & returns: number of updates
    public int replaceElementAttributeValue(NodeList nodeList, String attribute, String searchString, String replaceString) {
        try {
            int count = 0;
            for ( int i = 0;i < nodeList.getLength(); i++) {
                Element element=(Element)nodeList.item(i);
                //Check if the Substring part of attribute string
                if(element.getAttribute(attribute).contains(searchString)){
                    element.setAttribute(attribute,element.getAttribute(attribute).replaceAll(searchString, replaceString));
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return 0;
        }
    }
//Split the String in to 2 across the searchString and replace either 1 part or second part
    public int replaceElementAttributeValue(NodeList nodeList, String attribute,String searchString, boolean keepFirst, String replaceString) {
       return  replaceElementAttributeValue(nodeList,attribute,searchString,keepFirst,0,replaceString);
    }
//
    public int replaceElementAttributeValue(NodeList nodeList, String attribute,String searchString, boolean keepFirst, int numberOfChar2retain, String replaceString) {
        try {
            String [] splitString={"",""};
            String replaceStringInstance = "";
            int count = 0;
            for ( int i = 0;i < nodeList.getLength(); i++) {
                splitString[0]=""; splitString[1]=""; replaceStringInstance=replaceString;
                Element element=(Element)nodeList.item(i);
                //SplitString only in to two part
                splitString = element.getAttribute(attribute).split(searchString, 2);
                if(keepFirst){
                    replaceStringInstance=splitString[0]+searchString+splitString[1].substring(0,numberOfChar2retain)+replaceStringInstance;
                }else{
                    replaceStringInstance=replaceStringInstance+splitString[0].substring((splitString[0].length()-numberOfChar2retain),splitString[0].length())+searchString+splitString[1];
                }
                element.setAttribute(attribute,replaceStringInstance);
                count++;
            }
            return count;
        } catch (Exception e) {
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return 0;
        }
    }
//
    public void printXml(){
        printXml(doc);
    }
    public void printXml(Document documentToPrint){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(documentToPrint);
            //print on the screen
            StreamResult result = new StreamResult(System.out);
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source,result);
        }catch(Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
        }
    }
    public String xmlToString(){
        return xmlToString(doc);
    }

    public String xmlToString(Document documentToPrint){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(documentToPrint);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source,result);
            return writer.toString();
        }catch(Exception e){
            System.out.println("Exception in createXML.printXml :: " + e);
            return null;
        }
    }

    public void writeXml(String fileOut){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            //writing to File
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new File(fileOut));
            transformer.transform(source,result);
    }catch(Exception e){
        System.out.println("Exception in createXML.printXml :: " + e);
        }
   }
//    Purpose: Validate xml against xsd file
    public boolean validateAgainstXSD(String xsdFile ){
        try
        {
            InputStream xml = new FileInputStream(new File(this.inputXmlFile));;
            InputStream xsd = new FileInputStream(new File(xsdFile));;
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception e){
            System.out.println("Exception in "+ this.getClass()+":: " + e);
            return false;
        }
    }
}
