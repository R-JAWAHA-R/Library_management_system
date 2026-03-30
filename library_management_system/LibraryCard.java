public class LibraryCard implements Displayable {
    private String cardNumber;
    private double fineLimit;
    private double outstandingFine;

    public LibraryCard(String cardNumber, double fineLimit) {
        this.cardNumber = cardNumber;
        this.fineLimit = Math.max(0, fineLimit);
        this.outstandingFine = 0.0;
    }

    public void addFine(double amount) {
        if (amount <= 0) {
            System.out.println("Fine amount must be positive.");
            return;
        }
        if (outstandingFine + amount > fineLimit) {
            System.out.printf("Fine cannot be added. Limit exceeded (Limit: $%.2f).%n", fineLimit);
            return;
        }
        outstandingFine += amount;
        System.out.printf("Fine added: $%.2f. Outstanding fine: $%.2f%n", amount, outstandingFine);
    }

    public void payFine(double amount) {
        if (amount <= 0) {
            System.out.println("Payment must be positive.");
            return;
        }
        if (amount > outstandingFine) {
            System.out.printf("Payment adjusted to outstanding balance: $%.2f%n", outstandingFine);
            amount = outstandingFine;
        }
        outstandingFine -= amount;
        System.out.printf("Fine payment successful: $%.2f. Remaining fine: $%.2f%n", amount, outstandingFine);
    }

    @Override
    public void display() {
        System.out.println("Card Number      : " + cardNumber);
        System.out.printf("Fine Limit       : $%.2f%n", fineLimit);
        System.out.printf("Outstanding Fine : $%.2f%n", outstandingFine);
    }

    public String toArchiveString() {
        return "Card#" + cardNumber + " | Limit: $" + String.format("%.2f", fineLimit)
            + " | Outstanding: $" + String.format("%.2f", outstandingFine);
    }

    public double getOutstandingFine() {
        return outstandingFine;
    }
}
