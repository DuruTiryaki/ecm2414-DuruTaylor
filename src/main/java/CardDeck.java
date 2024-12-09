import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CardDeck {
    private List<Card> cards;
    private int deckID;

    public CardDeck(List<Card> cards, int deckID) {
        this.cards = cards;
        this.deckID = deckID;
    }

    public synchronized Card drawCard(){
        if(!cards.isEmpty()){
            return cards.remove(0);
        }
        else{
            return null;
        }
    }

    public synchronized void addToBottom(Card card){
        cards.add(card);
    }

    public int getDeckID(){
        return deckID;
    }

    // Returns Human Readable String for the Deck's contents
    public synchronized String getDeckString() {
        List<String> cardValueList = new ArrayList<>();
        for (Card card : cards) {
            cardValueList.add(String.valueOf(card.getValue()));
        }
        return String.join(" ", cardValueList);
    }

    //Called by the main thread, writes contents of deck to output file
    public void writeOutput() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("deck" + deckID + "_output.txt"))) {
            writer.write("deck" + deckID + " contents: " + getDeckString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
