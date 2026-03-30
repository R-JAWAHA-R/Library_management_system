import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClosedMembershipArchive {
    private static final String ARCHIVE_FILE = "closed_memberships.txt";

    public static void save(Patron owner, LibraryMember member, String closeReason) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVE_FILE, true))) {
            pw.println("================================================================");
            pw.println("  CLOSED MEMBERSHIP RECORD - " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            pw.println("================================================================");
            pw.println("PATRON DETAILS:");
            pw.print(owner.toArchiveString());
            pw.println("\nMEMBERSHIP DETAILS:");
            pw.print(member.toArchiveString());
            pw.println("Close Reason     : " + closeReason);
            pw.println("----------------------------------------------------------------");
            pw.println();
            System.out.println("[Archive] Closed membership #" + member.getMemberId()
                + " details saved to '" + ARCHIVE_FILE + "'.");
        } catch (IOException | SecurityException ex) {
            System.out.println("[Archive ERROR] Could not write to " + ARCHIVE_FILE + ": " + ex.getMessage());
        }
    }
}
