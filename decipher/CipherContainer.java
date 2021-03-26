/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ist440.decipher;

/**
 *
 * @author chrismorris
 */
public class CipherContainer implements CipherMatch {
    
    private final String decodedMessage;
    
    public CipherContainer(String decodedMessage) {
        this.decodedMessage = decodedMessage;
    }
    
    @Override
    public boolean matches(String guess) {
        return decodedMessage.equals(guess);
    }
}
