package cl.alke.wallet.service;

import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.model.User;

public interface WalletAccountService {
    WalletAccount createWalletAccountForUser(User user);
    WalletAccount getWalletAccountByUser(User user);
    WalletAccount findByAccountNumber(String accountNumber);
    void addWalletAccount(WalletAccount walletAccount);
}

