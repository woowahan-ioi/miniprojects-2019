package com.wootube.ioi.web.support;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean(value = "amazonS3Client")
    public AmazonS3 awsS3Client() {
            return AmazonS3ClientBuilder.standard()
					.withRegion(Regions.fromName(region))
					.build();
        }
}
