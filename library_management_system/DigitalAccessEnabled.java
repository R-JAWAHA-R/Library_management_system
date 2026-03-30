public interface DigitalAccessEnabled {
    void loginDigitalLibrary(String username, String password);
    void viewMemberSummaryOnline();
    void reserveBookOnline(String isbn);
    void changeDigitalLibraryPassword(String oldPassword, String newPassword);
    void logoutDigitalLibrary();
}
