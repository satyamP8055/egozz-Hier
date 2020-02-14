package com.hss.egoz.service.admin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hss.egoz.config.Initiator;
import com.hss.egoz.model.Admin;
import com.hss.egoz.repository.AdminDao;
import com.hss.egoz.service.role.RoleService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private RoleService roleService;

	@PostConstruct
	public void init() {
		if (adminDao.findAll().isEmpty()) {

			// Get SuperAdmin Data
			Admin superAdmin = Initiator.getSuperAdmin();

			// Set the SuperMost Role
			superAdmin.setRole(roleService.getSuperRole());

			// Add Admin to DB..
			adminDao.save(superAdmin);
		}
	}

	@Override
	public Admin getCurrent(Integer id) {
		return adminDao.getOne(id);
	}

	@Override
	public Admin login(Admin admin) {
		Admin inDb = adminDao.findByUserName(admin.getUserName());
		return inDb != null && new BCryptPasswordEncoder().matches(admin.getPassword(), inDb.getPassword()) ? inDb
				: null;
	}

}
