/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ist440.decipher;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chrismorris
 */
public class CaesarCipher implements Cipher {
    
    public static final int DEFAULT_NUMBER_OF_POSITIONS = 3;

    private final HashMap<Character, Integer> capitalLetterToPosition = new HashMap<>();
    private final HashMap<Integer, Character> capitalPositionToLetter = new HashMap<>();
    private final HashMap<Character, Integer> lowercaseLetterToPosition = new HashMap<>();
    private final HashMap<Integer, Character> lowercasePositionToLetter = new HashMap<>();
    private final int numberOfPositions;
    private final int numberOfLettersInAlphabet;
    
    public CaesarCipher(int numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
        
        capitalLetterToPosition.put('A', 0);
        capitalLetterToPosition.put('B', 1);
        capitalLetterToPosition.put('C', 2);
        capitalLetterToPosition.put('D', 3);
        capitalLetterToPosition.put('E', 4);
        capitalLetterToPosition.put('F', 5);
        capitalLetterToPosition.put('G', 6);
        capitalLetterToPosition.put('H', 7);
        capitalLetterToPosition.put('I', 8);
        capitalLetterToPosition.put('J', 9);
        capitalLetterToPosition.put('K', 10);
        capitalLetterToPosition.put('L', 11);
        capitalLetterToPosition.put('M', 12);
        capitalLetterToPosition.put('N', 13);
        capitalLetterToPosition.put('O', 14);
        capitalLetterToPosition.put('P', 15);
        capitalLetterToPosition.put('Q', 16);
        capitalLetterToPosition.put('R', 17);
        capitalLetterToPosition.put('S', 18);
        capitalLetterToPosition.put('T', 19);
        capitalLetterToPosition.put('U', 20);
        capitalLetterToPosition.put('V', 21);
        capitalLetterToPosition.put('W', 22);
        capitalLetterToPosition.put('X', 23);
        capitalLetterToPosition.put('Y', 24);
        capitalLetterToPosition.put('Z', 25);
        
        numberOfLettersInAlphabet = capitalLetterToPosition.size();
        
        for (Map.Entry<Character, Integer> entry : capitalLetterToPosition.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            capitalPositionToLetter.put(value, key);
        }
         
        for (Map.Entry<Character, Integer> entry : capitalLetterToPosition.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            lowercaseLetterToPosition.put(Character.toLowerCase(key), value);
        }
        
        for (Map.Entry<Character, Integer> entry : lowercaseLetterToPosition.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            lowercasePositionToLetter.put(value, key);
        }
    }
    
    /**
     * @return the numberOfPositions
     */
    public int getNumberOfPositions() {
        return numberOfPositions;
    }

    /**
     * @return the numberOfLettersInAlphabet
     */
    public int getNumberOfLettersInAlphabet() {
        return numberOfLettersInAlphabet;
    }
    
    public String encode(String decodedString) {
        StringBuilder encoded = new StringBuilder(decodedString.length());
        for (int x = 0; x < decodedString.length(); x++) {
            char character = decodedString.charAt(x);
            Integer positionInAlphabet = capitalLetterToPosition.get(character) == null 
                    ? lowercaseLetterToPosition.get(character) 
                    : capitalLetterToPosition.get(character);
            
            if (positionInAlphabet == null) {
                encoded.append(character);
                continue;
            }
            Integer shiftedPosition = positionInAlphabet + getNumberOfPositions();
            if (shiftedPosition >= getNumberOfLettersInAlphabet()) {
                shiftedPosition -= getNumberOfLettersInAlphabet();
            }
            boolean isLowercase = lowercaseLetterToPosition.get(character) != null;
            Character shiftedCharacter = isLowercase 
                    ? lowercasePositionToLetter.get(shiftedPosition) 
                    : capitalPositionToLetter.get(shiftedPosition);
            if (shiftedCharacter != null) {
                encoded.append(shiftedCharacter);
            }
        }
        return encoded.toString();
    }
    
    public String decode(String encodedString) {
        StringBuilder decoded = new StringBuilder(encodedString.length());
        for (int x = 0; x < encodedString.length(); x++) {
            char character = encodedString.charAt(x);
            final Integer currentPosition = capitalLetterToPosition.get(character) != null 
                    ? capitalLetterToPosition.get(character) 
                    : lowercaseLetterToPosition.get(character);
            if (currentPosition == null) {
                decoded.append(character);
                continue;
            }
            Integer shiftedPosition = currentPosition - getNumberOfPositions();
            if (shiftedPosition < 0) {
                shiftedPosition += getNumberOfLettersInAlphabet();
            }
            boolean isLowercase = lowercaseLetterToPosition.get(character) != null;
            Character shiftedCharacter = isLowercase 
                    ? lowercasePositionToLetter.get(shiftedPosition) 
                    : capitalPositionToLetter.get(shiftedPosition);
            if (shiftedCharacter != null) {
                decoded.append(shiftedCharacter);
            }
        }
        return decoded.toString();
    }

}
