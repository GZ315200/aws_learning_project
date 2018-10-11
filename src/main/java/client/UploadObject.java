package client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class UploadObject {

    public static void main(String[] args) {
        String clientRegion = "ap-northeast-1";
        String bucketName = "clearlove";
        String stringObjKeyName = "zean";
        String fileObjectKeyName = "zeanfile";
        String fileName = "pom.xml";

        try {

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            s3.putObject(bucketName,stringObjKeyName,"Uploaded String Object.");

            PutObjectRequest request = new PutObjectRequest(bucketName,fileObjectKeyName,new File(fileName));

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title","someTitle");
            request.setMetadata(metadata);
            s3.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }
}
