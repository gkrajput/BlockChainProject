package com.gaurav.blockchain;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a BlockChain of Transactions
 * Scope of improvements:
 * 1. Genesis block can be taken as a constructor argument to give BlockChain creator freedom to define it.
 * 2. Rewards can be added for mining.
 * 3. Concurrency can be used to improve overall performance.
 * 4. Generics can be used instead of Transaction to make this BlockChain useful for other type of data a block can contain.
 * @author gkrajput
 *
 */
public class BlockChain {
	private ArrayList<Block> chain; // Holds the BlockChain
	private LinkedList<Transaction> pendingTransactions; // Holds the transaction to be added in BlockChain
	private int difficulty; // Defines difficulty level to be used in mining the blocks inside this BlockChain

	/**
	 * Constructor to initialize this BlockChain.
	 * Genesis block is added during initialization.
	 * @param difficulty Used to set difficulty field
	 */
	public BlockChain(int difficulty) {
		this.chain = new ArrayList<Block>();
		this.pendingTransactions = new LinkedList<Transaction>();
		chain.add(new Block(new Transaction(null, null, 0)));
		this.difficulty = difficulty;
	}

	/**
	 * Used to return latest block from the BlockChain
	 * @return latest block in chain
	 */
	public Block getLatestBlock() {
		return chain.get(chain.size() - 1);
	}
	
	/**
	 * Used to add new Transaction in pending transaction list
	 * @param newTxn Transaction to be added
	 */
	public void addTransaction(Transaction newTxn) {
		this.pendingTransactions.addFirst(newTxn);
	}
	
	/**
	 * Used to check if any pending Transaction is left for mining
	 * @return true if yes; otherwise false
	 */
	public boolean isTransactionPendingToMine() {
		return this.pendingTransactions.isEmpty() ? false : true;
	}
	
	/**
	 * Picks a pending transaction, using FIFO pattern
	 * Creates a block for this transaction
	 * Sets value of prevHash for new Block
	 * Mines new block and add it to BlockChain if mining is successful
	 * Removes processed transaction from PendingTransactions list
	 */
	public void minePendingTransaction() {
		Block block = new Block(this.pendingTransactions.getLast());
		block.setPrevHash(this.getLatestBlock().getHash());
		block.mineBlock(this.difficulty);
		this.pendingTransactions.removeLast();
		this.chain.add(block);	
	}
	
	/**
	 * Used to get balance of a person/address in BlockChain
	 * @param address address of the person
	 * @return current balance of the person as per transactions in BlockChain
	 */
	public long getBalance(String address) {
		long balance = 0;
		for(Block b: chain) {
			if(address.equals(b.getTransaction().getTo())) {
				balance = balance + b.getTransaction().getAmount();
			}
			if(address.equals(b.getTransaction().getFrom())) {
				balance = balance - b.getTransaction().getAmount();
			}
		}
		return balance;
	}
	
	/**
	 * Tells if BlockChain is valid or it has been compromised
	 * @return true if valid; otherwise false
	 */
	public boolean isChainValid() {
		boolean isValid = true;
		Block currentBlock;
		Block previousBlock;
		for(int i=1; i < this.chain.size(); i++) {
			previousBlock = this.chain.get(i-1);
			currentBlock = this.chain.get(i);
			
			if(!CommonUtil.byteArrayToHexString(currentBlock.getHash()).equals(CommonUtil.byteArrayToHexString(currentBlock.calculateHash()))) {
				isValid = false;
				break;
			}
			
			if(!CommonUtil.byteArrayToHexString(currentBlock.getPrevHash()).equals(CommonUtil.byteArrayToHexString(previousBlock.getHash()))) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * Prints BlockChain in JSON format
	 */
	public void printBlockChain() {
		System.out.println("{\n\t\"chain\": [");
		for(Block b: chain) {
			System.out.println("\t\t{");
			System.out.println("\t\t\t \"timestamp\": " + b.getTimestamp());
			System.out.println("\t\t\t \"transaction\": " + b.getTransaction().toString());
			System.out.println("\t\t\t \"hash\": " + CommonUtil.byteArrayToHexString(b.getHash()));
			System.out.println("\t\t\t \"prevHash\": " + CommonUtil.byteArrayToHexString(b.getPrevHash()));
			System.out.println("\t\t\t \"nonce\": " + b.getNonce());
			System.out.println("\t\t},");
		}
		System.out.println("\t]\n}");
	}

	public LinkedList<Transaction> getPendingTransactions() {
		return pendingTransactions;
	}

}
