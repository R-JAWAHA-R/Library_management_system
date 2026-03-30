import java.util.ArrayList;
import java.util.List;

public class Patron implements Displayable {
    private String fullName;
    private long contactNo;
    private String email;
    private String membershipCategory;
    private List<LibraryMember> memberships;

    public Patron(String fullName, long contactNo, String email, String membershipCategory) {
        this.fullName = fullName;
        this.contactNo = contactNo;
        this.email = email;
        this.membershipCategory = membershipCategory;
        this.memberships = new ArrayList<>();
    }

    public void addMembership(LibraryMember member) {
        memberships.add(member);
        System.out.println("Membership #" + member.getMemberId() + " added successfully to " + fullName);
    }

    @Override
    public void display() {
        System.out.println("\n=== Patron Details ===");
        System.out.println("Name             : " + fullName);
        System.out.println("Contact          : " + contactNo);
        System.out.println("Email            : " + email);
        System.out.println("Category         : " + membershipCategory);
        System.out.println("\nMemberships:");
        boolean hasVisible = false;
        for (LibraryMember member : memberships) {
            if (!member.isClosed()) {
                member.display();
                hasVisible = true;
            }
        }
        if (!hasVisible) {
            System.out.println("  No active memberships linked.");
        }
    }

    public String toArchiveString() {
        return "Name             : " + fullName + "\n"
            + "Contact          : " + contactNo + "\n"
            + "Email            : " + email + "\n"
            + "Category         : " + membershipCategory + "\n";
    }

    public String getFullName() {
        return fullName;
    }

    public List<LibraryMember> getMemberships() {
        return memberships;
    }
}
