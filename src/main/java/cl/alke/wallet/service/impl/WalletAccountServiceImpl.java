package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.User;
import cl.alke.wallet.model.WalletAccount;
import cl.alke.wallet.repository.WalletAccountRepository;
import cl.alke.wallet.service.WalletAccountService;
import cl.alke.wallet.utils.NumeroCuentaGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletAccountServiceImpl implements WalletAccountService {

    private final WalletAccountRepository walletAccountRepository;

    @Autowired
    public WalletAccountServiceImpl(WalletAccountRepository walletAccountRepository) {
        this.walletAccountRepository = walletAccountRepository;
    }

    @Override
    @Transactional  // Asegúrate de importar javax.transaction.Transactional
    public WalletAccount createWalletAccountForUser(User user) {
        // Crear una nueva cuenta wallet para el usuario con saldo inicial 0
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setAccountNumber(NumeroCuentaGenerator.generarNumeroCuenta()); // Generar número de cuenta automáticamente
        walletAccount.setBalance(BigDecimal.ZERO); // Saldo inicial
        walletAccount.setUser(user); // Asociar la cuenta wallet con el usuario

        // Guardar la cuenta wallet en la base de datos
        walletAccount = walletAccountRepository.save(walletAccount);

        // Actualizar la referencia en el usuario
        user.setWalletAccount(walletAccount);

        return walletAccount;
    }

    @Override
    public WalletAccount getWalletAccountByUser(User user) {
        // Buscar la cuenta wallet asociada al usuario
        return walletAccountRepository.findByUser(user);
    }
}
