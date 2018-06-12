package com.gaurav.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a block in BlockChain
 * Scope of improvements:
 * 1. Multiple transactions can be supported.
 * 2. PrevHash can be used in hash calculation.
 * 3. Logging can be used instead of System.out.println statements.
 * 4. Mine block can be updated to return a boolean value to state mining status.
 * 5. Concurrency can be used to improve the mining process.
 * 6. nonce can be used as long for wider range of values.
 * 7. Generics can be used instead of using Transaction class.
 * @author gkrajput
 *
 */
public class Block {
	private long timestamp; // Time-stamp in milliseconds when Block was created
	private byte[] hash; // Hash of current block
	private Transaction transaction; // Transaction in current block
	private byte[] prevHash; // Hash of previous block. Genesis block will have prevHash as 'null'
	private int nonce; // Used to generate hash with unique pattern
	
	/**
	 * Constructor to initialize a block. Later BlockChain will update the hash and prevHash
	 * before adding a block into BlockChain
	 * @param transaction Transaction data for this block
	 */
	public Block(Transaction transaction) {
		this.transaction = transaction;
		this.prevHash = null;
		this.nonce = 0;
		this.timestamp = System.currentTimeMillis();
		this.hash = this.calculateHash();
	}
	
	/**
	 * Calculates hash for this block using SHA-256 algorithm
	 * @return Hash for this block
	 */
	public byte[] calculateHash() {
			byte[] hash = null;
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				hash = md.digest((this.timestamp + this.transaction.toString() + this.nonce).getBytes());
			} catch (NoSuchAlgorithmException e) {
				System.out.println("No algorithm found with a name used to generate hash!");
			}
			return hash;	
	}
	
	/**
	 * Mine this block as per given difficulty
	 * To generate unique hash each time, nonce is incremented by 1 in each iteration of while loop
	 * @param difficulty Used to decide the pattern to be matched in generated hash
	 */
	public void mineBlock(int difficulty) {
		StringBuffer pattern = new StringBuffer();
    	for (int i=0;i<difficulty;i++) {
    	  pattern.append("0");
    	}
    	
    	// long startTime = System.currentTimeMillis();
		while (!CommonUtil.byteArrayToHexString(this.hash).startsWith(pattern.toString())) {
			this.nonce++;
			this.hash = this.calculateHash();
		}
		
		// System.out.printf("Mining completed in %d milliseconds %n", System.currentTimeMillis() - startTime);
	}

	public byte[] getHash() {
		return hash;
	}

	public byte[] getPrevHash() {
		return prevHash;
	}

	public void setPrevHash(byte[] prevHash) {
		this.prevHash = prevHash;
	}

	public int getNonce() {
		return nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public String toString() {
		return "Block [timestamp=" + timestamp + ", hash=" + CommonUtil.byteArrayToHexString(getHash()) + ", transaction=" + transaction.toString() + ", prevHash="
				+ CommonUtil.byteArrayToHexString(getPrevHash()) + ", nonce=" + nonce + "]";
	}

}
