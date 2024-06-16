package cl.alke.wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;

    public Rol(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Rol() {
    super();
    }

    public Rol(String userName) {
        this.userName = userName;
    }
}
