package bookstoread;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class BookShelf {

    private final List<Book> books = new ArrayList<>();

    public List<Book> books() {
        return Collections.unmodifiableList(books);
    }

    public void add(Book... booksToAdd) {
        books.addAll(Arrays.asList(booksToAdd));
    }

    /**
     * Trie les livres selon l'ordre naturel défini dans la classe Book (par titre).
     */
    public List<Book> arrange() {
        return arrange(Comparator.naturalOrder());
    }

    /**
     * Trie les livres selon un critère spécifique (ex: par date ou par auteur).
     */
    public List<Book> arrange(Comparator<Book> criteria) {
        return books.stream()
                .sorted(criteria)
                .collect(Collectors.toList());
    }

    /**
     * Méthode générique de regroupement pour éviter la duplication de code.
     * Elle accepte une fonction qui définit la clé de regroupement.
     */
    public <K> Map<K, List<Book>> groupBy(Function<Book, K> fx) {
        return books.stream()
                .collect(groupingBy(fx));
    }

    /**
     * Regroupe les livres par année de publication en utilisant la méthode générique.
     */
    public Map<Year, List<Book>> groupByPublicationYear() {
        return groupBy(book -> Year.of(book.getPublishedOn().getYear()));
    }
}