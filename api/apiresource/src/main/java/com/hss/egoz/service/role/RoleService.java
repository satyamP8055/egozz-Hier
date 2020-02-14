package com.hss.egoz.service.role;

import com.hss.egoz.model.Role;

public interface RoleService {

	Role getRole(Integer id);

	Role getSuperRole();

}
