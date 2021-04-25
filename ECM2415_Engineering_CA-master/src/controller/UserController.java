/**
 * @author: Cai Davies
 */

package controller;

import main.Main;
import model.ModelManager;
import view.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static view.NavigationAction.MINUS;

public class UserController extends JFrame implements ActionListener, MouseListener {

    ModelManager mm;

    // graphics

    private Graphics2D g2d = null;
    private JPanel screen = null;

    private static BufferedImage gpsImage = null;
    private JPowerButton power = null;

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



    public UserController() {
        setTitle("GPS");
        setSize(1280,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(null);

        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Main.running = false;
            }
        };
        addWindowListener(exitListener);
    }

    public void setModelManager(ModelManager mm) {
        this.mm = mm;
    }

    public JPanel getScreen() { return this.screen; }

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
        screen.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

        // power button which will be active across all states
        power = new JPowerButton(new File("res/powerButtonOn.png"),new File("res/powerButtonOff.png"),Color.BLACK);
        power.addActionListener(this);
        power.setSize(65,65);
        power.setLocation(696,182);
        this.add(power);

        // add screen to frame
        getContentPane().add(screen);

        // read in image for device
        try {
            gpsImage = ImageIO.read(new File("res/gpsDevice.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GPS device image not found, system exiting...");
            System.exit(1);
        }

        super.repaint();
    }

    /**
     *
     * Handles the case of device physical button push
     *
     * @param action enum for device physical button action
     */

    public void doAction(NavigationAction action) {
        mm.doAction(action);

        paintScreen();
    }

    /** paints everything including the device **/
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.clearRect(0,0,1280,720);
        g2d.drawImage(gpsImage,GPS_X,GPS_Y,GPS_WIDTH,GPS_HEIGHT,this);
        if(mm!=null) mm.paint(g2d);
        if(power!=null) power.repaint();
    }

    /** paints the screen and the power button **/

    public void paintScreen() {
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setColor(new Color(27,27,27,255));
        g2d.fillRect(SCREEN_X+8,SCREEN_Y+32,SCREEN_WIDTH,SCREEN_HEIGHT);
        screen.revalidate();
        mm.paintScreen(g2d);
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
            mm.actionPerformed(e);
            screen.revalidate();
        }

        paintScreen();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(mm.isOff()) return;
        Point mousePoint = e.getPoint();
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
