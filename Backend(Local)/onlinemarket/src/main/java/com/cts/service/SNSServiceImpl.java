package com.cts.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cts.repository.UserRepository;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Service
public class SNSServiceImpl implements SNSService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	SnsClient snsClient;
	
	@Value("${aws.snsTopicARN}")
	String TOPIC_ARN;

	
	@Override
	public void subscribeUser(String email) {
		
		String filterPolicy = String.format("""
				{
					"recipient" : ["%s","USER"]
				}
				""", email);
		
		Map<String, String> attributes = new HashMap<>();
		attributes.put("FilterPolicy", filterPolicy);
		
		System.out.println("Inside subscribeUser");
		SubscribeRequest subscribeRequest = SubscribeRequest.builder().protocol("email")
				.endpoint(email).topicArn(TOPIC_ARN).attributes(attributes).build();
		
		snsClient.subscribe(subscribeRequest);

	}


	@Override
	public void notifyOnAddProduct() {
		String message = "Hey User!!! New Product has been launched!!";
		
		Map<String, MessageAttributeValue> attributes = Map.of(
	            "recipient", MessageAttributeValue.builder()
	                .dataType("String")
	                .stringValue("USER").build());
		
		PublishRequest publishRequest = PublishRequest.builder().message(message)
				.messageAttributes(attributes)
				.topicArn(TOPIC_ARN).build();
		
		snsClient.publish(publishRequest);
		
	}


	// Scrum-80 : Email to admin and users when admin update user preferences
	// and this method sends email to the admin
	@Override
	public void notifyAdminOnUnSubscription(String productName,String userEmail) {
		Map<String, MessageAttributeValue> attributes = Map.of(
	            "recipient", MessageAttributeValue.builder()
	                .dataType("String")
	                .stringValue("ADMIN").build());
	
		String subject = "Product Subscription Removed!";
		String message = "Hey Admin, you have removed Subscription of the product " + productName + " for " + userEmail;
 
        PublishRequest publishRequest = PublishRequest.builder()
        		.messageAttributes(attributes)
        		.topicArn(TOPIC_ARN)
                .subject(subject)
                .message(message)
                .build();
 
        snsClient.publish(publishRequest);
	}
	
	// Scrum-80 : Email to admin and users when admin update user preferences
	// and this method sends email to the user
	@Override
	public void notifyUserOnUnSubscription(String nickName,String productName,String userEmail) {
		Map<String, MessageAttributeValue> attributes = Map.of(
	            "recipient", MessageAttributeValue.builder()
	                .dataType("String")
	                .stringValue(userEmail).build());
	
		String subject = "Product Subscription Removed!";
		String message = "Hey " + nickName +", Admin have removed your Subscription for the product " + productName;
 
        PublishRequest publishRequest = PublishRequest.builder()
        		.messageAttributes(attributes)
        		.topicArn(TOPIC_ARN)
                .subject(subject)
                .message(message)
                .build();
 
        snsClient.publish(publishRequest);
		
	}
}
