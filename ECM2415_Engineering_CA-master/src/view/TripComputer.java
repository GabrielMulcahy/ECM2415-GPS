/**
 * @author Rob Wells
 */
package view;
/*
    Have calculation functions in a model class so they can be calculating in the background
 */

import model.ModelTripComputer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TripComputer implements MenuState {
    Graphics2D renderer;
    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;

    static MyText[] textLabels = new MyText[3];
    public TripComputer(){}

        /*
         * Overridden methods from the MenuState Interface that provides the basis for rendering, running, interacting
         * and stopping this GPS view
         */

    @Override
    public void setRenderer(Graphics2D renderer) {
        this.renderer = renderer;
    }

    @Override
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setPanel(JPanel panel) {
        this.screen = panel;
    }

    @Override
    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        textLabels[0] = new MyText("Trip odem", "0.0 KM");
        textLabels[1] = new MyText("Speed", "0 KM/H");
        textLabels[2] = new MyText("Moving time", "0min 0 sec");
        for (MyText x : textLabels) {
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
        for (MyText x : textLabels) {
            if (x != null) x.repaint();
        }
    }

    @Override
    public void navigationButton(NavigationAction e) {
        if (e == NavigationAction.POWER) {
            stop();
        } else if (e == NavigationAction.MENU) {

        }
        else if (e == NavigationAction.SELECT) ;
    }

    /**
     *  @author  Rob Wells
     *  Class which create Swing JTextPane elements which make the divisions to hold the Trip computer information
     */
    public class MyText extends JTextPane {
        String infoHeader;
        String infoValue;

        MyText(String header, String value) {
            StyledDocument doc = this.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            this.setFont(new Font("Verdana", Font.BOLD, 20));
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            this.infoHeader = header;
            this.infoValue = value;
            this.setText(infoHeader + "\n" + infoValue);
        }
        /*
         * Changes the Text in the JTextPane
         */
        public void resetValues(String newValue) {
            setText(infoHeader + "\n" + newValue);
        }
    }

    /*
     *  Method for updating the view with information from the model
     */
    public void updateTripComputerMode(String distance, String speed, String time) {
        if (speed != null && speed.length()>0) {//If there is no speed therefore there isn't GPS data being recieved
            double doubleSpeed = Double.parseDouble(speed);
            String roundedSpeed = String.format("%,.1f", doubleSpeed);
            textLabels[1].resetValues(roundedSpeed + "KM/H");
        } else {
            textLabels[1].resetValues("0" + "KM/H");
        }
        Double doubleDistance = Double.parseDouble(distance);
        String roundedDistance = String.format("%,.3f", doubleDistance);
        String timeInMins = ModelTripComputer.getTimeInMins(ModelTripComputer.getCurrentTime());
        textLabels[0].resetValues(roundedDistance + "KM");
        textLabels[2].resetValues(timeInMins);
    }
    /*
    Set the view values to zero
     */
    public static void resetTripComputer(){
        textLabels[0].resetValues("0" + "KM");
        textLabels[1].resetValues("0" + "KM/H");
        textLabels[2].resetValues("0 Min 0 Sec");
    }

}