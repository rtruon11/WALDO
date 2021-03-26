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
public interface Cipher {
    public String encode(String decodedString);
    public String decode(String encodedString);
}
