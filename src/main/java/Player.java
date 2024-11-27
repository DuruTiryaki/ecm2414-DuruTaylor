import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId; //number of the player
    private String playerName; //player name will be "player + playerId"
    private List<Card> hand; //player's cards

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

}
