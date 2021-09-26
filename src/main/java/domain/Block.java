package domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class Block {
    private String previousHash;
    private String hash;
    private LocalDate timestamp;
    private ArrayList<Contract> contracts;
    private long nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = LocalDate.now();
        this.contracts = new ArrayList<>();
    }

    public String getHash() {
        return this.hash;
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public void mine() {
        Miner miner = new Miner(generateStrToHash());
        this.hash = miner.mine(Node.DIFFICULTY_FIXED);
        this.nonce = miner.getNonce();
    }

    private String generateStrToHash() {
        StringBuffer stringToHash = new StringBuffer();

        stringToHash.append(previousHash);

        for (Contract contract : contracts) {
            stringToHash.append(contract.generateStringToHash());
        }
        return stringToHash.toString();
    }

    public String transactionsAsStringList() {
        String s = "";
        boolean containsTransaction = false;

        if (!contracts.isEmpty()) {
            int count = 0;
            for (Contract contract : contracts) {
                if (contract instanceof Transaction) {
                    count++;
                    containsTransaction = true;
                    s += "\nTransaction " + count + ":" + contract + "\n";
                }
            }
            if (!containsTransaction) {
                s = "No Transactions in this block!";
            }
        }
        return s;
    }

    public String smartContractsAsStringList() {
        String s = "";
        boolean containsSmartContract = false;

        if (!contracts.isEmpty()) {
            int count = 0;
            for (Contract contract : contracts) {
                if (contract instanceof SmartContract) {
                    count++;
                    containsSmartContract = true;
                    s += "\nSmart Contract " + count + ":" + contract + "\n";
                }
            }
            if (!containsSmartContract) {
                s = "No Smart Contracts in this block!";
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return "\nPrevious Hash: " + previousHash +
                "\nHash: " + hash +
                "\nTimestamp: " + timestamp +
                "\nNonce: " + nonce +
                "\nDifficulty: " + Node.DIFFICULTY_FIXED +
                "\nNumber of contracts: " + contracts.size();
    }
}
