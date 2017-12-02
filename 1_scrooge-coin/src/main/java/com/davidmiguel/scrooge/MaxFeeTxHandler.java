package com.davidmiguel.scrooge;

/**
 * Create a second file called MaxFeeTxHandler.java whose handleTxs() method finds a set of transactions with maximum
 * total transaction fees -- i.e. maximize the sum over all transactions in the set of (sum of input values - sum of
 * output values)).
 */
public class MaxFeeTxHandler extends TxHandler {
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public MaxFeeTxHandler(UTXOPool utxoPool) {
        super(utxoPool);
    }

    @Override
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // TODO
        return null;
    }
}
