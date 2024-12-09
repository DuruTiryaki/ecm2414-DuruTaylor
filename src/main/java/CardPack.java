import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardPack {
    private final List<Card> cards;

    // Constructor
    public CardPack() {
        cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    // Shuffles cards
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Returns the remaining cards
    public int remainingCards() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void listCards() {
        System.out.println("-----------------");
        System.out.println("Listing card pack:");
        for (Card card : cards) {
            System.out.print(card.getValue() + " ");
        }
        System.out.println("\n-----------------");
    }
}