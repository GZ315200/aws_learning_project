package client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

public class CreateBucket {

    public static void main(String[] args) {
        String clientRegion = "ap-northeast-1";
        String bucketName = "clearlove";

        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            if (! s3.doesBucketExistV2(bucketName)) {

                s3.createBucket(new CreateBucketRequest(bucketName));

                String bucketLocation = s3.getBucketLocation(new GetBucketLocationRequest(bucketName));

                System.out.println("Bucket location:    " + bucketLocation);
            }
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
     }
}
