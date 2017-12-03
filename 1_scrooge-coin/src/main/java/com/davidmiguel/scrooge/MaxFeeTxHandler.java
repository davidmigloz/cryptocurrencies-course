package com.davidmiguel.scrooge;

import java.util.*;

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
        Set<Transaction> orderedTxs = new TreeSet<>((tx1, tx2) -> {
            Double tx1Fee = calculateTxFee(tx1);
            Double tx2Fee = calculateTxFee(tx2);
            return tx1Fee.compareTo(tx2Fee);
        });
        Collections.addAll(orderedTxs, possibleTxs);
        return super.handleTxs(orderedTxs.toArray(new Transaction[orderedTxs.size()]));
    }

    /**
     * Calculates the transaction fee (sum of input values - sum of output values).
     */
    private double calculateTxFee(Transaction tx) {
        double totalInput = 0.0;
        double totalOutput = 0.0;
        // Calculate total input
        for (Transaction.Input input : tx.getInputs()) {
            UTXO prevOutput = new UTXO(input.prevTxHash, input.outputIndex);
            if(utxoPool.contains(prevOutput)) {
                totalInput += utxoPool.getTxOutput(prevOutput).value;
            }
        }
        // Calculate total output
        for (Transaction.Output output : tx.getOutputs()) {
            totalOutput += output.value;
        }
        return totalInput - totalOutput;
    }
}
