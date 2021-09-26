package domain;

import exceptions.InvalidPublicKeyException;

public class Transaction extends Contract {

    String receiver;
    double amount;

    public Transaction(String sender, String receiver, double amount) throws InvalidPublicKeyException {
        super(sender);
        setReceiver(receiver);
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setReceiver(String receiver) throws InvalidPublicKeyException {
        if (receiver.length() > 20) {
            this.receiver = receiver;
        } else {
            throw new InvalidPublicKeyException();
        }
    }

    public String generateStringToHash() {
        return super.generateStringToHash() + receiver + amount;
    }

    @Override
    public String toString() {
        return "\nSender: " + getSender() +
                "\nReceiver: " + receiver +
                "\nAmount: " + amount +
                "\nSignature: " + getSignature();
    }
}
