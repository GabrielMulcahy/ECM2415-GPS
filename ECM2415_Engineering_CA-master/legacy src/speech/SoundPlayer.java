package speech;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
/*
 * Original code by David Wakeling
 *
 * Modified by Joshua Chalcraft
 *  - FILENAME now has an additional "res/directions" added at the start of the string as output is stored there
 *  - Main method now replaced by a "play" method
 *  - Changed access modifiers of provided methods
 */
public class SoundPlayer
{
    private final static String FILENAME = "res/directions/sound_output.wav";

    /*
    * Set up stream.
    */
    private static AudioInputStream setupStream(String name)
    {
        try
        {
            File file = new File(name);
            System.out.println(file.getCanonicalPath());
            AudioInputStream stm = AudioSystem.getAudioInputStream(file);
            return stm;
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    /*
    * Read stream.
    */
    private static ByteArrayOutputStream readStream(AudioInputStream stm)
    {
        try
        {
            AudioFormat af  = stm.getFormat();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int bufferSize = (int) af.getSampleRate() * af.getFrameSize();
            byte buffer[] = new byte[bufferSize];

            for ( ; ; )
            {
                int n = stm.read( buffer, 0, buffer.length );
                if ( n > 0 )
                {
                    bos.write( buffer, 0, n );
                }
                else
                {
                    break;
                }
            }
            return bos;
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    /*
    * Play stream.
    */
    private static void playStream( AudioInputStream stm, ByteArrayOutputStream bos )
    {
        try
        {
            AudioFormat    af   = stm.getFormat();
            byte[]         ba   = bos.toByteArray();
            DataLine.Info  info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(af);
            line.start();
            line.write(ba, 0, ba.length);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }


    public static void play()
    {
        AudioInputStream stm = setupStream( FILENAME );
        playStream( stm, readStream( stm ) );
    }
}
