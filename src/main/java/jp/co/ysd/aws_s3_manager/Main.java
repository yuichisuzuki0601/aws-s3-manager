package jp.co.ysd.aws_s3_manager;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * 
 * @author yuichi
 *
 */
public class Main {

	// java -jar aws-s3-manager-1.0.0.jar upload [uploadFilePath]
	// java -jar aws-s3-manager-1.0.0.jar download [targetObjectName] [downloadDirectory]
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println("Not enough arguments.");
			return;
		}
		String command = args[0];
		if (!"upload".equals(command) && !"download".equals(command)) {
			System.err.println("You must specify 'upload' or 'download' as the first argument.");
			return;
		}

		System.out.println(command + "start!");

		AccessInfo accessInfo = new AccessInfo();
		String bucketName = accessInfo.getBucketName();
		AmazonS3 s3Client = accessInfo.getS3Client();

		if ("upload".equals(command)) {
			File uploadFile = new File(args[1]);
			if (!uploadFile.exists()) {
				System.err.println("no such file exists.[" + uploadFile.getPath() + "]");
				return;
			}
			s3Client.putObject(new PutObjectRequest(bucketName, uploadFile.getName(), uploadFile));
			if (args.length >= 3 && "-d".equals(args[2])) {
				FileUtils.forceDelete(uploadFile);
				System.out.println(uploadFile.getPath() + " is removed.");
			}
		}

		if ("download".equals(command)) {
			String fileName = args[1];
			if (args.length < 3) {
				System.err.println("You must specify download directory as the  third argument.");
				return;
			}
			String downloadDir = args[2];
			s3Client.getObject(new GetObjectRequest(bucketName, fileName),
					new File(downloadDir + File.separator + fileName));
		}

		System.out.println("finish!");
	}

}
