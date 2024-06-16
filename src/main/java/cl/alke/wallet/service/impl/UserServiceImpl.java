package cl.alke.wallet.service.impl;

import cl.alke.wallet.dto.UserRegisterDto;
import cl.alke.wallet.model.Rol;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRegisterDto userRegisterDto) {
       User user =  new User(userRegisterDto.getUserName(), userRegisterDto.getEmail(), userRegisterDto.getPassword(),userRegisterDto.getMoneyBalance(), Arrays.asList(new Rol("ROLE_USER")));
       return userRepository.save(user);
    }
}
