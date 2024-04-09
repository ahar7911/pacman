import java.awt.*;
import javax.swing.ImageIcon;

/**
The Inky class extends Ghost to develop its own starting point, AI of tracking
PacMan, corner, and drawing methods.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Inky extends Ghost
{
   /** ImageIcon representation of Inky when moving left */
   private ImageIcon inky1;
   /** ImageIcon representation of Inky when moving up */
   private ImageIcon inky2;
   /** ImageIcon representation of Inky when moving right */
   private ImageIcon inky3;
   /** ImageIcon representation of Inky when moving down */
   private ImageIcon inky4;
   
   /**
   Initializes an Inky object with a pre-determined set of coordinates
   (starting point) at (288, 348), diameter of 24, a default speed,
   corner coordinates of (636, 708), and instantiates ImageIcons for each
   direction.
   */
   public Inky()
   {
      super(288, 348, 24, 636, 708);
      inky1 = new ImageIcon("imageicons\\inky1.png");
      inky2 = new ImageIcon("imageicons\\inky2.png");
      inky3 = new ImageIcon("imageicons\\inky3.png");
      inky4 = new ImageIcon("imageicons\\inky4.png");
      setDirection(4);
   }
   
   /**
   Resets Inky's coordinates at its starting location and sets its
   direction back to default.
   */
   public void reset()
   {
      setX(288);
      setY(348);
      setDirection(4);
   }
   
   /**
   Draws Inky's blue ghost form at its coordinates using a specific
   ImageIcon correlating to its current direction. 
   @param g	Graphics object to draw ImageIcon
   */
   public void drawMe(Graphics g)
   {
      switch(getDirection()){
         case 1:
            g.drawImage(inky1.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 2:
            g.drawImage(inky2.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 3:
            g.drawImage(inky3.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 4:
            g.drawImage(inky4.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
      }
   
   }
   
   /**
   Moves Inky to the target of the reflection of a vertical line between Blinky
   and two spaces ahead of PacMan using two spaces ahead of PacMan's location
   as a reflection point.
   @param p	PacMan object that is being chased
   @param b	array of Bumper objects that prohibit PacMan's movement.
   @param blinky Blinky object whose location is being reflected
   */
   public void moveInky(PacMan p, Blinky blinky, Bumper[] b)
   {
      int mirrorX = 0;
      int mirrorY = 0;
      switch(p.getDirection()){
         case 1:
            mirrorX = p.getX() - 24;
            mirrorY = p.getY();
            break;
         case 2:
            mirrorX = p.getX();
            mirrorY = p.getY() - 24;
            break;
         case 3:
            mirrorX = p.getX() + 24;
            mirrorY = p.getY();
            break;
         case 4:
            mirrorX = p.getX();
            mirrorY = p.getY() + 24;
            break;
      }
      int xTarget = mirrorX + (mirrorX - blinky.getX());
      int yTarget = mirrorY + (mirrorY - blinky.getY());   
        
      super.moveToTarget(b, xTarget, yTarget);
   }
}