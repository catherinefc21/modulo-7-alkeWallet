package cl.alke.wallet.controller;

import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private WalletAccountService walletAccountService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInicialPageUnauthenticatedUser() {
        // Usuario no autenticado simulado
        when(authentication.isAuthenticated()).thenReturn(false);

        // Ejecución del método bajo prueba
        String viewName = userController.inicialPage(model, authentication);

        // Verificación de redirección
        assertEquals("redirect:/login", viewName);
    }
}
