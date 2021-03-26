/*
 * Copyright 2018 chrismorris.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ist440.decipher;

/**
 *
 * @author chrismorris
 */
public class CompoundDecipher {
    
    private final String cipheredMessage;
    
    /**
     * @return the cipheredMessage
     */
    public String getCipheredMessage() {
        return cipheredMessage;
    }

    public CompoundDecipher(String cipheredMessage) {
        this.cipheredMessage = cipheredMessage;
    }
    
    public CompoundDecipherResult decipher() {
        CompoundDecipherResult result = new CompoundDecipherResult();
        
        String possibleValues = "abcdefghijklmnopqrstuvwxyz0123456789 ";
        
        CaesarCipher cipher = new CaesarCipher(CaesarCipher.DEFAULT_NUMBER_OF_POSITIONS);
        CipherContainer container = new CipherContainer(cipher.decode(getCipheredMessage()));
        
        ExhaustiveSearch exhaustiveSearch = new ExhaustiveSearch(possibleValues);
        exhaustiveSearch.findMatch(getCipheredMessage().length(), container);
        if (exhaustiveSearch.isMatching()) {
            result.getSuccessfulProcesses().add(BruteForceProcess.EXHAUSTIVE_SEARCH);
        } else {
            result.getFailingProcesses().add(BruteForceProcess.EXHAUSTIVE_SEARCH);
        }
        
        DictionaryAttack dictionaryAttack = new DictionaryAttack();
        boolean isDictionaryAttackSuccessful = dictionaryAttack.attack(container);
        if (isDictionaryAttackSuccessful) {
            result.getSuccessfulProcesses().add(BruteForceProcess.DICTIONARY_ATTACK);
        } else {
            result.getFailingProcesses().add(BruteForceProcess.DICTIONARY_ATTACK);
        }
        
        RainbowCrack crack = new RainbowCrack(cipher);
        boolean isCrackSuccessful = crack.crack(getCipheredMessage());
        if (isCrackSuccessful) {
            result.getSuccessfulProcesses().add(BruteForceProcess.RAINBOW_TABLE);
        } else {
            result.getFailingProcesses().add(BruteForceProcess.RAINBOW_TABLE);
        }
        
        if (result.getFailingProcesses().isEmpty()) {
            result.setDecipheredMessage(cipher.decode(getCipheredMessage()));
        }
        
        return result;
    }
}
