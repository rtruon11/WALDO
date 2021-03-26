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
 */
public class CompoundDecipherResult {
    
    private String decipheredMessage;
    private ArrayList<BruteForceProcess> failingProcesses;
    private ArrayList<BruteForceProcess> successfulProcesses;

    public CompoundDecipherResult() {
        this.failingProcesses = new ArrayList<>();
        this.successfulProcesses = new ArrayList<>();
    }
    
    /**
     * @return the decipheredMessage
     */
    public String getDecipheredMessage() {
        return decipheredMessage;
    }

    /**
     * @param decipheredMessage the decipheredMessage to set
     */
    public void setDecipheredMessage(String decipheredMessage) {
        this.decipheredMessage = decipheredMessage;
    }
    
    /**
     * @return the failingProcesses
     */
    public ArrayList<BruteForceProcess> getFailingProcesses() {
        return failingProcesses;
    }

    /**
     * @param failingProcesses the failingProcesses to set
     */
    public void setFailingProcesses(ArrayList<BruteForceProcess> failingProcesses) {
        this.failingProcesses = failingProcesses;
    }

    /**
     * @return the successfulProcesses
     */
    public ArrayList<BruteForceProcess> getSuccessfulProcesses() {
        return successfulProcesses;
    }

    /**
     * @param successfulProcesses the successfulProcesses to set
     */
    public void setSuccessfulProcesses(ArrayList<BruteForceProcess> successfulProcesses) {
        this.successfulProcesses = successfulProcesses;
    }
    
    public boolean didDecipherMessage() {
        return getDecipheredMessage() != null;
    }
}
