public class Book implements Displayable {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private String shelfCode;

    public Book(String isbn, String title, String author, int totalCopies, String shelfCode) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = Math.max(1, totalCopies);
        this.availableCopies = this.totalCopies;
        this.shelfCode = shelfCode == null || shelfCode.isBlank() ? "GEN-01" : shelfCode;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public boolean issueCopy() {
        if (!isAvailable()) {
            return false;
        }
        availableCopies--;
        return true;
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public void moveShelf(String newShelfCode) {
        if (newShelfCode != null && !newShelfCode.isBlank()) {
            shelfCode = newShelfCode;
        }
    }

    @Override
    public void display() {
        System.out.println("ISBN            : " + isbn);
        System.out.println("Title           : " + title);
        System.out.println("Author          : " + author);
        System.out.println("Shelf Code      : " + shelfCode);
        System.out.println("Total Copies    : " + totalCopies);
        System.out.println("Available Copies: " + availableCopies);
        System.out.println("---");
    }

    public String toArchiveString() {
        return "  ISBN            : " + isbn + "\n"
            + "  Title           : " + title + "\n"
            + "  Author          : " + author + "\n"
            + "  Shelf Code      : " + shelfCode + "\n"
            + "  Copies (A/T)    : " + availableCopies + "/" + totalCopies + "\n";
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getShelfCode() {
        return shelfCode;
    }
}
