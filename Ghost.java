import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

/**
The Ghost class extends PacMan to allow movement throughout the board,
but also stores its own coordinates of a specific corner of the board,
and private information of whether it is frightened or not and dead
or not. Ghosts know how to move to a specified target coordinate in
the fastest way possible.
@author Aidan Harbison and Ajay Prabhakar
@since 5-29-2018
*/
public class Ghost extends PacMan
{
   /** x-coordinate of the Ghost object's corner */
   private int xCorner;
   /** y-coordinate of the Ghost object's corner */
   private int yCorner;
   /** whether the Ghost is frightened or not */
   private boolean fright;
   /** whether the Ghost is dead or not */
   private boolean dead;
   /** ImageIcon representation blue frightened ghost;
   drawn when frightened. */
   private ImageIcon blue = new ImageIcon("imageicons\\blue.png");
   /** ImageIcon representation white frightened ghost;
   drawn when frightened period is almost over. */
   private ImageIcon white = new ImageIcon("imageicons\\white.png");
   /** ImageIcon representation dead ghost moving left;
   drawn when dead and moving left. */
   private ImageIcon dead1 = new ImageIcon("imageicons\\dead1.png");
   /** ImageIcon representation dead ghost moving up;
   drawn when dead and moving up. */
   private ImageIcon dead2 = new ImageIcon("imageicons\\dead2.png");
   /** ImageIcon representation dead ghost moving right;
   drawn when dead and moving right. */
   private ImageIcon dead3 = new ImageIcon("imageicons\\dead3.png");
   /** ImageIcon representation dead ghost moving down;
   drawn when dead and moving down. */
   private ImageIcon dead4 = new ImageIcon("imageicons\\dead4.png");
   
   /**
   Default constructor of Ghost; creates a Ghost at the coordinates
   (0, 0) with a diameter of 0 pixels.
   */
   public Ghost(){
      super(0, 0, 0);
   }
   /**
   Initializes a ghost with the specified coordinates, diameter, and 
   coordinates of a corner, as well as a speed of 8 and white color.
   @param x x-coordinate of the Ghost
   @param y y-coordinate of the Ghost
   @param d diameter of the Ghost
   @param xC	x-coordinate of the corner
   @param yC	y-coordinate of the corner
   */
   public Ghost(int x, int y, int d, int xC, int yC)
   {
      super(x, y, d, Color.WHITE, 8);
      fright = false;
      xCorner = xC;
      yCorner = yC;
   }
   /**
   Moves the ghost towards a specific set of target coordinates,
   by determining in which way the Ghost can move, and which way
   is closest to the target as the crow flies, and then moving the 
   Ghost in the proper direction. An alternate distance is 
   calculated of going through the tunnel on the left and right sides
   of the screen, and used if optimal.
   @param b	array of Bumper obstacles inhibiting the Ghost’s path
   @param targetX x-coordinate of the target of the Ghost
   @param targetY 	y-coordinate of the target of the Ghost
   */
   public void moveToTarget(Bumper[] b, int targetX, int targetY)
   {
      double[] dis = {-50, -50, -50, -50};
      int[] xconsts = {-4, 0, 4, 0};
      int[] yconsts = {0, -4, 0, 4};
      for(int x = 1; x <= 4; x++) {
         if(canGo(x, b)) {
            int xconst = xconsts[x-1];
            int yconst = yconsts[x-1];
            double pacdis = distance(getX() + xconst, getY() + yconst, targetX, targetY);
            double altPacDis = 0;
            if(distance(getX() + xconst, getY() + yconst, -24, 348) < distance(getX() + xconst, getY() + yconst, 696, 348))
               altPacDis = distance(getX() + xconst, getY() + yconst, -24, 348) + distance(696, 348, targetX, targetY);
            else
               altPacDis = distance(getX() + xconst, getY() + yconst, 696, 348) + distance(-24, 348, targetX, targetY);
            if(altPacDis < pacdis && altPacDis != 0)
               dis[x-1] = altPacDis;
            else
               dis[x-1] = pacdis;
         }
      }
      switch(getDirection()){
         case 1:
            dis[2] = -50;
            break;
         case 2:
            dis[3] = -50;
            break;
         case 3:
            dis[0] = -50;
            break;
         case 4:
            dis[1] = -50;
            break;
      }
      
      int d = findMin(dis);
      if(d != -1)
         setDirection(d);
      move(b);
   }
   /**
   Uses an ImageIcon to draw a blue frightened Ghost.
   @param g	Graphics object used to draw image
   */
   public void drawBlue(Graphics g)
   {
      g.drawImage(blue.getImage(), getX() - 17, getY() - 16, 34, 32, null);
   }
   
   /**
   Uses an ImageIcon to draw a white frightened Ghost.
   @param g	Graphics object used to draw image
   */
   public void drawWhite(Graphics g)
   {
      g.drawImage(white.getImage(), getX() - 17, getY() - 16, 34, 32, null);
   }
   
