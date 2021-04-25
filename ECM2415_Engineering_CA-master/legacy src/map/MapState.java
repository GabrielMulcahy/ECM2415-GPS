package map;

import main.NavigationAction;
import main.StateManager;
import menu.MenuState;
import satellite.MockLocation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * MapState.
 *
 * Gabriel Mulcahy
 */
public class MapState extends JPanel implements Observer, MenuState {
  private BufferedImage image;
  private BufferedImage dot;
  private BufferedImage finding;
  private int rotation;
  private JFrame frame;
  private JPanel screen;
  private ActionListener listener;
  private Graphics2D renderer;
  private Maps map;
  private String[] data;
  MockLocation loc;


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
    map = new Maps();

    try {
      dot = ImageIO.read(new File("res/red.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    loc = new MockLocation();
    loc.openPort("COM4");
    Thread t = new Thread(loc);
    t.start();
    data = loc.getData();

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
    if(image==null) return;
    double radians = Math.toRadians( (double) rotation );
    renderer.rotate( radians, image.getWidth() / 2, image.getHeight() / 2 );
    renderer.drawImage( image, StateManager.SCREEN_X+8,StateManager.SCREEN_Y+32,screen.getWidth()-4,screen.getHeight()-4,screen);
    renderer.drawImage( dot, StateManager.SCREEN_X+96, StateManager.SCREEN_Y+142, 10, 10, screen );
  }

  @Override
  public void navigationButton(NavigationAction e){
    data = loc.getData();
    if(e== NavigationAction.PLUS){
      map.zoomIn();
      map.make();
        try {
            image = ImageIO.read(new File("src/map/output.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        render();
    } else if(e== NavigationAction.MINUS) {
      map.zoomOut();
      map.make();
      try {
          image = ImageIO.read(new File("src/map/output.png"));
      } catch (IOException e1) {
          e1.printStackTrace();
      }
      render();
    } else if(e== NavigationAction.SELECT) {
      map.setLat(data[0]);
      map.setLong(data[1]);
      map.make();
        try {
          image = ImageIO.read( new File( "src/map/output.png" ) );
        } catch (IOException e1) {
          e1.printStackTrace();
      }
      render();
    }
  }


  public MapState() {

  }

  public void update( Observable obs, Object obj ) {
    rotation = (int) obj;
    repaint();
  }
  
  
  public void north(){
    rotation = 0;
  }
  
  public void west(){
    rotation = 90;
  }
  
  public void south(){
    rotation = 180;
  }
  
  public void east(){
    rotation = 270;
  }

  public Dimension getPreferredSize() {
    return new Dimension( image.getWidth(), image.getHeight() );
  }
}
