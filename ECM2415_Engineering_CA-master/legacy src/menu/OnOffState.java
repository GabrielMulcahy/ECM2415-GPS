/**
 * Author: Cai Davies
 */

package menu;

import main.NavigationAction;
import main.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Empty class of type MenuState which displays nothing,
 * but comforms with state switching in UserController for on/off
 * to menu and from any state to on/off
 *
 */

public class OnOffState implements MenuState {

    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;
    private Graphics2D renderer;

    public OnOffState() {

    }

    public void setRenderer(Graphics2D renderer) {
        this.renderer = renderer;
    }
    public void setFrame(JFrame frame) { this.frame = frame; }
    public void setPanel(JPanel screen) { this.screen = screen; }
    public void setListener(ActionListener listener) { this.listener = listener; }

    public void start() {

    }

    public void stop() {

    }

    public void render() {

    }

    public void actionPerformed(ActionEvent e) {

    }

    public void navigationButton(NavigationAction e) {

    }


}
