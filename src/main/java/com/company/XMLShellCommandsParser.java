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
import java.util.stream.Collectors;

import static com.company.ResourceType.*;

public class XMLShellCommandsParser {

    public List<FileResource> getResourcesFromShellElement(Element shellElement) {

        List<FileResource> resources = new ArrayList<FileResource>();

        for(ResourceType type: ResourceType.values()) {

            NodeList resourceElements = shellElement.getElementsByTagName(type.getTag());

            for(int j = 0; j< resourceElements.getLength(); j++) {
                Element e = (Element) resourceElements.item(j);

                if (type == OUTPUT || type == STREAMABLE_OUTPUT) {
                    resources.add(new Output(e.getTextContent().trim(), type));
                } else {
                    resources.add(new Input(e.getTextContent().trim(), type));
                }
            }
        }

        return resources;
    }

    public Command buildCommand(String id, String command, List<FileResource> resources) {
        Command c = new Command(id, command);
        List<Output> outputs = resources.stream()
                .filter(r -> r.getType() == OUTPUT || r.getType() == STREAMABLE_OUTPUT)
                .map(r -> (Output) r)
                .collect(Collectors.toList());

        List<Input> inputs = resources.stream()
                .filter(r -> r.getType() == INPUT || r.getType() == STREAMABLE_INPUT)
                .map(r -> (Input) r)
                .collect(Collectors.toList());

        c.setOutputs(outputs);
        c.setInputs(inputs);

        return c;
    }

    public List<Command> parseShellCommandsFromXML(String filePath) {

        List<Command> shellCommands = new ArrayList<Command>();

        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(fXmlFile);

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList shellElements = doc.getElementsByTagName("shell");

            for (int i = 0; i < shellElements.getLength(); i ++) {

                Node shellNode = shellElements.item(i);

                if (shellNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element shellElement = (Element) shellNode;

                    String commandId = shellElement.getAttribute("id");
                    String commandString = shellNode.getTextContent().trim();

                    //System.out.println("Command [" + commandId + "]: " +  commandString);

                    List<FileResource> resources = getResourcesFromShellElement(shellElement);

                    //resources.forEach( r -> System.out.println("Resource: " + r.getResourceName() + " Type: " + r.getType().name()));

                    //System.out.println("\n");

                    Command c = buildCommand(commandId, commandString, resources);
                    shellCommands.add(c);
                }
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return shellCommands;
    }

}
