import java.awt.*;
import javax.swing.ImageIcon;

/**
The Clyde class extends Ghost to develop its own starting point, AI of trackin
 PacMan, corner, and drawing methods.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Clyde extends Ghost
{
   /** ImageIcon representation of Clyde when moving left */
   private ImageIcon clyde1;
   /** ImageIcon representation of Clyde when moving up */
   private ImageIcon clyde2;
   /** ImageIcon representation of Clyde when moving right */
   private ImageIcon clyde3;
   /** ImageIcon representation of Clyde when moving down */
   private ImageIcon clyde4;
   
   /**
   Initializes a Clyde object with a pre-determined set of coordinates
   (starting point) at (384, 348), diameter of 24, a default speed,
   corner coordinates of (36, 708), and instantiates ImageIcons for each
   direction.
   */
   public Clyde()
   {
      super(384, 348, 24, 36, 708);
      clyde1 = new ImageIcon("imageicons\\clyde1.png");
      clyde2 = new ImageIcon("imageicons\\clyde2.png");
      clyde3 = new ImageIcon("imageicons\\clyde3.png");
      clyde4 = new ImageIcon("imageicons\\clyde4.png");
      setDirection(4);
   }
   
   /**
   Resets Clyde's coordinates at its starting location
   and sets its direction back to default.
   */
   public void reset()
   {
      setX(384);
      setY(348);
      setDirection(4);
   }
   
   /**
   Draws Clyde's orange ghost form at its coordinates using a =
   specific ImageIcon correlating to its current direction. 
   @param g	Graphics object to draw ImageIcon
   */
   public void drawMe(Graphics g)
   {
      switch(getDirection()){
         case 1:
            g.drawImage(clyde1.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 2:
            g.drawImage(clyde2.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 3:
            g.drawImage(clyde3.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 4:
            g.drawImage(clyde4.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
      }

   }
   
   /**
   Moves Clyde one frame toward PacMan's current location, but returns
   to own corner when more than 8 spaces closer to him.
   @param p	PacMan object that is being chased
   @param b	array of Bumper objects that prohibit PacMan’s movement.
   */
   public void moveClyde(PacMan p, Bumper[] b)
   {
      if(distance(getX(), getY(), p.getX(), p.getY()) > 192)
         moveToTarget(b, p.getX(), p.getY());
      else
         moveScatter(b);
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