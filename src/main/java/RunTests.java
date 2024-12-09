import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class RunTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CardTest.class);

        System.out.println("Tests ran: " + result.getRunCount());
        System.out.println("Test failed: " + result.getFailureCount());

        for (Failure failure : result.getFailures()) {
            System.out.println("Failure Reason: " + failure.toString());
        }

        if(result.wasSuccessful()){
            System.out.println("All tests passed!");
        }
        else{
            System.out.println("[!] This class has not passed all tests [!]");
        }
    }
}
