package cl.alke.wallet.service.impl;

import cl.alke.wallet.service.CurrencyConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private static final BigDecimal EXCHANGE_RATE = new BigDecimal("0.85");

    @Override
    public BigDecimal convertToUSD(BigDecimal amount, String currency) {
        if (amount == null || currency == null || currency.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(EXCHANGE_RATE);
    }
}
