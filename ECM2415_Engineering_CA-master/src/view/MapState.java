/**
 * @author Gabriel Mulcahy
 */

package view;

import controller.UserController;

import model.Maps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MapState extends JPanel implements MenuState {
    private BufferedImage image;
    private BufferedImage dot;
    private BufferedImage error;
    private BufferedImage loading;
    private double rotation;              // current rotation of map so that direction of travel is at the top of the screen
    private double lastDir;               // last known direction of travel
    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;
    private Graphics2D renderer;
    private Maps map;
    private Rectangle clip;
    private Boolean newInstance = true;   // check if map mode has been run previously
    private Boolean lostSignal  = false;  // was the last update during a loss of signal



    @Override
    public void setRenderer(Graphics2D renderer){
        this.renderer = renderer;
    }

    @Override
    public void setFrame(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void setPanel(JPanel panel){
        this.screen = panel;
    }

    @Override
    public void setListener(ActionListener listener){
        this.listener = listener;
    }

    @Override
    public void start(){

        try {
            error   = ImageIO.read(new File("res/NoSat.png"));
            dot     = ImageIO.read(new File("res/red.png"));
            loading = ImageIO.read(new File("res/loading.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newInstance) {
            map = new Maps();
            clip = new Rectangle(UserController.SCREEN_X+8, UserController.SCREEN_Y+32, 191, 241);  //screen size and position
            renderer.clip(clip); //only draw the part of the image that fits the screen
            newInstance = false; //prevents the screen being re-clipped upon re-entering map mode
        }

    }

    @Override
    public void stop(){
        screen.removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e){

    }

    @Override
    public void render(){
        renderer.drawImage( loading, 568, 308, screen); //loading image displayed until map is created
        if (image==null) return;
        renderer.drawImage( image, UserController.SCREEN_X-51, UserController.SCREEN_Y-2, screen); //adjusted so that centre of map is at the centre of the screen
        renderer.drawImage( dot, UserController.SCREEN_X+101, UserController.SCREEN_Y+147, 10, 10, screen ); //centre the red dot to the screen
    }

    private void renderError(){
        renderer.drawImage( error, UserController.SCREEN_X+165, UserController.SCREEN_Y+35, 30, 30, screen ); //draw 'no satellite' image to top right of screen
    }

    /*
     * Redraw the map with a increased/decreased zoom level if plus/minus buttons are pressed, respectively.
     * If select is pressed, the style of the map is toggled
     */
    @Override
    public void navigationButton(NavigationAction e){
        if(e== NavigationAction.PLUS){
            map.zoomIn();
            map.make();
            try {
                image = ImageIO.read(new File("res/output.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            render();
        } else if(e== NavigationAction.MINUS) {
            map.zoomOut();
            map.make();
            try {
                image = ImageIO.read(new File("res/output.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            render();
        } else if(e== NavigationAction.SELECT) {
            map.toggleMapType();
            map.make();
            try {
                image = ImageIO.read(new File("res/output.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            render();
        }
    }


    public MapState() {

    }

    /*
     * Refresh the map based on new data.
     */
    public void update( String latitude, String longitude, String direction, String language ) {
        map.setLat(latitude);
        map.setLong(longitude);
        map.setLanguage(language);
        map.make();                   //create a new map image based on current information
        try {
            image = ImageIO.read(new File("res/output.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (!direction.equals("")) {  //rotate map on new direction
            resetDirection();
            setDirection(Double.parseDouble(direction));
            lastDir = Double.parseDouble(direction);
        }

        if (lostSignal) setDirection(lastDir); //ensures correct rotation upon regain of signal

        if (latitude.equals("")){    //empty string when gps signal is lost
            resetDirection();
            renderError();
            lostSignal = true;
        }else{
            render();
            lostSignal = false;
        }
    }

    /*
     * Rotate map based on direction of travel from north.
     */
    private void setDirection(double angle){
        rotation = -angle;                       //negative as a turn to the left requires a rotation anti-clockwise (and vice-versa)
        double radians = Math.toRadians( rotation );
        renderer.rotate( radians, UserController.SCREEN_X+104, UserController.SCREEN_Y+153); //centre of screen
    }

    /*
     * Reset the rotation of the renderer to 0.
     */
    private void resetDirection(){
        double radians = Math.toRadians(-rotation);
        renderer.rotate( radians, UserController.SCREEN_X+104, UserController.SCREEN_Y+153);
        rotation = 0; //stops more rotations occurring in case this method is called twice before a new direction is given
    }

}
