import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.io.*;
import java.nio.file.Paths;

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

public static void sendPushNotification(String message, String day2) {
    // Replace 'your-unique-topic-name' with a name only you know
    String ntfyUrl = "https://ntfy.sh/nfjdudidicic";
    //String shortcutUrl = "shortcuts://run-shortcut?name=school"; // Replace 'school' with your shortcut name
    int dayNum = Integer.parseInt(day2.substring(4));
    String tags = null; 
      if(dayNum%2==0)
            {
            tags = "athletic_shoe";
            }
        else if(dayNum==1 || dayNum==5 || dayNum==9)
            {
            tags = "test_tube";
            }
        else 
            {
            tags = "partying_face";
            }
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(ntfyUrl))
        //.header("Click", shortcutUrl) // This makes tapping the notification run your shortcut
        .header("Title", "Today's Schedule Update")  // Sets the notification title
        .header("Priority", "5")            // Sets urgency (1=min, 5=max)
        .header("Tags",tags)
        .header("Attach", "https://avatars.githubusercontent.com/u/168948773?v=4&size=64")
        .POST(HttpRequest.BodyPublishers.ofString(message))
        .build();

    try {
        client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void writeToFile(String data) {
    // This tells Java to write specifically to the current working directory
    String workingDir = System.getProperty("user.dir");
    String fullPath = workingDir + "/school_update.txt";
    
    try (FileWriter writer = new FileWriter(fullPath)) {
        writer.write(data);
        writer.flush();
    } catch (IOException e) {
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
        
        int dayNum = Integer.parseInt(day.substring(4));
        String emoji = null; 
          if(dayNum%2==0)
            {
            emoji = "👟";
            }
         else if(dayNum==1 || dayNum==5 || dayNum==9)
            {
            emoji = "🧪";
            }
        else 
            {
            emoji = "🥳";
            }
        thing.writeToFile(emoji + " " + finalMessage); 
        // Send to iPhone
        thing.sendPushNotification(finalMessage,day);
        
    }


    

}
