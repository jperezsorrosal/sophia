package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.company.ResourceType.OUTPUT;
import static com.company.ResourceType.STREAMABLE_OUTPUT;

public class ParseWithDom {

    public static List<FileResource> getResourcesFromShellElement(Element shellElement) {

        List<FileResource> resources = new ArrayList<FileResource>();

        for(ResourceType type: ResourceType.values()) {

            NodeList resourceElements = shellElement.getElementsByTagName(type.getTag());

            for(int j = 0; j< resourceElements.getLength(); j++) {
                Element e = (Element) resourceElements.item(j);

                if (type == OUTPUT || type == STREAMABLE_OUTPUT) {
                    resources.add(new Output(e.getTextContent(), type));
                } else {
                    resources.add(new Input(e.getTextContent(), type));
                }
            }
        }

        return resources;
    }

    public static void main(String[] args) {
        try {
            File fXmlFile = new File("/Users/jperezsl/IdeaProjects/sophia/src/main/resources/commands.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(fXmlFile);


            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList shellElements = doc.getElementsByTagName("shell");

            for (int i = 0; i < shellElements.getLength(); i ++) {

                Node shellNode = shellElements.item(i);

                if (shellNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element shellElement = (Element) shellNode;

                    System.out.println("Command [" + shellElement.getAttribute("id")+ "]: " + shellNode.getTextContent().trim() );

                    List<FileResource> resources = ParseWithDom.getResourcesFromShellElement(shellElement);

                    resources.forEach( r -> System.out.println("Resource: " + r.getResourceName() + " Type: " + r.getType().name()));

                    System.out.println("\n");
                }
            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
}
