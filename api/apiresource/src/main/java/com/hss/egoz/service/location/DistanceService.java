package com.hss.egoz.service.location;

import org.springframework.stereotype.Service;

@Service
public interface DistanceService {
	public Double distance(String addrOne, String addrTwo);
}
