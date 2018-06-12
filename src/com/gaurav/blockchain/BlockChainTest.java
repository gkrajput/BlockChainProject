package com.gaurav.blockchain;

public class BlockChainTest {

	public static void main(String[] args) {
		BlockChain bc = new BlockChain(2);
		
		bc.addTransaction(new Transaction("A", "B", 100));
		bc.addTransaction(new Transaction("A", "B", 100));
		bc.addTransaction(new Transaction("B", "A", 50));
		bc.addTransaction(new Transaction("A", "C", 10));
		
		while(bc.isTransactionPendingToMine()){
			bc.minePendingTransaction();
		}
		
		bc.printBlockChain();
		System.out.printf("Is BlockChain valid? %s %n", bc.isChainValid()?"Yes":"No");
		System.out.printf("Balance of A: %d %n", bc.getBalance("A"));
		System.out.printf("Balance of B: %d %n", bc.getBalance("B"));
		System.out.printf("Balance of C: %d %n", bc.getBalance("C"));
	}

}
