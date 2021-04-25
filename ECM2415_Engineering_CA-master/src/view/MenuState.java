/**
 * @author: Cai Davies
 */

package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface MenuState {

    /** set the renderer to render buttons and images **/
    void setRenderer(Graphics2D renderer);
    /** set the frame **/
    void setFrame(JFrame frame);
    /** set the panel, which is the screen **/
    void setPanel(JPanel panel);
    /** set the listener to add to buttons when made **/
    void setListener(ActionListener listener);
    /** used for loading and showing JButtons etc. **/
    void start();
    /** used to hide JButtons etc. **/
    void stop();
    /** for when a button is pressed, use e.getSource() to return the button object **/
    void actionPerformed(ActionEvent e);
    /** render only buttons and images, don't redraw panel or frame **/
    void render();
    /** for when a navigation button has been pressed **/
    void navigationButton(NavigationAction e);

}
