import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
*The LifePanel class displays either a fruit or a PacMan to signify a life left
or a fruit collected within the Scoreboard.
*@author Aidan Harbison
*@since 5-29-2018
*/
public class LifePanel extends JPanel
{
   /** BufferedImage object used for the image representation of the panel. */
   private BufferedImage myImage;
   /** Graphics object which adjusts what is drawn on the panel. */
   private Graphics g;
   /** Represents whether the LifePanel displays a life or not. */
   private boolean isOn;
   /** Represents whether the LifePanel displays a fruit or not. */
   private boolean hasFruit;
   /** width and height of panel in pixels */
   private static final int FRAME = 36;
   /** type of fruit being displayed */
   private int fruitType;
   
   /**
   Initializes a LifePanel object, specifying whether the PacMan life should be
   displayed or not, and starts a timer that continuously draws a PacMan life or 
   lack thereof.
   @param b whether a PacMan life is displayed on panel
   */
   public LifePanel(boolean b)
   {
      myImage =  new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
      g = myImage.getGraphics();
      
      isOn = b;
      setBackground(Color.BLACK);
      setPreferredSize(new Dimension(36, 36));
      
      Timer t = new Timer(0, new Listener());
      t.start();      
   }
   
   /**
   Displays board at current state when the pre-defined repaint method is 
   called.
   @param g	Graphics object to draw board with
   */
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   /**
   Returns whether the board is currently displaying a PacMan life or not.
   @return isOn; whether the board displays PacMan or not
   */
   public boolean isOn()
   {
      return isOn;
   }
   
   /**
   Returns whether the board is currently displaying a Fruit or not.
   @return  hasFruit; whether the level has a fruit or not.
   */
   public boolean hasFruit()
   {
      return hasFruit;
   }
   
   /**
   Sets whether the panel is displaying a PacMan to signify one life or not.
   Automatically set as not having fruit.
   @param b	determines whether PacMan is being displayed or not
   */
   public void setLife(boolean b)
   {
      isOn = b;
      hasFruit = false;
   }
   
   /**
   Sets what the Fruit through instantiation of new Fruit object that
   the panel is displaying to signify the collection of one.
   Automatically set as not displaying a life.
   @param num type of Fruit that panel is displaying
   */
   public void setFruit(int num)
   {
      hasFruit = true;
      isOn = false;
      fruitType = num;
   }
   
   /**
   The Listener class implements ActionListener to allow a continuous
   drawing of the panel to enable up-to-date feedback on interactions
   in PacManPanel through the appearance of loss of lives or the appearance
   of new fruits to signify new levels.
   */
   private class Listener implements ActionListener
   {
      /**
      If the board is currently displaying PacMan, draws a PacMan life;
      if the board is currently displaying a Fruit, draws the Fruit;
      otherwise draws a black panel.
      @param e event that determines whether method is called; required 
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e)
      {
         if(isOn){
            g.setColor(Color.YELLOW);
            g.fillOval(4, 4, 32, 32);
            g.setColor(Color.BLACK);
            int[] xPoints = new int[] {16, 36, 36};
            int[] yPoints = new int[] {20, 8, 32};
            g.fillPolygon(xPoints, yPoints, 3);
         }
         
         else if(hasFruit){
            switch(fruitType){
               case 1:
                  g.drawImage( new ImageIcon("imageicons\\cherry.png").getImage(), -4, -7, 52, 42, null);
                  break;
               case 2:
                  g.drawImage( new ImageIcon("imageicons\\strawberry.png").getImage(), 4, 3, 27, 29, null);
                  break;
               case 3:
                  g.drawImage( new ImageIcon("imageicons\\orange.png").getImage(), 1, 0, 36, 34, null);
                  break;
               case 4:
                  g.drawImage( new ImageIcon("imageicons\\apple.png").getImage(), -2, 0, 37, 35, null);
                  break;
               case 5:
                  g.drawImage( new ImageIcon("imageicons\\melon.png").getImage(), -2, -4, 37, 45, null);
                  break;
            }
         }
         else{
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, FRAME, FRAME);
         }
         repaint();
         updateUI();
      }
   }
}
