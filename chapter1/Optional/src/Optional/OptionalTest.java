package Optional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) throws IOException {
        String contents = Files.readString(Path.of("./tests/test.txt"));
        List<String> wordlist = List.of(contents.split("\\PL+"));
        Optional<String> optionalValue = wordlist.stream().filter(s -> s.contains("fred")).findFirst();
        System.out.println("\"%s\" contains fred.".formatted(optionalValue.orElse("No word")));

        Optional<String> optionalString = Optional.empty();
        String result = optionalString.orElse("N/A");
        System.out.println("Result: %s".formatted(result));
        result = optionalString.orElseGet(() -> Locale.getDefault().getDisplayName());
        System.out.println("Result: %s".formatted(result));
        try {
            result = optionalString.orElseThrow(IllegalStateException::new);
            System.out.println("Result: %s".formatted(result));
        } catch (Throwable t) { t.printStackTrace(); }

        optionalValue = wordlist.stream().filter(s -> s.contains("red")).findFirst();
        optionalValue.ifPresent(s -> System.out.println("\"%s\" contains red".formatted(s)));

        HashSet<String> results = new HashSet<>();
        optionalValue.ifPresent(results::add);
        Optional<Boolean> added = optionalValue.map(results::add);
        System.out.println(added.get());

        System.out.println(inverse(4.0).flatMap(OptionalTest::squareRoot).toString());
        System.out.println(inverse(-1.0).flatMap(OptionalTest::squareRoot).toString());
        System.out.println(inverse(0.0).flatMap(OptionalTest::squareRoot).toString());
        Optional<Double> result2 = Optional.of(-4.0).flatMap(OptionalTest::inverse).flatMap(OptionalTest::squareRoot);
        System.out.println(result2);
    }

    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
