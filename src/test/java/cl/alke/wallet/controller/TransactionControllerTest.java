package cl.alke.wallet.controller;

import cl.alke.wallet.model.Transaction;
import cl.alke.wallet.model.User;
import cl.alke.wallet.service.TransactionService;
import cl.alke.wallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private Model model;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setEmail("test@ejemplo.com");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@ejemplo.com");
    }

    @Test
    void testListTransactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionType("Credit");

        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        when(transactionService.findAllTransactionsByUser(any(User.class)))
                .thenReturn(Collections.singletonList(transaction));

        String viewName = transactionController.listTransactions(model);

        assertEquals("transactions", viewName);
        when(model.addAttribute(any(String.class), any(List.class))).thenReturn(model);
    }
}
