package com.hss.egoz.service.owner;

/*
 * @author Satyam Pandey
 * Service to perform owner based operations
 * */
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hss.egoz.model.Owner;
import com.hss.egoz.repository.OwnerDao;

@Service
public class OwnerServiceImpl implements OwnerService {

	@Autowired
	private OwnerDao ownerDao;

	private BCryptPasswordEncoder encoder;

	@PostConstruct
	public void init() {
		encoder = new BCryptPasswordEncoder();
	}

	// Service to register Owner
	@Override
	public Integer register(Owner owner) {
		String rawPassword = owner.getPassword();
		String encodedPassword = encoder.encode(rawPassword);
		owner.setPassword(encodedPassword);
		return ownerDao.save(owner).getOwnerId();
	}

	// Service to get Owner by Integer ID
	@Override
	public Owner findById(Integer ownerId) {
		return ownerDao.findById(ownerId).get();
	}

	/*
	 * @Param owner credentials (userName & password)
	 * 
	 * @Return ID of current owner or a negative integer
	 */
	@Override
	public Integer login(Owner owner) {
		Owner o = ownerDao.findByUserName(owner.getUserName());
		if (o != null && encoder.matches(owner.getPassword(), o.getPassword()))
			return o.getOwnerId();
		else
			return -1;
	}

	// Return list of all owners
	@Override
	public List<Owner> list() {
		return ownerDao.findAll();
	}

}
