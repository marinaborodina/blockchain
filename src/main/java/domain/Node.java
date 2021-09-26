package domain;

import exceptions.InvalidCodeException;
import exceptions.InvalidPublicKeyException;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Node {
    private ArrayList<Block> blockChain;
    private ArrayList<Contract> memPool;
    private Block genesisBlock;
    public static final String DIFFICULTY_FIXED = Integer.toString(ThreadLocalRandom.current().nextInt(0, 10001));

    public Node() {
        this.blockChain = new ArrayList<>();
        this.memPool = new ArrayList<>();
        this.genesisBlock = new Block("000000000000000000000000000");
        genesisBlock.mine();
        blockChain.add(genesisBlock);
    }

    public ArrayList<Contract> getMemPool() {
        return memPool;
    }

    public ArrayList<Block> getBlockChain() {
        return blockChain;
    }

    public void newBlock() {

        String previousHash = blockChain.get(blockChain.size() - 1).getHash();
        Block newBlock = new Block(previousHash);

        for (Contract c : memPool) {
            newBlock.addContract(c);
        }

        newBlock.mine();
        blockChain.add(newBlock);

        memPool.clear();
    }

    public void newTransaction(String sender, String receiver, double amount) throws InvalidPublicKeyException, NumberFormatException {
        Transaction transaction = new Transaction(sender, receiver, amount);
        memPool.add(transaction);
    }

    public void newSmartContract(String sender, String code) throws InvalidPublicKeyException, InvalidCodeException {
        SmartContract smartContract = new SmartContract(sender, code);
        memPool.add(smartContract);
    }

    @Override
    public String toString() {
        String output = "";
        int count = 1;

        for (Block block : blockChain) {
            output += "# Block " + count + block.toString() + "\n";
            count++;
        }
        return output;
    }

    public String showMempool() {
        String output = "";
        for (Contract c : memPool) {
            output += "\n" + c.toString();
        }
        return output;
    }
}
