package com.hss.egoz.service.user;

import java.util.List;

import com.hss.egoz.model.User;

public interface UserService {

	Integer userLogin(User user);

	Integer userSignUp(User user);

	void updateUser(User user);

	User getUser(Integer userId);

	List<User> list();
	
}
