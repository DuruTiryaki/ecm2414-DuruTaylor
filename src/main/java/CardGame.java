import java.io.File; // Import File Class for file access
import java.io.FileNotFoundException; // Import for handling errors accessing pack file
import java.util.Scanner; // Import for input and output handling

class Card{
    private int number;  // Stores the number of the card

    // Constructor for Card
    Card(int inputNum) {
        number = inputNum;  // Set Card Number
    }

    //getter
    public int getNumber(){
        return number;
    }
}

public class CardGame {
    public static void main(String[] args){

        Scanner cmdReader = new Scanner(System.in); // Scanner object to handle input
        int noOfPlayers = 0; // Store Number of players
        String packFileName; // Store pack file path
        boolean fileReadFlag = false; // Flag set to true when the pack file is successfully read
        int noOfCards = 0; // Store total number of cards
        Card[] cardArr = null;

        while(noOfPlayers <= 0){
            System.out.println("Please enter the number of players:");
            try {
                noOfPlayers = Integer.parseInt(cmdReader.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid Input - Must choose a whole number [!]");
            }

            if (noOfPlayers <= 0){
                System.out.println("[!] Invalid Input - Must be above zero [!]");
            }
        }

        while (!fileReadFlag){
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
                cardArr = new Card[noOfCards]; // Create Card Array
                noOfCards = 0; // Reset for use in next loop

                //Second loop to create the cards - Creates new card for each line in pack.txt
                while (myReader.hasNextLine()) {
                    cardArr[noOfCards] = new Card(Integer.parseInt(myReader.nextLine()));
                    noOfCards += 1;
                }
                myReader.close();
                fileReadFlag = true;

            } catch (FileNotFoundException e) {
                System.out.println("[!] An error occurred while trying to access " + packFileName + " [!]");
            }

        }

        for (int i = 0; i < noOfCards; i++) {
            System.out.println(cardArr[i].getNumber());
        }
    }
}

