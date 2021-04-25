/**
 * Author: Cai Davies
 */

package menu;

import main.MenuAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.IconUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JMenuButton extends JButton {

    MenuAction menuAction;
    int width,height;
    Image icon;

    public JMenuButton(String text, MenuAction action, File locationIcon) {
        super("<html><div style='text-align: center;'>" + text + "</div></html>");
        this.menuAction = action;
        setMargin(new Insets(4, 1, 10, 1));
        setVerticalAlignment(SwingConstants.TOP);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        try {
            icon = ImageIO.read(locationIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIcon(new ImageIcon(icon));
        setBorder(null);
        setModel(new FixedStateButtonModel());

    }

    public void setPreferredIconSize(Dimension d) {
        if(icon==null) return;
        this.width = d.width;
        this.height = d.height;
        setIcon(new ImageIcon(icon.getScaledInstance(width,height,Image.SCALE_DEFAULT)));
    }

    public MenuAction getMenuAction() {
        return menuAction;
    }

    public void select() { setBackground(Color.ORANGE); }
    public void unselect() { setBackground(new JButton().getBackground()); }

public class FixedStateButtonModel extends DefaultButtonModel{

    @Override
    public boolean isPressed() {
        return false;
    }

    @Override
    public boolean isRollover() {
        return false;
    }

    @Override
    public void setRollover(boolean b) {
        // do nothing
    }

}

}

