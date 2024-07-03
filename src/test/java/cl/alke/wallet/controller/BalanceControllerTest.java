package cl.alke.wallet.controller;

import cl.alke.wallet.service.CurrencyConversionService;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BalanceControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private WalletAccountService walletAccountService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @InjectMocks
    private BalanceController balanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertToUSD_validAmount_shouldReturnConvertedAmount() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expectedConvertedAmount = new BigDecimal("85.00");
        when(currencyConversionService.convertToUSD(amount, "USD")).thenReturn(expectedConvertedAmount);
        BigDecimal convertedAmount = balanceController.convertToUSD(amount);

        assertEquals(expectedConvertedAmount, convertedAmount);
    }

    @Test
    void convertToUSD_nullAmount_shouldReturnZero() {

        BigDecimal convertedAmount = balanceController.convertToUSD(null);

        assertEquals(BigDecimal.ZERO, convertedAmount);
    }
}
