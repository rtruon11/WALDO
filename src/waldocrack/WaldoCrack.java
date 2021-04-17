package waldocrack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WaldoCrack {

    public static String secretMessage = "";
    public static String secretWord = "";
    
     public static void main(String[] args) {

        char [] input;
        int key;
        String secretMessage = "";
        String eachWord = "";
        
        // Initialize the cracked message text file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("crackedMessage.txt"));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to create the cracked message file!");
        }
        
        // Read from the secret message text file
        try (BufferedReader br = new BufferedReader(new FileReader("secretMessage.txt"))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                // Converts the secret message to all uppercase
                secretMessage = response.toString().toUpperCase();
                System.out.println("The file contents are: " + secretMessage);
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            // Turns the secret message text into a character array and assigns it to the variable "input"
            input = secretMessage.toCharArray();
            for (key = 1; key < 27; key++) {
                System.out.println("");
                eachWord = "";
                for(int i = 0; i < input.length; i++) {
                    if (input[i] == ' ') {
                        // Takes the space and appends it to the "eachWord" variable
                        eachWord += " ";
                    continue;
                } else {
                    // Applies the vowel cases
                    if (input[i] == '!' || input[i] == '@' || input[i] == '#' || input[i] == '$' || input[i] == '%') {
                        switch(input[i]) {
                                case '!': 
                                    input[i] = 'I';
                                    break;
                                    case '@': 
                                    input[i] = 'U';
                                    break;
                                    case '#': 
                                    input[i] = 'A';
                                    break;
                                    case '$': 
                                    input[i] = 'E';
                                    break;
                                    case '%': 
                                    input[i] = 'O';
                                    break;
                                    default:
                            }
                        // Takes the cracked vowel and appends it to the "eachWord" variable
                        eachWord += input[i];
                        continue;
                    }
                    if (input[i] >='A' && input[i] <='Z') {
                        input[i] = (char) (input[i] - key);
                        if (input[i] < 'A') {
                            input[i] = (char) (input[i] + 26);
                        }
                        } else {
                            input[i] = (char) (input[i] - key);
                            if (input[i] < 'a') {
                                input[i] = (char) (input[i] + 26);
                            }
                        }
                    }
                    // Takes the cracked letter and appends it to the "eachWord" variable
                    eachWord += input[i];
                }            
                
                System.out.println("Key = " + key + " Decrypted String : " + String.valueOf(input).toUpperCase());
                input = secretMessage.toCharArray();
                secretWord = "";
                        // Builds a "secretWord" from the "eachWord" variable
                        for (int i = 0; i < eachWord.length(); i++) {
                            if(eachWord.charAt(i) != ' ') {
                                secretWord += eachWord.charAt(i);
                            } else {
                                /* As long as the "secretWord" variable is at least three (3) characters long, it will exit the loop
                                    The three (3) character minimum is set figuring a three-letter word should suffice for validating it's 
                                    a legit word
                                */
                                if (secretWord.length() >= 3) {
                                break;
                                } else {
                                    /* If secretWord is NOT at least three (3) characters long without a space, 
                                        it reinitializes secretWord back to empty 
                                    */
                                    secretWord = "";
                                }
                            }
                        }
        
                        System.out.println("The word that will be checked in the dictionary is: " + secretWord);
                        
                // Going to check the secretWord against the words in the dictionary file        
                try (BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"))) {
                    String dictionaryWord;
                    StringBuilder word = new StringBuilder();
                    // If the secretWord matches a word in the dictionary file, it assumes the message has been cracked
                    while ((dictionaryWord = br.readLine()) != null) {;
                        if (dictionaryWord.matches(secretWord)) {
                            System.out.println("Found a match!");
                            System.out.println("");
                            System.out.println("The secret message has been cracked and the message is: " + eachWord);
                            System.out.println("Writing secret message to file now.");
                            try {
                                // Writes the cracked message to a text file called "crackedMessage"
                                BufferedWriter out = new BufferedWriter(new FileWriter("crackedMessage.txt", true));
                                out.write(eachWord);
                                out.close();
                                System.out.println("Secret message has been successfully written to the file \"crackedMessage.txt.");
                            } catch (IOException e) {
                                System.out.println("Error writing to file.");
                            }
                            br.close();
                            System.exit(0);
                        }
                    }     
                } catch (IOException ex) {
                    ex.printStackTrace(); 
                }
            }
     }
}