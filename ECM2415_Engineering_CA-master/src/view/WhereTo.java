/**
 * @author Rob Wells
 */

package view;

import model.ModelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *@author Rob Wells
 */
public class WhereTo implements MenuState {
    //This class is the view for the "Where To?" mode. This classes uses the swing library to create a virtual keyboard to take in text input.
    private Graphics2D renderer;
    private JFrame frame;
    private JPanel screen;
    final JTextField txtf = new RoundJTextField(30);
    private int currentButton;
    private int currentMode;
    ModelManager model;

    private CharacterButton[] charButtons = new CharacterButton[28];
    private NumberButton[] numButtons = new NumberButton[12];
    private JPanel panel = new JPanel();

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

        FlowLayout keyboardLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        keyboardLayout.setAlignOnBaseline(true);
        panel.setLayout(keyboardLayout);
        this.screen = panel;
    }

    @Override
    public void setListener(ActionListener listener) {
        ActionListener listener1 = listener;
    }

    /*
     Overriden method from the implemented MenuState interface which will instantiate all the java.swing elements
     */
    @Override
    public void start() {
        currentButton = 0;
        currentMode = 1;
        charButtons[0] = new CharacterButton("A", 1);
        charButtons[1] = new CharacterButton("B", 2);
        charButtons[2] = new CharacterButton("C", 3);
        charButtons[3] = new CharacterButton("D", 4);
        charButtons[4] = new CharacterButton("E", 5);
        charButtons[5] = new CharacterButton("F", 6);
        charButtons[6] = new CharacterButton("G", 7);
        charButtons[7] = new CharacterButton("H", 8);
        charButtons[8] = new CharacterButton("I", 9);
        charButtons[9] = new CharacterButton("J", 10);
        charButtons[10] = new CharacterButton("K", 11);
        charButtons[11] = new CharacterButton("L", 12);
        charButtons[12] = new CharacterButton("M", 13);
        charButtons[13] = new CharacterButton("N", 14);
        charButtons[14] = new CharacterButton("O", 15);
        charButtons[15] = new CharacterButton("P", 16);
        charButtons[16] = new CharacterButton("Q", 17);
        charButtons[17] = new CharacterButton("R", 18);
        charButtons[18] = new CharacterButton("S", 19);
        charButtons[19] = new CharacterButton("T", 20);
        charButtons[20] = new CharacterButton("U", 21);
        charButtons[21] = new CharacterButton("V", 22);
        charButtons[22] = new CharacterButton("W", 23);
        charButtons[23] = new CharacterButton("X", 24);
        charButtons[24] = new CharacterButton("Y", 25);
        charButtons[25] = new CharacterButton("Z", 26);
        charButtons[26] = new CharacterButton("\u2423", 27);
        charButtons[27] = new CharacterButton("\u21d2",28);


        numButtons[0] = new NumberButton(1, "1");
        numButtons[1] = new NumberButton(2, "2");
        numButtons[2] = new NumberButton(3, "3");
        numButtons[3] = new NumberButton(4, "4");
        numButtons[4] = new NumberButton(5, "5");
        numButtons[5] = new NumberButton(6, "6");
        numButtons[6] = new NumberButton(7, "7");
        numButtons[7] = new NumberButton(8, "8");
        numButtons[8] = new NumberButton(9, "9");
        numButtons[9] = new NumberButton(0, "0");
        numButtons[10] = new NumberButton(-1, "DEL");
        numButtons[11] = new NumberButton(-1, "\u21d0");

        panel.setPreferredSize(new Dimension(180,210));
        panel.setLayout(null);
        panel.setOpaque(false);
        txtf.setPreferredSize(new Dimension(100, 30));
        screen.add(txtf);
        screen.add(panel);
        txtf.setFont(new Font("Ariel", Font.BOLD, 18));

        int charCount=0;
        int numbCount=0;
        int[] a = {0,45,90,135}; //X coordinates of the buttons
        int[] b = {0,30,60,90,120,150,180}; // Y coordinantes of the buttons
        for (int y: b){
            for( int x:a){
                if(charCount==0) {
                    charButtons[0].setBounds(x, y, 45, 30);
                    panel.add(charButtons[0]);
                    charButtons[0].setBackground(Color.ORANGE);
                    charCount++;
                }
                else{
                    charButtons[charCount].setBounds(x, y, 45, 30);
                    panel.add(charButtons[charCount]);
                    charCount++;
                }
            }
        }
        int[] c = {0,60,120};
        int[] d = {0,42,84};
        for(int y: d){
            for(int x: c){
                if(numbCount==0){
                    numButtons[numbCount].setBounds(x,y,60,42);
                    panel.add(numButtons[numbCount]);
                    numButtons[numbCount].setBackground(Color.ORANGE);
                    numButtons[numbCount].setVisible(false);//Setting the numerical keys to be invisible
                    numbCount++;
                }
                else{
                    numButtons[numbCount].setBounds(x,y,60,42);
                    panel.add(numButtons[numbCount]);
                    numButtons[numbCount].setVisible(false);
                    numbCount++;
                }
            }
        }
        numButtons[9].setBounds(0,126,60,42); panel.add(numButtons[9]);numButtons[9].setVisible(false);
        numButtons[10].setBounds(60,126,120,84); panel.add(numButtons[10]);numButtons[10].setVisible(false);
        numButtons[11].setBounds(0,168,60,42); panel.add(numButtons[11]);numButtons[11].setVisible(false);

    }

    /*
    Overriden method that is used to remove all the java.swing elements on the virtual screen when called
     */
    @Override
    public void stop() {
        for (CharacterButton x : charButtons) {
            screen.remove(x);
        }
        for (NumberButton x : numButtons) {
            screen.remove(x);
        }
        screen.remove(txtf);

        screen.remove(panel);
        screen.removeAll();

    }
        @Override
        public void actionPerformed (ActionEvent e){
        }

        @Override
        public void render () {
            for (CharacterButton x : charButtons) {
                if (x != null) x.repaint();
            }
            for (NumberButton x : numButtons) {
                if (x != null) x.repaint();
            }
            txtf.repaint();
        }
        /*
        Overridden method that will perform some sort of action when the emulated buttons are clicked
         */
        @Override
        public void navigationButton (NavigationAction e){
            /*
            The select button is used to press the buttons on the keyboard, either to add or remove characters to the
            text field, or to switch it from numerical/alphabetical mode
             */
            if (e == NavigationAction.SELECT) {
                if (currentMode > 0) {
                    if (currentButton == 27) {
                        charButtons[currentButton].switchToNums();
                    } else {
                        charButtons[currentButton].textInput();
                    }
                } else {
                    if (currentButton == 10) {
                        numButtons[currentButton].backSpace();
                    } else if (currentButton == 11) {
                        numButtons[currentButton].switchToChars();
                    } else {
                        numButtons[currentButton].numInput();
                    }
                }
            /*
            This plus button is set to cycle forward through the keyboard, highlighting the current key it is on. If
            it reaches it the last key in the keyboard, the it will cycle back to the first key
             */
            } else if (e == NavigationAction.PLUS) {
                if (currentMode > 0) {
                    if (this.currentButton == 27) {
                        charButtons[currentButton].setBackground(Color.WHITE);
                        this.currentButton = 0;
                        charButtons[currentButton].setBackground(Color.ORANGE);
                    } else {
                        charButtons[currentButton].setBackground(Color.WHITE);
                        this.currentButton++;
                        charButtons[currentButton].setBackground(Color.ORANGE);
                    }
                } else {
                    if (currentButton == 11) {
                        numButtons[currentButton].setBackground(Color.WHITE);
                        currentButton = 0;
                        numButtons[currentButton].setBackground(Color.ORANGE);
                    } else {
                        numButtons[currentButton].setBackground(Color.WHITE);
                        currentButton++;
                        numButtons[currentButton].setBackground(Color.ORANGE);
                    }
                }
            /*
            This plus button is set to cycle backwards through the keyboard, highlighting the current key it is on. If
            it reaches it the first key in the keyboard, the it will cycle back to the last key
             */
            } else if (e == NavigationAction.MINUS) {
                if (currentMode > 0) {
                    if (this.currentButton == 0) {
                        charButtons[currentButton].setBackground(Color.WHITE);
                        this.currentButton = 27;
                        charButtons[currentButton].setBackground(Color.ORANGE);
                    } else {
                        charButtons[currentButton].setBackground(Color.WHITE);
                        this.currentButton--;
                        charButtons[currentButton].setBackground(Color.ORANGE);
                    }
                } else {
                    if (currentButton == 0) {
                        numButtons[currentButton].setBackground(Color.WHITE);
                        currentButton = 11;
                        numButtons[currentButton].setBackground(Color.ORANGE);
                    } else {
                        numButtons[currentButton].setBackground(Color.WHITE);
                        currentButton--;
                        numButtons[currentButton].setBackground(Color.ORANGE);
                    }
                }
            /*
            The power button is set to turn off the emulator and provide a hard reset to the device, not saving the
            state of previous journeys
             */
            } else if (e == NavigationAction.POWER) {
                stop();
            /*
            The menu button acts as the starting point for the journey, taking what is in the text field and creating a
            route and provide directions based on the current location and the destination.
             */
            } else if (e == NavigationAction.MENU) {
                model.setDestination(getDirections());
            }

        }
        /*
        The class which create JTextPane objects which are used to create the keys for the numerical keyboard mode
         */
        public class NumberButton extends JTextPane {
            String num;
            Font font = new Font("Verdana", Font.BOLD, 12);

            NumberButton(int i, String special) {
                StyledDocument doc = this.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), center, false);
                this.setFont(font);
                this.num = special;
                this.setText(special);
                this.setBackground(Color.WHITE);
                this.setFont(new Font("Verdana", Font.BOLD, 13));
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                if (special.equals("\u21d0")){
                    this.setText(String.valueOf('\u21d0'));
                    this.setFont(new Font("Verdana", Font.BOLD, 20));
                }
            }
            /*
            Switch numerical keyboard to alphabetical keyboard
             */
            void switchToChars() {
                currentMode = -(currentMode);
                for (CharacterButton x : charButtons) {
                    x.setVisible(true);
                }
                for (NumberButton x : numButtons) {
                    x.setVisible(false);
                }
                numButtons[currentButton].setBackground(Color.WHITE);
                currentButton = 0;
                numButtons[currentButton].setBackground(Color.ORANGE);
            }

            String getChar() {
                return this.num;
            }

            void numInput() {
                txtf.setText(txtf.getText() + this.getChar());
            }

            void backSpace() {
                String currentField = txtf.getText();
                currentField = currentField.substring(0, currentField.length() - 1);
                txtf.setText(currentField);
            }
        }
        public class CharacterButton extends JTextPane {
            int alphaNum;
            public String s;

            CharacterButton(String s, int i) {
                StyledDocument doc = this.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), center, false);
                this.alphaNum = i;
                this.setOpaque(true);
                this.s = s;
                this.setBackground(Color.WHITE);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                switch (s) {
                    case "\u2423":
                        this.setText(String.valueOf('\u2423'));
                        this.setFont(new Font("Verdana", Font.BOLD, 20));
                        break;
                    case "\u21d2":
                        this.setText(String.valueOf('\u21d2'));
                        this.setFont(new Font("Verdana", Font.BOLD,     20));
                        break;
                    default:
                        this.setText(s);
                        this.setFont(new Font("Verdana", Font.BOLD, 13));
                        break;
                }
            }


            void textInput() {
                String text =this.getChar();
                    if(text.equals(String.valueOf('\u2423'))){
                        text=" ";
                    }
                txtf.setText(txtf.getText() + text);
            }

            String getChar() {
                return this.s;
            }
            /*
            Switch the alphabetical keyboard to the numerical one
             */
            void switchToNums() {
                currentMode = -(currentMode);
                for (CharacterButton x : charButtons) {
                    x.setVisible(false);
                }
                for (NumberButton x : numButtons) {
                    x.setVisible(true);
                }
                charButtons[currentButton].setBackground(Color.WHITE);
                currentButton = 0;
                charButtons[currentButton].setBackground(Color.ORANGE);
            }

        }
        /*
        Class to make the rounded text field to input the keyboard text into
         */
        public class RoundJTextField extends JTextField {
            private Shape shape;

            RoundJTextField(int size) {
                super(size);
                setOpaque(false);
                this.setEditable(false);
            }

            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(5, 0, 175, 30, 20, 20);
                super.paintComponent(g);
            }

            protected void paintBorder(Graphics g) {
                g.setColor(getForeground());
                g.drawRoundRect(5, 0, 175, 30, 20, 20);
            }

            public boolean contains(int x, int y) {
                if (shape == null || !shape.getBounds().equals(getBounds())) {
                    shape = new RoundRectangle2D.Float(5, 0, 175, 30, 20, 20);
                }
                return shape.contains(x, y);
            }
        }
    public WhereTo(ModelManager object) {
        this.model = object;
        }
        /*
        Method to return the directions in the textfield
         */
        public String getDirections () {
            return txtf.getText();
        }

}