package cl.alke.wallet.controller;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {


    private final UserService userService;
    private final WalletAccountService walletAccountService;

    @Autowired
    public UserController(UserService userService, WalletAccountService walletAccountService) {
        this.userService = userService;
        this.walletAccountService = walletAccountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping({"/","/menu"})
    public String inicialPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String username = authentication.getName();
        User currentUser = userService.findByEmail(username);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Obtener la cuenta wallet del usuario actual
        WalletAccount walletAccount = walletAccountService.getWalletAccountByUser(currentUser);

        model.addAttribute("balance", walletAccount.getBalance());
        model.addAttribute("users", userService.listarUsers());
        model.addAttribute("accountNumber", walletAccount.getAccountNumber());
        model.addAttribute("userName", currentUser.getUserName());

        return "menu";
    }
}
