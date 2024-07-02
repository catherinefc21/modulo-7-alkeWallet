package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.FrequentWalletAccount;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.FrequentWalletAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FrequentWalletAccountServiceImplTest {

    @Mock
    private FrequentWalletAccountRepository frequentWalletAccountRepository;

    @InjectMocks
    private FrequentWalletAccountServiceImpl frequentWalletAccountService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void testFindFrequentAccountsByUser() {
        FrequentWalletAccount account1 = new FrequentWalletAccount();
        account1.setId(1L);
        account1.setUser(user);

        FrequentWalletAccount account2 = new FrequentWalletAccount();
        account2.setId(2L);
        account2.setUser(user);

        List<FrequentWalletAccount> accounts = Arrays.asList(account1, account2);

        when(frequentWalletAccountRepository.findByUser(any(User.class))).thenReturn(accounts);

        List<FrequentWalletAccount> foundAccounts = frequentWalletAccountService.findFrequentAccountsByUser(user);

        assertEquals(2, foundAccounts.size());
        assertEquals(user, foundAccounts.get(0).getUser());
        assertEquals(user, foundAccounts.get(1).getUser());

        verify(frequentWalletAccountRepository, times(1)).findByUser(user);
    }

    @Test
    void testSaveFrequentWalletAccount() {
        FrequentWalletAccount account = new FrequentWalletAccount();
        account.setId(1L);
        account.setUser(user);

        when(frequentWalletAccountRepository.save(any(FrequentWalletAccount.class))).thenReturn(account);

        FrequentWalletAccount savedAccount = frequentWalletAccountService.saveFrequentWalletAccount(account);

        assertEquals(1L, savedAccount.getId());
        assertEquals(user, savedAccount.getUser());

        verify(frequentWalletAccountRepository, times(1)).save(account);
    }
}
