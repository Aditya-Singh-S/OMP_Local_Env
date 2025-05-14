package com.cts.service;

public interface SNSService {
	void subscribeUser(String email);
	void notifyOnAddProduct();
	void notifyAdminOnUnSubscription(String productName,String userEmail);
	void notifyUserOnUnSubscription(String nickName,String productName,String userEmail);
}
