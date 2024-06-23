package cl.alke.wallet.controller;

import cl.alke.wallet.model.Card;
import cl.alke.wallet.service.CardService;
import cl.alke.wallet.model.User;
import cl.alke.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/cards/new")
    public String agregarCard(Model model) {
        Card card = new Card();
        model.addAttribute("card", card);
        return "cardForm";
    }

    @PostMapping("/cards")
    public String guardarCard(@ModelAttribute("card") Card card) {
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

    @GetMapping("/cards/edit/{id}")
    public String editarCard(Model model, @PathVariable Long id) {
        Card card = cardService.buscarCardPorId(id);
        model.addAttribute("card", card);
        return "editCard";
    }

    @PostMapping("/cards/{id}")
    public String actualizarCard(@ModelAttribute("card") Card card, @PathVariable Long id) {
        Card cardActual = cardService.buscarCardPorId(id);
        cardActual.setCardNumber(card.getCardNumber());
        cardActual.setCardType(card.getCardType());
        cardActual.setBank(card.getBank());

        cardService.actualizarCard(cardActual);
        return "redirect:/cards";
    }

    @GetMapping("/cards/{id}")
    public String eliminarCard(@PathVariable Long id) {
        cardService.eliminarCard(id);
        return "redirect:/cards";
    }
}
