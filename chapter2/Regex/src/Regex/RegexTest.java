package Regex;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexTest {
    public static void main(String[] args) throws PatternSyntaxException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter pattern: ");
        String patternstring = in.next();
        Pattern pattern = Pattern.compile(patternstring);

        while (true) {
            System.out.print("Enter string to match: ");
            String input = in.next();
            if (input.equals("") || input == null) return;
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                System.out.println("Match");
                int groupNum = matcher.groupCount();
                if (groupNum > 0) {
                    for (int i = 0; i < input.length(); i++) {
                        for (int j = 1; j <= groupNum; j++)
                            if (i == matcher.start(j) && i == matcher.end(j)) System.out.print("()");
                        for (int j = 1; j <= groupNum; j++)
                            if (i == matcher.start(j) && i != matcher.end(j)) System.out.print("(");
                        System.out.print(input.charAt(i));
                        for (int j = 1; j <= groupNum; j++)
                            if (i + 1 != matcher.start(j) && i + 1 == matcher.end(j)) System.out.print(")");
                    }
                    System.out.println();
                }
            } else System.out.println("No match");
        }
    }
}
