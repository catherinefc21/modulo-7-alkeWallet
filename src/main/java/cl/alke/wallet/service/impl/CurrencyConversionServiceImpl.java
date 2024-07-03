package cl.alke.wallet.service.impl;

import cl.alke.wallet.service.CurrencyConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private static final Map<String, BigDecimal> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("USD", BigDecimal.ONE);
        exchangeRates.put("EUR", BigDecimal.valueOf(1.1));
        // Agregar más tasas de cambio según sea necesario
    }

    @Override
    public BigDecimal convertToUSD(BigDecimal amount, String currency) {
        BigDecimal exchangeRate = getExchangeRate(currency);
        return amount.multiply(exchangeRate);
    }

    @Override
    public BigDecimal getExchangeRate(String currency) {
        return exchangeRates.getOrDefault(currency, BigDecimal.ONE);
    }
}
