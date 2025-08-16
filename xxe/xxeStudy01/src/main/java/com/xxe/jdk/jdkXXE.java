package com.xxe.jdk;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class jdkXXE {
    public static void vul() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<?xml version='1.0'?>" +
                "<!DOCTYPE note [<!ENTITY xxe SYSTEM 'file:///D:/flag.txt'>]>" +
                "<note>" +
                "<to>&xxe;</to>" +
                "<from>webgoat</from>" +
                "</note>";
        InputStream inputStream = new java.io.ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 漏洞：未禁用外部实体
        Document doc = builder.parse(inputStream);

        // 遍历xml节点name和value
        StringBuffer buf = new StringBuffer();
        NodeList rootNodeList = doc.getChildNodes();
        for (int i = 0; i < rootNodeList.getLength(); i++) {
            Node rootNode = rootNodeList.item(i);
            NodeList child = rootNode.getChildNodes();
            for (int j = 0; j < child.getLength(); j++) {
                Node node = child.item(j);
                buf.append(node.getNodeName() + ": " + node.getTextContent() + "\n");
            }
        }
        System.out.println(buf.toString());
//        System.out.println(doc);
        System.out.println("Document parsed successfully");
    }

    public static void vulfixed() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<?xml version='1.0'?>" +
                "<!DOCTYPE note [<!ENTITY xxe SYSTEM 'file:///D:/flag.txt'>]>" +
                "<note>" +
                "<to>&xxe;</to>" +
                "<from>webgoat</from>" +
                "</note>";
        InputStream inputStream = new java.io.ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 漏洞：未禁用外部实体
        Document doc = builder.parse(inputStream);

        // 遍历xml节点name和value
        StringBuffer buf = new StringBuffer();
        NodeList rootNodeList = doc.getChildNodes();
        for (int i = 0; i < rootNodeList.getLength(); i++) {
            Node rootNode = rootNodeList.item(i);
            NodeList child = rootNode.getChildNodes();
            for (int j = 0; j < child.getLength(); j++) {
                Node node = child.item(j);
                // 只输出元素，不输出实体
                if (child.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    buf.append(node.getNodeName() + ": " + node.getTextContent() + "\n");
                }
            }
        }
        System.out.println(buf.toString());
//        System.out.println(doc);
        System.out.println("Document parsed successfully");
    }

    public static void main(String[] args) throws Exception {
        vul();
        vulfixed();
    }
}

