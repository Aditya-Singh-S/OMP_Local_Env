package com.cts.service;

import java.util.HashMap;
import java.util.List;
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


	@Override
	public void notifyUserOnUpdateProduct(List<String> userEmails) {
		// TODO Auto-generated method stub
		
		String message = "Hey User!! The product you have subscribed has been updated by Admin.";
		
		for(String email: userEmails) {
			Map<String, MessageAttributeValue> attributes = Map.of(
		            "recipient", MessageAttributeValue.builder()
		                .dataType("String")
		                .stringValue(email).build());
			
			PublishRequest requests = PublishRequest.builder().message(message)
					.topicArn(TOPIC_ARN)
					.messageAttributes(attributes)
					.build();
			
			snsClient.publish(requests);
		}
		
	}


	@Override
	public void notifyAdminOnUpdateProduct() {
		
		String message = "Attention ADMIN Group!! Someone just updated the product!!";
		
		Map<String, MessageAttributeValue> attribute = Map.of(
	            "recipient", MessageAttributeValue.builder()
	                .dataType("String")
	                .stringValue("ADMIN").build());
		
		PublishRequest request = PublishRequest.builder().message(message)
				.topicArn(TOPIC_ARN)
				.messageAttributes(attribute)
				.build();
		
		snsClient.publish(request);
		
	}
}
