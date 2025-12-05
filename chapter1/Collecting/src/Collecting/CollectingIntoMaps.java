package Collecting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingIntoMaps {
    public static void main(String[] args) throws IOException {
        Map<Integer, String> idToName = people().collect(Collectors.toMap(Person::id, Person::name));
        System.out.println("idToName: %s".formatted(idToName));
        Map<Integer, Person> idToPerson = people().collect(Collectors.toMap(Person::id, Function.identity()));
        System.out.println("idToPerson: %s%s".formatted(idToPerson.getClass().getName(), idToPerson));
        idToPerson = people().collect(Collectors.toMap(
            Person::id, Function.identity(),
            (existingValue, newValue) -> { throw new IllegalStateException(); }, TreeMap::new
        ));
        System.out.println("idToPerson: %s%s".formatted(idToPerson.getClass().getName(), idToPerson));

        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = locales.collect(Collectors.toMap(
            Locale::getDisplayLanguage, l -> l.getDisplayLanguage(l),
            (oldValue, newValue) -> "%s & %s".formatted(oldValue, newValue)
        ));
        System.out.println("languageNames: %s".formatted(languageNames));

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSets = locales.collect(Collectors.toMap(
            Locale::getDisplayLanguage, l -> Set.of(l.getDisplayLanguage()), (a, b) -> {
                Set<String> union = new HashSet<>(a);
                union.addAll(b);
                return union;
        }));
        System.out.println("CountryLanguageSets: %s".formatted(countryLanguageSets));

    }

    public record Person(int id, String name) {}

    public static Stream<Person> people() {
        return Stream.of(
            new Person(1001, "misaka1001"), new Person(1002, "misaka1002"), new Person(1003, "misaka1003")
        );
    }
}
