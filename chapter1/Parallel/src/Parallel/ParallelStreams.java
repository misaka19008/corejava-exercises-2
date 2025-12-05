package Parallel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParallelStreams {
    public static void main(String[] args) throws IOException {
        var contents = Files.readString(Path.of("./tests/test.txt"));
        List<String> wordlist = List.of(contents.split("\\PL+"));

        // Very bad code that has race condition problem.
        int[] shortwords = new int[10];
        wordlist.parallelStream().forEach(s -> {
            // So many threads will modify a same elements at the same time.
            if (s.length() < 10) shortwords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortwords));

        // Just try again, and you will find the result is different from the above one.
        Arrays.fill(shortwords, 0);
        wordlist.parallelStream().forEach(s -> {
            // So many threads will modify a same elements at the same time.
            if (s.length() < 10) shortwords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortwords));

        // The most safe way is filtering the stream then grouping it according to the length of each string and counting.
        Map<Integer, Long> shortwordcounts = wordlist.parallelStream().filter(s -> s.length() < 10).collect(
            // Must use groupingByConcurrent() function when grouping a parallel stream.
            Collectors.groupingByConcurrent(String::length, Collectors.counting())
        );
        System.out.println(shortwordcounts);

        Map<Integer, List<String>> shortwordlist = wordlist.parallelStream().filter(s -> s.length() < 10).collect(
            Collectors.groupingByConcurrent(String::length, Collectors.toList())
        );
        System.out.println(shortwordlist.get(14));

        Map<Integer, Long> normalwordcounts = wordlist.parallelStream().collect(
            Collectors.groupingByConcurrent(String::length, Collectors.counting())
        );
        System.out.println(normalwordcounts);
    }
}
