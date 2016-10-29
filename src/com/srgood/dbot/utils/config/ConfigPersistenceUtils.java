package com.srgood.dbot.utils.config;

import com.srgood.dbot.BotMain;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class ConfigPersistenceUtils {
    public static String generateDirtyXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Beautify XML
        // Set do indents
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Set indent amount
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMSource source = new DOMSource(ConfigBasicUtils.getDocument());
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);
        return stringWriter.toString();
    }

    public static String generateCleanXML() throws TransformerException {
        return cleanXMLString(generateDirtyXML());
    }

    //TODO fix the exceptions here
    public static void writeXML() throws TransformerException {
        String cleanXML = generateCleanXML();

        try (FileWriter fileWriter = new FileWriter(new File("servers.xml")); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(cleanXML);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanXMLString(String dirtyXML) {

        String[] lines = dirtyXML.split("\n");
        StringBuilder buffer = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().equals("") /* don't write out blank lines */) {
                line = line.replace("\f", "").replace("\r", "").replaceAll("\\s+$", "");
                buffer.append(line).append("\n");
            }
        }

        return buffer.toString();
    }

    public static void initConfig() throws IOException {
        File inputFile = new File("servers.xml");
        initConfigFromStream(new FileInputStream(inputFile));
    }

    public static void initConfigFromStream(InputStream inputStream) {
        try {
            ConfigGuildUtils.resetServers();

            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domInput = domFactory.newDocumentBuilder();

            ConfigBasicUtils.setDocument(domInput.parse(inputStream));
            ConfigBasicUtils.getDocument().getDocumentElement().normalize();

            // <config> element
            Element rootElem = ConfigBasicUtils.getDocument().getDocumentElement();
            // <server> element list
            NodeList ServerNodes = rootElem.getElementsByTagName("server");
            for (int i = 0; i < ServerNodes.getLength(); i++) {
                Element ServerNode = (Element) ServerNodes.item(i);

                ConfigGuildUtils.addServer(ServerNode.getAttribute("id"), ServerNode);
            }

            BotMain.prefix = ConfigBasicUtils.getFirstSubElement(ConfigBasicUtils.getFirstSubElement(rootElem, "default"), "prefix").getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
