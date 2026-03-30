import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LibraryMember implements Borrowable, Displayable {
    protected String memberId;
    protected String memberName;
    protected String memberType;
    protected MemberStatus status;
    protected LocalDate lastActivityDate;

    protected List<Book> borrowedBooks = new ArrayList<>();
    private List<BorrowTransaction> transactions = new ArrayList<>();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LibraryMember(String memberId, String memberName, String memberType) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberType = memberType;
        this.status = MemberStatus.ACTIVE;
        this.lastActivityDate = LocalDate.now();
    }

    protected abstract int getBorrowLimit();

    public void setActive() {
        if (status == MemberStatus.CLOSED) {
            System.out.println("Member #" + memberId + " is CLOSED and cannot be reactivated.");
            return;
        }
        status = MemberStatus.ACTIVE;
        System.out.println("Member #" + memberId + " is now ACTIVE.");
    }

    public void setSuspended() {
        if (status == MemberStatus.CLOSED) {
            System.out.println("Member #" + memberId + " is CLOSED.");
            return;
        }
        status = MemberStatus.SUSPENDED;
        System.out.println("Member #" + memberId + " is now SUSPENDED.");
    }

    public void checkAndAutoSuspend() {
        if (status == MemberStatus.ACTIVE
            && lastActivityDate != null
            && lastActivityDate.isBefore(LocalDate.now().minusYears(1))) {
            status = MemberStatus.SUSPENDED;
            System.out.println("[AUTO] Member #" + memberId
                + " set to SUSPENDED due to inactivity (over 1 year).");
        }
    }

    public boolean isActive() {
        checkAndAutoSuspend();
        return status == MemberStatus.ACTIVE;
    }

    public boolean isClosed() {
        return status == MemberStatus.CLOSED;
    }

    public MemberStatus getStatus() {
        return status;
    }

    @Override
    public boolean borrowBook(Book book) {
        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        if (!isActive()) {
            System.out.println("Cannot borrow. Member #" + memberId + " is " + status + ".");
            return false;
        }
        if (borrowedBooks.size() >= getBorrowLimit()) {
            System.out.println("Borrow limit reached for member #" + memberId + ".");
            return false;
        }
        if (!book.issueCopy()) {
            System.out.println("Book is currently unavailable.");
            return false;
        }

        borrowedBooks.add(book);
        recordTransaction(new BorrowTransaction(
            "BORROW", book.getIsbn(), book.getTitle(), "Borrowed successfully"
        ));
        System.out.println("Book borrowed: " + book.getTitle());
        return true;
    }

    @Override
    public boolean returnBook(Book book) {
        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        Iterator<Book> iterator = borrowedBooks.iterator();
        while (iterator.hasNext()) {
            Book b = iterator.next();
            if (b.getIsbn().equals(book.getIsbn())) {
                iterator.remove();
                book.returnCopy();
                recordTransaction(new BorrowTransaction(
                    "RETURN", book.getIsbn(), book.getTitle(), "Returned successfully"
                ));
                System.out.println("Book returned: " + book.getTitle());
                return true;
            }
        }
        System.out.println("This member does not have the requested book.");
        return false;
    }

    @Override
    public boolean transferIssuedBook(Book book, String toMemberId) {
        if (!isActive()) {
            System.out.println("Cannot transfer while member status is " + status + ".");
            return false;
        }
        if (book == null || toMemberId == null || toMemberId.isBlank()) {
            System.out.println("Transfer failed: invalid book or destination member.");
            return false;
        }
        Iterator<Book> iterator = borrowedBooks.iterator();
        while (iterator.hasNext()) {
            Book b = iterator.next();
            if (b.getIsbn().equals(book.getIsbn())) {
                iterator.remove();
                recordTransaction(new BorrowTransaction(
                    "TRANSFER_OUT", book.getIsbn(), book.getTitle(), "To Member#" + toMemberId
                ));
                return true;
            }
        }
        System.out.println("Transfer failed: source member does not hold this book.");
        return false;
    }

    @Override
    public void receiveTransferredBook(Book book, String fromMemberId) {
        borrowedBooks.add(book);
        recordTransaction(new BorrowTransaction(
            "TRANSFER_IN", book.getIsbn(), book.getTitle(), "From Member#" + fromMemberId
        ));
    }

    @Override
    public void printLastFiveTransactions() {
        System.out.println("\n--- Last " + transactions.size() + " Transaction(s) for Member: " + memberId + " ---");
        if (transactions.isEmpty()) {
            System.out.println("  No transactions recorded yet.");
        } else {
            for (BorrowTransaction transaction : transactions) {
                transaction.printStatement();
            }
        }
        System.out.println("------------------------------------------------------------");
    }

    protected void printStatusLine() {
        System.out.println("Status: " + status
            + (lastActivityDate != null
               ? "  |  Last Activity: " + lastActivityDate.format(DATE_FMT)
               : ""));
    }

    protected void recordTransaction(BorrowTransaction transaction) {
        transactions.add(transaction);
        if (transactions.size() > 5) {
            transactions.remove(0);
        }
        lastActivityDate = LocalDate.now();
    }

    public String toArchiveString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Member ID       : ").append(memberId).append("\n");
        sb.append("  Member Name     : ").append(memberName).append("\n");
        sb.append("  Member Type     : ").append(memberType).append("\n");
        sb.append("  Status          : ").append(status).append("\n");
        sb.append("  Last Activity   : ")
            .append(lastActivityDate != null ? lastActivityDate.format(DATE_FMT) : "N/A")
            .append("\n");
        sb.append("  Borrowed Books  : ").append(borrowedBooks.size()).append("\n");
        sb.append("  Last Transactions:\n");
        if (transactions.isEmpty()) {
            sb.append("    (none)\n");
        } else {
            for (BorrowTransaction transaction : transactions) {
                sb.append("    ").append(transaction.toArchiveString()).append("\n");
            }
        }
        return sb.toString();
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
