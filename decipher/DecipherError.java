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

import java.util.ArrayList;

/**
 *
 * @author chrismorris
 * 
 * {  
 *  "error":{  
 *    "failingProcesses":[  
 *      "RAINBOW_TABLE",
 *      "DICTIONARY_ATTACK"
 *    ],
 *    "successfulProcesses":[  
 *      "EXHAUSTIVE_SEARCH"
 *    ],
 *    "message":"Message could not be deciphered because the outputs were differing."
 *  }
 * }
 * 
 */
public class DecipherError {
    
    private final ArrayList<BruteForceProcess> failingProcesses;
    private final ArrayList<BruteForceProcess> successfulProcesses;
    private final String message;

    public DecipherError(ArrayList<BruteForceProcess> failingProcesses, ArrayList<BruteForceProcess> successfulProcesses, String message) {
        this.failingProcesses = failingProcesses;
        this.successfulProcesses = successfulProcesses;
        this.message = message;
    }
    
}
