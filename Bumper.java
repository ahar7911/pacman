import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
*The Bumper class stores information about its coordinates of its top-left
corner, and its width and height, and detects for collisions between the
bumper and any PacMan object.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Bumper
{
   /** x-coordinate of top-left corner of bumper */
   private int myX;
   /** y-coordinate of top-left corner of bumper */
   private int myY;
   /** width in pixels of bumper */
   private int myWidth;
   /** height in pixels of bumper */
   private int myHeight;
   
   /**
   Initializes a bumper with the specified coordinates, width, and height.
   @param x 	x-coordinate
   @param y	y-coordinate
   @param w	width
   @param h 	height
   */
   public Bumper(int x, int y, int w, int h)
   {
      myX = x;
      myY = y;
      myWidth = w;
      myHeight = h;
   }
   
   /**
   Returns the x-coordinate of the Bumper.
   @return  x-coordinate
   */
   public int getX()
   {
      return myX;
   }
   
   /**
   Returns the y-coordinate of the Bumper.
   @return  y-coordinate
   */
   public int getY()
   {
      return myY;
   }
   
   /**
   Returns the width of the Bumper.
   @return  width
   */
   public int getWidth()
   {
      return myWidth;
   }
   
   /**
   Returns the height of the Bumper.
   @return  height
   */
   public int getHeight()
   {
      return myHeight;
   }
   
   /**
   Draws a rounded rectangle of the Bumper using myBuffer, 
   from the coordinates outwards in the color blue, 12 pixels inset.
   @param myBuffer	Graphics object used to draw the rectangle
   */
   public void draw(Graphics myBuffer) 
   {
      Graphics2D g2 = (Graphics2D) myBuffer;
      g2.setColor(Color.BLUE);
      g2.setStroke(new BasicStroke(5.0f));
      g2.draw(new RoundRectangle2D.Double(myX + 12, myY + 12, myWidth - 24, myHeight - 24, 20, 20));
   }
   
   /**
   Draws a rounded rectangle of the Bumper using myBuffer,
   from the coordinates outwards in the color white, 12 pixels inset.
   @param myBuffer	Graphics object used to draw the rectangle
   */
   public void drawWhite(Graphics myBuffer) 
   {
      Graphics2D g2 = (Graphics2D) myBuffer;
      g2.setColor(Color.WHITE);
      g2.setStroke(new BasicStroke(5.0f));
      g2.draw(new RoundRectangle2D.Double(myX + 12, myY + 12, myWidth - 24, myHeight - 24, 20, 20));
   }
   
   /**
   Returns whether the given PacMan is inside the current Bumper object.
   @param p 	PacMan tested to see if inside Bumper
   @return whether PacMan overlaps with the Bumper object's coordinates or not
   */
   public boolean inBumper(PacMan p)
   {
      for(int x = myX; x <= myX + myWidth; x++)
         for(int y = myY; y <= myY + myHeight; y++)
            if(distance(x, y, p.getX(), p.getY()) < 12)
               return true;         
      return false;
   }
   
   /**
   Returns whether the given PacMan is inside any Bumper object
   in an array
   @param p PacMan tested to see if inside Bumper objects of array
   @param b array of Bumper objects that prohibit PacMan's movement.
   @return whether PacMan overlaps with any Bumper object's coordinates or not
   */
   public static boolean inBumper(PacMan p, Bumper[] b)
   {
      for(int i = 0; i < b.length; i++)
         for(int x = b[i].myX; x <= b[i].myX + b[i].myWidth; x++)
            for(int y = b[i].myY; y <= b[i].myY + b[i].myHeight; y++)
               if(distance(x, y, p.getX(), p.getY()) < 12)
                  return true;         
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
   private static double distance(double x1, double y1, double x2, double y2)
   {
      return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
   }	
}
