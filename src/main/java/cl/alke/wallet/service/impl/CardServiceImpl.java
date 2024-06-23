package cl.alke.wallet.service.impl;

import cl.alke.wallet.model.Card;
import cl.alke.wallet.repository.CardRepository;
import cl.alke.wallet.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;


    @Override
    public List<Card> listarCardsPorIdUsuario(Long userId) {
        return cardRepository.findByUserUserId(userId);
    }

    @Override
    public Card guardarCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card buscarCardPorId(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card actualizarCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void eliminarCard(Long id) {
        cardRepository.deleteById(id);
    }
}
