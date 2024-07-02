package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.repository.WalletAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WalletAccountServiceImplTest {

    @Mock
    private WalletAccountRepository walletAccountRepository;

    @InjectMocks
    private WalletAccountServiceImpl walletAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWalletAccountForUser() {
        User user = new User();
        user.setUserId(1L);

        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setAccountNumber("1234567890");
        walletAccount.setBalance(BigDecimal.ZERO);
        walletAccount.setUser(user);

        when(walletAccountRepository.save(any(WalletAccount.class))).thenReturn(walletAccount);

        WalletAccount createdAccount = walletAccountService.createWalletAccountForUser(user);

        assertEquals("1234567890", createdAccount.getAccountNumber());
        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());
        assertEquals(user, createdAccount.getUser());
    }

    @Test
    void testGetWalletAccountByUser() {
        User user = new User();
        user.setUserId(1L);

        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setAccountNumber("1234567890");
        walletAccount.setBalance(BigDecimal.ZERO);
        walletAccount.setUser(user);

        when(walletAccountRepository.findByUser(user)).thenReturn(walletAccount);

        WalletAccount foundAccount = walletAccountService.getWalletAccountByUser(user);

        assertEquals("1234567890", foundAccount.getAccountNumber());
        assertEquals(BigDecimal.ZERO, foundAccount.getBalance());
        assertEquals(user, foundAccount.getUser());
    }

    @Test
    void testFindByAccountNumber() {
        String accountNumber = "1234567890";

        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setAccountNumber(accountNumber);
        walletAccount.setBalance(BigDecimal.ZERO);

        when(walletAccountRepository.findByAccountNumber(accountNumber)).thenReturn(walletAccount);

        WalletAccount foundAccount = walletAccountService.findByAccountNumber(accountNumber);

        assertEquals(accountNumber, foundAccount.getAccountNumber());
        assertEquals(BigDecimal.ZERO, foundAccount.getBalance());
    }

}
