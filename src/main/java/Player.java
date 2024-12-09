import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread {
    private static final String PLAYER_FILE_IDENTIFIER = "player";
    private static final String PLAYER_FILE_IDENTIFIER_OUTPUT = "_output.txt";
    private final int playerId; //number of the player
    private final String playerName; //player name will be "player + playerId"
    private final List<Card> hand; //player's cards (4 cards)
    private final List<String> outputFileContents = new ArrayList<>(); // This contains the lines that will be written to the final output file
    private CardDeck takeDeck;
    private CardDeck giveDeck;
    private volatile boolean hasWon = false;
    private volatile boolean continueGame = true; // This needs to be volatile, as the main thread may modify also this if another winner has been declared to discontinue the game.

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

    public boolean getWinState() {
        return hasWon;
    }

    public Card discardCard() {
        Card resultCard;
        // For all cards in hand
        for (Card card : hand) {
            // If the card does not have a value equal to the playerID (i.e NOT their preferred denomination), remove from list and return
            if (card.getValue() != playerId) {
                resultCard = card;
                hand.remove(card);
                return resultCard;
            }
        }

        // Otherwise just remove and return the first card by default if all cards have the preferred denomination.
        return hand.remove(0);
    }

    // Returns human readable string for the hand
    public String getHandString() {
        List<String> cardValueList = new ArrayList<>();
        for (Card card : hand) {
            cardValueList.add(String.valueOf(card.getValue()));
        }
        return String.join(" ", cardValueList);
    }

    public void setTakeDeck(CardDeck takeDeck) {
        this.takeDeck = takeDeck;
    }

    public void setGiveDeck(CardDeck giveDeck) {
        this.giveDeck = giveDeck;
    }

    public void checkWinningHand() {
        //Stores the boolean to be set
        boolean provisionalState = true;
        // If hand is empty or less than 4, set return value to be false
        if (hand == null || hand.size() != 4) {
            provisionalState = false;
        }
        int firstCardValue = hand.get(0).getValue();
        // If not all cards are the same, set return value to be false
        for (Card card : hand) {
            if (card.getValue() != firstCardValue) {
                provisionalState = false;
            }
        }
        // Commit value to the hasWon attribute.
        hasWon = provisionalState;
    }

    // This is called by the main method in CardGame class when a thread other than this one has won the game.
    public void declareWinner(int winningPlayer) {
        // Stop the main game loop for this thread
        continueGame = false;
        // Add the winner to the output file
        outputFileContents.add("player " + winningPlayer + " has informed " + playerName + " that player " + winningPlayer + " has won");
        System.out.println("player " + winningPlayer + " has informed " + playerName + " that player " + winningPlayer + " has won");
    }

    public void run() {
        System.out.println(playerName + " initial hand " + getHandString());
        outputFileContents.add(playerName + " initial hand " + getHandString()); // Add initial hand to output file

        // First, check if the initial hand is a winning hand. If it is, don't enter the game loop and declare victory.
        checkWinningHand();
        if (hasWon) {
            continueGame = false;
            System.out.println(playerName + " wins");
            outputFileContents.add(playerName + " wins");
        }

        // Continues running until the continueGame flag is set to false.
        while (continueGame) {
            // Draw card from top of the left deck
            System.out.println("deck " + takeDeck.getDeckID() + " contents: " + takeDeck.getDeckString());
            Card drawnCard = takeDeck.drawCard();
            if (drawnCard != null) {
                hand.add(drawnCard);
                outputFileContents.add(playerName + " draws a " + hand.get(hand.size() - 1).getValue() + " from deck " + takeDeck.getDeckID());
                System.out.println(playerName + " draws a " + hand.get(hand.size() - 1).getValue() + " from deck " + takeDeck.getDeckID());
            } else {
                outputFileContents.add(playerName + " attempted to draw a card from deck " + takeDeck.getDeckID() + ", but the deck was empty!");
                System.out.println(playerName + " attempted to draw a card from deck " + takeDeck.getDeckID() + ", but the deck was empty!");
            }

            System.out.println("deck " + giveDeck.getDeckID() + " contents: " + giveDeck.getDeckString());
            // Discard card to the bottom of the right deck
            if (!hand.isEmpty()) {
                Card removedCard = discardCard();
                System.out.println("player " + getPlayerId() + " discards a " + removedCard.getValue() + " to deck " + giveDeck.getDeckID());
                giveDeck.addToBottom(removedCard);
                outputFileContents.add(playerName + " discards a " + removedCard.getValue() + " to deck " + giveDeck.getDeckID());
            } else {
                outputFileContents.add(playerName + " attempted to discard a card to deck " + giveDeck.getDeckID() + ", but they have no cards left in their hand!");
                System.out.println(playerName + " attempted to discard a card to deck " + giveDeck.getDeckID() + ", but they have no cards left in their hand!");
            }

            // After both the take and discard operations are complete, check if the hand is a winning hand (Check that the game is still on as well to prevent declaring of two winners)
            checkWinningHand();
            if (hasWon && continueGame) {
                continueGame = false;
                System.out.println(playerName + " wins");
                outputFileContents.add(playerName + " wins");
            }

            // Sleep to prevent the starvation of other player threads.
            try {
                sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Add details of the final hand to the output when the thread is about to terminate.
        outputFileContents.add(playerName + " exits");
        System.out.println(playerName + " exits");
        outputFileContents.add(playerName + " final hand: " + getHandString());
        System.out.println(playerName + " final hand: " + getHandString());

        // Write Output File
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE_IDENTIFIER + playerId + PLAYER_FILE_IDENTIFIER_OUTPUT))) {
            for (String line : outputFileContents) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error is occurred while writing player output file. Detail: " + e.getMessage());
        }
    }
}
