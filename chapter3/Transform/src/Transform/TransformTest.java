package Transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

public class TransformTest {
    public static void main(String[] args) throws Exception {
        Path path;
        if (args.length > 0) path = Path.of(args[0]);
        else path = Path.of("src", "Transform", "makehtml.xsl");

        try (InputStream styleIn = Files.newInputStream(path)) {
            StreamSource styleSource = new StreamSource(styleIn);
            Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            try (InputStream docIn = Files.newInputStream(Path.of("./employee_data.dat"))) {
                t.transform(new SAXSource(new EmployeeReader(), new InputSource(docIn)), new StreamResult(System.out));
            }
        }
    }
}

class EmployeeReader implements XMLReader {
    private ContentHandler handler;
    
    public void parse(InputSource source) throws IOException, SAXException {
        BufferedReader buffin = new BufferedReader(new InputStreamReader(source.getByteStream()));
        AttributesImpl atts = new AttributesImpl();

        if (this.handler == null) throw new SAXException("No content handler");
        this.handler.startDocument();
        this.handler.startElement("", "staff", "staff", atts);
        boolean done = false;
        while (!done) {
            String line = buffin.readLine();
            if (line == null) done = true;
            else {
                this.handler.startElement("", "employee", "employee", atts);
                StringTokenizer t = new StringTokenizer(line, "|");

                this.handler.startElement("", "name", "name", atts);
                String s = t.nextToken();
                this.handler.characters(s.toCharArray(), 0, s.length());
                this.handler.endElement("", "name", "name");

                this.handler.startElement("", "salary", "salary", atts);
                s = t.nextToken();
                this.handler.characters(s.toCharArray(), 0, s.length());
                this.handler.endElement("", "salary", "salary");
                
                String hiredateInfo = t.nextToken();
                StringTokenizer hiredate_sep = new StringTokenizer(hiredateInfo, "-");
                atts.addAttribute("", "year", "year", "CDATA", hiredate_sep.nextToken());
                atts.addAttribute("", "month", "month", "CDATA", hiredate_sep.nextToken());
                atts.addAttribute("", "day", "day", "CDATA", hiredate_sep.nextToken());
                this.handler.startElement("", "hiredate", "hiredate", atts);
                this.handler.endElement("", "hiredate", "hiredate");
                atts.clear();

                this.handler.endElement("", "employee", "employee");
            }
        }
        this.handler.endElement("", "staff", "staff");
        this.handler.endDocument();
    }

    public ContentHandler getContentHandler() { return this.handler; }
    public void setContentHandler(ContentHandler newvalue) { this.handler = newvalue; }

    public void setProperty(String name, Object value) {}
    public Object getProperty(String name) { return null; }
    public void setFeature(String name, boolean value) {}
    public boolean getFeature(String name) { return false; }
    public void setDTDHandler(DTDHandler handler) {}
    public DTDHandler getDTDHandler() { return null; }
    public void setErrorHandler(ErrorHandler handler) {}
    public ErrorHandler getErrorHandler() { return null; }
    public void setEntityResolver(EntityResolver resolver) {}
    public EntityResolver getEntityResolver() { return null; }
    public void parse(String systemId) throws IOException, SAXException {}

}
