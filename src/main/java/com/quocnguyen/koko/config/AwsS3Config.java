package com.quocnguyen.koko.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Quoc Nguyen on {2024-08-18}
 */

@Configuration
@EnableAsync
public class AwsS3Config {

    @Value("${aws.secret-key}")
    private String awsSecretKey;

    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.s3.region}")
    private String awsRegion;

    @Bean
    public AmazonS3 amazonS3() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey,
                awsSecretKey);

        return  AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.fromName(awsRegion))
                .build();
    }
}
