package Write;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriteTest {
    private static final Random generator = new Random();

    public static void main(String[] args) throws Exception {
        Document doc = newDrawing(600, 400);
        writeDocument(doc, "./tests/drawing1.svg");
        writeNewDrawing(600, 400, "./tests/drawing2.svg");
    }

    public static Document newDrawing(int drawingWidth, int drawingHeight) throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        Document doc_builder = builderFactory.newDocumentBuilder().newDocument();
        String namespace = "http://www.w3.org/2000/svg";

        Element svgElement = doc_builder.createElementNS(namespace, "svg");
        doc_builder.appendChild(svgElement);
        svgElement.setAttribute("width", "" + drawingWidth);
        svgElement.setAttribute("height", "" + drawingHeight);

        int n = 10 + generator.nextInt(20);
        for (int i = 1; i <= n; i++) {
            int x = generator.nextInt(drawingWidth);
            int y = generator.nextInt(drawingHeight);
            int width = generator.nextInt(drawingWidth - x);
            int height = generator.nextInt(drawingHeight - y);
            int r = generator.nextInt(256);
            int g = generator.nextInt(256);
            int b = generator.nextInt(256);

            Element rectElement = doc_builder.createElementNS(namespace, "rect");
            rectElement.setAttribute("x", "" + x);
            rectElement.setAttribute("y", "" + y);
            rectElement.setAttribute("width", "" + width);
            rectElement.setAttribute("height", "" + height);
            rectElement.setAttribute("fill", "#%02x%02x%02x".formatted(r, g, b));
            svgElement.appendChild(rectElement);
        }
        return doc_builder;
    }

    public static void writeDocument(Document doc, String filename) throws TransformerException, IOException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
        t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty("{http://xml.apache.org/xslt}inent-amount", "2");
        t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(Path.of(filename))));
    }

    public static void writeNewDrawing(int drawingWidth, int drawingHeight, String filename) throws XMLStreamException, IOException {
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(Files.newOutputStream(Path.of(filename)));
        xmlWriter.writeStartDocument();
        xmlWriter.writeDTD("""
            <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 20000802//EN" "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd">
        """);
        xmlWriter.writeStartElement("svg");
        xmlWriter.writeDefaultNamespace("http://www.w3.org/2000/svg");
        xmlWriter.writeAttribute("width", "" + drawingWidth);
        xmlWriter.writeAttribute("height", "" + drawingHeight);
        int n = 10 + generator.nextInt(20);
        for (int i = 1; i <= n; i++) {
            int x = generator.nextInt(drawingWidth);
            int y = generator.nextInt(drawingHeight);
            int width = generator.nextInt(drawingWidth - x);
            int height = generator.nextInt(drawingHeight - y);
            int r = generator.nextInt(256);
            int g = generator.nextInt(256);
            int b = generator.nextInt(256);

            xmlWriter.writeEmptyElement("rect");
            xmlWriter.writeAttribute("x", "" + x);
            xmlWriter.writeAttribute("y", "" + y);
            xmlWriter.writeAttribute("width", "" + width);
            xmlWriter.writeAttribute("height", "" + height);
            xmlWriter.writeAttribute("fill", "#%02x%02x%02x".formatted(r, g, b));
        }
        xmlWriter.writeEndDocument();
        xmlWriter.close();
    }
}
