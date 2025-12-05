package Read;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLReadTest {
    private static Map<Class<?>, Class<?>> toPrimitive = Map.of(Integer.class, int.class, Boolean.class, boolean.class);

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ReflectiveOperationException {
        String filename;
        if (args.length == 0) {
            try(Scanner in = new Scanner(System.in)) {
                System.out.print("Input file: ");
                filename = in.next();
            }
        } else filename = args[0];

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        if (filename.contains("-schema")) {
            factory.setNamespaceAware(true);
            final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        }
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException e) throws SAXException { System.err.println("Warning: %s".formatted(e.getMessage())); }
            public void error(SAXParseException e) throws SAXException {
                System.err.println("Error %s".formatted(e.getMessage()));
                System.exit(0);
            }
            public void fatalError(SAXParseException e) throws SAXException {
                System.err.println("Fatal Error: %s".formatted(e.getMessage()));
                System.exit(0);
            }
        });

        org.w3c.dom.Document doc = builder.parse(filename);
        Map<String, Object> config = parseConfig(doc.getDocumentElement());
        System.out.println(config);
    }

    private static Map<String, Object> parseConfig(Element e) throws ReflectiveOperationException {
        HashMap<String, Object> result = new HashMap<>();
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Element child = (Element) children.item(i);
            String name = child.getAttribute("id");
            Object value = parseObject((Element) child.getFirstChild());
            result.put(name, value);
        }
        return result;
    }

    private static Object parseObject(Element e) throws ReflectiveOperationException {
        String tagName = e.getTagName();
        if (tagName.equals("factory")) return parseFactory(e);
        else if (tagName.equals("construct")) return parseConstruct(e);
        else {
            String childData = e.getFirstChild().getTextContent();
            if (tagName.equals("int")) return Integer.valueOf(childData);
            else if (tagName.equals("boolean")) return Boolean.valueOf(childData);
            else return childData;
        }
    }

    private static Object parseConstruct(Element e) throws ReflectiveOperationException {
        String className = e.getAttribute("class");
        Object[] args = parseArgs(e.getChildNodes());
        Class<?>[] parameterTypes = getParameterTypes(args);
        Constructor constructor = Class.forName(className).getConstructor(parameterTypes);
        return constructor.newInstance(args);
    }

    private static Object parseFactory(Element e) throws ReflectiveOperationException {
        String className = e.getAttribute("class");
        String methodName = e.getAttribute("method");
        Object[] args = parseArgs(e.getChildNodes());
        Class<?>[] parameterTypes = getParameterTypes(args);
        Method method = Class.forName(className).getMethod(methodName, parameterTypes);
        return method.invoke(null, args);
    }

    private static Class<?>[] getParameterTypes(Object[] args) {
        var result = new Class<?>[args.length];
        for (int i = 0; i < result.length; i++) {
            Class<?> cl = args[i].getClass();
            result[i] = toPrimitive.get(cl);
            if (result[i] == null) result[i] = cl;
        }
        return result;
    }

    private static Object[] parseArgs(NodeList elements) throws ReflectiveOperationException {
        var result = new Object[elements.getLength()];
        for (int i = 0; i < result.length; i++) result[i] = parseObject((Element) elements.item(i));
        return result;
    }
}
