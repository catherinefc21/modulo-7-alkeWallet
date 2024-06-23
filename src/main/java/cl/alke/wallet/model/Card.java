package cl.alke.wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tarjeta")
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "Tipo", nullable = false)
    private String cardType;

    @Column(name = "Banco", nullable = false)
    private String bank;

    @Column(name = "saldo", nullable = false)
    private BigDecimal Balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Card() {
    }

    public Card(String cardNumber, String cardType, String bank, BigDecimal balance, User user) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.bank = bank;
        Balance = balance;
        this.user = user;
    }
}
