import java.util.ArrayList;
import java.util.List;

public class CardDeck {
    private List<Card> cards;
    private int deckID;

    public List<Card> getCards() {
        return cards;
    }

    public CardDeck(List<Card> cards, int deckID) {
        this.cards = cards;
        this.deckID = deckID;
    }

    public synchronized Card drawCard(){
        return cards.remove(0);
    }

    public synchronized void addToBottom(Card card){
        cards.add(card);
    }

    public int getDeckID(){
        return deckID;
    }

    public synchronized List<Card> listCardsInDeck() {
        return new ArrayList<>(cards);
    }

}
