package cl.alke.wallet.controller;

import cl.alke.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/")
    public String inicialPage(Model model) {
        model.addAttribute("users", userService.listarUsers());
        return "menu";
    }
}
