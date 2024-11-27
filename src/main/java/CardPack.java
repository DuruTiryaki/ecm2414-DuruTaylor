import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CardPack {
    private List<Card> cards;

    // Constructor
    public CardPack() {
        cards = new ArrayList<>();
    }


    // Shuffles cards
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Draws a card
    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        } else {
            throw new IllegalStateException("Card pack is empty!");
        }
    }

    // Returns the remaining cards
    public int remainingCards() {
        return cards.size();
    }
}