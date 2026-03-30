public class BasicMember extends LibraryMember {
    private int renewalsThisMonth;
    private static final int MAX_RENEWALS = 2;

    public BasicMember(String memberId, String memberName) {
        super(memberId, memberName, "Basic Member");
        this.renewalsThisMonth = 0;
    }

    @Override
    protected int getBorrowLimit() {
        return 3;
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
        if (renewalsThisMonth >= MAX_RENEWALS) {
            System.out.println("Monthly renewal limit reached for Basic members.");
            return;
        }
        renewalsThisMonth++;
        Book b = borrowedBooks.get(0);
        recordTransaction(new BorrowTransaction(
            "RENEW", b.getIsbn(), b.getTitle(), "Basic renewal count: " + renewalsThisMonth
        ));
        System.out.println("Renewed: " + b.getTitle());
    }

    @Override
    public void display() {
        System.out.println("Member ID        : " + memberId);
        System.out.println("Name             : " + memberName);
        System.out.println("Type             : Basic Member");
        System.out.println("Borrow Limit     : 3");
        System.out.println("Borrowed Count   : " + borrowedBooks.size());
        System.out.println("Renewals (Month) : " + renewalsThisMonth + "/" + MAX_RENEWALS);
        printStatusLine();
        System.out.println("---");
    }

    @Override
    public String toArchiveString() {
        return super.toArchiveString()
            + "  Renewal Policy  : Basic (2 renewals/month)\n";
    }
}
