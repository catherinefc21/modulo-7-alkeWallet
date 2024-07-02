package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.FrequentWalletAccount;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.FrequentWalletAccountRepository;
import cl.alke.wallet.service.FrequentWalletAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrequentWalletAccountServiceImpl implements FrequentWalletAccountService {
    @Autowired
    private FrequentWalletAccountRepository frequentWalletAccountRepository;

    @Override
    public List<FrequentWalletAccount> findFrequentAccountsByUser(User user) {
        return frequentWalletAccountRepository.findByUser(user);
    }

    @Override
    public FrequentWalletAccount saveFrequentWalletAccount(FrequentWalletAccount frequentWalletAccount) {
        return frequentWalletAccountRepository.save(frequentWalletAccount);
    }
}
