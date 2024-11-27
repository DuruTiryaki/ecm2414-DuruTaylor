import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId; //number of the player
    private String playerName; //player name will be "player + playerId"
    private List<Card> hand; //player's cards (4 cards)
    private CardDeck takenDeck;
    private CardDeck givenDeck;
    //elini kontrol et, 4 kart da aynıysa kazandım de metodları



    public Player(int playerId) {
        this.playerId = playerId;
        this.playerName = "Player" + playerId;
        this.hand = new ArrayList<>(); //it is empty before the game starts
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public synchronized void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        }
    }

    public synchronized Card discardCard(int preferredCardToDiscard) {
        Card resultCard = null;
        if (hand.size() < 4) {
            for (Card card : hand) {
                if (card.getValue() == preferredCardToDiscard) {
                    hand.remove(card);
                    resultCard = card;
                } else {
                    return hand.remove(0);
                }
            }
        }
        return resultCard;
    }

    public List<Card> getHand() { //to see the cards that the player has

        return new ArrayList<>(hand);
    }

    public CardDeck getTakenDeck() {
        return takenDeck;
    }

    public void setTakenDeck(CardDeck takenDeck) {
        this.takenDeck = takenDeck;
    }

    public CardDeck getGivenDeck() {
        return givenDeck;
    }

    public void setGivenDeck(CardDeck givenDeck) {
        this.givenDeck = givenDeck;
    }

    // TODO: player in elinde yer alan tüm Cardları listele.
}
