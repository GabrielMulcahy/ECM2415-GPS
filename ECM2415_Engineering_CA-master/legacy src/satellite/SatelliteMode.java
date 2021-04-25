package satellite;

import main.NavigationAction;
import menu.MenuState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * From rxtx-2.2pre2-bins.zip, extract RXTXcomm.jar and
 * the win64 version of rxtxSerial.dll.
 *
 * RXTXcomm.jar should be added to the CLASSPATH and 
 * rxtxSerial.dll should be placed in current directory.
 *
 * @author Scott Woodward
 */
public class SatelliteMode implements MenuState{

    private JFrame frame;
    private JPanel screen;
    private JLabel latitude, longitude, error;
    private ActionListener listener;
    private Graphics2D renderer;
    private Thread updateThread;


    @Override
    public void setRenderer(Graphics2D renderer) { this.renderer = renderer; }
    @Override
    public void setFrame(JFrame frame) { this.frame = frame; }
    @Override
    public void setPanel(JPanel panel) { this.screen = panel; }
    @Override
    public void setListener(ActionListener listener) { this.listener = listener; }

    @Override
    public void start() {

       // MockLocation loc = new MockLocation();
        Location loc = new Location(); //Comment in with access to satellite connection
        loc.openPort("COM4");
        Thread t = new Thread(loc);

        latitude = new JLabel("", SwingConstants.CENTER);
        longitude = new JLabel("", SwingConstants.CENTER);
        error = new JLabel("<html><div style='text-align: center;'>POSITION<br/>CANNOT<br/>BE<br/>DETERMINED</div></html>", SwingConstants.CENTER);

        latitude.setVerticalAlignment(SwingConstants.BOTTOM);
        longitude.setVerticalAlignment(SwingConstants.TOP);


        latitude.setPreferredSize(new Dimension(180, (this.screen.getHeight()-10)/2));
        longitude.setPreferredSize(new Dimension(180, (this.screen.getHeight()-10)/2));
        error.setPreferredSize(new Dimension(180, this.screen.getHeight()-10));


        Font f = new Font("Ariel", Font.BOLD, 25);
        latitude.setFont(f);
        longitude.setFont(f);
        error.setFont(f);

        this.screen.add(latitude);
        this.screen.add(longitude);
        this.screen.add(error);

        latitude.setVisible(false);
        longitude.setVisible(false);
        error.setVisible(true);

        updateThread = new Thread(){
          public void run() {
              while (true) {
                  String[] data = loc.getData();
                  if (data[0].equals("")){
                      latitude.setVisible(false);
                      longitude.setVisible(false);
                      error.setVisible(true);
                  }else{
                      error.setVisible(false);
                      latitude.setVisible(true);
                      longitude.setVisible(true);
                      String lat = data[0].charAt(0) == '-' ? data[0].substring(1) + " S": data[0] + " N" ;
                      String lon = data[1].charAt(0) == '-' ? data[1].substring(1) + " W": data[1] + " E" ;
                      latitude.setText(lat);
                      longitude.setText(lon);
                  }
                  try {
                      sleep(3000);
                  } catch (InterruptedException e) {
                      break;
                  }
              }
          }
        };


        t.start();
        updateThread.start();
    }

    @Override
    public void stop() {
        updateThread.interrupt();
        this.screen.setBackground(new Color(27,27,27,255));
        screen.remove(longitude);
        screen.remove(latitude);
        screen.remove(error);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void render() {
        longitude.repaint();
        latitude.repaint();
        error.repaint();
        this.screen.setBackground(Color.WHITE);
    }

    @Override
    public void navigationButton(NavigationAction e) {

    }

}