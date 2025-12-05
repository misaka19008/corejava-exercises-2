package Collecting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DownstreamCollectors {
    public record City(String name, String state, int population) {}

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Path.of(filename))
            .map(l -> l.split(", ")).map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<Locale>> countryToLocaleSet = locales.collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
        System.out.println("countryToLocaleSet: %s".formatted(countryToLocaleSet));

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Long> countryToLocaleCounts = locales.collect(Collectors.groupingBy(Locale::getCountry, Collectors.counting()));
        System.out.println("countryToLocaleCounts: %s".formatted(countryToLocaleCounts));

        Stream<City> cities = readCities("./tests/cities.txt");
        Map<String, Integer> stateToCityPopulation = cities.collect(Collectors.groupingBy(City::state, Collectors.summingInt(City::population)));
        System.out.println("stateToCityPopulation: %s".formatted(stateToCityPopulation));

        cities = readCities("./tests/cities.txt");
        Map<String, Optional<String>> stateToLongestCityName = cities.collect(Collectors.groupingBy(
            City::state, Collectors.mapping(City::name, Collectors.maxBy(Comparator.comparing(String::length)))
        ));
        System.out.println("stateToLongestCityName: %s".formatted(stateToLongestCityName));

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLanguages = locales.collect(Collectors.groupingBy(Locale::getDisplayCountry, Collectors.mapping(Locale::getDisplayLanguage, Collectors.toSet())));
        System.out.println("countryToLanguages: %s".formatted(countryToLanguages));

        cities = readCities("./tests/cities.txt");
        Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.collect(Collectors.groupingBy(City::state, Collectors.summarizingInt(City::population)));
        System.out.println(stateToCityPopulationSummary.get("Sea of Clouds"));

        cities = readCities("./tests/cities.txt");
        Map<String, String> stateToCityNames = cities.collect(Collectors.groupingBy(City::state, Collectors.reducing("", City::name, (s, t) -> s.length() == 0 ? t : "%s, %s".formatted(s, t))));

        cities = readCities("./tests/cities.txt");
        stateToCityNames = cities.collect(Collectors.groupingBy(City::state, Collectors.mapping(City::name, Collectors.joining(", "))));
        System.out.println("stateToCityNames: %s".formatted(stateToCityNames));

        cities = readCities("./tests/cities.txt");
        record Pair<S, T>(S first, T second) {}
        Pair<List<String>, Double> result = cities.filter(c -> c.state().equals("Sea of Clouds")).collect(Collectors.teeing(
            Collectors.mapping(City::name, Collectors.toList()),
            Collectors.averagingDouble(City::population), (names, avg) -> new Pair<>(names, avg)
        ));
        System.out.println(result);

        cities = readCities("./tests/cities.txt");
        int totalPopulation = cities.collect(Collectors.summingInt(City::population));
        cities = readCities("./tests/cities.txt");
        City maxPopulation = cities.collect(Collectors.maxBy(Comparator.comparingInt(City::population))).get();
        System.out.println("Total population of Liyue: %d".formatted(totalPopulation));
        System.out.println("The region has max population: %s - %d".formatted(maxPopulation.name, maxPopulation.population));
    }
}
