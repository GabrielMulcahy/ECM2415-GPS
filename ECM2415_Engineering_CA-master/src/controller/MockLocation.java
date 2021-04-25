/**
 * @author Scott Woodward
 */

package controller;

import model.SoundPlayer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MockLocation implements Runnable{

    private String latitude, longitude, time, velocity, direction;
    private final Object lock = new Object();

    public MockLocation() {
        this.latitude = "";
        this.longitude = "";
        this.time = "0";
        this.velocity = "";
        this.direction = "";
    }

    public String[] getData() {
        synchronized (lock) {
            return new String[]{this.latitude, this.longitude, this.direction, this.velocity, this.time};
        }
    }

    //Does nothing for the mock location
    public void openPort(String portName){}

    @Override
    public void run(){
        //Replays pre-recorded data from the GPS device to simulate a journey

        String lat;
        String lon;
        try{

            //Reads in the data
            FileInputStream fstream = new FileInputStream("res/MockLocationData.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine = br.readLine();
            //Behaves in a similar way to the Location class
            /*Can guarentee the order in which the NMEA messages appear and thus can always
             * assure that the GPVTG message will appear before the GPGLL message.
             * Further, with ot without connection, the messages will always contain the same
             * number of commas and so the splitting of the input string will be consistent.
             */
            while (strLine != null)   {

                //Read until a GPVTG message is read
                while(!strLine.startsWith("$GPVTG")){
                    strLine = br.readLine();
                }
                String s[] = strLine.split(",");
                //The direction and velocity will always be in positions 1 and 7 of the GPVTG message
                String direction = s[1];
                String speed = s[7];
                strLine = br.readLine();

                //Read until the GLL message is read.
                while(!strLine.startsWith("$GPGLL")){
                    strLine = br.readLine();
                }
                String ss[] = strLine.split(",");
                if (ss[1].equals("")||ss[3].equals("")){
                    lat="";
                    lon="";
                }else {
                    //Reformat the latitude and longitude
                    lat = String.format("%.6f", Integer.valueOf(ss[1].substring(0, 2)) + Float.valueOf(ss[1].substring(2)) / 60);
                    lon = String.format("%.6f", Integer.valueOf(ss[3].substring(0, 3)) + Float.valueOf(ss[3].substring(3)) / 60);
                    if (ss[2].equals("S")) lat = "-" + lat;
                    if (ss[4].equals("W")) lon = "-" + lon;
                }
                //Convert the time into seconds
                String time = String.format("%d", Integer.valueOf(ss[5].substring(0,2))*3600 + Integer.valueOf(ss[5].substring(2,4))*60 + Integer.valueOf(ss[5].substring(4,6)));

                synchronized (lock){
                    this.velocity = speed;
                    this.direction = direction;
                    this.latitude = lat;
                    this.longitude = lon;
                    this.time = time;
                }
                Thread.sleep(1000);
            }
            //Close the input stream
            in.close();
        }catch (Exception e){//Catch exception if any
            SoundPlayer.playFile("res/errorMessages/NoGPS.wav");
        }
    }
}