   /**
   Uses an ImageIcon to draw a dead Ghost, depending on its
   current direction.
   @param g	Graphics object used to draw image
   */
   public void drawDead(Graphics g)
   {
      switch(getDirection()){
         case 1:
            g.drawImage(dead1.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 2:
            g.drawImage(dead2.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 3:
            g.drawImage(dead3.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
         case 4:
            g.drawImage(dead4.getImage(), getX() - 17, getY() - 16, 34, 32, null);
            break;
      }
   }
   /**
   Moves Ghost towards a target of its pre-defined corner
   and circles around the closest barrier to it.
   @param b	array of Bumper obstacles blocking the Ghost’s path
   */
   public void moveScatter(Bumper[] b)
   {
      moveToTarget(b, xCorner, yCorner);
   }
   
   /**
   Moves Ghost towards the a target of the coordinates just
   above the entrance to the Ghost house.
   @param b	array of Bumper obstacles blocking the Ghost’s path
   */
   public void moveHome(Bumper[] b)
   {
      moveToTarget(b, 336, 276);
   }
   /**
   Moves in a frightened state by determing the directions the 
   Ghost can move to and randomly deciding a direction to go to
   without turning around.
   @param b	array of Bumper obstacles blocking the Ghost’s path
   */
   public void moveFrightened(Bumper[] b)
   {
      boolean[] bo = {canGo(1, b), canGo(2, b), canGo(3, b), canGo(4, b)};
      switch(getDirection()){
         case 1:
            bo[2] = false;
            break;
         case 2:
            bo[3] = false;
            break;
         case 3:
            bo[0] = false;
            break;
         case 4:
            bo[1] = false;
            break; 
      }
      int rand = (int)(Math.random() * bo.length);
      while(!bo[rand])
         rand = (int)(Math.random() * bo.length);
      setDirection(rand + 1);
      
      move(b);
   }
   /**
   Finds the minimum of an array of distances while ignoring negative
   distances.
   @param arr		array of doubles to be sorted
   @return  index of minimum of the array that is not negative
   */  
   private int findMin(double[] arr)
   {
      int min = 0;
      while(arr[min] == -50){
         min++;
         if(min == 4)
            return -1;
      }
      for(int x = min + 1; x < arr.length; x++)
         if(arr[min] > arr[x] && arr[x] != -50){
            min = x;
         }
      return min + 1;  
   }
   
   /**
   Detects whether the PacMan and Ghost object have collided, and calls 
   appropriate methods to the Scoreboard based on each case. 
   If the Ghost is frightened and has collided with PacMan, scoreGhost is 
   called using the succession of ghosts, and a sound is played to signal it.
   The Ghost's speed is increased.
   If the Ghost is not frightened and has collided with PacMan, a life is
   removed from the Scoreboard and both PacMan and the Ghost stop moving.
   @param p	PacMan tested for collision
   @param s	Scoreboard called if collision occurs
   @param num  succession of Ghost being scored
   @param sound   whether the player would like sound or not
   @return 	whether PacMan and ghost have collided
   */
   public boolean collideGhost(PacMan p, Scoreboard s, int num, boolean sound)
   {
      if(distance(getX(), getY(), p.getX(), p.getY()) < p.getRadius() + 17){
         if(fright){
            num++;
            s.scoreGhost(num);
            dead = true;
            
            if(sound)
               try{
                  AudioInputStream deadAIS = AudioSystem.getAudioInputStream(new File("sounds\\eatghost.wav").getAbsoluteFile());
                  Clip eatGhost = AudioSystem.getClip();
                  eatGhost.open(deadAIS);
                  eatGhost.start();
               } 
               catch(Exception e){}
            
            switch(getDirection()){
               case 1:
                  while( (getX() + 12) % 24 != 0)
                     setX(getX() - 4);
                  break;
               case 2:
                  while( (getY() + 12) % 24 != 0)
                     setY(getY() - 4);
                  break;
               case 3:
                  while( (getX() + 12) % 24 != 0)
                     setX(getX() + 4);
                  break;
               case 4:
                  while( (getY() + 12) % 24 != 0)
                     setY(getY() + 4);
                  break;
            }
               
            setChange(24);
            return true;
         }
         else{
            s.hitGhost();
            p.setDirection(0);
            setDirection(0);
            return true;
         }
      }
      return false;
   } 
   /**
   Moves ghosts one frame in an up and down pattern between
   specified y-values, if it still needs to be kept in the ghost house.
   */
   public void bounceInHouse()
   {
      if(getDirection() == 4){
         setY(getY() + 6);
         if(getY() == 360){
            setDirection(2);
         }
      }
      else if(getDirection() == 2){
         setY(getY() - 6);
         if(getY() == 336){
            setDirection(4);
         }
      }
   } 
   
   /**
   Sets whether the ghost is currently frightened or not, and
   slows speed if it becomes frightened or increases speed if
   not frightened.
   @param b	whether ghost is currently frightened
   @param inCorridor whether the ghost is located within the maze,
   in which it is true, or in the ghost house in which it is false.
   */
   public void setFrightened(boolean b, boolean inCorridor)
   {
      if(b)
         setChange(4);
         
      else if(!b && (fright || getChange() == 4) && !dead ){
         switch(getDirection()){
            case 1:
               if( (getX() + 4) % 8 != 0)
                  if(inCorridor)
                     setX(getX() - 4);
               break;
            case 2:
               if( (getY() + 4) % 8 != 0)
                  if(inCorridor)
                     setY(getY() - 4);
               break;
            case 3:
               if( (getX() + 4) % 8 != 0)
                  if(inCorridor)
                     setX(getX() + 4);
               break;
            case 4:
               if( (getY() + 4) % 8 != 0)
                  if(inCorridor)
                     setY(getY() + 4);
               break;
         }
         setChange(8);  
      }      
      fright = b;
   }
   
   /**
   Returns whether the Ghost object is currently frightened or not.
   @return 	fright; whether ghost is frightened
   */
   public boolean isFright()
   {
      return fright;
   }
   
   /**
   Returns whether the Ghost object is currently dead or not.
   @return 	dead; whether ghost is dead
   */
   public boolean isDead()
   {
      return dead;
   }
   
   /** Sets ghost as both not frightened and not dead. */
   public void isAlive()
   {
      dead = false;
      fright = false;
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