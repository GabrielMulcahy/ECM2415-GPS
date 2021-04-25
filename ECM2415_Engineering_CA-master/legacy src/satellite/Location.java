
package satellite;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;

/*
 *
 * COMMENT IN WHEN ALL MACHINES CAN RUN THE GPS
 * Code modified, with permission, from David Wakeling
 *
 * @author Scott Woodward
 */
public class Location implements Runnable {

    private String latitude, longitude, time, velocity, direction;
    private SerialPort serialPort;
    private final Object lock = new Object();
    final static int BAUD_RATE = 9600;
    final static int TIMEOUT = 2000;
    final static int BUFF_SIZE = 1024;

    public Location() {
        this.latitude = "";
        this.longitude = "";
        this.time = "0";
    }

    public String[] getData() {
        synchronized (lock) {
            return new String[]{this.latitude, this.longitude, this.direction, this.velocity, this.time};
        }
    }

    public void openPort(String portName) {
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            if (portId.isCurrentlyOwned()) {
                System.out.println("port in use");
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
                System.out.println("not a serial port");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void run() {
        try {
            InputStream in = this.serialPort.getInputStream();
            byte[] buffer = new byte[BUFF_SIZE];
            String s;
            int n;

            while ((n = in.read(buffer)) > -1) {
                s = new String(buffer, 0, n);
                String ss[] = s.split(",");
                if (s.startsWith("$GPVTG")){
                    synchronized (lock){
                        this.direction = ss[1];
                        this.velocity = ss[7];
                        System.out.println("DIRECTION: "+ss[1]);
                        System.out.println("VELOCITY: "+ss[7]);
                    }
                }
                //Can guarantee that the GPGLL message will have the same format with or without connection to the satellites
                else if (s.startsWith("$GPGLL")) {
                    //String ss[] = s.split(",");
                    synchronized (lock) {
                        if (ss[1].equals("")) {
                            this.latitude = "";
                            this.longitude = "";
                        } else {
                            //Converts to correct formatting for APIs
                            //String lat = String.valueOf(Integer.valueOf(ss[1].substring(0,2)) + Float.valueOf(ss[1].substring(2))/60);
                            //String lon = String.valueOf(Integer.valueOf(ss[3].substring(0,3)) + Float.valueOf(ss[3].substring(3))/60);
                            String lat = String.format("%.6f", Integer.valueOf(ss[1].substring(0,2)) + Float.valueOf(ss[1].substring(2))/60);
                            String lon = String.format("%.6f", Integer.valueOf(ss[3].substring(0,3)) + Float.valueOf(ss[3].substring(3))/60);
                            this.latitude = (ss[2].equals("N")) ? lat : "-" + lat;
                            this.longitude = (ss[4].equals("E")) ? lon : "-" + lon;
                        }
                        this.time = ss[5];
                    }
                    Thread.sleep(2000);
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
/**/