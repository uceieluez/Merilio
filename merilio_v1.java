import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class merilio_v1 {
    public static void main(String[] args) {
        String[] recipients = {"Your", "recipient", "numbers!", "Example:", "8015551234"};
        myTwilio_v1 textWordOfTheDay = new myTwilio_v1(
                "Your Twilio account SID",
                "Your Twilio authentication token",
                "Your Twilio phone number (Example: +18015556789)",
                recipients);
        System.out.println("\nLOG:\n");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(textWordOfTheDay, 1, 864000, TimeUnit.SECONDS);
    }
}
