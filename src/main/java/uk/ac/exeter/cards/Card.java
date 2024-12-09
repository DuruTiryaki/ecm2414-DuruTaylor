package uk.ac.exeter.cards;

public class Card {
    private final int value; //we can refer it as cardDenomination as well

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
