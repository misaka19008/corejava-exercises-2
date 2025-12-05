package URLConnection;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class URLConnectionTest {
    public static void main(String[] args) {
        try {
            String urlName;
            if (args.length > 0) urlName = args[0];
            else urlName = "http://192.168.9.50/";
            URL url = new URL(urlName);
            URLConnection connection = url.openConnection();

            String encoding;
            String username;
            String password;
            String http_auth_plaincred;
            if (args.length == 3) {
                username = args[1];
                password = args[2];
            } else {
                Scanner in = new Scanner(System.in);
                System.out.print("Enter username: ");
                username = in.nextLine();
                System.out.print("Enter password: ");
                password = in.nextLine();
            }
            http_auth_plaincred = "%s:%s".formatted(username, password);
            Base64.Encoder b64_encoder = Base64.getEncoder();
            encoding = b64_encoder.encodeToString(http_auth_plaincred.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encoding);

            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String http_header_name = entry.getKey();
                for (String http_header_value : entry.getValue()) System.out.println("%s: %s".formatted(http_header_name, http_header_value));
            }
            
            System.out.println(String.join("", Collections.nCopies(20, "-")));
            System.out.println("getContentType: " + connection.getContentType());
            System.out.println("getContentLength: " + connection.getContentLength());
            System.out.println("getContentEncoding: " + connection.getContentEncoding());
            System.out.println("getDate: " + connection.getDate());
            System.out.println("getExpiration: " + connection.getExpiration());
            System.out.println("getLastModifed: " + connection.getLastModified());
            System.out.println(String.join("", Collections.nCopies(20, "-")));

            String http_response_dataEncType = connection.getContentEncoding();
            if (http_response_dataEncType == null) http_response_dataEncType = "UTF-8";
            try (Scanner in = new Scanner(connection.getInputStream(), http_response_dataEncType)) {
                for (int i = 1; in.hasNextLine() && i <= 10; i++) System.out.println(in.nextLine());
                if (in.hasNextLine()) System.out.println(". . .");
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
