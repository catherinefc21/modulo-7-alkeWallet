package cl.alke.wallet.controller;

import cl.alke.wallet.model.Card;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.UserRepository;
import cl.alke.wallet.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con las tarjetas.
 */
@Controller
@Tag(name = "Card Controller", description = "Controlador de tarjetas")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Lista todas las tarjetas de un usuario.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Listar tarjetas de un usuario")
    @GetMapping("/cards")
    public String listarTarjetasUsuario(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername());
            List<Card> card = cardService.listarCardsPorIdUsuario(user.getUserId());
            model.addAttribute("cards", card);
        }
        return "cards";
    }

    /**
     * Muestra el formulario para agregar una nueva tarjeta.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario para agregar una nueva tarjeta")
    @GetMapping("/cards/new")
    public String agregarCard(Model model) {
        Card card = new Card();
        model.addAttribute("card", card);
        return "cardForm";
    }

    /**
     * Guarda una nueva tarjeta.
     *
     * @param card Tarjeta a guardar.
     * @return Redirección a la lista de tarjetas.
     */
    @Operation(summary = "Guardar una nueva tarjeta")
    @PostMapping("/cards")
    public String guardarCard(
            @Parameter(description = "Tarjeta a guardar") @ModelAttribute("card") Card card) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByEmail(username);
            card.setUser(user);
            cardService.guardarCard(card);
        }
        return "redirect:/cards";
    }

    /**
     * Muestra el formulario para editar una tarjeta existente.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @param id    ID de la tarjeta a editar.
     * @return Nombre de la plantilla Thymeleaf a renderizar.
     */
    @Operation(summary = "Mostrar formulario para editar una tarjeta existente")
    @GetMapping("/cards/edit/{id}")
    public String editarCard(
            Model model,
            @Parameter(description = "ID de la tarjeta a editar") @PathVariable Long id) {
        Card card = cardService.buscarCardPorId(id);
        model.addAttribute("card", card);
        return "editCard";
    }

    /**
     * Actualiza una tarjeta existente.
     *
     * @param card Tarjeta con la información actualizada.
     * @param id   ID de la tarjeta a actualizar.
     * @return Redirección a la lista de tarjetas.
     */
    @Operation(summary = "Actualizar una tarjeta existente")
    @PostMapping("/cards/{id}")
    public String actualizarCard(
            @Parameter(description = "Tarjeta con la información actualizada") @ModelAttribute("card") Card card,
            @Parameter(description = "ID de la tarjeta a actualizar") @PathVariable Long id) {
        Card cardActual = cardService.buscarCardPorId(id);
        cardActual.setCardNumber(card.getCardNumber());
        cardActual.setCardType(card.getCardType());
        cardActual.setBank(card.getBank());

        cardService.actualizarCard(cardActual);
        return "redirect:/cards";
    }

    /**
     * Elimina una tarjeta existente.
     *
     * @param id ID de la tarjeta a eliminar.
     * @return Redirección a la lista de tarjetas.
     */
    @Operation(summary = "Eliminar una tarjeta existente")
    @GetMapping("/cards/{id}")
    public String eliminarCard(
            @Parameter(description = "ID de la tarjeta a eliminar") @PathVariable Long id) {
        cardService.eliminarCard(id);
        return "redirect:/cards";
    }
}
