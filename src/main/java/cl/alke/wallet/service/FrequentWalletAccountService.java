package cl.alke.wallet.service;

import cl.alke.wallet.model.FrequentWalletAccount;
import cl.alke.wallet.model.User;

import java.util.List;

public interface FrequentWalletAccountService {
    List<FrequentWalletAccount> findFrequentAccountsByUser(User user);
    FrequentWalletAccount saveFrequentWalletAccount(FrequentWalletAccount frequentWalletAccount);
}
