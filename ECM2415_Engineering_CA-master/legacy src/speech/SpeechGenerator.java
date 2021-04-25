package speech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;

/*
 * Speech generation using Microsoft Cognitive Services
 *
 * Original code by David Wakeling
 *
 * Modified by Joshua Chalcraft
 *  - Changed some attributes to non-final as they could be changed via the SpeechMode class
 *  - Added set and (currently unused) get methods for these attributes
 *  - Changed access modifiers of attributes and provided methods
 */
public class SpeechGenerator
{
    private final static String KEY = "e9488f2304204a599d806da749983124";
    private static String text  = null;
    private static String lang   = null;
    private static String gender = null;
    private static String artist = null;
    private final static String OUTPUT = "sound_output.wav";
    private final static String PATH   = "res/directions/"+OUTPUT;
    private final static String FORMAT = "riff-16khz-16bit-mono-pcm";

    private static String renewAccessToken(String key1)
    {
        final String method = "POST";
        final String url = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
        final byte[] body = {};
        final String[][] headers = {{"Ocp-Apim-Subscription-Key", key1}, {"Content-Length", String.valueOf(body.length)}};
        byte[] response = HttpConnect.httpConnect(method, url, headers, body);
        return new String(response);
    }

    /*
    * Synthesize speech.
    */
    private static byte[] generateSpeech( String token, String text, String lang, String gender, String artist, String format)
    {
        try
        {
            if (text == null) throw new NullPointerException("No directions have been given");

            if ((lang == null) || (gender == null) || (artist == null)) throw new NullPointerException("language and artist must be specified");



            final String method = "POST";
            final String url = "https://speech.platform.bing.com/synthesize";
            final byte[] body = ( "<speak version='1.0' xml:lang='en-us'>" + "<voice xml:lang='" + lang + "' " + "xml:gender='" + gender + "' " + "name='Microsoft Server Speech Text to Speech Voice " + artist + "'>" + text + "</voice></speak>" ).getBytes();
            final String[][] headers
            = { { "Content-Type"             , "application/ssml+xml"      }
            , { "Content-Length"           , String.valueOf( body.length ) }
            , { "Authorization"            , "Bearer " + token             }
            , { "X-Microsoft-OutputFormat" , format                        }
            };
            byte[] response = HttpConnect.httpConnect(method, url, headers, body);
            return response;
        }
        catch (NullPointerException e)
        {
            System.out.println(e);
            return null;
        }
    }

    /*
    * Write data to file.
    */
    private static void writeData(byte[] buffer, String name)
    {
        try
        {
            File             file = new File(name);
            FileOutputStream fos  = new FileOutputStream(file);
            DataOutputStream dos  = new DataOutputStream(fos);
            dos.write(buffer);
            dos.flush();
            dos.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }

    /*
    * Generate speech.
    */
    public static void generate()
    {
        final String token = renewAccessToken(KEY);
        final byte[] speech = generateSpeech(token, text, lang, gender, artist, FORMAT);
        writeData(speech, PATH);
    }



    public static String getLanguage()  {return lang;}

    public static String getGender() {return gender;}

    public static String getArtist() {return artist;}

    public static String getText() {return text;}

    public static void setLanguage(String newLang) {SpeechGenerator.lang = newLang;}

    public static void setGender(String newGender) {SpeechGenerator.gender = newGender;}

    public static void setArtist(String newArtist) {SpeechGenerator.artist = newArtist;}

    public static void setText(String newText) {SpeechGenerator.text = newText;}

}
