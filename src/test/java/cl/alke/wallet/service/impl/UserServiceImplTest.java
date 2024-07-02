package cl.alke.wallet.service.impl;

import cl.alke.wallet.controller.dto.UserRegisterDto;
import cl.alke.wallet.model.Rol;
import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.WalletAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletAccountService walletAccountService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "nonexistent@ejemplo.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        try {
            userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            assertEquals("Usuario no encontrado: " + email, e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(email);
    }
}