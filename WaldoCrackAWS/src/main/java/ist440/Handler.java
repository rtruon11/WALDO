package ist440;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

// Handler value ist440.Handler
public class Handler implements RequestHandler<S3Event, String> {

    public Handler() {
    }

    int key;
    String secretWord = "";
    String decryptedString;
    String bucketTwo = "ist440-waldocrack-output";
    String keyTwo = "waldocrack-decryption-output.txt";
    String dictionaryBucket = "ist440-waldocrack-dictionary";
    String dictKey = "dictionary.txt";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    Region region = Region.US_EAST_2;
    S3Client s3 = S3Client.builder()
            .region(region)
            .build();

    @Override
    public String handleRequest(S3Event s3event, Context context) throws AmazonServiceException {

        try {
            LambdaLogger logger = context.getLogger();
            // log execution details
            logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
            logger.log("CONTEXT: " + gson.toJson(context));
            // process event
            logger.log("EVENT: " + gson.toJson(s3event));

            // Getting Object Record
            S3EventNotificationRecord record = s3event.getRecords().get(0);
            //getting source bucket name
            String srcBucket = record.getS3().getBucket().getName();
            // object file name as string
            String srcKey = record.getS3().getObject().getUrlDecodedKey();
            // Build GET request method call
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(srcBucket)
                    .key(srcKey)
                    .build();
            // Get dictionary from S3 bucket
            GetObjectRequest getDictionaryRequest = GetObjectRequest.builder()
                    .bucket(dictionaryBucket)
                    .key(dictKey)
                    .build();
            // Get InputStreams 
            InputStream secretMsgStream = s3.getObject(getObjectRequest);
            InputStream dictionaryStream = s3.getObject(getDictionaryRequest);

            // Takes InputStream and --> String
            String encryptedMsg = getAsString(secretMsgStream);
            String dictionary = getAsString(dictionaryStream);

            // runs brute force method
            String decryptedMsg = waldoCrackBruteForce(encryptedMsg);

            // Uploading to S3 destination bucket 
            s3.putObject(PutObjectRequest.builder().bucket(bucketTwo).key(keyTwo)
                    .build(),
                    RequestBody.fromString(decryptedMsg));

        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            System.exit(1);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            logger.info("IO Exception occurred ");
        }

        return "Ok";

    }

    public String waldoCrackBruteForce(String secretMessage) {

        char[] input = secretMessage.toCharArray();
        for (key = 1; key < 27; key++) {
            System.out.println("");
            decryptedString = "";
            for (int i = 0; i < input.length; i++) {
                if (input[i] == ' ') {
                    // Takes the space and appends it to the "decryptedString" variable
                    decryptedString += " ";
                    continue;
                } else {
                    // Applies the vowel cases
                    if (input[i] == '1' || input[i] == '2' || input[i] == '3' || input[i] == '4' || input[i] == '5') {
                        switch (input[i]) {
                            case '1':
                                input[i] = 'I';
                                break;
                            case '2':
                                input[i] = 'U';
                                break;
                            case '3':
                                input[i] = 'A';
                                break;
                            case '4':
                                input[i] = 'E';
                                break;
                            case '5':
                                input[i] = 'O';
                                break;
                            default:
                        }
                        // Takes the cracked vowel and appends it to the "decryptedString" variable
                        decryptedString += input[i];
                        continue;
                    }
                    if (input[i] >= 'A' && input[i] <= 'Z') {
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
                // Takes the cracked letter and appends it to the "decryptedString" variable
                decryptedString += input[i];
            }

            input = secretMessage.toCharArray();
            secretWord = "";
            // Builds a "secretWord" from the "decryptedString" variable
            for (int i = 0; i < decryptedString.length(); i++) {
                if (decryptedString.charAt(i) != ' ') {
                    secretWord += decryptedString.charAt(i);
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
            secretWord = secretWord.toUpperCase();

           // Get dictionary from S3 bucket
            GetObjectRequest getDictionaryRequest = GetObjectRequest.builder()
                    .bucket(dictionaryBucket)
                    .key(dictKey)
                    .build();
            // Get InputStreams 
            InputStream dictionaryStream = s3.getObject(getDictionaryRequest);

            // Going to check the secretWord against the words in the dictionary file        
            try ( BufferedReader br = new BufferedReader(new InputStreamReader(dictionaryStream))) {
                String dictionaryWord;
                StringBuilder word = new StringBuilder();
                // If the secretWord matches a word in the dictionary file, it assumes the message has been cracked
                while ((dictionaryWord = br.readLine()) != null) {
                    if (dictionaryWord.matches(secretWord)) {
                        return decryptedString.toUpperCase();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "Failed to decrypt message";
    }

    private static String getAsString(InputStream is) throws IOException {
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

}
