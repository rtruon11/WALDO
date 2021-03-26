package com.ist440.decipher;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chrismorris
 */
public class DictionaryAttack {
    
    private final String[] messages;

    public DictionaryAttack() {
        messages = new String[]{
            "Pennsylvania State University",
            "story",
            "my day",
            "excelence",
            "ts",
            "amigo"
        };
    }
    
    /**
     * @return the messages
     */
    public String[] getMessages() {
        return messages;
    }
    
    public boolean attack(CipherMatch matcher) {
        for (String message : getMessages()) {
            if (matcher.matches(message)) {
                return true;
            }
        }
        return false;
    }
}
