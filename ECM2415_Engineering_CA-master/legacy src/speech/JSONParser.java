package speech;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
/*
 * Joshua Chalcraft
 *
 * Class takes in JSON data stored as a string and extracts directions, which are stored in an ArrayList
 *
 * Code is open for extension. Could be used later to extract lang and long info, as well as distance and other things
 *
 * Examples of this are given (commented out) in the parseJSON method
 *
 * Code requires gson-2.2.2.jar to run, which is stored in the lib folder
 */

public class JSONParser
{
    /*
     * Traverse through JSON data and extract useful information. Commented code to be extended for another sprint.
     */
    private static void parseJSON(String data, ArrayList<String> directions)
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
            JsonObject names = it.next().getAsJsonObject();
            String direction = names.get("html_instructions").toString();
            directions.add(direction);

            //JsonObject distance = names.getAsJsonObject("distance");
            //String text = distance.get("text").toString();
            //Double value = distance.get("value").getAsDouble();

            //JsonObject startLocation = names.getAsJsonObject("start_location");
            //Double sLat = startLocation.get("lat").getAsDouble();
            //Double sLng = startLocation.get("lng").getAsDouble();

            //JsonObject endLocation = names.getAsJsonObject("end_location");
            //Double eLat = endLocation.get("lat").getAsDouble();
            //Double eLng = endLocation.get("lng").getAsDouble();
        }
    }

    /*
     * Removes the HTML tags from the direction strings. Used before generating speech.
     */
    private static ArrayList<String> tagRemover(ArrayList<String> directions)
    {
        for (String line : directions)
        {
            line = line.replaceAll("<.*?>", " ");
            System.out.println(line);
        }
        return directions;
    }


    /*
     * Returns the directions.
     */
    public static ArrayList<String> getDirections(String data, ArrayList<String> directions)
    {
        JSONParser.parseJSON(data, directions);
        directions = JSONParser.tagRemover(directions);
        return directions;
    }
}





