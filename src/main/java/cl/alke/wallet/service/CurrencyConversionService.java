package cl.alke.wallet.service;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertToUSD(BigDecimal amount, String currency);

}
