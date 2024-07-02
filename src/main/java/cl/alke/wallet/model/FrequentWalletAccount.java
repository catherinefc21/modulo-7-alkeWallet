package cl.alke.wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "frequent_wallet_account")
@Getter
@Setter
public class FrequentWalletAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "alias", nullable = false)
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FrequentWalletAccount() {
    }

    public FrequentWalletAccount(String accountNumber,String alias, User user) {
        this.accountNumber = accountNumber;
        this.alias = alias;
        this.user = user;
    }
}
