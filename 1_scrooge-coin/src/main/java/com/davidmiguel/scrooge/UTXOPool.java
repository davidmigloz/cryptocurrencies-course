package com.davidmiguel.scrooge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Further, you will be provided with a UTXOPool class that represents the current set of outstanding UTXOs and contains
 * a map from each UTXO to its corresponding transaction output. This class contains constructors to create a new empty
 * UTXOPool or a copy of a given UTXOPool, and methods to add and remove UTXOs from the pool, get the output
 * corresponding to a given UTXO, check if a UTXO is in the pool, and get a list of all UTXOs in the pool.
 */
public class UTXOPool {

    /**
     * The current collection of UTXOs, with each one mapped to its corresponding transaction output
     */
    private HashMap<UTXO, Transaction.Output> H;

    /**
     * Creates a new empty UTXOPool
     */
    public UTXOPool() {
        H = new HashMap<>();
    }

    /**
     * Creates a new UTXOPool that is a copy of {@code uPool}
     */
    public UTXOPool(UTXOPool uPool) {
        H = new HashMap<>(uPool.H);
    }

    /**
     * Adds a mapping from UTXO {@code utxo} to transaction output @code{txOut} to the pool
     */
    public void addUTXO(UTXO utxo, Transaction.Output txOut) {
        H.put(utxo, txOut);
    }

    /**
     * Removes the UTXO {@code utxo} from the pool
     */
    public void removeUTXO(UTXO utxo) {
        H.remove(utxo);
    }

    /**
     * @return the transaction output corresponding to UTXO {@code utxo}, or null if {@code utxo} is
     * not in the pool.
     */
    public Transaction.Output getTxOutput(UTXO ut) {
        return H.get(ut);
    }

    /**
     * @return true if UTXO {@code utxo} is in the pool and false otherwise
     */
    public boolean contains(UTXO utxo) {
        return H.containsKey(utxo);
    }

    /**
     * Returns an {@code ArrayList} of all UTXOs in the pool
     */
    public ArrayList<UTXO> getAllUTXO() {
        Set<UTXO> setUTXO = H.keySet();
        ArrayList<UTXO> allUTXO = new ArrayList<UTXO>();
        for (UTXO ut : setUTXO) {
            allUTXO.add(ut);
        }
        return allUTXO;
    }
}
