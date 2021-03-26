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
public class ExhaustiveSearch {
    
    private final String possibleValues;
    private boolean matching;

    public ExhaustiveSearch(String possibleValues) {
        this.matching = false;
        this.possibleValues = possibleValues;
    }
    
    /**
     * @return the matching
     */
    public boolean isMatching() {
        return matching;
    }
    
    public void findMatch(int inputLength, CipherMatch matcher) {
        matching = false;
        findMatch("", inputLength, matcher);
    }
    
    /**
     * 
     * Extends the functionality of the algorithm found at:
     * https://introcs.cs.princeton.edu/java/23recursion/Permutations.java.html
     * 
     */
    private void findMatch(String current, int inputLength, CipherMatch matcher) {
        if (current.length() == inputLength) {
            if (matcher.matches(current)) {
                matching = true;
            }
            return;
        }
        for (int i = 0; i < possibleValues.length(); i++) {
            findMatch(current + possibleValues.charAt(i), inputLength, matcher);
            if (isMatching()) {
                return;
            }
        }
    }
    
}
