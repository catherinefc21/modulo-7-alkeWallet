package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.Transaction;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.TransactionRepository;
import cl.alke.wallet.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void testFindAllTransactionsByUser() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionType("Credit");

        when(transactionRepository.findByUserParticipation(any(User.class)))
                .thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.findAllTransactionsByUser(user);

        assertEquals(1, transactions.size());
        assertEquals("Credit", transactions.get(0).getTransactionType());
    }
}
