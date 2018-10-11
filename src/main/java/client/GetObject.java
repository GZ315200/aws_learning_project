package client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetObject {

    public static void main(String[] args) throws IOException {
        String clientRegion = "ap-northeast-1";
        String bucketName = "clearlove";
        String key = "zeanfile";
        S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
        try {

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            System.out.println("Downloading an object.");
            fullObject = s3.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: ");

            displayTextInputStream(fullObject.getObjectContent());
            GetObjectRequest request = new GetObjectRequest(bucketName,key).withRange(0,9);

            objectPortion = s3.getObject(request);

            System.out.println("Printing bytes retrieved.");

            displayTextInputStream(objectPortion.getObjectContent());

            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                    .withCacheControl("No-cache")
                    .withContentDisposition("attachment;  filename=example.txt");
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,key)
                    .withResponseHeaders(headerOverrides);
            headerOverrideObject = s3.getObject(getObjectRequest);

            displayTextInputStream(headerOverrideObject.getObjectContent());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } finally {
            if (fullObject != null) {
                fullObject.close();
            }
            if (objectPortion != null) {
                objectPortion.close();
            }
            if (headerOverrideObject != null) {
                headerOverrideObject.close();
            }
        }

    }


    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }


}


