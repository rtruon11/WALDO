/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ist440.decipher;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OCRRequest", value = "/ocr")
@MultipartConfig
public class OCRRequest extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String credentials = "{\n"
                + "  \"type\": \"service_account\",\n"
                + "  \"project_id\": \"decipher-222503\",\n"
                + "  \"private_key_id\": \"a3f7f513c37c5233a22531e17f82b925a111a015\",\n"
                + "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDCu3otzoEhGuTu\\nu2vmFz6YhhQ3Wowm6ebKv7IErQaQ0BkBYFP1WMwbeEa0SS+i3w/FD10dieOzOyrx\\nPTcp1qaQrShd2O+CnNR6atSaa4N1uh6JGvz5Nn6d5Fndo+hP3Ri/OKnbM2AWttkH\\npiBdkjrKqY4iZycIn5hykPlhnz7/AhetH4qpPw+88TiRZhehYJSM6GTgsjJvAGMG\\n1Lh2SRhGSqivi9T23BbuoVhkT0pL3L3R9exa8zavNbX+mDz0mgkYdrwF7ReMnT10\\n9YUWvJKWqa+6AQRXu2C5lILg0doHxPtNhxbvit7Es5TxslKTOsxSOT4dxeRZCdsU\\nY1JY382jAgMBAAECggEATWTzr0b2vXCR8mwc+yrb3wTS/hkHwOOPrZ84onEePenn\\nNkvRsp5jKLvJ5hd8TlA3VUvA51+JVw+6Bn0c8oz0UGXxuDLjJlcsf6b77o+yhNS/\\nFCDc0DOfaSN7sAJX733hOwbLQ2qM0xpC8a5OCMJ4iVmQOw4iTmQX09jYtLr076Zz\\n+gOVs39iAcSyM31JlXanFLCLhaGdHFnIfUCLGOybZgMCqxYa8CTJuNkhz0zsPfmi\\nfJJFO6KIpzk0nFJlZRVS0M7MX9kibh7YJTspfKv4dDS7pxjpH93A60aBlYmqxJ54\\ngq4lVrVCUrUkK32pL/oxi2larJYUioQmkTmvn3ZxoQKBgQD1c+gqL5jg7fawT81w\\nvoKUovGjdu6rrEuzdX8sSNWoleZVNmCzvCw1FNogsW22k0gjeR0VypqE9qn3QaxK\\nHnFMRqnWulDcJ45efUH4YZQjZQWBKXkAis9TsZVOg+30mR6HGRpMvpmZsfdM4Kgx\\nbwMRyN7s9sQnHT8fHF44SCetkwKBgQDLGZ9THhtSiwSs6g9UNlZc0Xwsfd8O6d96\\n7TCg8z2Du5ylV8OWZpzgHVmLxDsbuiyECNst4VMocz9Ps5Wq/wS8uE8wVqwLC+Hn\\nSp7VjCZAT46eqzg8U2Kr/Eb7jeloLSvQZMEoEERb04PE/S2VZ6+SJU+GS4gQAJvA\\n9Txq33LpsQKBgQCj5HDxOjbqefpuX9rdnVezx9lxPMjA+JrTiXERMND5cJ4L5/NK\\nECu0hefgTJjlgBJ7HTO+iToD7nAvGTrhgtJEDCr7pgy38/GsRhb2srEnsRyr0fVp\\n10X4rGr1skIBQPyRWMGm3N3TMnHec2PbHjT+exHfSCZRt8WeEGMZUZSs4QKBgQC3\\nMShFh21rmZshAInNEqn5VLblRq+wuwDp1xG6y0vcC5hyhuQTQAEWIUMIz3nlO5s1\\niOtxycEBRlp/4GMKdrXmJ3S+DB9oY1JjFiqIQb6q2VeyBIxX1Xq12HqKIem7Hqtb\\nb9XN9kZCcVl0kEzSa1CwyNifMHfMv0+9zzWszFnRMQKBgQCKhaATfyp5wQezYUiC\\nirV6vo0Kw+8sUqY958pDN9g/+TxHHGnLOdmpCa7M8jBC2gWUjruYCoRCSjLVE+Pg\\nmVaJSfFn6Sxkv8yEmFFT7sMra/QWM2W0L8NL4LVpnXslHKlgeJ/tv38X+QiwqJa+\\nr2lJtlnhBMToOrd6FNN9DRlqeA==\\n-----END PRIVATE KEY-----\\n\",\n"
                + "  \"client_email\": \"starting-account-4a9q1v35j2kp@decipher-222503.iam.gserviceaccount.com\",\n"
                + "  \"client_id\": \"114063383422058481744\",\n"
                + "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n"
                + "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n"
                + "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n"
                + "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/starting-account-4a9q1v35j2kp%40decipher-222503.iam.gserviceaccount.com\"\n"
                + "}";

        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(
                ServiceAccountCredentials.fromStream(new ByteArrayInputStream(credentials.getBytes()))
        );
        
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings);
        
        InputStream stream = req.getPart("image").getInputStream();

        byte[] data = new byte[stream.available()];
        if (stream.read(data) < 1) {
            InvalidRequestError error = new InvalidRequestError(
                    InvalidRequestError.CODE_MISSING_BODY,
                    "This service requires a body."
            );
            resp.getOutputStream().print(new Gson().toJson(error));
            return;
        }

        ByteString imgBytes = ByteString.copyFrom(data);
        
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();
        
        if (responses.isEmpty()) {
            InvalidRequestError error = new InvalidRequestError(
                    InvalidRequestError.CODE_EMPTY_ANNOTATION_RESPONSE,
                    "No annotation responses returned."
            );
            resp.getOutputStream().print(new Gson().toJson(error));
            return;
        }
        
        String text = responses.get(0).getFullTextAnnotation().getText();
        
        resp.getOutputStream().print(new Gson().toJson(new OCRSuccessResponse(text)));
        
        vision.shutdown();
        try {
            vision.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(OCRRequest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            vision.close();
        }
    }

}
