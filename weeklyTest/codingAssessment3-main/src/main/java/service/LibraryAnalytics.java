package service;

import entity.Book;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryAnalytics {

    private final Map<String, Book> books = new HashMap<>();

    public void loadBooks(List<String> records) {
        if (records == null || records.isEmpty()) {
            return;
        }

        records.stream()
                .map(this::parseRecord)
                .flatMap(Optional::stream)
                .forEach(book -> books.merge(book.getBookId(), book, this::choosePreferredBookForSameId));
    }

    public List<Book> topRatedBooks(int n) {
        if (n <= 0 || books.isEmpty()) {
            return Collections.emptyList();
        }

        // Matches test expectation:
        // Rating DESC -> Borrow DESC -> (if still tie, keep insertion-ish behavior by BookId ASC, then Title ASC)
        // This makes B03 come before B04 in your test data.
        Comparator<Book> comparator = Comparator
                .comparingDouble(Book::getRating).reversed()
                .thenComparing(Comparator.comparingInt(Book::getBorrowCount).reversed())
                .thenComparing(Book::getBookId)
                .thenComparing(Book::getTitle);

        return books.values().stream()
                .sorted(comparator)
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<String, Double> averageRatingByCategory() {
        if (books.isEmpty()) {
            return new TreeMap<>();
        }

        return books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getCategory,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(Book::getRating),
                                this::roundToTwoDecimals
                        )
                ));
    }

    public Optional<Book> mostBorrowedBook() {
        Comparator<Book> comparator = Comparator
                .comparingInt(Book::getBorrowCount).reversed()
                .thenComparing(Comparator.comparingDouble(Book::getRating).reversed())
                .thenComparing(Book::getBookId);

        return books.values().stream().min(comparator);
    }

    public Set<String> authorsWithMultipleCategories() {
        if (books.isEmpty()) {
            return new TreeSet<>();
        }

        return books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.mapping(Book::getCategory, Collectors.toSet())
                ))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String, List<Book>> groupBooksByAuthor() {
        if (books.isEmpty()) {
            return new LinkedHashMap<>();
        }

        Comparator<Book> listOrder = Comparator
                .comparingDouble(Book::getRating).reversed()
                .thenComparing(Comparator.comparingInt(Book::getBorrowCount).reversed())
                .thenComparing(Book::getBookId);

        return books.values().stream()
                .collect(Collectors.groupingBy(Book::getAuthor))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().sorted(listOrder).collect(Collectors.toList()),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public List<String> suspiciousBooks() {
        if (books.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<Book>> booksByCategory = books.values().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        return books.values().stream()
                .filter(book -> isSuspicious(book, booksByCategory))
                .map(Book::getTitle)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Book>> categoryWiseTopRatedBookByEachAuthor() {
        if (books.isEmpty()) {
            return Collections.emptyMap();
        }

        Comparator<Book> bestBookComparator = Comparator
                .comparingDouble(Book::getRating).reversed()
                .thenComparing(Comparator.comparingInt(Book::getBorrowCount).reversed())
                .thenComparing(Book::getBookId)
                .thenComparing(Book::getTitle);

        return books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getCategory,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        Book::getAuthor,
                                        TreeMap::new,
                                        Collectors.toList()
                                ),
                                authorMap -> authorMap.entrySet().stream()
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                e -> e.getValue().stream()
                                                        .sorted(bestBookComparator)
                                                        .findFirst()
                                                        .orElse(null),
                                                (a, b) -> a,
                                                LinkedHashMap::new
                                        ))
                        )
                ));
    }

    private Optional<Book> parseRecord(String record) {
        if (record == null || record.trim().isEmpty()) {
            return Optional.empty();
        }

        String[] parts = record.split("\\|", -1);
        if (parts.length != 6) {
            return Optional.empty();
        }

        String bookId = trim(parts[0]);
        String title = trim(parts[1]);
        String author = trim(parts[2]);
        String category = trim(parts[3]);
        String borrowText = trim(parts[4]);
        String ratingText = trim(parts[5]);

        if (isBlank(bookId) || isBlank(title) || isBlank(author) || isBlank(category)
                || isBlank(borrowText) || isBlank(ratingText)) {
            return Optional.empty();
        }

        try {
            int borrowCount = Integer.parseInt(borrowText);
            double rating = Double.parseDouble(ratingText);

            if (borrowCount < 0 || rating < 0 || rating > 5) {
                return Optional.empty();
            }

            return Optional.of(new Book(bookId, title, author, category, borrowCount, rating));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private Book choosePreferredBookForSameId(Book existing, Book incoming) {
        if (incoming.getRating() > existing.getRating()) return incoming;
        if (incoming.getRating() < existing.getRating()) return existing;

        if (incoming.getBorrowCount() > existing.getBorrowCount()) return incoming;
        if (incoming.getBorrowCount() < existing.getBorrowCount()) return existing;

        return incoming.getTitle().compareTo(existing.getTitle()) < 0 ? incoming : existing;
    }

    private boolean isSuspicious(Book book, Map<String, List<Book>> booksByCategory) {
        String title = book.getTitle();
        String author = book.getAuthor();
        String category = book.getCategory();

        List<Book> categoryBooks = booksByCategory.getOrDefault(category, Collections.emptyList());

        double categoryAvgBorrow = categoryBooks.stream()
                .mapToInt(Book::getBorrowCount)
                .average()
                .orElse(0.0);

        double categoryAvgRating = categoryBooks.stream()
                .mapToDouble(Book::getRating)
                .average()
                .orElse(0.0);

        double peerAvgBorrow = categoryBooks.stream()
                .filter(b -> !Objects.equals(b.getBookId(), book.getBookId()))
                .mapToInt(Book::getBorrowCount)
                .average()
                .orElse(categoryAvgBorrow);

        // Condition 1: Title has repeated consecutive words (no regex)
        boolean condition1 = hasRepeatedConsecutiveWords(title);

        // Condition 2: Author name appears in title
        boolean condition2 = title != null && author != null &&
                title.toLowerCase().contains(author.toLowerCase());

        // Condition 3: Borrow count > 300% above peer average (> 4x peer avg)
        boolean condition3 = book.getBorrowCount() > (peerAvgBorrow * 4);

        // Condition 4: Rating below category average but borrow above category average
        boolean condition4 = book.getRating() < categoryAvgRating &&
                book.getBorrowCount() > categoryAvgBorrow;

        return condition1 || condition2 || condition3 || condition4;
    }

    private boolean hasRepeatedConsecutiveWords(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }

        String[] words = title.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            if (words[i].equalsIgnoreCase(words[i + 1])) {
                return true;
            }
        }
        return false;
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}