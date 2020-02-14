package com.hss.egoz.service.owner;

import java.util.List;

import com.hss.egoz.model.Owner;

public interface OwnerService {

	public Integer register(Owner owner);
	
	public Integer login(Owner owner);
	
	public List<Owner> list();

	Owner findById(Integer ownerId);
	
}
