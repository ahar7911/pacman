import java.awt.*;

/**
The Pellet class stores information about its coordinates, color, and diameter.
A Pellet object has methods to access and set its coordinates and color, return
the distance between two objects, and determine if a PacMan object has collided
with it.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class Pellet
{
   /** x-coordinate of Pellet object */
   private int myX;
   /** y-coordinate of Pellet object */
   private int myY;
   /** diameter of Pellet object */
   private int myDiameter;
   /** color of Pellet object */
   private Color myColor;
   
   /**
   Initializes a Pellet object with a specified x-coordinate, y-coordinate,
   and diameter, along with a default color of white.
   @param x x-coordinate of the Pellet
   @param y y-coordinate of the Pellet
   @param d diameter of the Pellet
   */
   public Pellet(int x, int y, int d)
   {
      myX = x;
      myY = y;
      myDiameter = d;
      myColor = Color.WHITE;
   }
   
   /**
   Initializes a Pellet object with a specified x-coordinate,
   y-coordinate, diameter, and color.
   @param x	x-coordinate of the Pellet
   @param y	y-coordinate of the Pellet
   @param d	diameter of the Pellet
   @param c	color of the Pellet
   */
   public Pellet(int x, int y, int d, Color c)
   {
      myX = x;
      myY = y;
      myDiameter = d;
      myColor = c;
   }
   
   /**
   Returns the x-coordinate of the Pellet object.
   @return	 myX; x-coordinate of Pellet.
   */
   public int getX()
   {
      return myX;
   }
   
   /**
   Returns the y-coordinate of the Pellet object.
   @return 	myY; y-coordinate of Pellet.
   */
   public int getY()
   {
      return myY;
   }
   
   /**
   Returns the radius of the Pellet object.
   @return radius of Pellet.
   */
   public int getRadius()
   {
      return myDiameter / 2;
   }
   
   /**
   Returns the diameter of the Pellet object.
   @return myDiameter; diameter of Pellet.
   */
   public int getDiameter()
   {
      return myDiameter;
   }
   
   /**
   Returns the color of the Pellet object.
   @return myColor; color of Pellet.
   */
   public Color getColor()
   {
      return myColor;
   }
   
   /**
   Sets the x-coordinate of the Pellet object as the input of the given method.
   @param x assigns to myX
   */
   public void setX(int x)
   {
      myX = x;
   }
   
   /**
   Sets the y-coordinate of the Pellet object as the input of the given method.
   @param y assigns to myY
   */
   public void setY(int y)
   {
      myY = y;
   }
   
   /**
   Sets the color of the Pellet object as the input of the given method.
   @param c assigns to myColor
   */
   public void setColor(Color c)
   {
      myColor = c;
   }
   
   /**
   Determines if the Pellet object has collided with the Pellet object, or if
   they overlap each other, determined by if their combined radii are less than
   the distance between them or not. If they do collide, the scoreboard adds a
   certain amount of points based on the type of pellet, found by its diameter.
   @param p	PacMan to check collision against
   @param s	Scoreboard to update score if collision has occurred.
   @return whether the PacMan has collided with the Pellet
   */
   public boolean collide(PacMan p, Scoreboard s)
   {
      if(distance(myX, myY, p.getX(), p.getY()) < p.getRadius() + getRadius() && myColor == Color.WHITE){
         if(myDiameter == 8)
            s.scorePellet();
         else if(myDiameter == 18)
            s.scorePowerPellet();
         setColor(Color.BLACK);
         return true;
      }
      return false;
   }
   
   /**
   Determines if the Pellet object has collided with any Pellet object of an
   array, or ifthey overlap each other, determined by if their combined radii
   are less than thedistance between them or not.
   @param pac	PacMan to check collision against
   @param p	array of regular pellets (pac-dots) for PacMan to check for
   collisions
   @param pp array of power pellets for PacMan to check for collisions
   @return	whether collision between PacMan and any Pellet object of the array
   has occurred.
   */
   public static boolean collide(PacMan pac, Pellet[] p, Pellet[] pp)
   {
      for(int x = 0; x < p.length; x++){
         if(distance(p[x].myX, p[x].myY, pac.getX(), pac.getY()) < pac.getRadius() + p[x].getRadius() && p[x].getColor() == Color.WHITE)
            return true;
         if(x < pp.length)
            if(distance(pp[x].myX, pp[x].myY, pac.getX(), pac.getY()) < pac.getRadius() + pp[x].getRadius() && pp[x].getColor() == Color.WHITE)
               return true;
      }
      return false;
   }
   
   /**
   Draws the Pellet object based off its previously defined parameters.
   @param g	Graphics object with which to display the Pellet object
   */
   public void drawMe(Graphics g)
   {
      g.setColor(myColor);
      g.fillOval(myX - getRadius(), myY - getRadius(), myDiameter, myDiameter);
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