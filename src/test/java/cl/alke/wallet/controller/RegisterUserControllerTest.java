package cl.alke.wallet.controller;

import cl.alke.wallet.controller.dto.UserRegisterDto;
import cl.alke.wallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class RegisterUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterUserController registerUserController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterForm() {
        String viewName = registerUserController.registerForm();
        assertEquals("register", viewName);
    }

    @Test
    void testUserRegister() {
        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setUserName("TestUser");
        registerDto.setEmail("test@ejemplo.com");
        registerDto.setPassword("password");

        String redirectUrl = registerUserController.userRegister(registerDto);

        assertEquals("redirect:/register?success", redirectUrl);
        verify(userService).createUser(registerDto);
    }
}
