package com.davidmiguel.scrooge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        return doClaimedOutputsExist(tx)
                && areInputsSignaturesValid(tx)
                && hasNoDoubleSpending(tx)
                && areAllOutputsValuesPositive(tx)
                && noExtraCoins(tx);
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        List<Transaction> acceptedTxs = new ArrayList<>(possibleTxs.length);
        for (Transaction tx : possibleTxs) {
            if (isValidTx(tx)) {
                removeSpentTxOutFromUTXOPool(tx);
                addTransactionToUTXOPool(tx);
                acceptedTxs.add(tx);
            }
        }
        return acceptedTxs.toArray(new Transaction[acceptedTxs.size()]);
    }

    /**
     * Checks whether all outputs claimed by {@code tx} are in the current UTXO pool.
     */
    private boolean doClaimedOutputsExist(Transaction tx) {
        for (Transaction.Input input : tx.getInputs()) {
            UTXO prevUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            if (!utxoPool.contains(prevUTXO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the signatures on each input of {@code tx} are valid.
     */
    private boolean areInputsSignaturesValid(Transaction tx) {
        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO prevUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output prevTxOutput = utxoPool.getTxOutput(prevUTXO);
            if (!Crypto.verifySignature(prevTxOutput.address, tx.getRawDataToSign(i), input.signature)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether no UTXO is claimed multiple times by {@code tx}.
     */
    private boolean hasNoDoubleSpending(Transaction tx) {
        Set<UTXO> prevUTXOs = new HashSet<>(tx.getInputs().size());
        for (Transaction.Input input : tx.getInputs()) {
            UTXO prevUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            if (!prevUTXOs.add(prevUTXO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether all of {@code tx}s output values are non-negative.
     */
    private boolean areAllOutputsValuesPositive(Transaction tx) {
        for (Transaction.Output output : tx.getOutputs()) {
            if (output.value < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the sum of {@code tx}s input values is greater
     * than or equal to the sum of its output values.
     */
    private boolean noExtraCoins(Transaction tx) {
        double sumInputs = 0.0;
        double sumOutputs = 0.0;
        for (Transaction.Input input : tx.getInputs()) {
            UTXO prevUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output prevTxOutput = utxoPool.getTxOutput(prevUTXO);
            sumInputs += prevTxOutput.value;
        }
        for (Transaction.Output output : tx.getOutputs()) {
            sumOutputs += output.value;
        }
        return sumInputs >= sumOutputs;
    }

    /**
     * Adds all the outputs of the transaction to the UTXO pool.
     */
    private void addTransactionToUTXOPool(Transaction tx) {
        for (int i = 0; i < tx.getOutputs().size(); i++) {
            Transaction.Output output = tx.getOutput(i);
            utxoPool.addUTXO(new UTXO(tx.getHash(), i), output);
        }
    }

    /**
     * Removes spent transactions outputs from UTXO pool.
     */
    private void removeSpentTxOutFromUTXOPool(Transaction tx) {
        for (Transaction.Input input : tx.getInputs()) {
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            utxoPool.removeUTXO(utxo);
        }
    }
}
