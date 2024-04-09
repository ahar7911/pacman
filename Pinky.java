import java.awt.*;
import javax.swing.ImageIcon;

/**
*The Pinky class extends Ghost to develop its own starting point, AI of
tracking PacMan, corner, and drawing methods. Pinky also stores
information about its current target's coordinates.
*@author Ajay Prabhakar
*@since 5-29-2018
*/
public class Pinky extends Ghost
{
   /** x-coordinate of Pinky's current target to be chased */
   private int xTarget;
   /** y-coordinate of Pinky's current target to be chased */
   private int yTarget;
   /** ImageIcon representation of Pinky when moving left */
   private ImageIcon pinky1;
   /** ImageIcon representation of Pinky when moving up */
   private ImageIcon pinky2;
   /** ImageIcon representation of Pinky when moving right */
   private ImageIcon pinky3;
   /** ImageIcon representation of Pinky when moving down */
   private ImageIcon pinky4;
   
   /**
   Initializes a Pinky object with a pre-determined set of coordinates
   (starting point) at (336, 348), diameter of 24, a default speed,
   corner coordinates of (36, 36), and instantiates ImageIcons for each
   direction.
   */
   public Pinky()
   {
      super(336, 348, 24, 36, 36);
      pinky1 = new ImageIcon("imageicons\\pinky1.png");
      pinky2 = new ImageIcon("imageicons\\pinky2.png");
      pinky3 = new ImageIcon("imageicons\\pinky3.png");
      pinky4 = new ImageIcon("imageicons\\pinky4.png");
      setDirection(2);
   }
   
   /**
   Resets Pinky's coordinates at its starting location and sets its
   direction back to default.
   */
   public void reset()
   {
      setX(336);
      setY(348);
      setDirection(2);
   }
   
   /**
   Draws Pinky's pink ghost form at its coordinates using a specific
   ImageIcon correlating to its current direction. 
   @param g	Graphics object to draw ImageIcon
   */
   public void drawMe(Graphics g)
   {
      switch(getDirection()){
         case 1:
            g.drawImage(pinky1.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 2:
            g.drawImage(pinky2.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 3:
            g.drawImage(pinky3.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 4:
            g.drawImage(pinky4.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
      }
   
   }
   
   /**
   Moves Pinky towards a target of four spaces ahead of PacMan's
   current location in its direction.
   @param p	PacMan object that is being chased
   @param b	Array of Bumper objects that prohibit PacMan's movement
   */
   public void movePinky(PacMan p, Bumper[] b)
   {
      switch(p.getDirection()){
         case 1:
            xTarget = p.getX() - 96;
            if(xTarget < -24)
               xTarget = (xTarget + 24) + 692;
            yTarget = p.getY();
            break;
         case 2:
            xTarget = p.getX();
            yTarget = p.getY() - 96;
            break;
         case 3:
            xTarget = p.getX() + 96;
            if(xTarget > 692)
               xTarget = -24 + (xTarget - 692);
            yTarget = p.getY();
            break;
         case 4:
            xTarget = p.getX();
            yTarget = p.getY() + 96;
            break;
      }
      super.moveToTarget(b, xTarget, yTarget);
   }
}