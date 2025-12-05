package Stax;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class StAXTest {
    public static void main(String[] args) throws Exception {
        URL url;
        if (args.length == 0) {
            url = new URL(new File("./tests/test.xhtml").toURI().toString());
        } else url = new URL(args[0]);

        InputStream in = url.openStream();
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(in);
        while (parser.hasNext()) {
            int event = parser.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("a")) {
                    String href = parser.getAttributeValue(null, "href");
                    if (href != null) System.out.println(href);
                }
            }
        }
    }
}
