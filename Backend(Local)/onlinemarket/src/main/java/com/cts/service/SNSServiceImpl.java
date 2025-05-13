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
}
