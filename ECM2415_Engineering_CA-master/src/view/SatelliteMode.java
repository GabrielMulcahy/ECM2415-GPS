package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
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

        latitude = new JLabel("", SwingConstants.CENTER);
        longitude = new JLabel("", SwingConstants.CENTER);
        error = new JLabel();

        //Reads in the no satellite image and scales it to the appropriate size
        ImageIcon icon = new ImageIcon("res/NoSat.png");
        Image display = icon.getImage().getScaledInstance(this.screen.getWidth()-10, this.screen.getWidth()-10, Image.SCALE_SMOOTH);
        error.setIcon(new ImageIcon(display));

        latitude.setVerticalAlignment(SwingConstants.BOTTOM);
        longitude.setVerticalAlignment(SwingConstants.TOP);
        error.setHorizontalAlignment(SwingConstants.CENTER);


        latitude.setPreferredSize(new Dimension(180, (this.screen.getHeight()-10)/2));
        longitude.setPreferredSize(new Dimension(180, (this.screen.getHeight()-10)/2));
        error.setPreferredSize(new Dimension(this.screen.getWidth()-10, this.screen.getHeight()-10));


        Font f = new Font("Ariel", Font.BOLD, 25);
        latitude.setFont(f);
        longitude.setFont(f);

        this.screen.add(latitude);
        this.screen.add(longitude);
        this.screen.add(error);

        latitude.setVisible(false);
        longitude.setVisible(false);
        error.setVisible(true);
    }

    public void update(String latitude, String longitude){
        //When updated, displays the latitude and longitude coordinates or the error message if there is no connection
        if (latitude.equals("") || longitude.equals("")){
            this.latitude.setVisible(false);
            this.longitude.setVisible(false);
            this.error.setVisible(true);
        }else{
            this.error.setVisible(false);
            this.latitude.setVisible(true);
            this.longitude.setVisible(true);
            String lat = latitude.charAt(0) == '-' ? latitude.substring(1) + " S": latitude + " N" ;
            String lon = longitude.charAt(0) == '-' ? longitude.substring(1) + " W": longitude + " E" ;
            this.latitude.setText(lat);
            this.longitude.setText(lon);
        }
    }

    @Override
    public void stop() {
        //Removes the items on the screen
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