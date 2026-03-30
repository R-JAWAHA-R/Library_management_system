public class BookTransferService {
    public static void transfer(LibraryMember from, LibraryMember to, Book book) {
        if (from == null || to == null || book == null) {
            System.out.println("Transfer failed: source, destination, or book is missing.");
            return;
        }
        if (from.getMemberId().equals(to.getMemberId())) {
            System.out.println("Cannot transfer a borrowed book to the same member.");
            return;
        }
        try {
            if (from.transferIssuedBook(book, to.getMemberId())) {
                to.receiveTransferredBook(book, from.getMemberId());
                System.out.printf("Transfer successful! Book '%s' moved from Member#%s to Member#%s%n",
                    book.getTitle(), from.getMemberId(), to.getMemberId());
            } else {
                System.out.println("Transfer failed: source member does not hold this book or status invalid.");
            }
        } catch (RuntimeException ex) {
            System.out.println("Transfer failed due to an unexpected error: " + ex.getMessage());
        }
    }
}
