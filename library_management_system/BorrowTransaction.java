import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BorrowTransaction {
    private String action;
    private String bookIsbn;
    private String bookTitle;
    private String notes;
    private String timestamp;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public BorrowTransaction(String action, String bookIsbn, String bookTitle, String notes) {
        this.action = action;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.notes = notes;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public void printStatement() {
        System.out.printf("  [%s] %-14s ISBN: %-15s Title: %-24s | %s%n",
            timestamp, action, bookIsbn, trimTitle(bookTitle), notes);
    }

    public String toArchiveString() {
        return String.format("[%s] %-14s ISBN: %-15s Title: %-24s | %s",
            timestamp, action, bookIsbn, trimTitle(bookTitle), notes);
    }

    private String trimTitle(String title) {
        if (title == null) {
            return "N/A";
        }
        if (title.length() <= 24) {
            return title;
        }
        return title.substring(0, 21) + "...";
    }
}
