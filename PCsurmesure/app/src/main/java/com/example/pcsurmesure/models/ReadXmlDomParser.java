package com.example.pcsurmesure.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadXmlDomParser {


    public static List<Component> getComponentsFromStream(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<Component> components = new ArrayList<>();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("Component");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String subType = element.getElementsByTagName("subType").item(0).getTextContent();
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String quantity = element.getElementsByTagName("quantity").item(0).getTextContent();
                    String comment = element.getElementsByTagName("comment").item(0).getTextContent();



                    components.add(new Component(type, subType, description, Integer.parseInt(quantity), comment));
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return components;
    }

    public static List<User> getUsersFromStream(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<User> users = new ArrayList<>();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            // Use the correct tag name "User" (case-sensitive)
            NodeList list = doc.getElementsByTagName("User");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String email = element.getElementsByTagName("email").item(0).getTextContent();
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    String role = element.getElementsByTagName("role").item(0).getTextContent();


                    // Based on role, create the appropriate user type
                    if (role.equals("Client")) {
                        users.add(new Requester(username, firstName, lastName, email, password));
                    } else if (role.equals("Admin")) {
                        users.add(new Admin(username, firstName, lastName, email, password));
                    } else if (role.equals("Assembler")) {
                        users.add(new Assembler(username, firstName, lastName, email, password));
                    } else if (role.equals("StoreKeeper")) {
                        users.add(new StoreKeeper(username, firstName, lastName, email, password));
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return users;
    }



}
