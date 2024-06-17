package cl.alke.wallet.service;

import cl.alke.wallet.controller.dto.UserRegisterDto;
import cl.alke.wallet.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User createUser(UserRegisterDto userRegisterDto);

    public Object listarUsers();

}
