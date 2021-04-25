package whereTo;

import main.NavigationAction;
import menu.MenuState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class WhereTo implements MenuState {
    Graphics2D renderer;
    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;
    final JTextField txtf = new RoundJTextField(30);
    int currentButton;
    int currentMode;

    CharacterButton[] charButtons = new CharacterButton[28];
    NumberButton[] numButtons = new NumberButton[12];

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
        this.listener = listener;
    }

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


        txtf.setPreferredSize(new Dimension(100, 30));
        screen.add(txtf);
        txtf.setFont(new Font("Ariel", Font.BOLD, 18));

        for (CharacterButton x : charButtons) {
            if (x.getChar() == "A") {
                x.setPreferredSize(new Dimension(46, 30));
                screen.add(x);
                x.setBackground(Color.ORANGE);
            } else {
                x.setBackground(Color.WHITE);
                x.setPreferredSize(new Dimension(46, 30));
                screen.add(x);
            }
        }
        for (NumberButton x : numButtons) {
            if (x.getChar() == "1") {
                x.setPreferredSize(new Dimension(60, 40));

                x.setVisible(false);
                x.setBackground(Color.ORANGE);
                screen.add(x);
            } else if (x.getChar().equals("DEL")) {
                x.setPreferredSize(new Dimension(120, 80));
                x.setVisible(false);
                screen.add(x);
            } else {
                x.setPreferredSize(new Dimension(60, 40));
                x.setVisible(false);
                screen.add(x);
            }
        }
    }


    @Override
    public void stop() {
        for (CharacterButton x : charButtons) {
            screen.remove(x);
        }
        for (NumberButton x : numButtons) {
            screen.remove(x);
        }
        screen.remove(txtf);
        System.out.println("STOP");
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
        @Override
        public void navigationButton (NavigationAction e){
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
            } else if (e == NavigationAction.POWER) {
                stop();
            } else if (e == NavigationAction.MENU) ;
            String directions = getDirections();
        }

        public class NumberButton extends JButton {
            public String num;
            Font font = new Font("Verdana", Font.BOLD, 12);

            NumberButton(int i, String special) {
                this.setVerticalAlignment(JLabel.TOP);
                this.setFont(font);
                this.num = special;
                this.setText(special);
                this.setBackground(Color.WHITE);
                this.setFont(new Font("Verdana", Font.BOLD, 13));
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                if (special == "\u21d0"){
                    this.setText(String.valueOf('\u21d0'));
                    this.setFont(new Font("Verdana", Font.BOLD, 20));
                }
            }

            public void switchToChars() {
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

            public String getChar() {
                return this.num;
            }

            public void numInput() {
                txtf.setText(txtf.getText() + this.getChar());
            }

            public void backSpace() {
                String currentField = txtf.getText();
                currentField = currentField.substring(0, currentField.length() - 1);
                txtf.setText(currentField);
            }
        }
        public class CharacterButton extends JTextPane {
            public int alphaNum;
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
                if (s == "\u2423" ) {
                    this.setText(String.valueOf('\u2423'));
                    this.setFont(new Font("Verdana", Font.BOLD, 20));
                }
                else if (s== "\u21d2"){
                    this.setText(String.valueOf('\u21d2'));
                    this.setFont(new Font("Verdana", Font.BOLD, 20));
                }
                else {
                    this.setText(s);
                    this.setFont(new Font("Verdana", Font.BOLD, 13));
                }
            }


            public void textInput() {
                txtf.setText(txtf.getText() + this.getChar());
            }

            public String getChar() {
                System.out.println(this.s);
                return this.s;
            }

            public void switchToNums() {
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
        public class RoundJTextField extends JTextField {
            private Shape shape;

            public RoundJTextField(int size) {
                super(size);
                setOpaque(false); // As suggested by @AVD in comment.
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
    public WhereTo() {
        }
        public String getDirections () {
            String destination = txtf.getText();
            return destination;
        }
        public int increment () {
            currentButton++;
            return currentButton;
        }
}