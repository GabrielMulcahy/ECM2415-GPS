/**
 * @author: Cai Davies
 */

package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JPowerButton extends JIconButton {

    private Image imgOn;
    private Image imgOff;

    boolean on = false;

    public JPowerButton(File imgOn, File imgOff, Color backgroundColor) {
        super(imgOn,backgroundColor);
        try {
            this.imgOn = ImageIO.read(imgOn);
            this.imgOff = ImageIO.read(imgOff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIcon(new ImageIcon(this.imgOff));
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x,y);
        setIcon(new ImageIcon(this.imgOff.getScaledInstance(width,height,Image.SCALE_DEFAULT)));
    }

    public void clickButton() {
        if(on) {
            on = false;
            setIcon(new ImageIcon(this.imgOff.getScaledInstance(width,height,Image.SCALE_DEFAULT)));
        }else{
            on = true;
            setIcon(new ImageIcon(this.imgOn.getScaledInstance(width,height,Image.SCALE_DEFAULT)));
        }
    }

}
