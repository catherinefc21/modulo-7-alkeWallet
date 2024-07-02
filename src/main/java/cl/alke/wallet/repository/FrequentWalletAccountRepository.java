package cl.alke.wallet.repository;

import cl.alke.wallet.model.FrequentWalletAccount;
import cl.alke.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrequentWalletAccountRepository extends JpaRepository<FrequentWalletAccount, Long> {
    List<FrequentWalletAccount> findByUser(User user);
}
