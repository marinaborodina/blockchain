package domain;

public class Miner {

    private String generateStrToHash;
    private long nonce;

    public Miner(String generateStrToHash) {
        this.generateStrToHash = generateStrToHash;
    }

    public long getNonce() {
        return nonce;
    }

    public String mine(String difficulty) {
        this.nonce = 0;
        boolean isHashFound = false;
        String foundHash = null;

        while (!isHashFound && nonce <= Long.MAX_VALUE) {
            foundHash = Encryptor.calculateHash(generateStrToHash + nonce);
            nonce++;

            if (foundHash.startsWith(difficulty)) {
                isHashFound = true;
            }
        }
        return foundHash;
    }
}
