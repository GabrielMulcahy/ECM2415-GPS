package controller;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import model.SoundPlayer;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Code modified, with permission, from David Wakeling
 *
 * @author David Wakeling, Scott Woodward
 */
public class Location implements Runnable {

    private String latitude, longitude, time, velocity, direction;
    private SerialPort serialPort;
    private final Object lock = new Object();
    private final static int BAUD_RATE = 9600;
    private final static int TIMEOUT = 2000;
    private final static int BUFF_SIZE = 1024;

    public Location() {
        this.latitude = "";
        this.longitude = "";
        this.time = "0";
        this.direction="";
        this.velocity="0";
    }

    public String[] getData() {
        //Returns data in a synchronized manner to avoid concurrency issues
        synchronized (lock) {
            return new String[]{this.latitude, this.longitude, this.direction, this.velocity, this.time};
        }
    }

    public void openPort(String portName) {
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            if (portId.isCurrentlyOwned()) {
                //System.out.println("port in use");
                SoundPlayer.playFile("res/errorMessages/NoGPS.wav");
                return;
            }
            CommPort commPort = portId.open("LocationObject", TIMEOUT);

            if (commPort instanceof SerialPort) {
                this.serialPort = (SerialPort) commPort;
                this.serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                this.serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                serialPort.setRTS(true);
                //Prevents the error messages appearing in the console
                System.err.close();
            } else {
                SoundPlayer.playFile("res/errorMessages/NoGPS.wav");
            }
        } catch (Exception ex) {
            //Exits in the case of an error as the device will no longer work.
            SoundPlayer.playFile("res/errorMessages/NoGPS.wav");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            InputStream in = this.serialPort.getInputStream();
            byte[] buffer = new byte[BUFF_SIZE];
            String s;
            int n;
            String d, v;


            /*Can guarentee the order in which the NMEA messages appear and thus can always
            * assure that the GPVTG message will appear before the GPGLL message.
            * Further, with ot without connection, the messages will always contain the same
            * number of commas and so the splitting of the input string will be consistent.
            */

            while ((n = in.read(buffer)) > -1) {
                s = new String(buffer, 0, n);

                //Read until the VTG message is read
                while (!s.startsWith("$GPVTG")){
                    n = in.read(buffer);
                    s = new String(buffer,0,n);
                }
                String ss[] = s.split(",");
                //The direction and velocity will always be in positions 1 and 7 of the GPVTG message
                d = ss[1];
                v = ss[7];
                n = in.read(buffer);
                s = new String(buffer,0,n);

                //Read until the GLL message is read.
                while (!s.startsWith("$GPGLL")) {
                    n = in.read(buffer);
                    s = new String(buffer, 0, n);
                }
                ss = s.split(",");
                //Synchronize to ensure that no concurrency issues occur when updating
                synchronized (lock) {
                    if (ss[1].equals("")) {
                        this.latitude = "";
                        this.longitude = "";
                    } else {
                        //Converts from Degrees and Decimal minutes to Decimal degrees as it is the correct format for the APIs
                        String lat = String.format("%.6f", Integer.valueOf(ss[1].substring(0,2)) + Float.valueOf(ss[1].substring(2))/60);
                        String lon = String.format("%.6f", Integer.valueOf(ss[3].substring(0,3)) + Float.valueOf(ss[3].substring(3))/60);
                        this.latitude = (ss[2].equals("N")) ? lat : "-" + lat;
                        this.longitude = (ss[4].equals("E")) ? lon : "-" + lon;
                    }
                        //Converts the time into seconds
                        this.time = String.format("%d", Integer.valueOf(ss[5].substring(0,2))*3600 + Integer.valueOf(ss[5].substring(2,4))*60 + Integer.valueOf(ss[5].substring(4,6)));
                        this.direction = d;
                        this.velocity = v;
                    }
                    Thread.sleep(500);
                }

        } catch (IOException | InterruptedException e) {
            SoundPlayer.playFile("res/errorMessages/NoGPS.wav");
        }
    }
}
/**/