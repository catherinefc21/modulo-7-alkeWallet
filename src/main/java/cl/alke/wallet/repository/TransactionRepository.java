package cl.alke.wallet.repository;

import cl.alke.wallet.model.Transaction;
import cl.alke.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN t.walletAccount wa " +
            "LEFT JOIN t.targetWalletAccount twa " +
            "LEFT JOIN t.card c " +
            "WHERE wa.user = :user OR twa.user = :user OR c.user = :user")
    List<Transaction> findByUserParticipation(User user);
}
