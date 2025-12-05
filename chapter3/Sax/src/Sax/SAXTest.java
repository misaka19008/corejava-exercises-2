package Sax;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXTest {
    public static void main(String[] args) throws Exception {
        String url;
        if (args.length == 0)
            url = new File("./tests/test.xhtml").toURI().toString();
        else url = args[0];
        System.out.println("Using %s".formatted(url));

        var handler = new DefaultHandler() {
            public ArrayList<String> hrefUrlList = new ArrayList<>();

            public void startElement(String namespaceURI, String lname, String qname, Attributes attrs) {
                if (lname.equals("a") && attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aname = attrs.getLocalName(i);
                        if (aname.equals("href")) {
                            String urlHrefString = attrs.getValue(i);
                            this.hrefUrlList.add(urlHrefString);
                            System.out.println(urlHrefString);
                        }
                    }
                }
            }
        };

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware(true);
        saxFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        SAXParser parser = saxFactory.newSAXParser();
        InputStream in = new URL(url).openStream();
        parser.parse(in, handler);
        PrintWriter resultWriter = new PrintWriter(new FileOutputStream("./tests/url_result.txt", false));
        for (String urlResult : handler.hrefUrlList) resultWriter.println(urlResult);
        resultWriter.close();
        System.out.println("All results have been written to the file.");
    }
}
