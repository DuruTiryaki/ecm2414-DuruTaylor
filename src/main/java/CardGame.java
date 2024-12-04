import java.io.File; // jaImport File Class for file access
import java.io.FileNotFoundException; // Import for handling errors accessing pack file
import java.util.*;

public class CardGame {
    public static boolean isFileValid(CardPack cardPack, int noOfPlayers) {
        // file elements size  (card size) should equal to 8xnoOfPlayers
        if (cardPack.remainingCards() != (8 * noOfPlayers)) {
            return false;
        }
        // HashMap to keep track of how many times elements are repeated
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Traverse array and increment frequency for each occurence of the relevant card in the hasmap.
        for (Card card : cardPack.getCards()) {
            frequencyMap.put(card.getValue(), frequencyMap.getOrDefault(card.getValue(), 0) + 1);
        }

        // If a card has a frequency of 4 or above, it is valid.
        for (int frequency : frequencyMap.values()) {
            if (frequency >= 4) {
                return true; // Valid
            }
        }

        // Otherwise, if a card does not appear 4 or more times, it is invalid.
        return false;
    }

    public static CardPack cardListToPack(List<Card> listOfCards) {
        // Create Card Pack object
        CardPack outputCardPack = new CardPack();

        // For each card give, add it to the pack
        for(Card targetCard : listOfCards){
            outputCardPack.addCard(targetCard);
        }

        // Return that pack
        return outputCardPack;
    }


    public static void main(String[] args){

        Scanner cmdReader = new Scanner(System.in); // Scanner object to handle input
        String packFileName; // Store pack file path
        boolean fileReadFlag = false; // Flag set to true when the pack file is successfully read

        int noOfPlayers = 0; // Store Number of players
        List<Player> playerList = new ArrayList<>(); // List of all Player Objects

        int noOfCards = 0; // Store total number of cards
        List<Card> cardList = new ArrayList<>(); // Stores cards from the pack file
        List<CardDeck> cardDecks = new ArrayList<>(); // List of all card decks
        CardPack packOfCards = null;

        // Check number of players
        while(noOfPlayers <= 0){
            System.out.println("Please enter the number of players:");
            try {
                noOfPlayers = Integer.parseInt(cmdReader.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid Input - Must choose a whole number [!]");
            }

            if (noOfPlayers <= 1){
                System.out.println("[!] Invalid Input - Must be above one [!]");
            }
        }

        // Get pack file
        while (!fileReadFlag){
            System.out.println("Please enter location of pack to load:");
            packFileName = cmdReader.nextLine();
            try {
                // Create Scanner
                File fileObj = new File(packFileName);
                Scanner myReader = new Scanner(fileObj);

                //First loop to get total number of cards (this is needed to determine the length of the card array)
                while (myReader.hasNextLine()) {
                    myReader.nextLine();
                    noOfCards += 1;
                }

                myReader = new Scanner(fileObj); // Reset scanner to first line
                noOfCards = 0; // Reset for use in next loop

                //Second loop to create the cards - Creates new card for each line in pack.txt
                while (myReader.hasNextLine()) {
                    cardList.add(new Card(Integer.parseInt(myReader.nextLine())));
                    noOfCards += 1;
                }
                myReader.close();
                fileReadFlag = true;

            }
            // Handler if file not found
            catch (FileNotFoundException e) {
                System.out.println("[!] An error occurred while trying to access " + packFileName + " [!]");
            }

            // Construct the total card pack
            packOfCards = cardListToPack(cardList);

            // Final checks to ensure the pack file is valid, before proceeding with the game
            if(fileReadFlag){
                fileReadFlag = isFileValid(packOfCards, noOfPlayers);
            }
        }

        // Shuffle cards
        packOfCards.shuffle();
        cardList.clear();
        for (Card targetCard : packOfCards.getCards()){
            cardList.add(targetCard);
        }

        // DECK SETUP
        // Each deck will have 4 cards, same as the players, because...
        // The total number of cards is 8n ( 8 * the number of players)
        // The total number of cards allocated TO PLAYERS will be 4n (4 * the number of players), because each player gets 4 cards each.
        // That leaves us with 4n cards left (8n - 4n = 4n) as the total number of cards FOR DECKS
        // Because there is an equal number of decks to players, that means each single deck will have 4 cards (4n / n = 4)

        List<Card> tempDeck = new ArrayList<>();

        for(int deckNum = 0; deckNum < noOfPlayers; deckNum++){
            for (int i = 0; i < 4; i++){
                // Collect 4 cards from the total deck
                tempDeck.add(cardList.remove(0));
            }
            // Create a new deck with those 4 cards
            cardDecks.add(new CardDeck(tempDeck, deckNum + 1));
            tempDeck.clear();
        }

        // PLAYER SETUP
        // Both playerList and cardDecks have the same length
        // Therefore, we can assign which deck is the 'take deck' and 'give deck' (decks to left and right of player respectively), by using the index.
        // The 'give deck' (the one the player discards cards to) will have the same index as the player
        // The 'take deck' (the one the player picks up cards from) will have the index of the player's - 1.
        // We can then pass these through as references for manipulation by the Player threads.

        // E.G ...[Player N] -> (Deck N) -> [Player 1] -> (Deck 1) -> [Player 2] -> (Deck 2) -> [Player 3] -> (Deck 3)...
        // ('->' is the direction of cards, N is the number of players)

        // Set up Players
        for(int playerNum = 0; playerNum < noOfPlayers; playerNum++){
            playerList.add(new Player(playerNum + 1));
            playerList.get(playerNum).setGiveDeck(cardDecks.get(playerNum)); // Set the 'give deck' reference to the deck that it shares its index with

            // For the first player, set its 'take deck' to be the last deck in the list
            if(playerNum == 0){
                playerList.get(playerNum).setTakeDeck(cardDecks.get(cardDecks.size() - 1));
            }
            else{
                // Otherwise, set the 'take deck' to be the one immediately behind the player.
                playerList.get(playerNum).setTakeDeck(cardDecks.get(playerNum - 1));
            }
        }

        // Each player will be given 4 cards, so do this 4 times.
        for (int i = 0; i < 4; i++){
            // Traverse player list and for each player give them a card
            for(Player targetPlayer : playerList){
                targetPlayer.addCardToHand(cardList.remove(0));
            }
        }

        //Start all Player Threads, this begins the game
        for (Player targetPlayer : playerList){
            targetPlayer.start();
        }

        boolean allThreadsAlive = true;
        //Thread manager loop. This continuously checks for a thread that has completed. If so, it informs all other threads that player thread has won and stops their execution.
        while(allThreadsAlive){
            // For each player object, check that it is still alive
            for (Player targetPlayer : playerList){
                // If not, go through each player object and inform them that there has been a winner
                if(!targetPlayer.isAlive()){
                    playerList.remove(targetPlayer);
                    for (Player remainingPlayers : playerList){
                        // Declare Winner method stops the game loop in the thread from executing further, and informs them of the victor.
                        remainingPlayers.declareWinner(targetPlayer.getPlayerId());
                    }
                    System.out.println(targetPlayer.getPlayerName() + " wins");
                    allThreadsAlive = false;
                    break;
                }
            }
            // Sleep to prevent starvation of the child threads.
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
