package Match;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HrefMatch {
    public static void main(String[] args) {
        try {
            String urlstring = "https://www.misaka19008-lab.icu/";
            InputStream in = new URL(urlstring).openStream();
            String input = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            String patternstring = "<a\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s]*)\\s*>";
            Pattern pattern = Pattern.compile(patternstring, Pattern.CASE_INSENSITIVE);
            pattern.matcher(input).results().map(MatchResult::group).forEach(System.out::println);
        } catch (IOException | PatternSyntaxException e) { e.printStackTrace(); }
    }
}
