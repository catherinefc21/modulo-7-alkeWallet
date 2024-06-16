package cl.alke.wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "money_balance", nullable = false)
    private double moneyBalance;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuario_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private Collection<Rol> roles;


    public User(Long userId, String userName, String email, String password, double moneyBalance, Collection<Rol> roles) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.moneyBalance = moneyBalance;
        this.roles = roles;
    }

    public User(String userName, String email, String password, double moneyBalance, Collection<Rol> roles) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.moneyBalance = moneyBalance;
        this.roles = roles;
    }

    public User() {
        super();
    }
}
