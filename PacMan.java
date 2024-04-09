import java.awt.*;

/**
The PacMan class extends Pellet to hold information not only about its
x-coordinate, y-coordinate, color, and diameter, but also its rate of
speed and its current direction.
@author Aidan Harbison and Ajay Prabhakar
@since 5-29-2018
*/
public class PacMan extends Pellet
{
   /** change in x-value for each frame */
   private int dx;
   /** change in y-value for each frame */
   private int dy;
   /**
   direction of PacMan object's movement; 1 correlates to left,
   2 with up, 3 with right, and 4 with down.
   */
   private int myDirection;
   /**
   pixels PacMan will move in any direction at each frame change;
   rate of change or speed 
   */
   private int change;
   
   /**
   Initializes an unmoving PacMan object with the specified
   coordinates and diameter.
   @param x	x-coordinate of PacMan
   @param y	y-coordinate of PacMan
   @param d	diameter of PacMan
   */
   public PacMan(int x, int y, int d)
   {
      super(x, y, d);
      setDirection(0);
      change = 0;
   }
   
   /**
   Initializes an currently unmoving PacMan object with the
   specified coordinates, diameter, color, and rate of speed.
   @param x	x-coordinate of PacMan
   @param y	y-coordinate of PacMan
   @param d	diameter of PacMan
   @param c	color of PacMan
   @param ch	PacMan's rate of speed
   */
   public PacMan(int x, int y, int d, Color c, int ch)
   {
      super(x, y, d, c);
      setDirection(0);
      change = ch;
   }
   
   /**
   Sets PacMan's location back to his starting position, with
   no specified direction.
   */
   public void reset()
   {
      setX(336);
      setY(564);
      myDirection = 0;
   }
   
   /**
   Returns the speed of PacMan.
   @return  speed of PacMan.
   */
   public int getChange()
   {
      return change;
   }
   
   /**
   Returns direction that PacMan is currently moving at in
   the form of an integer; 1 correlating with left, 2 with
   up, 3 with right, and 4 with down.
   @return  direction of PacMan.
   */
   public int getDirection()
   {
      return myDirection;
   }
   
   /**
   Sets the speed or rate of change of a PacMan object.
   @param x value to be set as speed of PacMan
   */
   public void setChange(int x)
   {
      change = x;
   }
   
   /**
   Sets PacMan's direction to any number from 1-4, depending on
   the desired direction, and varies the movement in the dx and
   dy accordingly.
   @param d direction number inputted to set current direction and dx and dy
   */
   public void setDirection(int d)
   {
      switch(d){
         case 0:
            dx = dy = 0;
            myDirection = 0;
            break;
         case 1:
            dx = change * -1;
            dy = 0;
            myDirection = 1;
            break;
         case 2:
            dx = 0;
            dy = change * -1;
            myDirection = 2;
            break;
         case 3:
            dx = change;
            dy = 0;
            myDirection = 3;
            break;
         case 4:
            dx = 0;
            dy = change;
            myDirection = 4;
            break;
      }
   }
   
   /**
   Returns true or false depending on whether PacMan can move
   in the specified direction or if bumpers block its path.
   @param d	direction being checked if PacMan can move
   @param b	array of bumpers that prohibit PacMan's movement
   @return 	whether PacMan can move in specified direction
   */
   public boolean canGo(int d, Bumper[] b)
   {
      PacMan test;
      switch(d){
         case 1:
            if(myDirection == 3)
               return true;
            else if((((getX() + 12) % 24 != 0) || ((getY() + 12) % 24 != 0)) && myDirection != d) {
               return false;
            }
            else{
               test = new PacMan(getX() - change, getY(), getDiameter());
               for(int q = 0; q < b.length; q++)
                  if(b[q].inBumper(test)){
                     return false;
                  }
            }
            break;
         case 2:
            if(myDirection == 4)
               return true;
            else if(getY() - change < 36){
               return false;
            }
            else if((((getX() + 12) % 24 != 0) || ((getY() + 12) % 24 != 0)) && myDirection != d){               
               return false;
            }
            else {
               test = new PacMan(getX(), getY() - change, getDiameter());
               for(int q = 0; q < b.length; q++)
                  if(b[q].inBumper(test)){
                     return false;
                  }
            }
            break;
         case 3:
            if(myDirection == 1)
               return true;
            else if((((getX() + 12) % 24 != 0) || ((getY() + 12) % 24 != 0)) && myDirection != d){
               return false;
            }
            else {
               test = new PacMan(getX() + change, getY(), getDiameter());
               for(int q = 0; q < b.length; q++)
                  if(b[q].inBumper(test)){
                     return false;
                  }
            }
            break;
         case 4:
            if(myDirection == 2)
               return true;
            else if(getY() + change > 708){
               return false;
            }
            else if((((getX() + 12) % 24 != 0) || ((getY() + 12) % 24 != 0)) && myDirection != d){
               return false;
            }
            else {
               test = new PacMan(getX(), getY() + change, getDiameter());
               for(int q = 0; q < b.length; q++)
                  if(b[q].inBumper(test)){
                     return false;
                  }
            }
            break;
      }
      return true;
   }
   
