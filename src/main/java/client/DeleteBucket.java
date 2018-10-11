package client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.util.Iterator;

public class DeleteBucket {

    public static void main(String[] args) {
        String clientRegion = "ap-northeast-1";
        String bucketName = "clearlove";

        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            ObjectListing objectListing = s3.listObjects(bucketName);

            while (true) {
                Iterator<S3ObjectSummary> objectSummaryIterator = objectListing.getObjectSummaries().iterator();
                while (objectSummaryIterator.hasNext()) {
                    s3.deleteObject(bucketName, objectSummaryIterator.next().getKey());
                }
                if (objectListing.isTruncated()) {
                    objectListing = s3.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
            // list all of buckets version
            VersionListing versionListing = s3.listVersions(new ListVersionsRequest()
                    .withBucketName(bucketName));

            while (true) {
                Iterator<S3VersionSummary> versionIter = versionListing.getVersionSummaries().iterator();
                while (versionIter.hasNext()) {
                    S3VersionSummary vs = versionIter.next();
                    s3.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                }

                if (versionListing.isTruncated()) {
                    versionListing = s3.listNextBatchOfVersions(versionListing);
                } else {
                    break;
                }
            }
            s3.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }
}
