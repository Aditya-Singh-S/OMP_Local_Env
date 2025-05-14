package com.cts.service;

import java.util.List;

public interface SNSService {
	
	void subscribeUser(String email);
	
	void notifyOnAddProduct();
<<<<<<< HEAD
	
	void userEmailVerify(String email);
	
=======
	void userEmailVerify(String email);
>>>>>>> 08dfabc2f82bd6acd8aa1123a78abca9eebaafc0
	void notifyUserOnUpdateProduct(List<String> userEmails);
	
	void notifyAdminOnUpdateProduct();
	
	void notifyReviewCreated(String userEmail, String productName, double rating, String review);
	
    void notifyReviewDeleted(String userEmail, String productName, double rating, String review);
<<<<<<< HEAD
    
    void notifyOnSubscribing(String email, String nickName, String productName);
    
    void notifyOnUnSubscribing(String email, String nickName, String productName);
=======
>>>>>>> 08dfabc2f82bd6acd8aa1123a78abca9eebaafc0

}
