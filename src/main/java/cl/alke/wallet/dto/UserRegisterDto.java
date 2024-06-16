package cl.alke.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    private Long userId;
    private String userName;
    private String email;
    private String password;
    private double moneyBalance;

    public UserRegisterDto(Long userId, String userName, String email, String password, double moneyBalance) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.moneyBalance = moneyBalance;
    }

    public UserRegisterDto(String userName, String email, String password, double moneyBalance) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.moneyBalance = moneyBalance;
    }

    public UserRegisterDto(String email) {
        super();
        this.email = email;
    }

    public UserRegisterDto() {
    }
}
