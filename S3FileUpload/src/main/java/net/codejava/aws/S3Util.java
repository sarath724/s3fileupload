package net.codejava.aws;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import com.amazonaws.auth.BasicAWSCredentials;


//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

public class S3Util {
	private static final String BUCKET = "nam-public-images";

	
	public static void uploadFile(String fileName, InputStream inputStream) 
			throws S3Exception, AwsServiceException, SdkClientException, IOException {

		//BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIA3P2YCI7TT5OLVPHY", "0c01/k+5oIO3JRi7mNHFeaFxvsZs1K3Tlbr7MgHz");
		//AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		//		.withRegion("ap-south-1")
		//		.build();

		//String AWS_ACCESS_KEY_ID = "AKIA3P2YCI7TT5OLVPHY";

		Region region = Region.AP_SOUTH_1;
		System.setProperty("aws.accessKeyId", "AKIA3P2YCI7TT5OLVPHY");
		System.setProperty("aws.secretAccessKey", "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");
		S3Client client = S3Client.builder()
				.region(region)
				//.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();


		
		PutObjectRequest request = PutObjectRequest.builder()
										.bucket(BUCKET)
										.key(fileName)
										.acl("public-read")
										.build();
		
		client.putObject(request,
				RequestBody.fromInputStream(inputStream, inputStream.available()));
		
		S3Waiter waiter = client.waiter();
		HeadObjectRequest waitRequest = HeadObjectRequest.builder()
											.bucket(BUCKET)
											.key(fileName)
											.build();
		
		WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectExists(waitRequest);
		
		waitResponse.matched().response().ifPresent(x -> {
			// run custom code that should be executed after the upload file exists
		});
	}
}
