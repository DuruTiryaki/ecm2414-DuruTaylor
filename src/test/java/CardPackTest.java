import org.junit.Assert;
import org.junit.Test;

public class CardPackTest {
    @Test
    public void testCardPackCreation() {
        CardPack cardPack = new CardPack();
        Assert.assertEquals(0, cardPack.getCards().size());
        Assert.assertEquals(0, cardPack.remainingCards());
    }

    @Test
    public void testAddCardToCardPack() {
        CardPack cardPack = new CardPack();
        cardPack.addCard(new Card(1));
        Assert.assertEquals(1, cardPack.remainingCards());
    }
}
