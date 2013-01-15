//Secret Santa - acquired from rubyquiz.com/quiz2.html

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SecretSanta {

    Boolean[] santa;
    Boolean[] chosen;
    boolean santaCompleted = true;
    boolean chosenCompleted = true;

    public static void main(String[] args) {
        SecretSanta ss = new SecretSanta();
        ss.fileRead();
    }

    public void fileRead() {

        List<String> aList = new ArrayList<String>();

        int size = 0;

        try {

            File mf = new File("/Users/Steve/IdeaProjects/SecretSanta/src/familylist.txt");
            BufferedReader br = new BufferedReader(new FileReader(mf));

            String line = null;

            while((line = br.readLine()) != null) {
                size = size +1;
                aList.add(line);
            }

            br.close();

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        chosen = new Boolean[size];
        santa = new Boolean[size];
        Arrays.fill(chosen, false);
        Arrays.fill(santa, false);

        Random rand = new Random();

        while(true) {
            int randomInt1 = rand.nextInt(size);
            int randomInt2 = rand.nextInt(size);

            String p1 = aList.get(randomInt1);
            String p2 = aList.get(randomInt2);

            if(p1==p2){
                //do nothing
            }
            if((p1!=p2) && (santa[randomInt1] == false) && (chosen[randomInt2] == false)) {

                String[] person1 = p1.split(" ");
                String[] person2 = p2.split(" ");

                String message = person1[0] + ", you are " + person2[0] + " " + person2[1] + "'s secret Santa!";
                System.out.println(message);  //show what message is being sent
                sendEmail(person1[2], message);

                santa[randomInt1] = true;
                chosen[randomInt2] = true;

                if(arrCompleted(santa) == true && arrCompleted(chosen) == true) {
                    break;

                }
            }
        }
    }

    //runs test to see if all array is true and returns true
    //otherwise returns false.
    boolean arrCompleted(Boolean[] boolarray) {
        for (int i=0; i < boolarray.length; i++) {
            if (boolarray[i] == false) {
                return false;
            }
        }
        return true;
    }
    //send emails
    public void sendEmail(String to, String mess) {

        try{
            String host = "smtp.gmail.com";
            String username = "xxxxxx";
            String password = "xxxxxx";
            String email = "xxxxx@gmail.com";

            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", username);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session s = Session.getDefaultInstance(props, null);
            MimeMessage m = new MimeMessage(s);
            m.setFrom(new InternetAddress(email));

            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject("Secret Santa");
            m.setText(mess);

            Transport t = s.getTransport("smtp");
            t.connect(host, 465, username, password);
            t.sendMessage(m, m.getRecipients(Message.RecipientType.TO));
            t.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
