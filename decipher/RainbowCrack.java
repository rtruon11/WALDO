package com.ist440.decipher;

import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chrismorris
 */
public class RainbowCrack {
    private final Map<String, String> hashes = new HashMap();

    public RainbowCrack(Cipher cipher) {
        String[] messages = new DictionaryAttack().getMessages();
        for (String message : messages) {
            hashes.put(cipher.encode(message), message);
        }
    }
    
    public boolean crack(String encodedString) {
        String decodedString = hashes.get(encodedString);
        return decodedString != null;
    }
}
