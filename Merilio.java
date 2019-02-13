import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Merilio {

    private static String startTime = "0800"; //8:00am (24-hour clock, no colon or seconds)
    private static final String myAccount_SID = "Your Twilio account SID.";
    private static final String myAuthToken = "Your Twilio account authentication token.";
    private static final String myTwilioNumber = "The Twilio number sending out messages. (Example: +18015551234)";
    private static final String[] recipientPhoneNumbers = {"Examples:", "+18015555678", "+18015550910"};


    public static void main(String[] args) throws InterruptedException {
        MyTwilio myTwilioAccount = new MyTwilio(myAccount_SID, myAuthToken, myTwilioNumber, recipientPhoneNumbers);
        System.out.println("\nStarted running on " + new Date() + ".\n");
        System.out.println("LOG:\n");
        textWordOfTheDayOnceIfAfterStartTime(myTwilioAccount);
        waitForStartTime();
        textWordOfTheDayContinuously(myTwilioAccount);
    }

    private static void textWordOfTheDayOnceIfAfterStartTime(MyTwilio myTwilioAccount) throws InterruptedException {
        if (checkIfAfterStartTime()) {
            textWordOfTheDayOnce(myTwilioAccount);
            TimeUnit.SECONDS.sleep(3);
            System.out.println("--------------------------------------------------\n");
            System.out.println(new Date() + ":");
            System.out.println("Next text message will be sent out at regular start time.\n");
        }
    }

    private static boolean checkIfAfterStartTime() {
        String now = getCurrentHourAndMinute();
        int currentTime = Integer.parseInt(now);
        int startTimeInt = Integer.parseInt(startTime);
        return currentTime > startTimeInt;
    }

    private static void textWordOfTheDayOnce(MyTwilio myTwilioAccount) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.execute(myTwilioAccount);
    }

    private static void waitForStartTime() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println(new Date() + ":");
        System.out.println("Waiting until entered start time to begin...\n");
        String currentTime = getCurrentHourAndMinute();
        while (!compareStartTimeTo(currentTime)) {
            TimeUnit.SECONDS.sleep(60);
            if (compareStartTimeTo(currentTime)) {
                break;
            }
            currentTime = getCurrentHourAndMinute();
        }
    }

    private static String getCurrentHourAndMinute() {
        return new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
    }

    private static boolean compareStartTimeTo(String time) {
        return startTime.equals(time);
    }

    private static void textWordOfTheDayContinuously(MyTwilio myTwilioAccount) {
        System.out.println(new Date() + ":");
        System.out.println("Text messages will now be regularly sent at scheduled start time.\n");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(myTwilioAccount, 1, 86400, TimeUnit.SECONDS);
    }
}
