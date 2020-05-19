package jp.co.ysd.aws_s3_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * 
 * @author yuichi
 *
 */
public class Main {

	// java -jar aws-s3-manager-1.0.0.jar upload [filepath]
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Not enough arguments.");
			return;
		}
		if (!"upload".equals(args[0])) {
			System.err.println("You must specify 'upload' as the first argument.");
			return;
		}
		File file = new File(args[1]);
		if (!file.exists()) {
			System.err.println("no such file exists.[" + file.getPath() + "]");
			return;
		}
		Properties properties = new Properties();
		try (InputStream is = new FileInputStream("s3-access.properties")) {
			properties.load(is);
		}

		System.out.println("start!");

		String region = properties.getProperty("region");
		String accessKey = properties.getProperty("accessKey");
		String secretKey = properties.getProperty("secretKey");
		String bucketName = properties.getProperty("bucketName");
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
		s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file));

		if (args.length >= 3 && "-d".equals(args[2])) {
			FileUtils.forceDelete(file);
			System.out.println(file.getPath() + " is removed.");
		}

		System.out.println("finish!");
	}

}
