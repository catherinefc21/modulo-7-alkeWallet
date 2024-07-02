package cl.alke.wallet.controller;

import cl.alke.wallet.controller.dto.UserRegisterDto;
import cl.alke.wallet.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@Tag(name = "Register User Controller", description = "Controlador de registro de usuarios")
@RequestMapping("/register")
public class RegisterUserController {

    private final UserService userService;

    public RegisterUserController(UserService userService) {
        super();
        this.userService = userService;
    }
    @ModelAttribute("user")
    public UserRegisterDto newUserRegisterDto() {
        return new UserRegisterDto();
    }

    @GetMapping
    public String registerForm() {
        return "register";
    }

    @PostMapping
    public String userRegister(@ModelAttribute("user") UserRegisterDto registerDto) {
        userService.createUser(registerDto);
        return "redirect:/register?success";
    }
}
