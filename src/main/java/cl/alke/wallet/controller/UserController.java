package cl.alke.wallet.controller;

import cl.alke.wallet.model.User;
import cl.alke.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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
        model.addAttribute("balance", currentUser.getBalance());
        model.addAttribute("users", userService.listarUsers());

        return "menu";
    }
}
