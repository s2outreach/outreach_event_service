package com.cts.outreach.event;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.aws.dynamodb.repo")
public class DynamoDBConfigLocalTest {
    
    @Value("us-west-2")
    private String awsRegion;
    
    @Value("http://localhost:8000")
    private String awsDynamoDBEndPoint;
    
    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfigTest() {
        return DynamoDBMapperConfig.DEFAULT;
    }
    
    @Bean
    public AmazonDynamoDB amazonDynamoDBConfigTest() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, awsRegion))
                .build();
    }
    
    @Bean
    public DynamoDBMapper mapperTest() {
        return new DynamoDBMapper(amazonDynamoDBConfigTest(), dynamoDBMapperConfigTest());
    }

}

