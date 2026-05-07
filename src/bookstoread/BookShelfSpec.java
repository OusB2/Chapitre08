package bookstoread;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spécifications de la bibliothèque (BookShelf)")
public class BookShelfSpec {

    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;

    @BeforeEach
    void init() {
        shelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch", LocalDate.of(2008, Month.MAY, 8));
        codeComplete = new Book("Code Complete", "Steve McConnel", LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1));
    }

    @Nested
    @DisplayName("Initialisation et état de base")
    class BasicTests {
        @Test
        @DisplayName("est vide quand aucun livre n'est ajouté")
        public void shelfEmptyWhenNoBookAdded() {
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
        }

        @Test
        @DisplayName("contient deux livres quand deux livres sont ajoutés")
        void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            assertEquals(2, books.size(), () -> "BookShelf should have two books.");
        }

        @Test
        @DisplayName("reste vide si la méthode add est appelée sans arguments")
        public void emptyBookShelfWhenAddIsCalledWithoutBooks() {
            shelf.add();
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
        }
    }

    @Nested
    @DisplayName("Tests de tri")
    class SortingTests {
        @Test
        @DisplayName("trie les livres par titre de façon lexicographique")
        void bookshelfArrangedByBookTitle() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange();
            assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth), books,
                    () -> "Books in a bookshelf should be arranged lexicographically by book title");
        }

        @Test
        @DisplayName("trie les livres selon un critère personnalisé (date de publication)")
        void bookshelfArrangedByPublishedDate() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange(Comparator.comparing(Book::getPublishedOn));
            assertEquals(Arrays.asList(mythicalManMonth, codeComplete, effectiveJava), books,
                    () -> "Books in a bookshelf should be arranged by publication date");
        }

        @Test
        @DisplayName("conserve l'ordre d'insertion original après un appel à arrange")
        void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            shelf.arrange();
            List<Book> books = shelf.books();
            assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth), books,
                    () -> "Books in bookshelf are in insertion order");
        }
    }

    @Nested
    @DisplayName("Tests de regroupement")
    class GroupingTests {
        @Test
        @DisplayName("regroupe les livres par année de publication")
        void booksInBookshelfAreGroupedByPublicationYear() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            Map<Year, List<Book>> booksByYear = shelf.groupByPublicationYear();

            assertAll(
                    () -> assertEquals(1, booksByYear.get(Year.of(2008)).size()),
                    () -> assertEquals(1, booksByYear.get(Year.of(2004)).size()),
                    () -> assertEquals(1, booksByYear.get(Year.of(1975)).size())
            );
        }
    }

    @Test
    @DisplayName("la liste renvoyée est immuable pour le client")
    void booksReturnedFromBookShelfIsImmutableForClient() {
        shelf.add(effectiveJava, codeComplete);
        List<Book> books = shelf.books();
        assertThrows(UnsupportedOperationException.class, () -> {
            books.add(mythicalManMonth);
        }, "Should not be able to add book to books list");
    }
}