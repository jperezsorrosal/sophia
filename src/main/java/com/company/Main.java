package com.company;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here


        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                private boolean inShellElement = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    //System.out.println(attributes.getValue("id"));

                    if (qName.equals("shell")) {
                        inShellElement = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equals("shell")) {
                        inShellElement = false;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {

                    if (inShellElement) {
                        System.out.println(new String(ch, start, length));
                    }
                }
            };

            saxParser.parse("/Users/jperezsl/IdeaProjects/sophia/src/main/resources/commands.xml", handler);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