   /**
   Checks if PacMan is able to collide with a pellet with the given direction 
   @param pellet array of regular pellets, or pac-dots
   @param powerP array of power pellets
   @param b array of Bumper obstacles hindering PacMan's path
   @return whether PacMan is colliding or in on path of colliding with another
   pellet
   */
   public boolean inDirectionOfPellets(Pellet[] pellet, Pellet[] powerP, Bumper[] b)
   {
      int xct = 0;
      int yct = 0;
      int xct2 = 0;
      int yct2 = 0;
      switch(getDirection()){
         case 1: 
            xct = -1 * change;
            xct2 = 1;
            break;
         case 2: 
            yct = -1 * change;
            yct2 = -1;
            break;
         case 3: 
            xct = change;
            xct2 = 1;
            break;
         case 4: 
            yct = change;
            yct2 = 1;
            break;
      }
      
      PacMan pac1 = new PacMan(getX() + xct, getY() + yct, 20);
      PacMan pac2 = new PacMan(getX() + (2*xct), getY() + (2*yct), 20);
      PacMan pac3 = new PacMan(getX() + xct2, getY() + yct2, 20);
      
      boolean pelletCollision = !(Pellet.collide(pac1, pellet, powerP) ||  Pellet.collide(pac2, pellet, powerP));
      
      if(Bumper.inBumper(pac1, b)){
         return true;
      }
      else
         return pelletCollision;
   }
   
   /**
   Draws PacMan using pre-defined parameters and an interval of mouth width.
   @param buffer	Graphics object with which to draw PacMan
   @param i interval of PacMan's mouth width
   */
   public void drawMe(Graphics buffer, int i)
   {
      buffer.setColor(getColor());
      buffer.fillOval(getX() - 16, getY() - 16, 32, 32);
      buffer.setColor(Color.BLACK);
      int[] xPoints = new int[] {0, 0, 0};
      int[] yPoints = new int[] {0, 0, 0};
      switch(myDirection){
         case 0:
            i = 12;
            xPoints = new int[] {getX() + 5, getX() - 16, getX() - 16};
            yPoints = new int[] {getY(), getY() - i, getY() + i};
            break;
         case 1:
            xPoints = new int[] {getX() + 5, getX() - 16, getX() - 16};
            yPoints = new int[] {getY(), getY() - i, getY() + i};
            break; 
         case 2:
            xPoints = new int[] {getX(), getX() - i, getX() + i};
            yPoints = new int[] {getY() + 5, getY() - 16, getY() - 16};
            break;
         case 3:
            xPoints = new int[] {getX() - 5, getX() + 16, getX() + 16};
            yPoints = new int[] {getY(), getY() - i, getY() + i};
            break;
         case 4:
            xPoints = new int[] {getX(), getX() + i, getX() - i};
            yPoints = new int[]  {getY() - 5, getY() + 16, getY() + 16};
            break;
      }
      buffer.fillPolygon(xPoints, yPoints, 3);
   }
   
   /**
   Draws an black arc depending with a variable angle depending on the
   position of PacMan.
   Used to block out portions of PacMan to create death animation.
   @param g Graphics object
   @param x x-coordinate of PacMan
   @param y y-coordinate of PacMan
   @param s Starting angle of arc drawn
   @param e Entire angle of arc drawn
   */
   public void drawDying(Graphics g, int x, int y, int s, int e)
   {
      g.setColor(Color.BLACK);
      g.fillArc(x-17, y-16, 35, 35, s, e);
   }

   /**
   Moves PacMan one frame, using its pre-defined change in x-coordinates
   and change in y-coordinates; moves PacMan to other side of the maze's
   tunnel if need be. Ensures that PacMan does not go into each Bumper that
   serves as its obstacles.
   @param b	array of bumpers that prohibit PacMan's movement
   */
   public void move(Bumper[] b)
   {
      setX(getX() + dx);
      for(int x = 0; x < b.length; x++)
         if(b[x].inBumper(this)){
            setX(getX() - dx);
         }
             
      if(getX() < -24)
         if(dx == -8)
            setX(692);
         else
            setX(696);
      if(getX() > 696)
         if(dx == 8)
            setX(-20);
         else
            setX(-24);
         
      setY(getY() + dy);
      for(int x = 0; x < b.length; x++)
         if(b[x].inBumper(this)){
            setY(getY() - dy);
         }
   }
}