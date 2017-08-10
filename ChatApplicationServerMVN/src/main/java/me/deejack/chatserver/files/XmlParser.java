package me.deejack.chatserver.files;

import me.deejack.chatserver.database.UserDatabase;
import me.deejack.chatserver.user.User;
import me.deejack.chatserver.utils.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * @author DeeJack
 */
public class XmlParser {
    private File xmlFile;
    private static XmlParser parser;

    private XmlParser() {
        xmlFile = new File(System.getProperty("user.home") + File.separator + ".serverchat" + File.separator + "users.xml");
        File path = new File(xmlFile.getPath());
        if(!path.exists())
            path.mkdir();
        if(!xmlFile.exists())
            try {
                xmlFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        xmlFile = xmlFile;
    }

    public static XmlParser getParser() {
        if(parser == null)
            parser = new XmlParser();
        return parser;
    }

    public void fillDatabase() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) n;

                    String username = eElement.getElementsByTagName("username").item(0).getTextContent();
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
                    String key = eElement.getElementsByTagName("key").item(0).getTextContent();
                    String creationDate = eElement.getElementsByTagName("creation").item(0).getTextContent();
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();

                    User u = new User(username, new Date(creationDate).toString(), Integer.parseInt(type),null);
                    u.setPassword(password);
                    u.setKey(key);

                    UserDatabase.getDatabase().add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(User u) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);

            Element newUser = doc.createElement("user");

            Element username = doc.createElement("username");
            username.appendChild(doc.createTextNode(u.getUsername()));
            newUser.appendChild(username);

            Element password = doc.createElement("password");
            password.appendChild(doc.createTextNode(u.getPassword()));
            newUser.appendChild(password);

            Element key = doc.createElement("key");
            key.appendChild(doc.createTextNode(u.getKey()));
            newUser.appendChild(key);

            Element creation = doc.createElement("creation");
            creation.appendChild(doc.createTextNode(u.getCreation().toString()));
            newUser.appendChild(creation);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(Integer.toString(u.getType())));
            newUser.appendChild(type);
            doc.getElementsByTagName("users").item(0).appendChild(newUser);

            DOMSource source = new DOMSource(doc);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(xmlFile);
            t.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
