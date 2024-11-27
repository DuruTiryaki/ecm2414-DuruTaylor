import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) {

        // TODO:
        // List<CardDeck> n tane
        // List<Player> n tane
        // CardPack -> 8n cards ->  drawCard() çekerek List<CardDeck> ve List<Player> kartlarını dağıt.
        // (her yere (Deck ve player.) 4 adet)
        // tüm decklerin içeriğini listele, tüm playerların elini listele.
        // two.txt deki tüm elemanları sırasıyla Player1 (4 adet) Player2 (4 adet) Deck1(4 adet) Deck2 (4 adet)


        Scanner cmdReader = new Scanner(System.in); // Scanner object to handle input
        int noOfPlayers = 0; // Store Number of players
        String packFileName; // Store pack file path
        boolean fileReadFlag = false; // Flag set to true when the pack file is successfully read
        int noOfCards = 0; // Store total number of cards
        CardPack cardPack = new CardPack();
        // Card[] cardArr = null;
        while (noOfPlayers <= 0) {
            System.out.println("Please enter the number of players:");
            try {
                noOfPlayers = Integer.parseInt(cmdReader.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid Input - Must choose a whole number [!]");
            }
            if (noOfPlayers <= 0) {
                System.out.println("[!] Invalid Input - Must be above zero [!]");
            }
        }
        while (!fileReadFlag) {
            cardPack = new CardPack();
            System.out.println("Please enter location of pack to load:");
            packFileName = cmdReader.nextLine();
            try {
                // Create Scanner
                File fileObj = new File(packFileName);
                Scanner myReader = new Scanner(fileObj);
                //First loop to get total number of cards
                while (myReader.hasNextLine()) {
                    myReader.nextLine();
                    noOfCards += 1;
                }
                myReader = new Scanner(fileObj); // Reset scanner to first line

                // cardArr = new Card[noOfCards]; // Create Card Array
                noOfCards = 0; // Reset for use in next loop
                //Second loop to create the cards - Creates new card for each line in pack.txt
                while (myReader.hasNextLine()) {
                    Card card = new Card(Integer.parseInt(myReader.nextLine()));
                    cardPack.addCard(card);
                    // cardArr[noOfCards] = card;
                    noOfCards += 1;
                }
                myReader.close();

                // validity check
                if (isFileValid(cardPack, noOfPlayers)) {
                    System.out.println("File is valid.");
                    fileReadFlag = true;
                } else {
                    System.out.println("Fİle is not valid.");
                    fileReadFlag = false;
                }
            } catch (FileNotFoundException e) {
                System.out.println("[!] An error occurred while trying to access " + packFileName + " [!]");
                fileReadFlag = false;
            }
        }
        cardPack.listCards();
    }

    public static boolean isFileValid(CardPack cardPack, int noOfPlayers) {
        // file elements size  (card size) should equal to 8xnoOfPlayers
        if (cardPack.remainingCards() != (8 * noOfPlayers)) {
            return false;
        }
        // Elemanların kaç kez tekrarlandığını tutmak için bir HashMap kullanıyoruz
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Diziyi dolaşıp elemanların frekanslarını hesaplıyoruz
        for (Card card : cardPack.getCards()) {
            frequencyMap.put(card.getValue(), frequencyMap.getOrDefault(card.getValue(), 0) + 1);
        }

        // Eğer herhangi bir eleman 4 defa geçiyorsa valid kabul edilir
        for (int frequency : frequencyMap.values()) {
            if (frequency >= 4) {
                return true; // Valid
            }
        }

        // Hiçbir eleman 4 kez geçmiyorsa valid değil
        return false;
    }
}