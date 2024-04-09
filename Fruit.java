import java.awt.*;
import javax.swing.ImageIcon;

/**
The Fruit class stores coordinates, a diameter, and to know when it has
collided with PacMan; it in turn also stores information about what type
of Fruit it is.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Fruit 
{
   /**
   type of Fruit; each number corresponds to a specific type. 1 represents
   a cherry, 2 represents a strawberry, 3 represents an orange, 4 represents
   an apple, and 5 represents a melon
   */
   private int type;
   /** whether the Fruit has been eaten or not, and thus whether it should
   be displayed */
   private boolean eaten = false;
   /** ImageIcon representation of the cherry Fruit */
   private ImageIcon cherImg = new ImageIcon("imageicons\\cherry.png");
   /** ImageIcon representation of the strawberry Fruit */
   private ImageIcon strawImg = new ImageIcon("imageicons\\strawberry.png");
   /** ImageIcon representation of the orange Fruit */
   private ImageIcon orngImg = new ImageIcon("imageicons\\orange.png");
   /** ImageIcon representation of the apple Fruit */
   private ImageIcon appleImg = new ImageIcon("imageicons\\apple.png");
   /** ImageIcon representation of the melon Fruit */
   private ImageIcon melonImg = new ImageIcon("imageicons\\melon.png");
   
   /**
   Initializes a fruit with the designated type.
   @param t type of fruit
   */
   public Fruit(int t)
   {
      type = t;
   }
   
   /**
   Returns the type of the fruit.
   @return Fruit object's type.
   */
   public int getType()
   {
      return type;
   }
   
   /**
   Sets the type of the fruit. 
   @param t type
   */
   public void setType(int t)
   {
      type = t;
   }
   
   /** 
   Returns whether the fruit is eaten or not.
   @return eaten
   */
   public boolean isEaten()
   {
      return eaten;
   }
   
   /**
   Sets if the fruit has been eaten or not. 
   @param b whether fruit has been eaten or not
   */
   public void setEaten(boolean b)
   {
      eaten = b;
   }
   
   /**
   Draws ImageIcon of pre-determined type of fruit based on the type at
   its coordinates and of a size close to its diameter, and tests for
   collisions between PacMan and the fruit.
   @param g Graphics object used to draw the ImageIcon
   @param pac  PacMan object used to test collisons against
   @param s Scoreboard object used to add to score if collision occurs
   @return  whether or not the PacMan has collided with the fruit
   */
   public boolean drawMe(Graphics g, PacMan pac, Scoreboard s)
   {
      switch(type){
         case 1:
            g.drawImage(cherImg.getImage(), 312, 395, 52, 42, null);
            return collide(pac, s, 52, 312, 395);
         case 2:
            g.drawImage(strawImg.getImage(), 322, 405, 27, 29, null);
            return collide(pac, s, 27, 322, 405);
         case 3:
            g.drawImage(orngImg.getImage(), 319, 402, 36, 34, null);
            return collide(pac, s, 36, 319, 402);
         case 4:
            g.drawImage(appleImg.getImage(), 316, 402, 37, 35, null);
            return collide(pac, s, 37, 316, 402);
         case 5:
            g.drawImage(melonImg.getImage(), 316, 398, 37, 45, null);
            return collide(pac, s, 37, 316, 398); 
      }
      return false;
   }
   
   /**
   Checks if distance between PacMan and Fruit is less than radius of PacMan
   and half of width. Scores fruit accordingly if condition is satisfied.
   @param p   PacMan object used to test collisions against
   @param s   Scoreboard object used to update score based on fruit type if
   collision occurs
   @param width  width of Fruit object
   @param x   top left x-coordinate of the current Fruit's ImageIcon
   @param y   top left y-coordinate of the current Fruit's ImageIcon
   @return whether or not PacMan has collided with the Fruit object
   */
   public boolean collide(PacMan p, Scoreboard s, int width, int x, int y)
   {
      if(p.getDirection() == 1 || p.getDirection() == 3)        
         if(distance(x + (width / 2.0), y + (width / 2.0), p.getX(), p.getY()) < p.getRadius() + (width / 2.0)){
            s.scoreFruit(type);
            eaten = true;
            return true;
         }
      return false;
   }
   
   /**
   Returns the distance between two points on a coordinate plane.
   @param x1	x-coordinate of first point
   @param y1	y-coordinate of first point
   @param x2	x-coordinate of second point
   @param y2	y-coordinate of second point
   @return 	distance between the two points
   */
   private double distance(double x1, double y1, double x2, double y2)
   {
      return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
   }
}