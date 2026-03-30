public class EliteMember extends LibraryMember {
    private static int cardCounter = 5000;

    private LibraryCard libraryCard;
    private DigitalLibraryAccess digitalAccess;

    public EliteMember(String memberId, String memberName, String username, String password) {
        super(memberId, memberName, "Elite Member");
        this.libraryCard = new LibraryCard("E-CARD-" + (++cardCounter), 500.0);
        this.digitalAccess = new DigitalLibraryAccess(username, password, this);
    }

    @Override
    protected int getBorrowLimit() {
        return 12;
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
            "RENEW", b.getIsbn(), b.getTitle(), "Elite extended renewal"
        ));
        System.out.println("Renewed with elite extension: " + b.getTitle());
    }

    @Override
    public void display() {
        System.out.println("Member ID        : " + memberId);
        System.out.println("Name             : " + memberName);
        System.out.println("Type             : Elite Member");
        System.out.println("Borrow Limit     : 12");
        System.out.println("Borrowed Count   : " + borrowedBooks.size());
        printStatusLine();
        System.out.println("--- Library Card ---");
        libraryCard.display();
        System.out.println("--- Digital Access ---");
        System.out.println("Enabled          : Yes");
        System.out.println("---");
    }

    @Override
    public String toArchiveString() {
        return super.toArchiveString()
            + "  Tier            : Elite\n"
            + "  Library Card    : " + libraryCard.toArchiveString() + "\n"
            + "  Digital Access  : " + digitalAccess.toArchiveString() + "\n";
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public DigitalLibraryAccess getDigitalAccess() {
        return digitalAccess;
    }
}
