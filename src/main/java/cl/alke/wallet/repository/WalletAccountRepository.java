package cl.alke.wallet.repository;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletAccountRepository extends JpaRepository<WalletAccount, Long> {
    WalletAccount findByUser(User user);
    WalletAccount findByAccountNumber(String accountNumber);
}
