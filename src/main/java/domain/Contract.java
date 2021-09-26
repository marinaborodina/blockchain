package domain;

import exceptions.InvalidPublicKeyException;

public class Contract {

    private String hash;
    private String sender;
    private String signature;

    public Contract(String sender) throws InvalidPublicKeyException {
        setSender(sender);
        this.hash = generateHash();
        this.signature = checkSignature();
    }

    public String getSender() {
        return sender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSender(String sender) throws InvalidPublicKeyException {
        if (sender.length() > 20) {
            this.sender = sender;
        } else {
            throw new InvalidPublicKeyException();
        }
    }

    public String generateStringToHash() {
        StringBuffer strToHash = new StringBuffer();
        strToHash.append(sender + signature);
        return strToHash.toString();
    }

    private String generateHash() {
        return Encryptor.calculateHash(generateStringToHash());
    }


    private String checkSignature() {
        AsymKrypt krypt = new AsymKrypt("ContractKey", true);

        byte[] signature = krypt.signData(hash);

        boolean isValid = AsymKrypt.verify(signature, hash.getBytes(), krypt.getPublicKey());

        if (isValid) {
            return "VALID";
        } else {
            return "INVALID";
        }
    }

    @Override
    public String toString() {
        return "\nsender:" + sender +
                "\nsignature: " + signature;
    }
}

