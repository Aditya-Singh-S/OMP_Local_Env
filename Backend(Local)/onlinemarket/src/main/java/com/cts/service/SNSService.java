package com.cts.service;

import java.util.List;

public interface SNSService {
	void subscribeUser(String email);
	void notifyOnAddProduct();
	void notifyUserOnUpdateProduct(List<String> userEmails);
	void notifyAdminOnUpdateProduct();
}
