package cl.alke.wallet.service;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import org.springframework.stereotype.Service;

@Service
public interface WalletAccountService {

    public WalletAccount createWalletAccountForUser(User user);
    public WalletAccount getWalletAccountByUser(User user);
}
