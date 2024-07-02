package cl.alke.wallet.controller;

import cl.alke.wallet.model.*;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.CardService;
import cl.alke.wallet.service.FrequentWalletAccountService;
import cl.alke.wallet.service.TransactionService;
import cl.alke.wallet.service.UserService;
import cl.alke.wallet.service.WalletAccountService;
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

/**
 * Controlador para manejar las operaciones de depósitos y retiros en la Wallet.
 */

@Controller
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

    @GetMapping("/transactions")
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionService.findAllTransactions());
        return "transactions";
    }

    /**
     * Muestra el formulario para realizar un depósito.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */

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

    @PostMapping("/deposit")
    public String deposit(@RequestParam("cardNumber") String cardNumber,
                          @RequestParam("amount") BigDecimal amount,
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
     * Muestra el formulario para realizar un retiro.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("cardNumber") String cardNumber, @RequestParam("amount") BigDecimal amount, Model model) {
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

    @PostMapping("/add-frequent-account")
    public String addFrequentAccount(@RequestParam("accountNumber") String accountNumber,
                                     @RequestParam("alias") String alias,
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

    @PostMapping("/transactions/transfer")
    public String transfer(@RequestParam("targetAccountNumber") String targetAccountNumber,
                           @RequestParam("amount") BigDecimal amount, Model model) {
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