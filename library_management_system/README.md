# Library Management System - Project Structure

## How to Compile and Run
```bash
javac *.java
java LibraryApp
```

## File Structure

| File | Type | Description |
|------|------|-------------|
| MemberStatus.java | Enum | ACTIVE / SUSPENDED / CLOSED |
| Borrowable.java | Interface | borrow, return, transfer methods |
| Displayable.java | Interface | display() contract |
| DigitalAccessEnabled.java | Interface | digital library operations |
| BorrowTransaction.java | Class | Stores a single membership transaction |
| Book.java | Class | Catalog book model with stock tracking |
| LibraryCard.java | Class | Fine limit, add fine, pay fine |
| DigitalLibraryAccess.java | Class | Login-protected digital access |
| LibraryMember.java | Abstract Class | Base class for all membership tiers |
| BasicMember.java | Class | Borrow limit 3, capped renewals |
| PremiumMember.java | Class | Borrow limit 7 + library card |
| EliteMember.java | Class | Borrow limit 12 + card + digital access |
| Patron.java | Class | Holds patron details + list of memberships |
| BookTransferService.java | Class | Static transfer of borrowed books |
| ClosedMembershipArchive.java | Class | Saves closed records to closed_memberships.txt |
| LibraryApp.java | Main Class | Entry point and menu-driven app |

## Class Hierarchy

```text
LibraryMember (abstract)
|- BasicMember
|- PremiumMember  --> LibraryCard
|- EliteMember    --> LibraryCard
|               --> DigitalLibraryAccess

Patron --> List<LibraryMember>
BookTransferService (static utility)
ClosedMembershipArchive (static utility)
```

## Notes
- Last 5 transactions are retained per membership.
- Membership status supports auto-suspend after prolonged inactivity.
- Closed memberships are archived to `closed_memberships.txt`.
