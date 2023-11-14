package kr.co.smh.util.amazonS3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	@Value("${cloud.aws.credentials.access-key}") // application.yml 에 명시한 내용
    private String ACCESSKEY;

	@Value("${cloud.aws.credentials.secret-key}")
    private String SECRETKEY;
    
    @Value("${cloud.aws.region.static}")
    private String REGION;
    
    @Bean
    public AmazonS3Client amazonS3Client() {
    	AWSCredentials basicAWSCredentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);
    
        return(AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(REGION)
                .build(); 
    }   
}
