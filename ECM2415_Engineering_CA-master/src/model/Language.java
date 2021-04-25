package model;
import java.util.HashMap;
/**
 * @author Joshua Chalcraft
 * - The Language enum class stores the languages and all information that comes with it
 * - This makes it universal for the sat-nav and safer to use
 */
public enum Language
{
    OFF(new HashMap<String, String>() {{
        put(null, null);
        put(null, null);
        put(null, null);
    }}),
    ENGLISH(new HashMap<String, String>() {{
        put("Code", "en-US");
        put("Gender", "Apollo");
        put("Artist", "(en-GB, Susan, Apollo)");
    }}),
    FRENCH(new HashMap<String, String>() {{
        put("Code", "fr-FR");
        put("Gender", "Apollo");
        put("Artist", "(fr-FR, Julie, Apollo)");
    }}),
    GERMAN(new HashMap<String, String>() {{
        put("Code", "de-DE");
        put("Gender", "Hedda");
        put("Artist", "(de-DE, Hedda)");
    }}),
    ITALIAN(new HashMap<String, String>() {{
        put("Code", "it-IT");
        put("Gender", "Apollo");
        put("Artist", "(it-IT, Cosimo, Apollo)");
    }}),
    SPANISH(new HashMap<String, String>() {{
        put("Code", "es-ES");
        put("Gender", "Apollo");
        put("Artist", "(es-ES, Laura, Apollo)");
    }}),;


    private HashMap<String, String> data;

    Language(HashMap<String, String> data) {
        this.data = data;
    }

    public HashMap<String,String> getData()
    {
        return data;
    }
    public String getCode() { return data.get("Code"); }
    public String getGender() { return data.get("Gender"); }
    public String getArtist() { return data.get("Artist"); }

}

