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
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Rol(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rol() {
        super();
    }

    public Rol(String name) {
        this.name = name;
    }

}
