import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Patron> patrons = new ArrayList<>();
    private static final List<Book> catalog = new ArrayList<>();

    public static void main(String[] args) {
        seedCatalog();
        System.out.println("=== Advanced Library Management System ===");
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt();
            try {
                switch (choice) {
                    case 1:
                        createMembership();
                        break;
                    case 2:
                        addBookToCatalog();
                        break;
                    case 3:
                        borrowBook();
                        break;
                    case 4:
                        returnBook();
                        break;
                    case 5:
                        renewBorrowing();
                        break;
                    case 6:
                        transferBorrowedBook();
                        break;
                    case 7:
                        viewTransactionHistory();
                        break;
                    case 8:
                        viewPatron();
                        break;
                    case 9:
                        manageMembershipStatus();
                        break;
                    case 10:
                        manageLibraryCard();
                        break;
                    case 11:
                        manageDigitalLibrary();
                        break;
                    case 12:
                        showBooks();
                        break;
                    case 13:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid data provided: " + ex.getMessage());
            } catch (IllegalStateException ex) {
                System.out.println("Operation not allowed in current state: " + ex.getMessage());
            } catch (NullPointerException ex) {
                System.out.println("Operation failed due to missing data. Please try again.");
            } catch (RuntimeException ex) {
                System.out.println("Unexpected runtime error: " + ex.getMessage());
            }
        }

        scanner.close();
        System.out.println("Thank you for using Library Management System!");
    }

    private static void showMenu() {
        System.out.println("\n========================================================");
        System.out.println("            ADVANCED LIBRARY MANAGEMENT SYSTEM          ");
        System.out.println("========================================================");
        System.out.println("  1.  Create Membership");
        System.out.println("  2.  Add Book to Catalog");
        System.out.println("  3.  Borrow Book");
        System.out.println("  4.  Return Book");
        System.out.println("  5.  Renew Borrowing (tier-based)");
        System.out.println("  6.  Transfer Borrowed Book");
        System.out.println("  7.  View Last 5 Transactions");
        System.out.println("  8.  View Patron Details");
        System.out.println("  9.  Manage Membership Status");
        System.out.println("  10. Library Card Operations (Premium/Elite)");
        System.out.println("  11. Digital Library (Elite only)");
        System.out.println("  12. Show Books");
        System.out.println("  13. Exit");
        System.out.println("========================================================");
        System.out.print("Choose: ");
    }

    private static void createMembership() {
        System.out.println("\n-- Create Membership --");
        System.out.print("Full Name         : ");
        String name = scanner.nextLine();
        System.out.print("Contact No        : ");
        long contact = readLong();
        System.out.print("Email             : ");
        String email = scanner.nextLine();
        String category = chooseCategory();

        Patron patron = new Patron(name, contact, email, category);
        patrons.add(patron);
        System.out.println("Patron '" + name + "' created!");

        System.out.print("\nMembership ID     : ");
        String memberId = scanner.nextLine().trim();
        System.out.println("Membership Types:");
        System.out.println("  B  - Basic   (borrow limit 3)");
        System.out.println("  P  - Premium (borrow limit 7, + Library Card)");
        System.out.println("  E  - Elite   (borrow limit 12, + Card + Digital Library)");
        System.out.print("Type: ");
        String type = scanner.nextLine().trim().toUpperCase();

        switch (type) {
            case "B":
                patron.addMembership(new BasicMember(memberId, name));
                break;
            case "P":
                patron.addMembership(new PremiumMember(memberId, name));
                break;
            case "E":
                System.out.print("Digital Username  : ");
                String user = scanner.nextLine();
                System.out.print("Digital Password  : ");
                String pass = scanner.nextLine();
                patron.addMembership(new EliteMember(memberId, name, user, pass));
                break;
            default:
                System.out.println("Invalid membership type. Membership not created.");
        }
    }

    private static String chooseCategory() {
        while (true) {
            System.out.println("Category Options:");
            System.out.println("  1. Student");
            System.out.println("  2. Faculty");
            System.out.println("  3. Staff");
            System.out.println("  4. External Member");
            System.out.println("  5. Premium Patron");
            System.out.print("Choose Category (1-5): ");

            int choice = readInt();
            switch (choice) {
                case 1:
                    return "Student";
                case 2:
                    return "Faculty";
                case 3:
                    return "Staff";
                case 4:
                    return "External Member";
                case 5:
                    return "Premium Patron";
                default:
                    System.out.println("Invalid category choice. Please choose 1 to 5.");
            }
        }
    }

    private static void addBookToCatalog() {
        System.out.println("\n-- Add Book to Catalog --");
        System.out.print("ISBN              : ");
        String isbn = scanner.nextLine().trim();
        if (findBookByIsbn(isbn) != null) {
            System.out.println("Book with this ISBN already exists.");
            return;
        }
        System.out.print("Title             : ");
        String title = scanner.nextLine();
        System.out.print("Author            : ");
        String author = scanner.nextLine();
        System.out.print("Total Copies      : ");
        int copies = readInt();
        System.out.print("Shelf Code        : ");
        String shelf = scanner.nextLine();

        Book book = new Book(isbn, title, author, copies, shelf);
        catalog.add(book);
        System.out.println("Book added to catalog.");
        book.display();
    }

    private static void borrowBook() {
        System.out.print("\nMember ID         : ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member == null) {
            return;
        }

        System.out.print("Book ISBN         : ");
        Book book = findBookByIsbn(scanner.nextLine().trim());
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        member.borrowBook(book);
    }

    private static void returnBook() {
        System.out.print("\nMember ID         : ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member == null) {
            return;
        }

        System.out.print("Book ISBN         : ");
        Book book = findBookByIsbn(scanner.nextLine().trim());
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        member.returnBook(book);
    }

    private static void renewBorrowing() {
        System.out.print("\nMember ID         : ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member == null) {
            return;
        }

        if (member instanceof EliteMember) {
            ((EliteMember) member).renewAnyBook();
            return;
        }
        if (member instanceof PremiumMember) {
            ((PremiumMember) member).renewAnyBook();
            return;
        }
        if (member instanceof BasicMember) {
            ((BasicMember) member).renewAnyBook();
            return;
        }
        System.out.println("This membership type does not support renewals.");
    }

    private static void transferBorrowedBook() {
        System.out.println("\n-- Transfer Borrowed Book --");
        System.out.print("Source Member ID  : ");
        LibraryMember from = findMemberGlobally(scanner.nextLine().trim());
        if (from == null) {
            return;
        }

        System.out.print("Destination Member ID: ");
        LibraryMember to = findMemberGlobally(scanner.nextLine().trim());
        if (to == null) {
            return;
        }

        System.out.print("Book ISBN         : ");
        Book book = findBookByIsbn(scanner.nextLine().trim());
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        BookTransferService.transfer(from, to, book);
    }

    private static void viewTransactionHistory() {
        System.out.print("\nMember ID         : ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member != null) {
            member.printLastFiveTransactions();
        }
    }

    private static void viewPatron() {
        System.out.print("Membership ID to find patron: ");
        Patron patron = findPatronByMembership(scanner.nextLine().trim());
        if (patron != null) {
            patron.display();
        } else {
            System.out.println("Patron not found.");
        }
    }

    private static void manageMembershipStatus() {
        System.out.println("\n-- Manage Membership Status --");
        System.out.print("Membership ID     : ");
        String memberId = scanner.nextLine().trim();

        LibraryMember member = findMemberGlobally(memberId);
        Patron owner = findPatronByMembership(memberId);

        if (member == null || owner == null) {
            System.out.println("Membership not found.");
            return;
        }
        if (member.isClosed()) {
            System.out.println("Membership #" + memberId + " is already CLOSED.");
            return;
        }

        System.out.println("Membership #" + memberId + " is currently: " + member.getStatus());
        System.out.println("\nOptions:");
        System.out.println("  1. Set ACTIVE");
        System.out.println("  2. Set SUSPENDED");
        System.out.println("  3. CLOSE Membership");
        System.out.println("  4. Check if ACTIVE");
        System.out.print("Choose: ");

        int choice = readInt();
        switch (choice) {
            case 1:
                member.setActive();
                break;
            case 2:
                member.setSuspended();
                break;
            case 3:
                closeMembership(owner, member);
                break;
            case 4:
                System.out.println("Membership #" + memberId + " active? -> "
                    + (member.isActive() ? "YES (ACTIVE)" : "NO (" + member.getStatus() + ")"));
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageLibraryCard() {
        System.out.println("\n-- Library Card Operations --");
        System.out.print("Member ID (Premium/Elite): ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member == null) {
            return;
        }

        LibraryCard card = null;
        if (member instanceof EliteMember) {
            card = ((EliteMember) member).getLibraryCard();
        } else if (member instanceof PremiumMember) {
            card = ((PremiumMember) member).getLibraryCard();
        } else {
            System.out.println("Library card is only available for Premium and Elite members.");
            return;
        }

        System.out.println("\nCard Menu:");
        System.out.println("  1. View Card Details");
        System.out.println("  2. Add Fine");
        System.out.println("  3. Pay Fine");
        System.out.print("Choose: ");
        int choice = readInt();

        switch (choice) {
            case 1:
                card.display();
                break;
            case 2:
                System.out.print("Fine Amount: $");
                card.addFine(readDouble());
                break;
            case 3:
                System.out.printf("Outstanding fine: $%.2f%n", card.getOutstandingFine());
                System.out.print("Payment Amount: $");
                card.payFine(readDouble());
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageDigitalLibrary() {
        System.out.println("\n-- Digital Library (Elite Only) --");
        System.out.print("Elite Member ID   : ");
        LibraryMember member = findMemberGlobally(scanner.nextLine().trim());
        if (member == null) {
            return;
        }

        if (!(member instanceof EliteMember)) {
            System.out.println("Digital library is only available for Elite members.");
            return;
        }

        DigitalLibraryAccess access = ((EliteMember) member).getDigitalAccess();

        System.out.println("\nDigital Library Menu:");
        System.out.println("  1. Login");
        System.out.println("  2. View Member Summary Online");
        System.out.println("  3. Reserve Book Online");
        System.out.println("  4. Change Password");
        System.out.println("  5. Logout");
        System.out.print("Choose: ");
        int choice = readInt();

        switch (choice) {
            case 1:
                System.out.print("Username: ");
                String user = scanner.nextLine();
                System.out.print("Password: ");
                String pass = scanner.nextLine();
                access.loginDigitalLibrary(user, pass);
                break;
            case 2:
                access.viewMemberSummaryOnline();
                break;
            case 3:
                System.out.print("Book ISBN: ");
                String isbn = scanner.nextLine();
                access.reserveBookOnline(isbn);
                break;
            case 4:
                System.out.print("Old Password: ");
                String oldPass = scanner.nextLine();
                System.out.print("New Password: ");
                String newPass = scanner.nextLine();
                access.changeDigitalLibraryPassword(oldPass, newPass);
                break;
            case 5:
                access.logoutDigitalLibrary();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void showBooks() {
        System.out.println("\n-- Catalog Books --");
        if (catalog.isEmpty()) {
            System.out.println("No books available in the catalog.");
            return;
        }

        for (Book book : catalog) {
            book.display();
        }
    }

    private static void closeMembership(Patron owner, LibraryMember member) {
        if (member.isClosed()) {
            System.out.println("Membership #" + member.getMemberId() + " is already CLOSED.");
            return;
        }

        System.out.println("\nMembership #" + member.getMemberId() + " - Closing will auto-return all books.");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes")) {
            System.out.println("Close operation cancelled.");
            return;
        }

        List<Book> toReturn = new ArrayList<>(member.getBorrowedBooks());
        for (Book book : toReturn) {
            member.returnBook(book);
        }

        ClosedMembershipArchive.save(owner, member, "Membership closed by librarian");
        member.status = MemberStatus.CLOSED;

        System.out.println("Membership #" + member.getMemberId() + " has been CLOSED.");
        System.out.println("Membership data saved to 'closed_memberships.txt'.");
        System.out.println("This membership is no longer accessible.");
    }

    private static LibraryMember findMemberGlobally(String memberId) {
        for (Patron patron : patrons) {
            for (LibraryMember member : patron.getMemberships()) {
                if (member.getMemberId().equals(memberId)) {
                    if (member.isClosed()) {
                        System.out.println("Membership #" + memberId
                            + " does not exist (this membership has been closed).");
                        return null;
                    }
                    return member;
                }
            }
        }
        System.out.println("Membership #" + memberId + " not found.");
        return null;
    }

    private static Patron findPatronByMembership(String memberId) {
        for (Patron patron : patrons) {
            for (LibraryMember member : patron.getMemberships()) {
                if (member.getMemberId().equals(memberId)) {
                    return patron;
                }
            }
        }
        return null;
    }

    private static Book findBookByIsbn(String isbn) {
        for (Book book : catalog) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }
        return null;
    }

    private static int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid input. Please enter a numeric value: ");
            }
        }
    }

    private static long readLong() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid input. Please enter digits only: ");
            }
        }
    }

    private static void seedCatalog() {
        catalog.add(new Book("978-0134685991", "Games of Wajidh", "George RR Ali", 3, "CS-01"));
        catalog.add(new Book("978-0596009205", "Project Hail Wajidh", "Wajidh Gosling", 4, "CS-02"));
        catalog.add(new Book("978-0262033848", "Cracking Wajidh - A Oru Ruba Bus Tale", "Ali Giligan", 2, "CS-03"));
        catalog.add(new Book("978-0132350884", "Wajidh No way Home", "Tom Ali", 5, "SE-01"));
        catalog.add(new Book("978-0321356680", "Vaa Wajidh", "Naalan Ali", 2, "SE-02"));
        catalog.add(new Book("978-1491950357", "Wajidh Unchained", "Quentin Ali", 3, "DS-01"));
        catalog.add(new Book("978-1617294945", "Alitar", "Wajidh Cameron", 4, "JV-01"));
        catalog.add(new Book("978-0134494166", "50 Shades of Wajidh", "JamesALi", 3, "SE-03"));
        catalog.add(new Book("978-1098125978", "Wajidh Next Door", "Greenali", 4, "DB-01"));
        catalog.add(new Book("978-1492078005", "One Ali After Another", "Paul Thomali Wajidhson", 2, "CL-01"));
        catalog.add(new Book("978-9355426989", "Wadjidh Supreme", "Timoali Chawajidh", 6, "FI-01"));
        catalog.add(new Book("978-9355426996", "10 Things I hate about Ali", "Aajidh Wli", 5, "FI-02"));
        catalog.add(new Book("978-9355726996", "How To Stalk : 101", "Ali Stalker", 5, "FI-02"));

    }
}
