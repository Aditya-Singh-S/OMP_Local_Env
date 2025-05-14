package com.cts.service;

import java.util.List;

public interface SNSService {
	void subscribeUser(String email);
	void notifyOnAddProduct();
	void userEmailVerify(String email);
	void notifyUserOnUpdateProduct(List<String> userEmails);
	void notifyAdminOnUpdateProduct();
	void notifyReviewCreated(String userEmail, String productName, double rating, String review);
    void notifyReviewDeleted(String userEmail, String productName, double rating, String review);

}
