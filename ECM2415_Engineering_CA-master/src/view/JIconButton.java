/**
 * @Author: Cai Davies
 */

package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JIconButton extends JButton {

    protected Image iconImage;
    protected int width,height = 0;

    JIconButton(File imageLocation, Color backgroundColor) {
        super();
        try {
            iconImage = ImageIO.read(imageLocation);
        } catch (IOException e) {
            iconImage = null; // adds null, meaning no icon, not a problem as text will still be visible
            e.printStackTrace();
        }
        setModel(new FixedStateButtonModel());
        setBackground(backgroundColor);
        setForeground(backgroundColor);
        setBorder(null);
        setFocusPainted(false);
        setIcon(new ImageIcon(iconImage));
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x,y);
        width = x;
        height = y;
        setIcon(new ImageIcon(iconImage.getScaledInstance(x,y,Image.SCALE_DEFAULT)));
    }

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
