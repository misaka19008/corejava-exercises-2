package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class HttpClientTest {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        System.setProperty("jdk.httpclient.HttpClient.log", "headers,errors");
        String propFileName = "src/client/postdata.properties";
        Path propFilePath = Path.of(propFileName);
        Properties props = new Properties();
        try (BufferedReader in = Files.newBufferedReader(propFilePath, StandardCharsets.UTF_8)) {
            props.load(in);
        }

        String urlString = props.remove("url").toString();
        String contentType = props.remove("Content-Type").toString();
        if (contentType.equals("multipart/form-data")) {
            Random generator = new Random();
            String boundary = new BigInteger(256, generator).toString();
            contentType += ";boundary=" + boundary;
            props.replaceAll((k, v) -> v.toString().startsWith("file://") ? propFilePath.getParent().resolve(Path.of(v.toString().substring(7))) : v);
        }
        String result = doPost(urlString, contentType, props);
        System.out.println(result);
    }

    public static String doPost(String url, String contentType, Map<Object, Object> data) throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        BodyPublisher publisher = null;
        if (contentType.startsWith("multipart/form-data")) {
            String boundary = contentType.substring(contentType.lastIndexOf("=") + 1);
            publisher = MoreBodyPublishers.ofMimeMultipartData(data, boundary);
        } else if (contentType.equals("application/x-www-form-urlencoded"))
            publisher = MoreBodyPublishers.ofFormData(data);
        else {
            contentType = "application/json";
            publisher = MoreBodyPublishers.ofSimpleJSON(data);
        }

        HttpRequest request = HttpRequest.newBuilder()
        .version(HttpClient.Version.HTTP_1_1).uri(new URI(url)).header("Content-Type", contentType)
        .POST(publisher).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

class MoreBodyPublishers {
    private static Map<Character, String> replacements = Map.of('\b', "\\b", '\f', "\\f", '\n', "\\n", '\r', "\\r", '\t', "\\t", '"', "\\\"", '\\', "\\\\");

    private static byte[] bytes(String s) { return s.getBytes(StandardCharsets.UTF_8); }

    private static StringBuilder jsonEscape(String str) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String replacement = replacements.get(ch);
            if (replacement == null) result.append(ch);
            else result.append(replacement);
        }
        return result;
    }

    public static BodyPublisher ofFormData(Map<Object, Object> data) {
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (first) first = false;
            else builder.append("&");
            String post_arguKey = URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8);
            String post_arguValue = URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8);
            builder.append("%s=%s".formatted(post_arguKey, post_arguValue));
        }
        return BodyPublishers.ofString(builder.toString());
    }

    public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {
        ArrayList<byte[]> byteArrays = new ArrayList<>();
        byte[] separator = bytes("--%s\nContent-Disposition: form-data; name=".formatted(boundary));
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);
            if (entry.getValue() instanceof Path path) {
                String mimeType = Files.probeContentType(path);
                byteArrays.add(bytes("\"%s\"; filename=\"%s\"\nContent-Type: %s\n\n".formatted(
                    entry.getKey(), path.getFileName(), mimeType
                )));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add(bytes("\n"));
            } else byteArrays.add(bytes("\"%s\"\n\n%s\n".formatted(entry.getKey(), entry.getValue())));
        }
        byteArrays.add(bytes("--%s--".formatted(boundary)));
        return BodyPublishers.ofByteArrays(byteArrays);
    }

    public static BodyPublisher ofSimpleJSON(Map<Object, Object> data) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        boolean first = true;
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (first) first = false;
            else builder.append(", ");
            String json_key = jsonEscape(entry.getKey().toString()).toString();
            String json_value = jsonEscape(entry.getValue().toString()).toString();
            builder.append("%s: %s".formatted(json_key, json_value));
        }
        builder.append("}");
        return BodyPublishers.ofString(builder.toString());
    }
}