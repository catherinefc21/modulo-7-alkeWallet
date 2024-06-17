package cl.alke.wallet.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    private Long userId;
    private String userName;
    private String email;
    private String password;

    public UserRegisterDto(Long userId, String userName, String email, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserRegisterDto(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserRegisterDto() {
    }
}
