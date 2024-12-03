import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends Thread{
    private int playerId; //number of the player
    private String playerName; //player name will be "player + playerId"
    private List<Card> hand; //player's cards (4 cards)
    private CardDeck takeDeck;
    private CardDeck giveDeck;
    private boolean continueGame = true;
    private List<String> outputFileContents = new ArrayList<>(); // This contains the lines that will be written to the final output file
    //elini kontrol et metodu


    public Player(int playerId) {
        this.playerId = playerId;
        this.playerName = "player " + playerId;
        this.hand = new ArrayList<>(); //it is empty before the game starts
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        }
    }

    public Card discardCard() {
        Card resultCard = null;
        if (hand.size() < 4) {
            // For all cards in hand
            for (Card card : hand) {
                // If the card does not have a value equal to the playerID (i.e NOT their preferred denomination), remove from list and return
                if (card.getValue() != playerId) {
                    resultCard = card;
                    hand.remove(card);
                    return resultCard;
                }
            }
        }

        // Fallback, this line should not be reached in intended operation as the discardCard method should not be called if a player has a winning hand of all cards with value equal to their player number (or if the player has a winning hand at all), in which case the game would have already stopped.
        return hand.remove(0);
    }

    public List<Card> getHand() { //to see the cards that the player has

        return new ArrayList<>(hand);
    }

    public String getHandString() { // returns human readable string for the hand
        return hand.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public CardDeck getTakeDeck() {
        return takeDeck;
    }

    public void setTakeDeck(CardDeck takeDeck) {
        this.takeDeck = takeDeck;
    }

    public CardDeck getGiveDeck() {
        return giveDeck;
    }

    public void setGiveDeck(CardDeck giveDeck) {
        this.giveDeck = giveDeck;
    }

    public List<Card> listCardsInHand() {
        return new ArrayList<>(hand);
    }

    public boolean checkWinningHand() {
        // If hand is empty or less than 4, return false
        if (hand == null || hand.size() != 4) {
            return false;
        }

        int firstCardValue = hand.get(0).getValue();

        // If not all cards are the same, return false
        for (Card card : hand) {
            if (card.getValue() != firstCardValue) {
                return false;
            }
        }

        // If the method hasn't already finished (because the cards were the same), return true.
        return true;
    }

    public void run() {
        outputFileContents.add(playerName + " initial hand " + getHandString()); // Add initial hand to output file

        // First, check if the initial hand is a winning hand. If it is, don't enter the game loop and declare victory.
        if(checkWinningHand()){
            continueGame = false;
            outputFileContents.add(playerName + " wins");
            System.out.println(playerName + " wins");
        }

        // Continues running until the continueGame flag is set to false.
        while(continueGame){
            // Draw card from top of the left deck
            hand.add(takeDeck.drawCard());
            outputFileContents.add(playerName + " draws a " + hand.get(hand.size() - 1).getValue() + " from deck " + takeDeck.getDeckID());

            // Discard card to the bottom of the right deck
            Card removedCard = discardCard();
            giveDeck.addToBottom(removedCard);
            outputFileContents.add(playerName + " discards a " + removedCard.getValue() + " to deck " + giveDeck.getDeckID());

            // After both the take and discard operations are complete, check if the hand is a winning hand
            if(checkWinningHand()){
                continueGame = false;
                outputFileContents.add(playerName + " wins");
                System.out.println(playerName + " wins");
            }
        }

        // Add details of the final hand to the output when the thread is about to terminate.
        outputFileContents.add(playerName + " exits");
        outputFileContents.add(playerName + " final hand: " + getHandString());

        //TODO: write the contents to an actual player_output.txt file
        // Broadcast win notification to all other threads
    }
}
