package cl.alke.wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "usuario")
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuario_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private Collection<Rol> roles;

    public User(Long userId, String userName, String email, String password, Collection<Rol> roles) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String userName, String email, String password, Collection<Rol> roles) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {

    }

}