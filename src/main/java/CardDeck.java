import java.util.ArrayList;
import java.util.List;

public class CardDeck {
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card getFromTop(){
        return null;
    }

    public void addToBottom(Card card){

    }

    public synchronized List<Card> listCardsInDeck() {
        return new ArrayList<>(cards);
    }

}
