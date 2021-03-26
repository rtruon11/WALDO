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
public enum BruteForceProcess {
    EXHAUSTIVE_SEARCH("EXHAUSTIVE_SEARCH"), 
    DICTIONARY_ATTACK("DICTIONARY_ATTACK"), 
    RAINBOW_TABLE("RAINBOW_TABLE");
    
    private final String value;
    
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    private BruteForceProcess(String value) {
        this.value = value;
    }
}
