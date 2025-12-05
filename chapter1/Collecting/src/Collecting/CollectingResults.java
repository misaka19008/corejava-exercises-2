package Collecting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingResults {
    public static void main(String[] args) throws IOException {
        Iterator<Integer> iter = Stream.iterate(0, n -> n + 1).limit(10).iterator();
        while (iter.hasNext())
            System.out.println(iter.next());
        
        Object[] numbers = Stream.iterate(0, n -> n + 1).limit(10).toArray();
        System.out.println("Object array: %s".formatted(numbers));

        try {
            int number = (Integer) numbers[0];
            System.out.println("number: %d".formatted(number));
            System.out.println("The following statement throws an exception:");
            var numbers2 = (Integer[]) numbers;
        } catch (ClassCastException e) { System.out.println(e); }

        Integer[] numbers3 = Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);
        System.out.println(numbers3);

        Set<String> noVowelSet = noVowels().collect(Collectors.toSet());
        show("noVowelSet", noVowelSet);
        TreeSet<String> noVowelTreeSet = noVowels().collect(Collectors.toCollection(TreeSet::new));
        show("noVowelTreeSet", noVowelTreeSet);

        String result = noVowels().collect(Collectors.joining());
        System.out.println("Joining %s".formatted(result));
        result = noVowels().limit(10).collect(Collectors.joining(", "));
        System.out.println("Joining with commas: %s".formatted(result));

        IntSummaryStatistics summary = noVowels().collect(Collectors.summarizingInt(String::length));
        double averageWordLength = summary.getAverage();
        double maxWordlength = summary.getMax();
        System.out.println("Average word length: %s".formatted(averageWordLength));
        System.out.println("Max word length: %s".formatted(maxWordlength));
        System.out.println("forEach:");
        noVowels().limit(10).forEach(System.out::println);
    }

    public static Stream<String> noVowels() throws IOException {
        String contents = Files.readString(Path.of("./tests/test.txt"));
        List<String> wordlist = List.of(contents.split("\\PL+"));
        Stream<String> words = wordlist.stream();
        return words.map(s -> s.replaceAll("[aeiouAEIOU]", ""));
    }

    public static <T> void show(String label, Set<T> set) {
        System.out.print("%s: %s".formatted(label, set.getClass().getName()));
        System.out.println("[%s]".formatted(set.stream().limit(10).map(Object::toString).collect(Collectors.joining(", "))));
    }
}
