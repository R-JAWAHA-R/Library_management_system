public class PremiumMember extends LibraryMember {
    private static int cardCounter = 1000;
    private LibraryCard libraryCard;

    public PremiumMember(String memberId, String memberName) {
        super(memberId, memberName, "Premium Member");
        this.libraryCard = new LibraryCard("P-CARD-" + (++cardCounter), 150.0);
    }

    @Override
    protected int getBorrowLimit() {
        return 7;
    }

    public void renewAnyBook() {
        if (!isActive()) {
            System.out.println("Cannot renew while status is " + status + ".");
            return;
        }
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books available to renew.");
            return;
        }
        Book b = borrowedBooks.get(0);
        recordTransaction(new BorrowTransaction(
            "RENEW", b.getIsbn(), b.getTitle(), "Premium priority renewal"
        ));
        System.out.println("Renewed with premium priority: " + b.getTitle());
    }

    @Override
    public void display() {
        System.out.println("Member ID        : " + memberId);
        System.out.println("Name             : " + memberName);
        System.out.println("Type             : Premium Member");
        System.out.println("Borrow Limit     : 7");
        System.out.println("Borrowed Count   : " + borrowedBooks.size());
        printStatusLine();
        System.out.println("--- Library Card ---");
        libraryCard.display();
        System.out.println("---");
    }

    @Override
    public String toArchiveString() {
        return super.toArchiveString()
            + "  Tier            : Premium\n"
            + "  Library Card    : " + libraryCard.toArchiveString() + "\n";
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }
}
