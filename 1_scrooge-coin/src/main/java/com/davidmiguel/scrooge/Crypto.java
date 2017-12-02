package com.davidmiguel.scrooge;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * To verify a signature, you will use the verifySignature() method included in the provided file Crypto.java.
 * <p>
 * This method takes a public key, a message and a signature, and returns true if and only signature correctly verifies
 * over message with the public key pubKey.
 * <p>
 * Note that you are only given code to verify signatures, and this is all that you will need for this assignment. The
 * computation of signatures is done outside the Transaction class by an entity that knows the appropriate private keys.
 * <p>
 * A transaction consists of a list of inputs, a list of outputs and a unique ID (see the getRawTx() method). The class
 * also contains methods to add and remove an input, add an output, compute digests to sign/hash, add a signature to an
 * input, and compute and store the hash of the transaction once all inputs/outputs/signatures have been added.
 */
public class Crypto {

    /**
     * @return true is {@code signature} is a valid digital signature of {@code message} under the
     * key {@code pubKey}. Internally, this uses RSA signature, but the student does not
     * have to deal with any of the implementation details of the specific signature
     * algorithm
     */
    public static boolean verifySignature(PublicKey pubKey, byte[] message, byte[] signature) {
        Signature sig = null;
        try {
            sig = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sig.initVerify(pubKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            sig.update(message);
            return sig.verify(signature);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;

    }
}
