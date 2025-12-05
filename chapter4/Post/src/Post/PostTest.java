package Post;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PostTest {
    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        try (Reader prop_in = Files.newBufferedReader(Path.of("postdata.properties"), StandardCharsets.UTF_8)) {
            prop.load(prop_in);
        }
        String result = doPost(
            new URL("http://192.168.9.50/index.php?content_provider=misaka19008"),
            prop,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            1
        );
        System.out.println(result);
    }

    public static String doPost(URL url, Map<Object, Object> nameValuePairs, String userAgent, int redirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (userAgent != null) connection.setRequestProperty("User-Agent", userAgent);
        if (redirects >= 0) connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);

        try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
            boolean first = true;
            for (Map.Entry<Object, Object> pair : nameValuePairs.entrySet()) {
                if (first) first = false;
                else out.print("&");
                String post_arguName = pair.getKey().toString();
                String post_arguValue = URLEncoder.encode(pair.getValue().toString(), StandardCharsets.UTF_8);
                out.print("%s=%s".formatted(post_arguName, post_arguValue));
            }
        }
        String response_encType = connection.getContentEncoding();
        if (response_encType == null) response_encType = "UTF-8";

        if (redirects > 0) {
            int response_code = connection.getResponseCode();
            if (response_code == 301 || response_code == 302 || response_code == 304) {
                String redirect_location = connection.getHeaderField("Location");
                if (redirect_location != null) {
                    URL base = connection.getURL();
                    connection.disconnect();
                    return doPost(new URL(base, redirect_location), nameValuePairs, userAgent, redirects - 1);
                }
            }
        } else if (redirects == 0) throw new IOException("Too many redirects");

        StringBuilder response = new StringBuilder();
        try (Scanner in = new Scanner(connection.getInputStream(), response_encType)) {
            while (in.hasNextLine()) {
                response.append(in.nextLine() + "\n");
            }
        } catch (IOException e) {
            InputStream err = connection.getErrorStream();
            if (err == null) throw e;
            try (Scanner in = new Scanner(err)) {
                response.append(in.nextLine() + "\n");
            }
        }

        return response.toString();
    }
}
