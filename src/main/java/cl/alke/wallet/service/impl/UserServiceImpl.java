package cl.alke.wallet.service.impl;

import cl.alke.wallet.controller.dto.UserRegisterDto;
import cl.alke.wallet.model.Rol;
import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import cl.alke.wallet.utils.NumeroCuentaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final WalletAccountService walletAccountService;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, @Lazy UserRepository userRepository, WalletAccountService walletAccountService) {
        super();
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.walletAccountService = walletAccountService;
    }


    @Override
    public User createUser(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setUserName(userRegisterDto.getUserName());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        // Crear una nueva lista de roles y agregar el rol a esa lista
        List<Rol> roles = new ArrayList<>();
        roles.add(new Rol("ROLE_USER"));
        user.setRoles(roles);

        // Guardar el usuario primero
        user = userRepository.save(user);

        // Crear una nueva cuenta wallet para el usuario
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setAccountNumber(NumeroCuentaGenerator.generarNumeroCuenta()); // Generar número de cuenta automáticamente
        walletAccount.setBalance(BigDecimal.ZERO); // Saldo inicial
        walletAccount.setUser(user); // Asociar la cuenta wallet con el usuario

        // Guardar la cuenta wallet asociada al usuario
        walletAccount = walletAccountService.createWalletAccountForUser(user);

        // Asignar la cuenta wallet al usuario
        user.setWalletAccount(walletAccount);

        // Actualizar el usuario con la cuenta wallet asociada
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapearAutoridadesRoles(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<User> listarUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}