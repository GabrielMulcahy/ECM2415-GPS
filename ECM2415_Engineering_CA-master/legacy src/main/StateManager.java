/**
 * Author: Cai Davies
 */

package main;

import menu.JPowerButton;
import menu.MainMenuState;
import menu.MenuState;
import menu.OnOffState;
import satellite.AboutMode;
import satellite.SatelliteMode;
import whereTo.TripComputer;
import whereTo.WhereTo;
import map.MapState;
import speech.SpeechMode; //Change by josh - renamed class

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static main.NavigationAction.MINUS;

public class StateManager extends JFrame implements ActionListener, MouseListener {

    // graphics
    private MenuState states[] = new MenuState[8];
    private Graphics2D g2d = null;
    private JPanel screen = null;

    private static BufferedImage gpsImage = null;
    private JPowerButton power = null;

    private static MenuAction state = MenuAction.ON_OFF_STATE;

    // graphics constants
    public static final int GPS_WIDTH = 650;
    public static final int GPS_HEIGHT = 650;
    public static final int GPS_X = 340;
    public static final int GPS_Y = 60;
    public static final int SCREEN_WIDTH = 191;
    public static final int SCREEN_HEIGHT = 241;
    public static final int SCREEN_X = 561;
    public static final int SCREEN_Y = 278;

    // device physical button bounds
    public final Rectangle boundsPlusButton = new Rectangle(512,154,6,40);
    public final Rectangle boundsMinusButton = new Rectangle(508,206,6,40);

    public final Rectangle boundsSelectButton = new Rectangle(508,279,8,50);
    public final Rectangle boundsMenuButton = new Rectangle(810,158,14,52);



    public StateManager() {
        setTitle("GPS");
        setSize(1280,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
    }

    /**
     * Sets up the frame, screen and power button ready for use, and creates state objects for the array of states
     */

    public void start() {
        this.g2d = (Graphics2D)(super.getGraphics());
        this.addMouseListener(this); // add mouselistener to frame

        // create screen of device as JPanel
        screen = new JPanel();
        screen.setBackground(new Color(27,27,27,255));
        screen.setBounds(SCREEN_X,SCREEN_Y,SCREEN_WIDTH,SCREEN_HEIGHT);

        // power button which will be active across all states
        power = new JPowerButton(new File("res/powerButtonOn.png"),new File("res/powerButtonOff.png"),Color.BLACK);
        power.addActionListener(this);
        power.setSize(65,65);
        power.setLocation(696,182);
        this.add(power);

        /**TODO:  change layout of screen to null when using absolute locations for buttons**/
        //screen.setLayout(null);

        // add screen to frame
        getContentPane().add(screen);

        // read in image for device
        try {
            gpsImage = ImageIO.read(new File("res/gpsDevice.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // create new state objects
        states[0] = new OnOffState();
        states[1] = new MainMenuState(this);
        states[MenuAction.TRIP_COMPUTER_STATE.getVal()] = new TripComputer();
        states[MenuAction.WHERE_TO_STATE.getVal()] = new WhereTo();
        states[MenuAction.MAP_STATE.getVal()] = new MapState();
        states[MenuAction.SPEECH_STATE.getVal()] = new SpeechMode(); //change by Josh - renamed class
        states[MenuAction.SATELLITE_STATE.getVal()] = new SatelliteMode();
        states[MenuAction.ABOUT_STATE.getVal()] = new AboutMode();

        // set rendering and listening objects to states
        for(MenuState state:states) {
            if(state!=null) {
                state.setRenderer(g2d);
                state.setFrame(this);
                state.setListener(this);
                state.setPanel(screen);
            }
        }

        // start the first state, and paint
        states[state.getVal()].start();
        super.repaint();
    }

    /**
     *
     * Handles the case of device physical button push
     *
     * @param action enum for device physical button action
     */

    public void doAction(NavigationAction action) {
        switch(action) {
            case POWER:
                if(state==MenuAction.ON_OFF_STATE) {
                    states[state.getVal()].stop();
                    state = MenuAction.MAIN_STATE;
                    states[state.getVal()].start();
                }else {
                    states[state.getVal()].stop();
                    state = MenuAction.ON_OFF_STATE;
                    states[state.getVal()].start();
                    screen.removeAll();
                    screen.repaint();
                }
                break;
            case MENU:
                if(state==MenuAction.MAIN_STATE) return;
                states[state.getVal()].stop();
                state = MenuAction.MAIN_STATE;
                states[state.getVal()].stop();
                states[state.getVal()].start();
                //states[state.getVal()].render();
                break;
            case SELECT:
                states[state.getVal()].navigationButton(NavigationAction.SELECT);
                break;
            case PLUS:
                states[state.getVal()].navigationButton(NavigationAction.PLUS);
                break;
            case MINUS:
                states[state.getVal()].navigationButton(NavigationAction.MINUS);
                break;
        }

        paintScreen();
    }


    public void goToState(MenuAction state) {
        if(states[state.getVal()]==null) return;
        states[this.state.getVal()].stop();
        this.state = state;
        states[this.state.getVal()].start();
        screen.revalidate();
        states[this.state.getVal()].render();
    }


    /** paints everything including the device **/
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.clearRect(0,0,1280,720);
        g2d.drawImage(gpsImage,GPS_X,GPS_Y,GPS_WIDTH,GPS_HEIGHT,this);
        if(states[state.getVal()]!=null) states[state.getVal()].render();
        if(power!=null) power.repaint();
    }

    /** paints the screen and the power button **/

    public void paintScreen() {
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setColor(new Color(27,27,27,255));
        g2d.fillRect(SCREEN_X+8,SCREEN_Y+32,SCREEN_WIDTH,SCREEN_HEIGHT);
        screen.revalidate();
        if(states[state.getVal()]!=null) states[state.getVal()].render();
        power.repaint();
    }

    /**
     * Called when a JButton which has had an instance of UserController added as it's action listener
     * is pressed, can then send event object to appropriate state
     *
     * @param e event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(power)) {
            doAction(NavigationAction.POWER);
            power.clickButton();
        }else{
            states[state.getVal()].actionPerformed(e);
            screen.revalidate();
        }

        paintScreen();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(state==MenuAction.ON_OFF_STATE) return;
        Point mousePoint = e.getPoint();
        System.out.println(mousePoint);
        if(intersects(mousePoint,boundsSelectButton)) {
            doAction(NavigationAction.SELECT);
        }
        if(intersects(mousePoint,boundsPlusButton)) {
            doAction(NavigationAction.PLUS);
        }
        if(intersects(mousePoint,boundsMinusButton)) {
            doAction(MINUS);
        }
        if(intersects(mousePoint,boundsMenuButton)) {
            doAction(NavigationAction.MENU);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean intersects(Point point, Rectangle rectangle) {
        if(point.x>=rectangle.x && point.x<=rectangle.x+rectangle.width) {
            if(point.y>=rectangle.y && point.y<=rectangle.y+rectangle.height) return true;
        }
        return false;
    }
}
