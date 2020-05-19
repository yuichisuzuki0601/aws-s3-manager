package jp.co.ysd.aws_s3_manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * 
 * @author yuichi
 *
 */
public class AccessInfo {

	private String region;
	private String accessKey;
	private String secretKey;
	private String bucketName;

	public AccessInfo() throws IOException {
		Properties properties = new Properties();
		try (InputStream is = new FileInputStream("s3-access.properties")) {
			properties.load(is);
		}
		this.region = properties.getProperty("region");
		this.accessKey = properties.getProperty("accessKey");
		this.secretKey = properties.getProperty("secretKey");
		this.bucketName = properties.getProperty("bucketName");
	}

	public String getBucketName() {
		return bucketName;
	}

	public AmazonS3 getS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}

}
