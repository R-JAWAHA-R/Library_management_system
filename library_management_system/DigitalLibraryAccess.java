public class DigitalLibraryAccess implements DigitalAccessEnabled {
    private String username;
    private String password;
    private boolean loggedIn;
    private LibraryMember linkedMember;

    public DigitalLibraryAccess(String username, String password, LibraryMember linkedMember) {
        this.username = username;
        this.password = password;
        this.linkedMember = linkedMember;
        this.loggedIn = false;
    }

    @Override
    public void loginDigitalLibrary(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            loggedIn = true;
            System.out.println("Digital library login successful.");
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    @Override
    public void viewMemberSummaryOnline() {
        if (!loggedIn) {
            printNotLoggedIn();
            return;
        }
        System.out.println("\n=== Online Member Summary ===");
        linkedMember.display();
    }

    @Override
    public void reserveBookOnline(String isbn) {
        if (!loggedIn) {
            printNotLoggedIn();
            return;
        }
        if (isbn == null || isbn.isBlank()) {
            System.out.println("ISBN is required for reservation.");
            return;
        }
        System.out.println("Reservation request received for ISBN " + isbn + ".");
        System.out.println("A librarian will validate stock and notify the member.");
    }

    @Override
    public void changeDigitalLibraryPassword(String oldPassword, String newPassword) {
        if (!loggedIn) {
            printNotLoggedIn();
            return;
        }
        if (!password.equals(oldPassword)) {
            System.out.println("Old password is incorrect.");
            return;
        }
        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("New password must be at least 4 characters long.");
            return;
        }
        password = newPassword;
        System.out.println("Digital library password changed successfully.");
    }

    @Override
    public void logoutDigitalLibrary() {
        loggedIn = false;
        System.out.println("Logged out from digital library.");
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String toArchiveString() {
        return "Username: " + username + " | LoggedIn: " + loggedIn;
    }

    private void printNotLoggedIn() {
        System.out.println("Please login to digital library first.");
    }
}
