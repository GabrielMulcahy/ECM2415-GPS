package view;

import model.Language;
import model.ModelManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.LineBorder;

/**
 * @author Joshua Chalcraft
 * - Class provides the interface for speech functionality
 * - Renders the menus for the languages
 * - Once a language is selected, it sets that language for the ModelManager
 */
public class SpeechMode extends JFrame implements MenuState
{
    //Attributes
    private JFrame frame;
    private JPanel screen;
    private ActionListener listener;
    private Graphics2D renderer;
    private LanguageMenuBar menuBar = new LanguageMenuBar();
    private int menuIndex = -1;
    private int selectedIndex = 0;
    ModelManager mm;

    //Constructor
    public SpeechMode(ModelManager m)
    {
        this.mm = m;
    }

    //Methods
    @Override
    public void setRenderer(Graphics2D renderer) {this.renderer = renderer;}

    @Override
    public void setFrame (JFrame frame) {this.frame = frame;}

    @Override
    public void setPanel(JPanel screen) {this.screen = screen;}

    @Override
    public void setListener(ActionListener listener){this.listener = listener;}

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void start()
    {
        //Set up the menus.
        menuBar.add(new LanguageMenu(Language.OFF, "Off"));
        menuBar.add(new LanguageMenu(Language.ENGLISH, "English"));
        menuBar.add(new LanguageMenu(Language.FRENCH, "French"));
        menuBar.add(new LanguageMenu(Language.GERMAN, "German"));
        menuBar.add(new LanguageMenu(Language.ITALIAN, "Italian"));
        menuBar.add(new LanguageMenu(Language.SPANISH, "Spanish"));
        screen.add(menuBar);
        menuBar.getMenu(0).setBackground(Color.GREEN); //Indicates it's off by default.
    }

    @Override
    public void stop()
    {
        screen.removeAll();
    }

    @Override
    public void render()
    {
        for (int i = 0; i < menuBar.getMenuCount(); i++) menuBar.getMenu(i).repaint();

    }

    @Override
    public void navigationButton(NavigationAction e)
    {
        //Go up.
        if ((e == NavigationAction.PLUS) && (menuIndex > 0)) //The checks on menuIndex prevents the menuIndex being out of bounds.
        {
            menuIndex--;
            menuBar.getMenu(menuIndex+1).setBackground(Color.WHITE);
            menuBar.getMenu(menuIndex).setBackground(Color.ORANGE);
            menuBar.getMenu(selectedIndex).setBackground(Color.GREEN);
        }

        //Go down.
        if ((e == NavigationAction.MINUS) && (menuIndex < (menuBar.getMenuCount()-1)))
        {
            menuIndex++;
            if (menuIndex == 0)
            {
                menuBar.getMenu(menuIndex).setBackground(Color.ORANGE);
            }
            else
            {
                menuBar.getMenu(menuIndex - 1).setBackground(Color.WHITE);
                menuBar.getMenu(menuIndex).setBackground(Color.ORANGE);
            }
            menuBar.getMenu(selectedIndex).setBackground(Color.GREEN);
        }

        //Select the chosen language.
        if ((e == NavigationAction.SELECT) && (menuIndex > 0))
        {
            menuBar.getMenu(selectedIndex).setBackground(Color.WHITE);
            menuBar.getMenu(menuIndex).setBackground(Color.GREEN);
            selectedIndex = menuIndex;
            LanguageMenu menu = (LanguageMenu) menuBar.getMenu(menuIndex);
            mm.setLanguage(menu.type);
        }
    }

    /*
     * Custom JMenuBar that is vertical.
     */
    public class LanguageMenuBar extends JMenuBar
    {
        private final LayoutManager grid = new GridLayout(0,1);
        LanguageMenuBar()
        {
            setLayout(grid);
        }
    }


    /*
     * LanguageMenu contains extra attributes that will set the SpeechGenerator's attributes
     * once that menu has been selected through the SELECT navigation action.
     */
    public class LanguageMenu extends JMenu
    {
        Language type;
        LanguageMenu(Language type, String languageName)
        {
            this.type = type;
            this.setText(languageName);
            this.setFont(new Font("Ariel", Font.BOLD, 24));
            this.setBorder(new LineBorder(Color.BLACK, 3));
            this.setPreferredSize((new Dimension(191, 40)));
            this.setOpaque(true);
            this.setBackground(Color.WHITE);
            this.addActionListener(listener);
        }
    }
}

