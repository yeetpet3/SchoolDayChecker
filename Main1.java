import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.io.*;

public class Main1 {

    private String date = LocalDate.now().toString();
    private File thing1;
    private Scanner thing2;

    public String formatDate()
    {
        String[] parted = date.split("-");
        return parted[0]+parted[1]+parted[2];
    }

    public void createFile() throws FileNotFoundException 
    {
        thing1 = new File("calendar_365.ics");
        thing2 = new Scanner(thing1);
    }

    public String getDay()
    {
        String current = ""; 
        String dayFr = "";
        while(!current.equals("DTSTART;VALUE=DATE:" + formatDate()))
        {
           
                current = thing2.nextLine();
    
                if(current.equals("DTSTART;VALUE=DATE:" + formatDate()))
                {
                    dayFr = thing2.nextLine();
                    
                }
        }
        String[] parted = dayFr.split(":");
        dayFr = parted[1];
        return dayFr;



    }

    public void sendPushNotification(String message) {
        String webhookUrl = System.getenv("PUSHCUT_EXECUTE_URL");
        if (webhookUrl == null) return;

        String jsonPayload = String.format("{\"input\": \"%s\"}", message.replace("\"", "\\\""));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String activities(String day1)
    {
        
        int dayNum = Integer.parseInt(day1.substring(4));
        if(dayNum%2==0)
        {
            return "Gym";
        }
        else if(dayNum==1 || dayNum==5 || dayNum==9)
        {
            return "A Double";
        }
        else 
        {
            return "A Free";
        }

        
        
    }
    
    public static void main(String[] args) throws FileNotFoundException
    {    
        Main1 thing = new Main1();
        thing.createFile();
        String day = thing.getDay();
        String activity = thing.activities(day);
        System.out.println("It is a: " + day);
        System.out.println("For Second Period You Have: " + activity);

         String finalMessage = "It is a " + day + ". Second period: " + activity;
        
        // Send to iPhone
        thing.sendPushNotification(finalMessage);
        
    }


    

}
