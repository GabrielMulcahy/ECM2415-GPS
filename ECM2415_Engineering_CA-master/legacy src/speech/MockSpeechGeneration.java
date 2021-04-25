package speech;

import map.Directions;
import java.util.ArrayList;

/*
 * Joshua Chalcraft
 *
 * Simulation class
 * - JSON data being sent from Directions.java
 * - JSONParser class handles the data and returns an ArrayList of the directions
 * - SpeechGenerator sets the language, gender and artist
 * - Go through each line of directions, convert text to speech and play the output
 *
 * By scrum 3, this behaviour should be exhibited through the GPS gui application (or behaviour similar)
 */
public class MockSpeechGeneration
{
    private static ArrayList<String> directions = new ArrayList<>();

    public static void main(String[] args)
    {
        String data = Directions.sendToParser();
        JSONParser.getDirections(data, directions);
        SpeechGenerator.setLanguage("en-US");
        SpeechGenerator.setGender("Apollo");
        SpeechGenerator.setArtist("(en-GB, Susan, Apollo)");
        for (String line : directions)
        {
            SpeechGenerator.setText(line);
            SpeechGenerator.generate();
            SoundPlayer.play();
        }
    }
}



















/*
public class SpeechMain
{
    private static ArrayList<String> directions = new ArrayList<>();
    //static String input = "directions.json";
    //static String pathInput = "res/directions/" + input;
    public static void main(String[] args)
    {
        SpeechJSONParser.formDirections(directions);
        SpeechGenerator.setLanguage("en-US");
        SpeechGenerator.setGender("Apollo");
        SpeechGenerator.setArtist("(en-GB, Susan, Apollo)");
        for (String line : directions)
        {
            SpeechGenerator.setText(line);
            SpeechGenerator.generate();
            SoundPlayer.play();
        }
    }
}
*/