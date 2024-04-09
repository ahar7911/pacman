import java.awt.*;
import javax.swing.ImageIcon;

/**
The Blinky class extends Ghost to develop its own starting point, AI of tracking
PacMan, corner, and drawing methods.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Blinky extends Ghost
{
   /** ImageIcon representation of Blinky when moving left */
   private ImageIcon blinky1;
   /** ImageIcon representation of Blinky when moving up */
   private ImageIcon blinky2;
   /** ImageIcon representation of Blinky when moving down */
   private ImageIcon blinky3;
   /** ImageIcon representation of Blinky when moving right */
   private ImageIcon blinky4;
   
   /**
   Initializes a Blinky object with a pre-determined set of coordinates
   (starting point) at (336, 276), diameter of 24, a default speed,
   corner coordinates of (636, 36), and instantiates ImageIcons for each
   direction.
   */
   public Blinky()
   {
      super(336, 276, 24, 636, 36);
      blinky1 = new ImageIcon("imageicons\\blinky1.png");
      blinky2 = new ImageIcon("imageicons\\blinky2.png");
      blinky3 = new ImageIcon("imageicons\\blinky3.png");
      blinky4 = new ImageIcon("imageicons\\blinky4.png");
      setDirection(1);
   }
   
   /**
   Resets Blinky's coordinates at its starting location and sets its direction
   back to default.
   */
   public void reset()
   {
      setX(336);
      setY(276);
      setDirection(1);
   }
   
   /**
   Draws Blinky's red ghost form at its coordinates using a specific ImageIcon
   correlating to its current direction. 
   @param g	Graphics object to draw ImageIcon
   */
   public void drawMe(Graphics g)
   {
      switch(getDirection()){
         case 1:
            g.drawImage(blinky1.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 2:
            g.drawImage(blinky2.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 3:
            g.drawImage(blinky3.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 4:
            g.drawImage(blinky4.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
      }
   }
   
   /**
   Moves Blinky towards a target of PacMan's current location.
   @param p	PacMan object that is being chased
   @param b	array of Bumper objects that prohibit PacMan's movement.
   */
   public void moveBlinky(PacMan p, Bumper[] b)
   {
      super.moveToTarget(b, p.getX(), p.getY());
   }
}