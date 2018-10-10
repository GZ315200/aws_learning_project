package client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;

public class DualStackEndpoints {

    public static void main(String[] args) {
        String clientRegion = "ap-northeast-1";
        String bucketName = "aws-lambda-test-etl";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .withDualstackEnabled(true)
                    .build();
            ObjectListing objectListing =  s3Client.listObjects(bucketName);

//           list all objects in this bucket
            objectListing.getObjectSummaries()
                    .forEach(s3ObjectSummary -> {
                        System.out.println(s3ObjectSummary.getBucketName());
                        System.out.println(s3ObjectSummary.getETag());
                        System.out.println(s3ObjectSummary.getKey());
                        System.out.println(s3ObjectSummary.getLastModified());
                        System.out.println(s3ObjectSummary.getOwner());
                        System.out.println(s3ObjectSummary.getSize());
                        System.out.println(s3ObjectSummary.getStorageClass());
            });

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
}
