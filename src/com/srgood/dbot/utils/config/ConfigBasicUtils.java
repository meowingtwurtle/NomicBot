package com.srgood.dbot.utils.config;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.utils.SimpleLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class ConfigBasicUtils {
    private static Document document;

    static Document getDocument() {
        return document;
    }

    static List<Node> nodeListToList(NodeList nl) {
        List<Node> ret = new ArrayList<>();

        for (int i = 0; i < nl.getLength(); i++) {
            ret.add(nl.item(i));
        }

        return ret;
    }

    public static boolean verifyConfig() {

        Document doc = getDocument();
        SimpleLog.getLog("Reasons").warn("**XML IS BEING VERIFIED**");

        if (!doc.getDocumentElement().getTagName().equals("config")) {
            SimpleLog.getLog("Reasons").info("Invalid document element");
            return false;
        }

        NodeList defaultNodeList = doc.getDocumentElement().getElementsByTagName("default");
        if (defaultNodeList.getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 default element");
            return false;
        }
        Element defaultElement = (Element) defaultNodeList.item(0);
        if (defaultElement.getElementsByTagName("prefix").getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 default/prefix element");
            return false;
        }


        if (doc.getDocumentElement().getElementsByTagName("servers").getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 servers element");
            return false;
        }

        for (Node n : nodeListToList(((Element) doc.getDocumentElement().getElementsByTagName("servers").item(0)).getElementsByTagName("server"))) {
            Element serverElement = (Element) n;

            if (serverElement.getElementsByTagName("prefix").getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/prefix element");
                return false;
            }

            NodeList rolesNodeList = serverElement.getElementsByTagName("roles");
            if (rolesNodeList.getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/roles element");
                return false;
            }
            if (((Element) rolesNodeList.item(0)).getElementsByTagName("role").getLength() < 1) {
                SimpleLog.getLog("Reasons").info("Less than 1 servers/server/roles/role element");
                return false;
            }

            NodeList commandsNodeList = serverElement.getElementsByTagName("commands");

            if (commandsNodeList.getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands element");
                return false;
            }
            NodeList commandNodeList = ((Element) commandsNodeList.item(0)).getElementsByTagName("command");
            if (commandNodeList.getLength() < 1) {
                SimpleLog.getLog("Reasons").info("Less than 1 servers/server/commands/command element");
                return false;
            }
            {
                for (Node node : nodeListToList(commandNodeList)) {
                    Element commandElement = (Element) node;
                    if (commandElement.getElementsByTagName("permLevel").getLength() != 1) {
                        SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/permLevel element");
                        return false;
                    }
                    if (commandElement.getElementsByTagName("isEnabled").getLength() != 1) {
                        SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/isEnabled element");
                        return false;
                    }
                }
            }
        }

        return true;

    }

    static Element getFirstSubElement(Element parent, String subTagName) {
        List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
        return (Element) (nList.size() > 0 ? nList.get(0) : null);
    }

    // TODO fix the exceptions here, too
    public static void initStorage() throws Exception {
        File inputFile = new File("servers.xml");
        initFromStream(new FileInputStream(inputFile));
    }

    public static void initFromStream(InputStream inputStream) throws Exception {
        try {
            ConfigDangerUtils.resetServers();

            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domInput = domFactory.newDocumentBuilder();

            document = domInput.parse(inputStream);
            getDocument().getDocumentElement().normalize();

            // <config> element
            Element rootElem = getDocument().getDocumentElement();
            // <server> element list
            NodeList ServerNodes = rootElem.getElementsByTagName("server");
            for (int i = 0; i < ServerNodes.getLength(); i++) {
                Element ServerNode = (Element) ServerNodes.item(i);

                ConfigDangerUtils.addServer(ServerNode.getAttribute("id"), ServerNode);
            }

            BotMain.prefix = getFirstSubElement(getFirstSubElement(rootElem, "default"), "prefix").getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
