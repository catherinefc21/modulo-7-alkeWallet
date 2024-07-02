package cl.alke.wallet.service;

import cl.alke.wallet.model.Transaction;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    List<Transaction> findAllTransactions();
}
