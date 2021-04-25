/**
 * @author Scott Woodward
 */

package view;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AboutMode implements MenuState {

    private JFrame frame;
    private JPanel screen;
    private BufferedImage display;
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
        //Displays the static image of the information
        try {
            display = ImageIO.read(new File("res/aboutMode.png"));
        } catch (IOException e) {
            //Won't happen as it is a given resource
            e.printStackTrace();
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
        renderer.drawImage(display, 568, 308, screen);


    }

    @Override
    public void navigationButton(NavigationAction e) {

    }
}
