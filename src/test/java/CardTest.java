import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    // Tests successful creation of the card and the getter function
    @Test
    public void testCardCreation() {
        int value = 5;
        Card card = new Card(value);
        assertEquals(value, card.getValue());
    }

}
