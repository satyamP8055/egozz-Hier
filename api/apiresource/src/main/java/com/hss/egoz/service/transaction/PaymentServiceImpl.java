package com.hss.egoz.service.transaction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.model.Owner;
import com.hss.egoz.model.Transaction;
import com.hss.egoz.repository.TransactionDao;

/*
 * @author Satyam Pandey
 * Service to perform transaction related operations
 * */
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private TransactionDao dao;

	// Add Transaction to DB
	@Override
	public Integer insert(Transaction transaction) {
		return dao.save(transaction).getPaymentId();
	}

	// Fetch List of Transactions
	@Override
	public List<Transaction> list() {
		return dao.findAll();
	}

	// Filtered List for owner
	@Override
	public List<Transaction> listForOwner(Owner owner) {
		return dao.findAll().stream()
				.filter((transaction) -> (transaction.getOwner() != null
						&& transaction.getOwner().getOwnerId().intValue() == owner.getOwnerId().intValue()))
				.collect(Collectors.toList());
	}

	/*
	 * Service to add payment for Transaction
	 * 
	 * @Param transactionId
	 */
	@Override
	public void pay(Integer transactionId) {
		String status = "IncDeposit";
		Transaction transaction = dao.findById(transactionId).get();
		transaction.setTransactionType(status);
		dao.save(transaction);
	}

}
