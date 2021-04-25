package model;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/**
 * @author Joshua Chalcraft
 * - Class takes in JSON data stored as a string and extracts directions and other info
 * - This information is stored in a HashMap
 * - Example infromation includes the directions, distance and co-ordinates of next checkpoint
 * - Code requires gson-2.2.2.jar to run, which is stored in the lib folder
 */

public class JSONParser
{
    /**
     * @author Joshua Chalcraft, Scott Woodward
     * Traverse through JSON data and extract useful information.
     */
    private static ArrayList<HashMap<String, String>> parseJSON(String data)
    {
        ArrayList<HashMap<String, String>> directions = new ArrayList<>();
        try
        {
            //Moving to the 'steps' JsonArray, which is where the data we want is stored.
            JsonObject obj1 = new JsonParser().parse(data).getAsJsonObject(); //Parse string from Directions.
            JsonArray routes = obj1.getAsJsonArray("routes");
            JsonObject obj2 = routes.get(0).getAsJsonObject();
            JsonArray legs = obj2.getAsJsonArray("legs");
            JsonObject obj3 = legs.get(0).getAsJsonObject();
            JsonArray steps = obj3.getAsJsonArray("steps");

            //Go through the 'steps' and pick out the data required.
            Iterator<JsonElement> it = steps.iterator();
            while (it.hasNext())
            {
                HashMap<String, String> leg = new HashMap<>();
                JsonObject names = it.next().getAsJsonObject();
                String direction = names.get("html_instructions").toString();
                leg.put("Directions", direction);

                JsonObject distance = names.getAsJsonObject("distance");
                String text = distance.get("text").toString();
                leg.put("Distance", text);

                JsonObject startLocation = names.getAsJsonObject("start_location");
                String sLat = String.valueOf(startLocation.get("lat").getAsDouble());
                String sLng = String.valueOf(startLocation.get("lng").getAsDouble());
                leg.put("startLat", sLat);
                leg.put("startLong", sLng);

                JsonObject endLocation = names.getAsJsonObject("end_location");
                String eLat = String.valueOf(endLocation.get("lat").getAsDouble());
                String eLng = String.valueOf(endLocation.get("lng").getAsDouble());
                leg.put("endLat", eLat);
                leg.put("endLong", eLng);

                directions.add(leg); //Add all this information in the HashMap to an ArrayList
            }
        }
        //The error that can occur if the use of the Google API is all used up.
        catch (Exception ex) {SoundPlayer.playFile("res/errorMessages/GoogleError.wav");}
        return directions;

    }

    /*
     * Removes the HTML tags from the direction strings and elongates abbreviated terms. Used before generating speech.
     */
    private static ArrayList<HashMap<String, String>> filter(ArrayList<HashMap<String,String>> directions)
    {
        for (int i =0; i<directions.size(); i++)
        {
            HashMap<String,String> h = directions.get(i);
            h.put("Directions", elongator(h.get("Directions").replaceAll("<.*?>", " ")));
        }
        return directions;
    }

    /*
     * Elongates abbreviated words. E.g. 'rd' to 'road'
     */
    private static String elongator(String line)
    {
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++)
        {
            switch (words[i])
            {
                case "Rd": words[i] = "Road"; break;
                case "Ln": words[i] = "Lane"; break;
                case "Dr": words[i] = "Drive"; break;
                case "Av": words[i] = "Avenue"; break;
                case "St": words[i] = "Street"; break;
                case "N" : words[i] = "North"; break;
                case "E" : words[i] = "East"; break;
                case "S" : words[i] = "South"; break;
                case "W" : words[i] = "West"; break;
                default: break;
            }
        }
        return line.join(" ", words);
    }

    /*
     * Returns the directions.
     */
    public static ArrayList<HashMap<String,String>> getDirections(String data)
    {

        ArrayList<HashMap<String,String>> directions = parseJSON(data);
        directions = filter(directions);
        return directions;
    }
}





