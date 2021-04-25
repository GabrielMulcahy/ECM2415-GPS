/*
Author-Rob Wells
 */
package whereTo;
import main.NavigationAction;
import menu.MenuState;
import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TripComputer implements MenuState, Runnable  {


    Graphics2D renderer;
    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;
    MyText[] textLabels = new MyText[3];
    private static double prevLat;
    private static double prevnLong;
    private static double currentLat;
    private static double currentLong;
    private static double prevTime;
    private static double currentTime;
    private double totalDistace;




    @Override
    public void setRenderer(Graphics2D renderer) {
        this.renderer = renderer;
    }

    @Override
    public void setFrame(JFrame frame) {
        this.frame=frame;
    }

    @Override
    public void setPanel(JPanel panel) {
        this.screen=panel;
    }

    @Override
    public void setListener(ActionListener listener) {
        this.listener=listener;
    }

    @Override
    public void start() {
        textLabels[0]= new MyText("Trip odem", "0.86 KM");
        textLabels[1] = new MyText("Speed", "7 KM/H");
        textLabels[2]= new MyText("Moving time", "27min 8 sec");
        for (MyText x: textLabels){
            x.setPreferredSize(new Dimension(180, 77));
            screen.add(x);
        }
    }


    @Override
    public void stop() {
        screen.removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void render() {
        for (MyText x: textLabels){
            if(x!=null) x.repaint();
        }
    }
    @Override
    public void navigationButton(NavigationAction e) {
        if (e== NavigationAction.POWER){
            stop();
        }
        else if (e == NavigationAction.MENU);
            stop();
    }

    @Override
    public void run(){
        int x=2;
        while(x==2){}
    }
    StyledDocument doc;
    public class MyText extends JTextPane  {
        String infoHeader;
        String infoValue;
        MyText(String header, String value){
            StyledDocument doc = this.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            this.setFont(new Font("Verdana", Font.BOLD, 20));
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            this.infoHeader=header;
            this.infoValue=value;
            this.setText(infoHeader+"\n"+infoValue);
        }
        public void resetValues(String newValue){
            setText(infoHeader+"\n"+newValue);
        }
    }
    public double square(double n ){
        return java.lang.Math.pow(n,2);
    }
    public double sqaureRoot(double n){
        return java.lang.Math.pow(n,2);
    }
    public int getTimeDifference(int prevTime, int currentTime){
        return currentTime-prevTime;
    }
    public double getCurrentSpeed(double prevLat,double prevnLong, double currentLat,double currentLong,int prevTime, int currentTime){
        double distance = getDistanceFromLatLonInKm( prevLat,prevnLong,currentLat,currentLong);
        double time = getTimeDifference(prevTime, currentTime);
        double speed = distance/time/1000;
        return speed;
    }
    public void setCoords(String latitude, String longitude, String time) {
        if (latitude != "" && longitude != " ") {
            prevLat = currentLat;
            prevnLong = currentLong;
            prevTime = currentTime;
            currentLat = Double.parseDouble(latitude);
            currentLong = Double.parseDouble(longitude);
            currentTime = Double.parseDouble(time);
        }
    }
    public double getDistanceFromLatLonInKm(Double lat1,Double lon1,Double lat2, Double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public Double deg2rad(Double deg) {
        return deg * (Math.PI/180);
    }
}
