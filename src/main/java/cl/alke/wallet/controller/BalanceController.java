package cl.alke.wallet.controller;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.service.CurrencyConversionService;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@Tag(name = "Balance Controller", description = "Controlador de balance")
public class BalanceController {

    private final UserService userService;
    private final WalletAccountService walletAccountService;
    private final CurrencyConversionService currencyConversionService;

    @Autowired
    public BalanceController(UserService userService, WalletAccountService walletAccountService, CurrencyConversionService currencyConversionService) {
        this.userService = userService;
        this.walletAccountService = walletAccountService;
        this.currencyConversionService = currencyConversionService;
    }

    @Operation(summary = "conversion de moneda", description = "Convierte el monto ingresado a USD y lo muestra en la vista de balance")
    @GetMapping("/currency-conversion")
    public String balancePage(Model model, Authentication authentication) {

        String username = authentication.getName();
        User currentUser = userService.findByEmail(username);

        WalletAccount walletAccount = walletAccountService.getWalletAccountByUser(currentUser);

        model.addAttribute("balance", walletAccount.getBalance());
        model.addAttribute("currency", "USD");
        model.addAttribute("accountNumber", walletAccount.getAccountNumber());
        model.addAttribute("userName", currentUser.getUserName());

        return "currencyConversion";
    }

    @GetMapping("/convertToUSD")
    @ResponseBody
    public BigDecimal convertToUSD(@RequestParam BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO; // Manejo del caso cuando amount es null
        }
        return currencyConversionService.convertToUSD(amount, "USD");
    }
}
