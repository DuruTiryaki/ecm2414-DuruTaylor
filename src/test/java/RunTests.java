import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class RunAllTests {
    public static void executeTest(Result result, String className) {
        System.out.println("TESTING FOR '" + className + "' CLASS");
        System.out.println("___________________________________________________");
        System.out.println("Tests ran: " + result.getRunCount());
        System.out.println("Test failed: " + result.getFailureCount());

        for (Failure failure : result.getFailures()) {
            System.out.println("Failure Reason: " + failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("[!] This class has not passed all tests [!]");
        }
    }

    public static void main(String[] args) {
        executeTest(JUnitCore.runClasses(CardTest.class), "Card");
        executeTest(JUnitCore.runClasses(PlayerTest.class), "Player");
        executeTest(JUnitCore.runClasses(CardDeckTest.class), "CardDeck");
        executeTest(JUnitCore.runClasses(CardPackTest.class), "CardPack");
    }
}
