package cl.alke.wallet.controller;

import cl.alke.wallet.model.*;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.CardService;
import cl.alke.wallet.service.FrequentWalletAccountService;
import cl.alke.wallet.service.TransactionService;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para manejar las operaciones de depósitos y retiros en la Wallet.
 */
@Controller
@Tag(name = "Transaction Controller", description = "Controlador de transacciones")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletAccountService walletAccountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FrequentWalletAccountService frequentWalletAccountService;

    @Autowired
    private UserService userService;

    /**
     * Lista todas las transacciones en las que el usuario autenticado tiene participación.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Listar todas las transacciones en las que el usuario autenticado tiene participación")
    @GetMapping("/transactions")
    public String listTransactions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            List<Transaction> transactions = transactionService.findAllTransactionsByUser(user);
            model.addAttribute("transactions", transactions);
        }
        return "transactions";
    }

    /**
     * Muestra el formulario para realizar un depósito.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario de depósito")
    @GetMapping("/deposit")
    public String showDepositForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            model.addAttribute("cards", cardService.listarCardsPorIdUsuario(user.getUserId()));
        }
        model.addAttribute("selectedCard", new Card());
        return "depositForm";
    }

    /**
     * Procesa la solicitud de depósito de dinero en la billetera.
     *
     * @param cardNumber Número de la tarjeta desde la cual se va a realizar el depósito.
     * @param amount     Monto a depositar.
     * @param model      Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Depositar dinero en la Wallet")
    @PostMapping("/deposit")
    public String deposit(
            @Parameter(description = "Número de la tarjeta desde la cual se va a realizar el depósito") @RequestParam("cardNumber") String cardNumber,
            @Parameter(description = "Monto a depositar") @RequestParam("amount") BigDecimal amount,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            Card card = cardService.findByCardNumberAndUser(cardNumber, user);
            WalletAccount walletAccount = walletAccountService.getWalletAccountByUser(user);

            if (card != null && walletAccount != null && card.getBalance().compareTo(amount) >= 0) {
                card.setBalance(card.getBalance().subtract(amount));
                walletAccount.setBalance(walletAccount.getBalance().add(amount));

                Transaction transaction = new Transaction("Deposit", amount, LocalDateTime.now(), walletAccount, null, card);
                transactionService.saveTransaction(transaction);

                cardService.guardarCard(card);
                walletAccountService.addWalletAccount(walletAccount);

                model.addAttribute("success", "¡El dinero ha sido abonado exitosamente en tu Wallet!");
            } else {
                model.addAttribute("error", "¡Error, saldo insuficiente!");
            }

            // Recargar las tarjetas para el formulario
            model.addAttribute("cards", cardService.listarCardsPorIdUsuario(user.getUserId()));
            model.addAttribute("selectedCard", new Card());
        }
        return "depositForm";
    }

    /**
     * Muestra el formulario para realizar un retiro.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario de retiro")
    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            model.addAttribute("cards", cardService.listarCardsPorIdUsuario(user.getUserId()));
        }
        return "withdrawForm";
    }

    /**
     * Procesa la solicitud de retiro de dinero de la billetera.
     *
     * @param cardNumber Número de la tarjeta a la cual se va a realizar el retiro.
     * @param amount     Monto a retirar.
     * @param model      Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Retirar dinero de la Wallet")
    @PostMapping("/withdraw")
    public String withdraw(
            @Parameter(description = "Número de la tarjeta a la cual se va a realizar el retiro") @RequestParam("cardNumber") String cardNumber,
            @Parameter(description = "Monto a retirar") @RequestParam("amount") BigDecimal amount,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            WalletAccount walletAccount = walletAccountService.getWalletAccountByUser(user);
            Card card = cardService.findByCardNumberAndUser(cardNumber, user);

            if (walletAccount != null && card != null && walletAccount.getBalance().compareTo(amount) >= 0) {
                walletAccount.setBalance(walletAccount.getBalance().subtract(amount));
                card.setBalance(card.getBalance().add(amount));

                Transaction transaction = new Transaction("Withdraw", amount, LocalDateTime.now(), walletAccount, null, card);
                transactionService.saveTransaction(transaction);

                walletAccountService.addWalletAccount(walletAccount);
                cardService.guardarCard(card);

                model.addAttribute("success", "¡El dinero ha sido retirado exitosamente!");
            } else {
                model.addAttribute("error", "¡Error, saldo insuficiente!");
            }
        }
        return "withdrawForm";
    }

    /**
     * Muestra el formulario para agregar una cuenta frecuente.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario para agregar cuenta frecuente")
    @GetMapping("/add-frequent-account")
    public String showAddFrequentAccountForm(Model model) {
        model.addAttribute("frequentWalletAccount", new FrequentWalletAccount());
        return "addFrequentAccountForm";
    }

    /**
     * Procesa la solicitud para agregar una cuenta frecuente.
     *
     * @param accountNumber Número de la cuenta frecuente.
     * @param alias         Alias para la cuenta frecuente.
     * @param model         Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Agregar cuenta frecuente")
    @PostMapping("/add-frequent-account")
    public String addFrequentAccount(
            @Parameter(description = "Número de la cuenta frecuente") @RequestParam("accountNumber") String accountNumber,
            @Parameter(description = "Alias para la cuenta frecuente") @RequestParam("alias") String alias,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());

            FrequentWalletAccount frequentWalletAccount = new FrequentWalletAccount(accountNumber, alias, user);
            frequentWalletAccountService.saveFrequentWalletAccount(frequentWalletAccount);

            model.addAttribute("success", "¡Cuenta frecuente agregada exitosamente!");
        }
        return "addFrequentAccountForm";
    }

    /**
     * Muestra el formulario para realizar una transferencia.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario de transferencia")
    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("userWalletAccount", walletAccountService.getWalletAccountByUser(user));
            model.addAttribute("frequentAccounts", frequentWalletAccountService.findFrequentAccountsByUser(user));
        }
        return "transferForm";
    }

    /**
     * Procesa la solicitud de transferencia de dinero.
     *
     * @param targetAccountNumber Número de cuenta de destino.
     * @param amount              Monto a transferir.
     * @param model               Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Transferir dinero entre cuentas")
    @PostMapping("/transactions/transfer")
    public String transfer(
            @Parameter(description = "Número de cuenta de destino") @RequestParam("targetAccountNumber") String targetAccountNumber,
            @Parameter(description = "Monto a transferir") @RequestParam("amount") BigDecimal amount,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());

            // Obtener la cuenta de origen (la wallet del usuario autenticado)
            WalletAccount sourceWalletAccount = walletAccountService.getWalletAccountByUser(user);

            // Obtener la cuenta de destino (por su número de cuenta)
            WalletAccount targetWalletAccount = walletAccountService.findByAccountNumber(targetAccountNumber);

            if (sourceWalletAccount == null) {
                model.addAttribute("error", "Cuenta de origen no encontrada.");
            } else if (targetWalletAccount == null) {
                model.addAttribute("error", "Cuenta de destino no encontrada.");
            } else if (sourceWalletAccount.getBalance().compareTo(amount) < 0) {
                model.addAttribute("error", "Saldo insuficiente en la cuenta de origen.");
            } else {
                // Realizar la transferencia
                sourceWalletAccount.setBalance(sourceWalletAccount.getBalance().subtract(amount));
                targetWalletAccount.setBalance(targetWalletAccount.getBalance().add(amount));

                // Guardar las actualizaciones en las cuentas
                walletAccountService.addWalletAccount(sourceWalletAccount);
                walletAccountService.addWalletAccount(targetWalletAccount);

                // Registrar la transacción
                Transaction transaction = new Transaction("Transfer", amount, LocalDateTime.now(), sourceWalletAccount, targetWalletAccount, null);
                transactionService.saveTransaction(transaction);

                model.addAttribute("success", "¡El dinero ha sido transferido exitosamente!");
            }
        } else {
            model.addAttribute("error", "Usuario no autenticado.");
        }
        return "transferForm";
    }
}
