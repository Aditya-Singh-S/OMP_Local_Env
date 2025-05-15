package com.cts.service;

public interface SNSService {
	void subscribeUser(String email);
	void notifyOnAddProduct();
	void notifyonresetPassword(String email);
}
