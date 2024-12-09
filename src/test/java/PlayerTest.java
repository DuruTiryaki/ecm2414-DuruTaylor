import org.junit.Assert;
import org.junit.Test;
import uk.ac.exeter.cards.Card;
import uk.ac.exeter.cards.Player;

public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        int playerId = 1;
        Player player = new Player(playerId);
        Assert.assertEquals(playerId, player.getPlayerId());
        String playerName = "player " + playerId;
        Assert.assertEquals(playerName, player.getPlayerName());
    }

    @Test
    public void testAddCardToPlayerHand() {
        int playerId = 1;
        int cardValue = 5;
        Player player = new Player(playerId);
        Card card = new Card(cardValue);
        player.addCardToHand(card);
        Assert.assertEquals(String.valueOf(cardValue), player.getHandString());
    }

    @Test
    public void testDiscardCard() {
        int playerId = 1;
        int cardValue = 5;
        Player player = new Player(playerId);
        Card card = new Card(cardValue);
        player.addCardToHand(card);
        player.discardCard();
        Assert.assertEquals("", player.getHandString());
    }

    @Test
    public void testDiscardCard_WithHandIncludesNoCards() {
        int playerId = 1;
        Player player = new Player(playerId);
        Assert.assertNull(player.discardCard());
        Assert.assertEquals("", player.getHandString());
    }

    @Test
    public void testAddCardToHand_WithHandIncludesFullCards() {
        int playerId = 1;
        Player player = new Player(playerId);
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(2));
        player.addCardToHand(new Card(3));
        player.addCardToHand(new Card(4));
        player.addCardToHand(new Card(5));
        Assert.assertEquals("1 2 3 4", player.getHandString());
    }

    @Test
    public void testCheckPlayerHasWon() {
        int playerId = 1;
        Player player = new Player(playerId);
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(1));

        player.checkWinningHand();
        Assert.assertTrue(player.getWinState());

    }

    @Test
    public void testCheckPlayerHasWon_Negative() {
        int playerId = 1;
        Player player = new Player(playerId);
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(1));
        player.addCardToHand(new Card(2));

        player.checkWinningHand();
        Assert.assertFalse(player.getWinState());

    }
}
