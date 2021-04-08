/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ist440;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Utility class used for operations concerning the Caesar cipher - encryption,
 * decryption, cracking
 */
public class CaesarCipher {

    public static void main(String[] args) throws Exception {
        
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
//                "AKIAXULXS6ZZRNBMMP7O",
//                "sgCY6+yMIPtiVHEAAaqLlXyHOdf4i8H3DHP9Emcz");
//
//        S3Client s3 = S3Client.builder()
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .build();
        Handler handle = new Handler();
      
       

    }

    public static String getAsString(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader;
            reader = new BufferedReader(
                    new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            is.close();
        }
        return sb.toString();
    }

    /**
     * boring stuff - returns the string of data held in the file
     *
     * @param url
     * @return
     */
    public static String readFile(URL url) throws IOException, URISyntaxException {
        byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
    }

    /**
     * Decrypts the ciphertext by shifting each character in the ciphertext by
     * the negated value of shift.
     * <p>
     * <p>
     * e.g. if the shift value used to encrypt was 3, to decrypt, the shift
     * value must be -3 (3 + -3 = 0)
     * <p>
     * Note: Could call {@code encrypt(ciphertext, -shift)} for same result. I'm
     * just being explicit here for learning purposes.
     *
     * @param ciphertext
     * @param shift the value to subtract from each character in the ciphertext
     * @return
     */
    public static String decrypt(String ciphertext, int shift) {
        // only interested in the alphabet
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        StringBuilder plaintext = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            // all upper case chars are in the ascii range 65-90.
            // Subtracting A (65) from from the character gives us a value in the range of 0 25
            int newPos = c - 'A';
            // subtract the shift from the position
            newPos -= shift;
            // perform the modulo to make sure the result is in the range of 0-25
            newPos = Math.floorMod(newPos, LetterFrequencyUtils.ALPHABET_COUNT);
            // add A (65) to the value to get the uppercase character
            newPos += 'A';
            plaintext.append((char) newPos);
        }
        return plaintext.toString();
    }

    /**
     * Looks at the frequency of characters in the ciphertext and compares them
     * with the frequencies of characters in the english language.
     * <p>
     * It then calculates the most likely shift value by finding the shift that
     * results in similar frequencies to the english language.
     *
     * @param ciphertext the encrypted message
     * @return best guess at plaintext
     */
    public static String frequencyAnalysis(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        int bestShift = calculateShift(ciphertext);
        return decrypt(ciphertext, bestShift);
    }

    /**
     * Uses the {@link LetterFrequencyUtils#chiSquareAgainstEnglish(String)}
     *
     * @param ciphertext the ciphertext to analyse
     * @return a best guess at the shift value used, or 0 if one can't be
     * determined.
     */
    public static int calculateShift(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        int shift = 0;
        double fitness = Integer.MAX_VALUE;
        for (int i = 0; i < LetterFrequencyUtils.ALPHABET_COUNT; i++) {
            // shift the ciphertext by i characters and compute the chi-square for the result
            double tempFitness = LetterFrequencyUtils.chiSquareAgainstEnglish(CaesarCipher.decrypt(ciphertext, i));
            // if the chi-square was lower than the previous value, make a note of it
            if (tempFitness < fitness) {
                fitness = tempFitness;
                shift = i;
            }
        }
        return shift;
    }

}
