import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Date;

class myTwilio_v1 implements Runnable {

    private final String myAccount_SID;
    private final String myAuthToken;
    private final String myTwilioNumber;
    private final String[] recipients;
    private String wordOfTheDay = wordOfTheDay_v1.getWordOfTheDay();

    myTwilio_v1(String account_SID, String authToken, String twilioNumber, String[] recipients) {
        this.myAccount_SID = account_SID;
        this.myAuthToken = authToken;
        this.myTwilioNumber = twilioNumber;
        this.recipients = recipients;
    }

    private void sendTextMessage(String recipientNumber, String newMessage) {
        Twilio.init(myAccount_SID, myAuthToken);
        Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber(myTwilioNumber),
                newMessage).create();
    }

    private void textWordOfTheDay() {
        for (String recipient : recipients) {
            sendTextMessage("+1" + recipient, wordOfTheDay);
        }
    }

    @Override
    public void run() {
        try {
            textWordOfTheDay();
            System.out.println("Last message sent on " + new Date() + ":");
            System.out.println(wordOfTheDay + "\n");
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
