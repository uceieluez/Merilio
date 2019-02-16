import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Date;

class MyTwilio implements Runnable {

    private final String myAccount_SID;
    private final String myAuthToken;
    private final String myTwilioNumber;
    private final String[] recipientPhoneNumbers;

    MyTwilio(String account_SID, String authToken, String twilioNumber, String[] recipients) {
        this.myAccount_SID = account_SID;
        this.myAuthToken = authToken;
        this.myTwilioNumber = twilioNumber;
        this.recipientPhoneNumbers = recipients;
    }

    private static String getWordOfTheDay(){
        return MerriamWebster.getWordOfTheDay();
    }

    private void sendTextMessage(String recipientNumber, String newMessage) {
        Twilio.init(myAccount_SID, myAuthToken);
        Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber(myTwilioNumber),
                newMessage).create();
    }

    private void textWordOfTheDay() {
        String wordOfTheDay = getWordOfTheDay();
        for (String recipient : recipientPhoneNumbers) {
            sendTextMessage(recipient, wordOfTheDay);
        }
    }

    @Override
    public void run() {
        try {
            String wordOfTheDay = getWordOfTheDay();
            textWordOfTheDay();
            System.out.println("--------------------------------------------------\n");
            System.out.println("Text message sent on " + new Date() + ":\n");
            System.out.println(wordOfTheDay + "\n");
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
