package Streams;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingStreams {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("./tests/test.txt");
        String contents = Files.readString(path);

        Stream<String> words = Stream.of(contents.split("\\PL+"));
        show("words", words);
        Stream<String> song = Stream.of("gently", "down", "the", "stream");
        show("song", song);
        Stream<String> silence = Stream.empty();
        show("silence", silence);

        Stream<String> echos = Stream.generate(() -> "Echo");
        show("echos", echos);
        Stream<Double> randoms = Stream.generate(Math::random);
        show("randoms", randoms);
        Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));
        show("integers", integers);
        Stream<String> greetings = "Hello\nGuten Tag\nBonjour".lines();
        show("greetings", greetings);
        Stream<String> wordsAnotherWay = Pattern.compile("\\PL+").splitAsStream(contents);

        try (Stream<String> lines = Files.lines(path)) { show("lines", lines); }
        Iterable<Path> iterable = FileSystems.getDefault().getRootDirectories();
        Stream<Path> rootDirectories = StreamSupport.stream(iterable.spliterator(), false);
        show("RootDirectories", rootDirectories);

        Iterator<Path> iterator = Path.of("C:\\Windows\\System32").iterator();
        Stream<Path> pathComponents = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
        show("pathComponents", pathComponents);
    }

    public static <T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).toList();
        System.out.print("%s: ".formatted(title));
        for (int i = 0; i < firstElements.size(); i++) {
            if (i > 0) System.out.print(", ");
            if (i < SIZE) System.out.print(firstElements.get(i));
            else System.out.print("...");
        }
        System.out.println();
    }
}
