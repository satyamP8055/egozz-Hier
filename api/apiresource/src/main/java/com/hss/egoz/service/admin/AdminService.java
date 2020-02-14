package com.hss.egoz.service.admin;

import com.hss.egoz.model.Admin;

public interface AdminService {

	Admin getCurrent(Integer id);

	Admin login(Admin admin);

}
