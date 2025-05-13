package com.cts.service;

public interface SNSService {
	void subscribeUser(String email);
	void notifyOnAddProduct();
	void notifyReviewCreated(String userEmail, String productName, double rating, String review);
    void notifyReviewDeleted(String userEmail, String productName, double rating, String review);
}
