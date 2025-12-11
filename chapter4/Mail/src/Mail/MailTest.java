package Mail;

import com.sun.jdi.connect.Transport;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;


public class MailTest {
    public static void main(String[] args) throws MessagingException, IOException {
        Properties mailconn_props = new Properties();
        Properties maildata_props = new Properties();
        try (Reader connprop_in = Files.newBufferedReader(Path.of("tests/mailconn.properties"), StandardCharsets.UTF_8);
    Reader dataprop_in = Files.newBufferedReader(Path.of("tests/maildata.properties"), StandardCharsets.UTF_8)) {
            mailconn_props.load(connprop_in);
            maildata_props.load(dataprop_in);
        }
        
        String from = maildata_props.remove("from").toString();
        String to = maildata_props.remove("to").toString();
        String subject = maildata_props.remove("subject").toString();
        StringBuilder maildata = new StringBuilder();
        try (Scanner letter_in = new Scanner(new FileInputStream("tests/letter.txt"))) {
            while (letter_in.hasNextLine()) maildata.append(letter_in.nextLine() + "\n");
        }
        
        Console console = System.console();
        String password = new String(console.readPassword("Password: "));

        Session mailSession = Session.getDefaultInstance(mailconn_props);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(maildata.toString());
        Transport tr = mailSession.getTransport();
        try {
            tr.connect(null, password);
            tr.sendMessage(message, message.getAllRecipients());
        } finally { tr.close(); }
    }
}
