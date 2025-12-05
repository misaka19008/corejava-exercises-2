package Dom;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JSONConverter {
    private static Map<Character, String> replacements = Map.of('\b', "\\b", '\f', "\\f", '\n', "\\n", '\r', "\\r", '\t', "\\t", '"', "\\\"", '\\', "\\\\");

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        String filename;
        if (args.length == 0) {
            try (Scanner in = new Scanner(System.in)) {
                System.out.print("Input file: ");
                filename = in.nextLine();
            }
        } else filename = args[0];
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document doc = builder.parse(filename);
        Element root = doc.getDocumentElement();
        System.out.println(convert(root, 0));
    }

    private static StringBuilder pad(StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) builder.append("  ");
        return builder;
    }

    private static StringBuilder attributeObject(NamedNodeMap attrs) {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < attrs.getLength(); i++) {
            if (i > 0) result.append(", ");
            result.append(jsonEscape(attrs.item(i).getNodeName()));
            result.append(": ");
            result.append(jsonEscape(attrs.item(i).getNodeValue()));
        }
        result.append("}");
        return result;
    }

    private static StringBuilder jsonEscape(String str) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String replacement = replacements.get(ch);
            if (replacement == null) result.append(ch);
            else result.append(replacement);
        }
        result.append("\"");
        return result;
    }

    private static StringBuilder characterString(CharacterData node, int level) {
        StringBuilder result = new StringBuilder();
        StringBuilder data = jsonEscape(node.getData());
        if (node instanceof Comment) data.insert(1, "Comment: ");
        pad(result, level).append(data);
        return result;
    }

    private static StringBuilder elementObject(Element elem, int level) {
        StringBuilder result = new StringBuilder();
        pad(result, level).append("{\n");
        pad(result, level + 1).append("\"name\": ");
        result.append(jsonEscape(elem.getTagName()));
        NamedNodeMap attrs = elem.getAttributes();
        if (attrs.getLength() > 0) {
            pad(result.append(",\n"), level + 1).append("\"attributes\": ");
            result.append(attributeObject(attrs));
        }
        NodeList children = elem.getChildNodes();
        if (children.getLength() > 0) {
            pad(result.append(",\n"), level + 1).append("\"children\": [\n");
            for (int i = 0; i < children.getLength(); i++) {
                if (i > 0) result.append(",\n");
                result.append(convert(children.item(i), level + 2));
            }
            result.append("\n");
            pad(result, level + 1).append("]\n");
        }
        pad(result, level).append("}");
        return result;
    }

    public static StringBuilder convert(Node node, int level) {
        if (node instanceof Element elem) return elementObject(elem, level);
        else if (node instanceof CharacterData cd) return characterString(cd, level);
        else return pad(new StringBuilder(), level).append(jsonEscape(node.getClass().getName()));
    }
}
