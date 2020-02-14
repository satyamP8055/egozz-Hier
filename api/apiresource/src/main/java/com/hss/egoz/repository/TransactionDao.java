package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.Transaction;

public interface TransactionDao extends JpaRepository<Transaction, Integer>{

}
