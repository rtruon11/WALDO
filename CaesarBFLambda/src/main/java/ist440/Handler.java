package ist440;



import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.AmazonServiceException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ist440.CaesarCipher;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


// Handler value com.ist440.bruteforce::Handler
public class Handler implements RequestHandler<S3Event, String> {

    public Handler() {
    }

    String bucketTwo = "waldo-caesar";
    String keyTwo = "decryptedMsg";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    @Override
    public String handleRequest(S3Event s3event, Context context) throws AmazonServiceException {
        Region region = Region.US_EAST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();
       
        try {
            logger.info("EVENT: " + gson.toJson(s3event));
            S3EventNotificationRecord record = s3event.getRecords().get(0);

            String srcBucket = record.getS3().getBucket().getName();

            // Object key may have spaces or unicode non-ASCII characters.
            String srcKey = record.getS3().getObject().getUrlDecodedKey();
//       S3Event record.toString();
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(srcBucket)
                    .key(srcKey)
                    .build();
            // Get String from json file ?
            InputStream secretMsgStream = s3.getObject(getObjectRequest);
            
            String secretMsg = CaesarCipher.getAsString(secretMsgStream);
            
            // Download the image from S3 into a stream
            String decryptedMsg = CaesarCipher.frequencyAnalysis(secretMsg);
            // Uploading to S3 destination bucket 
            s3.putObject(PutObjectRequest.builder().bucket(bucketTwo).key(keyTwo)
                    .build(),
                    RequestBody.fromString(decryptedMsg));

        logger.info("Writing to: " + bucketTwo + "/" + keyTwo);
         logger.info("Successfully decrypted " + srcBucket + "/"
                + srcKey + " and uploaded to " + bucketTwo + "/" + keyTwo);
       
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            System.exit(1);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            logger.info("IO Exception occurred ");
        }
       
        return "Ok";
    
    }
}
