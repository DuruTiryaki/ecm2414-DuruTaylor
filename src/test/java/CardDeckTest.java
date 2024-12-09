import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CardDeckTest {
    @Test
    public void testCardDeckCreation() {
        int cardDeckId = 1;
        List<Card> cards = List.of(new Card(1), new Card(2), new Card(3), new Card(4));
        CardDeck cardDeck = new CardDeck(cards, 1);
        Assert.assertEquals(cardDeckId, cardDeck.getDeckID());
        Assert.assertEquals("1 2 3 4", cardDeck.getDeckString());
    }

    @Test
    public void addCardToDeck() {
        int cardDeckId = 1;
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card(1);
        Card card2 = new Card(2);
        Card card3 = new Card(3);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        CardDeck cardDeck = new CardDeck(cards, 1);
        cardDeck.addToBottom(new Card(4));
        Assert.assertEquals("1 2 3 4", cardDeck.getDeckString());
    }

    @Test
    public void testDrawCard() {
        int cardDeckId = 1;
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card(1);
        Card card2 = new Card(2);
        cards.add(card1);
        cards.add(card2);
        CardDeck cardDeck = new CardDeck(cards, cardDeckId);
        cardDeck.drawCard();
        Assert.assertEquals("2", cardDeck.getDeckString());
    }

    @Test
    public void testDrawCard_WithNoCard() {
        CardDeck cardDeck = new CardDeck(new ArrayList<>(), 1);
        Assert.assertNull(cardDeck.drawCard());
    }
}
