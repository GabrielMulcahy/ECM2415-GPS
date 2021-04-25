package model;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;

/**
 * @author David Wakeling, Joshua Chalcraft
 *  - SpeechGenerator takes in a string of text, a language, a gender and an artist
 *  - It then uses Microsoft Cognitive Services to generate a sound file through this information
 */
public class SpeechGenerator
{
    private final static String KEY = "e9488f2304204a599d806da749983124";
    private static String token;
    private final static String OUTPUT = "sound_output.wav";
    private final static String PATH   = "res/directions/"+OUTPUT;
    private final static String FORMAT = "riff-16khz-16bit-mono-pcm";
    private final static Object LOCK = new Object();

    public static String renewAccessToken()
    {
        final String method = "POST";
        final String url = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
        final byte[] body = {};
        final String[][] headers = {{"Ocp-Apim-Subscription-Key", KEY}, {"Content-Length", String.valueOf(body.length)}};
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
            final byte[] body = ("<speak version='1.0' xml:lang='en-us'>" + "<voice xml:lang='" + lang + "' " + "xml:gender='" + gender + "' " + "name='Microsoft Server Speech Text to Speech Voice " + artist + "'>" + text + "</voice></speak>").getBytes();
            final String[][] headers
                    = { { "Content-Type"             , "application/ssml+xml"      }
                    , { "Content-Length"           , String.valueOf( body.length ) }
                    , { "Authorization"            , "Bearer " + token             }
                    , { "X-Microsoft-OutputFormat" , format                        }
            };
            byte[] response = HttpConnect.httpConnect(method, url, headers, body);
            return response;
        }
        catch (Exception ex)
        {
            SoundPlayer.playFile("res/errorMessages/MicrosoftError");
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
        catch (Exception ex)
        {
            SoundPlayer.playFile("res/errorMessages/HardwareDirectionsFailure");
        }
    }

    /*
     * Generate speech.
     */
    public static void generate(String token, String text, String lang, String gender, String artist)
    {
        //Store token in model manager, have thread that sleeps for 10 minutes that renews token.
        synchronized (LOCK)
        {
            final byte[] speech = generateSpeech(token, text, lang, gender, artist, FORMAT);
            writeData(speech, PATH);
        }
    }



}
