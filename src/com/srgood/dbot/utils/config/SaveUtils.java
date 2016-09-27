package com.srgood.dbot.utils.config;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class SaveUtils {
    public static String generateDirtyXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Beautify XML
        // Set do indents
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Set indent amount
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMSource source = new DOMSource(ConfigBasicUtils.getDocument());
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);
        return stringWriter.toString();
    }

    public static String generateCleanXML() throws TransformerException  {
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

//        try (FileReader fr = new FileReader("servers.xml"); FileWriter fw = new FileWriter("temp.xml")) {
//            BufferedReader br = new BufferedReader(fr);
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                if (!line.trim().equals("")) // don't write out blank lines
//                {
//                    line = line.replace("\n", "").replace("\f", "").replace("\r", "");
//                    fw.write(line + "\n", 0, line.length() + 1);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        try {
//            Files.deleteIfExists(new File("servers.xml").toPath());
//            Files.move(new File("temp.xml").toPath(), new File("servers.xml").toPath());
//            Files.deleteIfExists(new File("temp.xml").toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String[] lines = dirtyXML.split("\n");
        StringBuilder buffer = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().equals("") /* don't write out blank lines */ ) {
                line = line.replace("\f", "").replace("\r", "").replaceAll("\\s+$", "");
                buffer.append(line).append("\n");
            }
        }

        return buffer.toString();
    }
}
