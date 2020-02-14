package com.hss.egoz.service.transaction;

import java.util.List;

import com.hss.egoz.model.Owner;
import com.hss.egoz.model.Transaction;

public interface PaymentService {

	Integer insert(Transaction transaction);

	List<Transaction> list();

	List<Transaction> listForOwner(Owner owner);

	void pay(Integer transactionId);

}
