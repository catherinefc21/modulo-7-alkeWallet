package cl.alke.wallet.controller;

import cl.alke.wallet.dto.UserRegisterDto;
import cl.alke.wallet.model.User;
import cl.alke.wallet.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("User")
    public UserRegisterDto newUserRegisterDto(){
        return new UserRegisterDto();
    }
    @GetMapping
    public String registerForm(){
     return "register";
    }

    @PostMapping
    public String userRegister(@ModelAttribute("usuario") UserRegisterDto registerDto){
        userService.createUser(registerDto);
        return "redirect:/register?exit";
    }
}
