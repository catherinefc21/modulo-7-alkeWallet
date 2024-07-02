package cl.alke.wallet.service;

import cl.alke.wallet.model.Card;
import cl.alke.wallet.model.User;

import java.util.List;

public interface CardService {

    List<Card> listarCardsPorIdUsuario(Long userId);
    public Card guardarCard(Card card);
    public Card buscarCardPorId(Long id);
    public Card actualizarCard(Card card);
    public void eliminarCard(Long id);
    Card findByCardNumberAndUser(String cardNumber, User user);
}
