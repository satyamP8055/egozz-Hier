package com.hss.egoz.service.role;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.config.Initiator;
import com.hss.egoz.model.Role;
import com.hss.egoz.repository.RoleDao;

/*
 * @author Satyam Pandey
 * Service to perform role specific Operations...
 * */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@PostConstruct
	public void init() {
		if (roleDao.findAll().isEmpty())
			this.initiate();
	}

	@Override
	public Role getSuperRole() {
		List<Role> roles=roleDao.findAll();
		return roles.isEmpty() ? this.initiate() : roles.get(0);
	}

	@Override
	public Role getRole(Integer id) {
		return roleDao.getOne(id);
	}
	
	private Role initiate() {

		// Get the Basic Fundamental Admin Role...
		Role role = Initiator.getFundaMentalRole();

		// Get Mandatory Access for Admin
		role.setAccess(Initiator.getFundamentalAccess());

		// Add Admin to DB
		return roleDao.save(role);
	}
	
}
