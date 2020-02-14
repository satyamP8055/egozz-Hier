package com.hss.egoz.service.user;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hss.egoz.model.User;
import com.hss.egoz.repository.UserDao;
import com.hss.egoz.service.user.UserService;

/*
 * @author Satyam Pandey
 * Service to perform user related operations
 * */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	private BCryptPasswordEncoder encoder;

	@PostConstruct
	public void init() {
		encoder = new BCryptPasswordEncoder();
	}

	// Service for user login
	@Override
	public Integer userLogin(User user) {
		User u = userDao.findByUserName(user.getUserName());
		if (u != null && encoder.matches(user.getPassword(), u.getPassword()))
			return u.getUserId();
		return -1;
	}

	// Service for user registration
	@Override
	public Integer userSignUp(User user) {
		Integer returning = -1;
		String rawPassword = user.getPassword();
		String encoded = encoder.encode(rawPassword);
		user.setPassword(encoded);
		returning = userDao.save(user).getUserId();
		return returning;
	}

	// Service to update user
	@Override
	public void updateUser(User user) {
		User u = userDao.findById(user.getUserId()).get();
		userDao.save(u);
	}

	// Service to get User by userID
	@Override
	public User getUser(Integer userId) {
		return userDao.findById(userId).get();
	}

	@Override
	public List<User> list() {
		return userDao.findAll();
	}

}
