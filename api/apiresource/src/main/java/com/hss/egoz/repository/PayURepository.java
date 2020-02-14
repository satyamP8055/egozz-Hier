package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.PayUPayments;

public interface PayURepository extends JpaRepository<PayUPayments, Integer> {

}
