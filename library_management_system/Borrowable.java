public interface Borrowable {
    boolean borrowBook(Book book);
    boolean returnBook(Book book);
    boolean transferIssuedBook(Book book, String toMemberId);
    void receiveTransferredBook(Book book, String fromMemberId);
    void printLastFiveTransactions();
}
