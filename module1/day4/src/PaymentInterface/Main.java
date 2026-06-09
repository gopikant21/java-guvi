package PaymentInterface;

public class Main {
    public static void main(String[] args) {
        CreditCard creditCard = new CreditCard();
        creditCard.pay(500);

        UPI upi = new UPI();
        upi.pay(600);
    }
}
