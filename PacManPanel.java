import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;
import java.awt.geom.RoundRectangle2D;
import javax.sound.sampled.*;

/**
The PacManPanel class holds the maze-like structure of the PacMan, including
PacMan, the ghosts, pellets, and fruit. Using animation, the class moves PacMan
around the board based on keyboard inputs, moves the ghosts, and checks for
collisions between pellets, PacMan, the ghosts, and the fruit to initiate
special behaviors.
@author Aidan Harbison and Ajay Prabhakar
@since 5-29-2018
*/
public class PacManPanel extends JPanel
{
   /** Image representation of the panel. */
   private BufferedImage myImage;
   
   /** Object used to draw the panel. */
   private Graphics g;
   
   /** PacMan object that the player controls throughout the game. */
   private PacMan pac;
   
   /** AI-controlled red ghost that chases PacMan throughout the maze-like
   structure of the game. */
   private Blinky blinky;
   
   /** AI-controlled pink ghost that chases PacMan throughout the maze-like
   structure of the game. */
   private Pinky pinky;
   
   /** AI-controlled blue ghost that chases PacMan throughout the maze-like
   structure of the game. */
   private Inky inky;
   
   /** AI-controlled orange ghost that chases PacMan throughout the maze-like
   structure of the game. */
   private Clyde clyde;
   
   /** Fruit that periodically appears within the maze for PacMan to eat for
   points. */
   private Fruit fruit;
   
   /** Width of the panel in pixels. */
   private static final int WIDTH = 672;
   
   /** Height of the panel in pixels. */
   private static final int HEIGHT = 744;
   
   /** Timer that animates the introductory sequence to each level after
   completing the previous level or dying and starts the animation Timer. */
   private Timer intro;
   
   /** Timer that animates the game while the player controls the PacMan
   through the maze. */
   private Timer animation;
   
   /** Timer that animates the blinking, or changing of colors from blue to
   white, effect found at the completion of a level and the starts the
   introduction Timer. */
   private Timer blinker;
   
   /** Timer that animates the section of the game when the player has lost
   all lives, displaying "Game Over" */
   private Timer gameOver;
   
   /** Timer that animates the pause and display of addition to the score
   found after eating a ghost or a fruit and then continues the animation
   Timer. */
   private Timer pause;
   
   /** Timer that animates the dying animation of PacMan after running into
   a ghost and starts the introduction Timer. */
   private Timer collider;
   
   /** Array of Bumpers that organize the panel of PacMan into a maze-like
   structure, serving as the walls of the maze. */
   private Bumper[] maze;
   
   /** Scoreboard to display the current score of the player, the high score
   overall, and to update the score as necessary. */
   private Scoreboard scoreboard;
   
   /** Array of power pellets, or larger pellets that allow the ghosts to be
   eaten by PacMan for a short amount of time. */
   private Pellet[] powerPellet;
   
   /** Array of all pac-dots, or pellets, found within the maze of PacMan that
   signify each level. The eating of all pac-dots signifies the end of a
   level. */
   private Pellet[] pellet;
   
   /** Current level that the player has reached. */
   private int level = 1;
   
   /** Width of PacMan's mouth during animation. */
   private int chompWidth = 12;
   
   /** Counter of all pellets the player has collected since they have either
   died or begun a new level. */
   private int pelletsCollectedSinceDeath = 0;
   
   /** Counter of all pellets the player has collected since they have begun a
   new level. */
   private int allPelletsCollected = 0;
   
   /** Counter of all ghosts eaten by PacMan since the last power pellet was
   eaten. */
   private int ghostsEaten = 0;
   
   /** Listener controlled by the introduction Timer to animate the
   introduction. */
   private ReadyListener ready;
   
   /** Listener controlled by the pause Timer to animate the pause and display
   of addition to the score found after eating a ghost or a fruit. */
   private PauseListener pauseListener;
   
   /** Represents whether PacMan's mouth is currently closing, in which case
   it is true, or opening, in which case it is false. */
   private boolean chompDecreasing = true;
   
   /** Represents whether the player has died since reaching the level they
   are currently playing. */
   private boolean died = false;
   
   /** Represents whether the player is currently collecting pellets or in the
   path of pellets or not. */
   private boolean collecting = false;
   
   /** Represents whether Blinky, the red ghost, is currently chasing PacMan
   within the maze, where it is true, or is within the ghost house in the 
   center of the screen, where it is false. */
   private boolean blinkyInCorridor = true;
   
   /** Represents whether Pinky, the pink ghost, is currently chasing PacMan
   within the maze, where it is true, or is within the ghost house in the 
   center of the screen, where it is false. */
   private boolean pinkyInCorridor = false;
   
   /** Represents whether Inky, the blue ghost, is currently chasing PacMan
   within the maze, where it is true, or is within the ghost house in the 
   center of the screen, where it is false. */
   private boolean inkyInCorridor = false;
   
   /** Represents whether Clyde, the orange ghost, is currently chasing PacMan
   within the maze, where it is true, or is within the ghost house in the 
   center of the screen, where it is false. */
   private boolean clydeInCorridor = false;
   
   /** Ensures that Inky, the blue ghost, does not return to bouncing within
   the ghost house in the center of the screen after the trigger has occurred
   for it to leave the house. */
   private boolean inkyNoReturn = true;
   
   /** Ensures that Clyde, the orange ghost, does not return to bouncing within
   the ghost house in the center of the screen after the trigger has occurred
   for it to leave the house. */
   private boolean clydeNoReturn = true;
   
   /** Determines whether Blinky, the red ghost, is going upwards within the
   ghost house as it leaves. */
   private boolean blinkyGoesUp = false;
   
   /** Determines whether Blinky, the red ghost, is going downwards within the
   ghost house as it enters. */
   private boolean blinkyGoesDown = false;
   
   /** Determines whether Pinky, the pink ghost, is going upwards within the
   ghost house as it leaves. */
   private boolean pinkyGoesUp = true;
   
   /** Determines the time in milliseconds (since midnight, January 1st, 1970)
   when the effect of PacMan's eating the power pellet wears off and the ghosts
   resume chasing. */
   private long frightenedDuration = 0;
   
   /** Responds to keyboard input by the player to control the movement of the
   PacMan character. */
   private Key keyListener;
   
   /** Determines the time in milliseconds (since midnight, January 1st, 1970)
   when the fruit that has appeared in the center of the screen disappears once
   more. */
   private long fruitTimer = 0;
   
   /** Plays the sound that occurs when PacMan is currently collecting or in the
   path of collecting pellets. */
   private Clip eatSound;
   
   /** Plays the siren sound that occurs when the ghosts are chasing PacMan
   throughout the maze. */
   private Clip siren;
   
   /** Plays the sound that occurs when PacMan has eaten a power pellet and all
   ghosts are in a frightened state and are blue. */
   private Clip frightened;
   
   /** Plays the sound that occurs when PacMan eats a fruit. */
   private Clip fruitS;
   
   /** Plays the sound that occurs when PacMan has eaten a ghost and it is in
   the process of returning to the ghost house. */
   private Clip deadGhost;
   
   /** Represents whether the player would like sound to be played or not. */
   private boolean sound;
   
   /**
   Initializes PacManPanel object using Scoreboard object. Creates the
   background, pellets, PacMan, and ghosts, and displays them on the board,
   and uses a KeyListener to initiate input by the player, as well as
   creating the private Timer and Listener objects to be used. A cherry is
   displayed in the bottom left corner. If sound is wanted, sounds are
   instantiated and the introduction sound is started. The introduction Timer
   is start to begin its animation.
   @param s	reference to the scoreboard used to change scoring based on
   interactions in panel
   @param sound   whether the player would like sound to be played or not
   @throws Exception removes complication of scanning text files.
   */

   public PacManPanel(Scoreboard s, boolean sound) throws Exception
   {
      myImage =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
      g = myImage.getGraphics();
      
      scoreboard = s;
      this.sound = sound;
         
      makeBackground();
      makePellets();
      drawBackground(false);
      drawPellets();
      
      pac = new PacMan(336, 564, 24, Color.YELLOW, 12);
      pac.drawMe(g, 6);
      
      blinky = new Blinky();
      pinky = new Pinky();
      inky = new Inky();
      clyde = new Clyde();
      
      fruit = new Fruit(1);
      
      drawPlayerReady(false);
      
      animation = new Timer(30, new AnimationListener());
      
      ready = new ReadyListener(System.currentTimeMillis() + 4700, this);
      intro = new Timer(100, ready);
      
      pauseListener = new PauseListener(this);
      pause = new Timer(0, pauseListener);
      
      keyListener = new Key(false);
      
      scoreboard.showFruit(1);
        
      if(sound){          
         try{
            AudioInputStream introAIS = AudioSystem.getAudioInputStream(new File("sounds\\intro.wav").getAbsoluteFile());
            Clip introS = AudioSystem.getClip();
            introS.open(introAIS);
            introS.start();
         } 
         catch(Exception e){
            e.printStackTrace();
         }
      
         try{
            AudioInputStream sirenAIS = AudioSystem.getAudioInputStream(new File("sounds\\siren.wav").getAbsoluteFile());
            siren = AudioSystem.getClip();
            siren.open(sirenAIS);
         } 
         catch(Exception e){
            e.printStackTrace();
         }
         try{
            AudioInputStream eatAIS = AudioSystem.getAudioInputStream(new File("sounds\\pacdot.wav").getAbsoluteFile());
            eatSound = AudioSystem.getClip();
            eatSound.open(eatAIS);
         }
         catch(Exception e){
            e.printStackTrace();
         }
      } 
      intro.start();
   }
   
   /**
   Displays board at current state when the pre-defined repaint method is
   called.
   @param g Graphics object to draw board with
   */
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   /**
   Instantiates array of bumpers, maze, that serves as walls of PacMan's maze
   using parameters read from a text file.
   @throws Exception removes complication of scanning text files.
   */
   private void makeBackground() throws Exception
   {
      maze = new Bumper[42];
      Scanner u = new Scanner(new File("bumpers.txt"));
      for(int x = 0; x < maze.length; x++)
         maze[x] = new Bumper(u.nextInt(), u.nextInt(), u.nextInt(), u.nextInt());
   }
   
   /**
   Draws all bumpers as rounded rectangles using their coordinate, and covers
   overlaps between the each using parameters read from a text file. The ghost
   house is also separately drawn.
   @param white   determines whether the bumpers are draw in white outlines,
   where it would be true, or blue outlines, where it would be false.
   */
   private void drawBackground(boolean white)
   {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, WIDTH, HEIGHT);
      if(!white)
         for(int x = 0; x < maze.length - 6; x++)
            maze[x].draw(g);
      else
         for(int x = 0; x < maze.length - 6; x++)
            maze[x].drawWhite(g);
      
      Scanner box = new Scanner(System.in);
      try{
         box = new Scanner(new File("boxes.txt"));
      }
      catch(Exception e){
         System.exit(0);
      }
      
      Graphics2D g2 = (Graphics2D) g;
      if(!white)
         g2.setColor(Color.BLUE);
      else
         g2.setColor(Color.WHITE);
      g2.setStroke(new BasicStroke(5.0f));
      for(int x = 0; x < 7; x++)
         g2.draw(new RoundRectangle2D.Double(box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt()));
      
      g.drawRect(box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt());
      g.drawRect(box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt());
      
      g.setColor(Color.BLACK);
      for(int x = 0; x < 46; x++)
         g.fillRect(box.nextInt(), box.nextInt(), box.nextInt(), box.nextInt());
      
      g.setColor(new Color(236, 185, 221));
      g.fillRect(312, 302, 48, 9);
   }
   
   /**
   Draws the words "Player" above the ghost house, and "Ready!" below it.
   @param ready   determines whether "Player Ready!" is drawn, where
   it is false, or simply "Ready!", where it is true.
   */   
   private void drawPlayerReady(boolean ready)
   {
      g.setColor(Color.CYAN.darker());
      g.setFont(new Font("Monospaced", Font.BOLD + Font.ITALIC, 30));
      if(!ready)
         g.drawString("PLAYER", 282, 285);
      g.setColor(Color.YELLOW.darker());
      g.drawString("READY!", 288, 429);
   }
   
   /**
   Instantiates array of power pellets, powerPellet, and pac-dots, pellet,
   using parameters read from a text file.
   @throws Exception removes complication of scanning text files.
   */
   private void makePellets() throws Exception
   {
      powerPellet = new Pellet[4];
      powerPellet[0] = new Pellet(36, 84, 18);
      powerPellet[1] = new Pellet(636, 84, 18);
      powerPellet[2] = new Pellet(36, 564, 18);
      powerPellet[3] = new Pellet(636, 564, 18);
   
      pellet = new Pellet[240];
      Scanner g = new Scanner(new File("pellets.txt"));
      for(int x = 0; x < pellet.length; x++)
         pellet[x] = new Pellet(g.nextInt(), g.nextInt(), 8);
   }
   
   /**
   Draws all pac-dots and power pellets that have not yet been collected
   in a white color at their specified coordinates.
   */
   public void drawPellets()
   {
      for(int x = 0; x < pellet.length; x++){
         if(pellet[x].getColor() == Color.WHITE)
            pellet[x].drawMe(g);
         if(x < powerPellet.length)
            if(powerPellet[x].getColor() == Color.WHITE)
               powerPellet[x].drawMe(g);
      }
   }
   
   /**
   Resets the panel when PacMan has either died or reached a new level, through
   stopping all sounds and animation, resetting PacMan to its proper position,
   all power pellets and pac-dots and all ghosts to their proper positions, and
   either starting a death or blinking animation depending on whether the player
   has either died or reached a new level. If a new level is reached, the level
   counter increases, and the fruit type changes depending on its value. If the
   player has died and would like sound, the dying sound begins.
   @param newLevel   whether player has reached a new level, in which case it is
   true, or they have died, in which case it is false.
   */
   public void reset(boolean newLevel)
   {
      if(sound){
         siren.stop();
         eatSound.stop();
         if(deadGhost != null)
            deadGhost.stop();
         if(frightened != null)
            frightened.stop();
      }
      
      animation.stop();
      removeKeyListener(keyListener);
      
      if(newLevel){
         for(int x = 0; x < pellet.length; x++){
            if(x < powerPellet.length)
               powerPellet[x].setColor(Color.WHITE);
            pellet[x].setColor(Color.WHITE);
         }
         level++;
         died = false;
         allPelletsCollected = 0;
         if(sound)
            frightened.stop();
      }
      else
         died = true;
         
      fruit.setEaten(true);
         
      if(level != 0){
         blinky = new Blinky();
         pinky = new Pinky();
         inky = new Inky();
         clyde = new Clyde();
      
         drawBackground(false);
         drawPellets();
         drawPlayerReady(true);
      
         blinky.drawMe(g);
         pinky.drawMe(g);
         inky.drawMe(g);
         clyde.drawMe(g);
      
      
         pelletsCollectedSinceDeath = 0;
         blinkyInCorridor = true;
         pinkyInCorridor = false;
         inkyInCorridor = false;
         clydeInCorridor = false;
         inkyNoReturn = true;
         clydeNoReturn = true;
         pinkyGoesUp = !died;
         
         ghostsEaten = 0;
      
         if(!newLevel){
            collider = new Timer(0, new CollideListener(this, false));
            try{Thread.sleep(500);}
            catch(InterruptedException b){}
            collider.start();
            if(sound){
               try{
                  AudioInputStream pdiesAIS = AudioSystem.getAudioInputStream(new File("sounds\\pacdeath.wav").getAbsoluteFile());
                  Clip pacDeath = AudioSystem.getClip();
                  pacDeath.open(pdiesAIS);
                  pacDeath.start();
               }
               catch(Exception e){} 
            }  
         }
         else{
            blinker = new Timer(0, new BlinkListener(this));
            blinker.start();
         }
      }
   }
   
   /**
   Stops animation and ensures the player cannot do anything except 
   restart the level after losing all lives. Begins a new death animation 
   that displays "Game Over" after concluding, and starts the death sound if
   sound is wanted.
   */
   public void lose()
   {
      level = 0;
      animation.stop();
      removeKeyListener(keyListener);
      addKeyListener(new Key(true));
      
      if(sound){
         try{
            AudioInputStream pdiesAIS = AudioSystem.getAudioInputStream(new File("sounds\\pacdeath.wav").getAbsoluteFile());
            Clip pacDeath = AudioSystem.getClip();
            pacDeath.open(pdiesAIS);
            pacDeath.start();
         }
         catch(Exception e){} 
      }
      
      gameOver = new Timer(0, new CollideListener(this, true));
      gameOver.start();
   }
   
   /**
   Determines whether all pellets have been collected or have not.
   @param p array of pac-dots
   @param pp   array of power pellets
   @return  whether all pellets have been collected, in which case true, or not,
   in which case false
   */
   private boolean allPellets(Pellet[] p, Pellet[] pp)
   {
      for(int x = 0; x < pellet.length; x++){
         if(x < pp.length)
            if(pp[x].getColor() == Color.WHITE)
               return false;
         if(p[x].getColor() == Color.WHITE)
            return false;
      }
      return true;
   }
   
   /**
   The Key class extends KeyAdapter to act as a Listener 
   to detect keyboard input for the player to control PacMan's movement.
   */
   private class Key extends KeyAdapter
   {
      /**
      Represents whether the player can move PacMan, 
      as they are playing the game and thus do not need 
      to restart, in which case it is true. If a restart is needed, it is false.
      */
      private boolean restart;
      /**
      Constructs a new Key object with the specified type of input wanted to be
      acted upon. If a true boolean is inputted, the player can only restart the
      game; if not, the player can move PacMan as the game is played.
      @param b whether the player can only restart the game (true) or move 
      PacMan as the game plays (false)
      */
      public Key(boolean b)
      {
         restart = b;
      }
      /**
      Reacts to a specified input key to control PacMan's movement and the game.
      If the player can move PacMan, arrow or WASD keys can be used to control
      PacMan's movement, each changing his direction depending on the key 
      pressed. Up and W keys change his movement to upwards, A and left keys to 
      leftwards,D and right keys to rightwards, and S and down keys to 
      downwards. The player is given three frames to change direction, and
      direction is not changed if not possible. If the escape key is pressed,
      the game is quit and the program ends. If the player can reset, if space
      is pressed, the game is reset.
      @param e Event of key being pressed
      */
      public void keyPressed(KeyEvent e)
      {
         if(!restart){
            if(e.getKeyCode() == 37 || e.getKeyCode() == KeyEvent.VK_A){
               if(pac.canGo(1, maze))
                  pac.setDirection(1);
               else {
                  for(int c = 0; c < 2; c++){
                     long time = System.currentTimeMillis() + 5;
                     while(System.currentTimeMillis() < time){
                        updateUI();
                     }
                     drawBoard();
                     updateUI();
                     if(pac.canGo(1, maze)){
                        pac.setDirection(1); 
                        break;
                     }
                  }  
               }
            }
            if(e.getKeyCode() == 38 || e.getKeyCode() == KeyEvent.VK_W){
               if(pac.getX() > 0 && pac.getX() < WIDTH){
                  if(pac.canGo(2, maze))
                     pac.setDirection(2);
                  else {
                     for(int c = 0; c < 2; c++){
                        long time = System.currentTimeMillis() + 5;
                        while(System.currentTimeMillis() < time){
                           updateUI();
                        }
                        drawBoard();
                        updateUI();
                        if(pac.canGo(2, maze)){
                           pac.setDirection(2);
                           break;
                        } 
                     }               
                  }
               }
            }
            if(e.getKeyCode() == 39 || e.getKeyCode() == KeyEvent.VK_D){
               if(pac.canGo(3, maze))
                  pac.setDirection(3);
               else {
                  for(int c = 0; c < 2; c++){
                     long time = System.currentTimeMillis() + 5;
                     while(System.currentTimeMillis() < time){
                        updateUI();
                     }
                     drawBoard();
                     updateUI();
                     if(pac.canGo(3, maze)){
                        pac.setDirection(3); 
                        break;
                     }
                  }  
               }
            }
            if(e.getKeyCode() == 40 || e.getKeyCode() == KeyEvent.VK_S){
               if(pac.getX() > 0 && pac.getX() < WIDTH)
                  if(pac.canGo(4, maze))
                     pac.setDirection(4);
                  else {
                     for(int c = 0; c < 2; c++){
                        long time = System.currentTimeMillis() + 5;
                        while(System.currentTimeMillis() < time){
                           updateUI();
                        }
                        drawBoard();
                        updateUI();
                        if(pac.canGo(4, maze)){
                           pac.setDirection(4); 
                           break;
                        }
                     }  
                  }
            }
         }
         
         else if(restart)
            if(e.getKeyCode() == KeyEvent.VK_SPACE)
               scoreboard.restart();
               
               
         if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
            
      }
   }
   
   /**
   The AnimationListener class implements the ActionListener class to animate
   the playing of the game through calling the drawBoard method.
   */
   private class AnimationListener implements ActionListener
   {
      /**
      Updates the panel by moving PacMan and each ghost, and drawing each as 
      well as a fruit, pellets and power pellets based on previous interactions
      in the panel.
      @param e event that determines whether method is called; required
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e)
      {
         drawBoard();
      }
   }
   
   /**
   The ReadyListener class implements the ActionListener class to animate
   the introductory sequence of the PacMan game.
   */
   private class ReadyListener implements ActionListener
   {
      /** Time in milliseconds (since January 1st, 1970) when the introductory
      sequence concludes. */
      private long durationUntil;
      /** Reference to the original PacManPanel, to update and display the
      changes in the panel. */
      private JPanel panel;
      /**
      Constructs a new ReadyListener with specified stopping point and reference
      back to   PacManPanel to update animation.
      @param milliSec	assigned to durationUntil; milliseconds at desired
      stopping point since January 1st 1970
      @param panel	assigned to panel; reference to PacManPanel
      */
      public ReadyListener(long milliSec, JPanel panel)
      {
         durationUntil = milliSec;
         this.panel = panel;
      }
      /**
      Performs preparatory animations before the game starts. If the player has
      just died, PacMan and each ghost are reset to ensure movement from the
      concurring of the previous Timer does not move each ghost. If the Timer is
      1700 milliseconds from ending, the panel is redrawn with only the display
      of "Ready!". When the timer stops, the animation begins, including the 
      siren sound if sound is wanted, as well as changing Blinky's x-coordinate
      to ensure it can properly move.
      @param e event that determines whether method is called; required
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e)
      {
         if(died){
            pinky.reset();
            inky.reset();
            clyde.reset();
         }
         if(System.currentTimeMillis() > durationUntil - 1700)
         {
            drawBackground(false);
            drawPellets();
            
            pac.drawMe(g, 12);
            blinky.drawMe(g);
            pinky.drawMe(g);
            inky.drawMe(g);
            clyde.drawMe(g);
            
            drawPlayerReady(true);
            
            panel.updateUI();
         }
         if(durationUntil - System.currentTimeMillis() <= 0){
            panel.setFocusable(true);
            panel.requestFocus();
            
            panel.addKeyListener(keyListener);
            
            animation.start();
            pac.setDirection(1);
            
            blinky.setX(blinky.getX() - 4);
            if(sound)
               siren.start();
            intro.stop();
         }
      }
      /**
      Sets a new time for the Timer to continue until before stopping its
      animation.
      @param milliSec	assigned to durationUntil; milliseconds at desired
      stopping point since January 1st 1970
      */
      public void setDuration(long milliSec)
      {
         durationUntil = milliSec;
      }
   }
   
   /**
   The BlinkListener class implements the ActionListener class to animate
   the blinking sequence found at the conclusion of a PacMan level.
   */
   private class BlinkListener implements ActionListener
   {
      /** Time in milliseconds (since January 1st, 1970) when the blinking
      sequence concludes. */
      private long timeToStop;
      /** Reference to the original PacManPanel, to update and display 
      the changes in the panel. */
      private JPanel p;
      /**
      Constructs a new BlinkListener with specified reference back to   
      PacManPanel to update animation, and creating a specified stopping point
      two seconds within the future.
      @param panel	assigned to p; reference to PacManPanel
      */
      public BlinkListener(JPanel panel)
      {
         timeToStop = System.currentTimeMillis() + 2000;
         p = panel;
      }
      /**
      Draws the entire background as well as PacMan either white or blue every
      eighth of a second until the specified stopping point. When reached, a
      fruit is displayed in the bottom right corner depending on the new level
      reached, and the introductory Timer is started.
      @param e event that determines whether method is called; required
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e){
         if(System.currentTimeMillis() < timeToStop - 1750){
            drawBackground(false);
         }
         else if(System.currentTimeMillis() < timeToStop - 1500){
            drawBackground(true);
         }
         else if(System.currentTimeMillis() < timeToStop - 1250){
            drawBackground(false);
         }
         else if(System.currentTimeMillis() < timeToStop - 1000){
            drawBackground(true);
         }
         else if(System.currentTimeMillis() < timeToStop - 750){
            drawBackground(false);
         }
         else if(System.currentTimeMillis() < timeToStop - 500){
            drawBackground(true);
         }
         else if(System.currentTimeMillis() < timeToStop - 250){
            drawBackground(false);
         }
         else if(System.currentTimeMillis() < timeToStop){
            drawBackground(true);
         }
         else if(System.currentTimeMillis() >= timeToStop){
            drawBackground(false);
            pac.drawMe(g, 0);
            pac.reset();
            p.updateUI();
            
            ready.setDuration(System.currentTimeMillis() + 2000);
            intro.start();
            blinker.stop();
            
            if(level == 2){
               fruit.setType(2);
               scoreboard.showFruit(2);
            }
            else if(level == 3){
               fruit.setType(3);
               scoreboard.showFruit(3);
            }
            else if(level == 5){
               fruit.setType(4);
               scoreboard.showFruit(4);
            }
            else if(level == 7){
               fruit.setType(5);
               scoreboard.showFruit(5);
            }
         }
         pac.drawMe(g, 0);
         p.updateUI();
      }
   }
             
   /**
   The Pause Listener class implements the ActionListener class to animate
   the pause sequence of the PacMan game when bonus points are scored and
   displayed.
   */
   private class PauseListener implements ActionListener
   {
      /** Reference to the original PacManPanel, to update and display 
      the changes in the panel. */
      private JPanel panel;
      /** Point value displayed when Listener is being called. */
      private int score;
      /** x-coordinate where the displaying of the point value is
      centered around. */
      private int centerX;
      /** y-coordinate where the displaying of the point value is =
      centered around. */
      private int centerY;
      /** Time in milliseconds (since January 1st, 1970) when the pause sequence
      concludes. */
      private long duration;
      /** The ghost that PacMan has collided with, if that induced the pause
      sequence. */
      private Ghost ghost;
      /**
      Constructs a new PauseListener with specified reference back to   
      PacManPanel to update animation.
      @param p assigned to panel; reference to PacManPanel
      */
      public PauseListener(JPanel p)
      {
         panel = p;
      }
      /** Specifies new instance of pausing, and all parameters of what this 
      pause sequence will display.
      @param x assigned to centerX; x-coordinate which the score displayed is
      centered around
      @param y assigned to centerY; y-coordinate which the score displayed is
      centered around
      @param s assigned to score; point value being displayed
      @param g assigned to ghost; ghost PacMan has collided with, if that has
      induced the pause sequence. If not, a generic Ghost object is used.
      @param d assigned to duration; time in milliseconds (since January 1st, 
      1970) when the pause sequence concludes. 
      **/
      public void setCoordinates(int x, int y, int s, Ghost g, long d)
      {
         score = s;
         centerX = x;
         centerY = y;
         ghost = g;
         duration = d;
      }
      /**
      Draws entire board, including each ghost, pellet, and background, but
      excluding PacMan and the ghost it collided with.
      Draws specified point value at specified, pre-determined coordinates.
      Restarts the animation and siren sound (if sound is wanted)
      when the stopping point is reached.
      @param e event that determines whether method is called; required
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e)
      {
         if(duration > System.currentTimeMillis()){
            drawBackground(false);
            drawPellets();
            
            if(!ghost.equals(blinky)){
               if(blinky.isDead())
                  blinky.drawDead(g);
               else if(blinky.isFright())
                  blinky.drawBlue(g);
               else
                  blinky.drawMe(g);
            }
            
            if(!ghost.equals(pinky)){
               if(pinky.isDead())
                  pinky.drawDead(g);
               else if(pinky.isFright())
                  pinky.drawBlue(g);
               else
                  pinky.drawMe(g);
            }
            
            if(!ghost.equals(inky)){
               if(inky.isDead())
                  inky.drawDead(g);
               else if(inky.isFright())
                  inky.drawBlue(g);
               else
                  inky.drawMe(g);
            }
            
            if(!ghost.equals(clyde)){
               if(clyde.isDead())
                  clyde.drawDead(g);
               else if(clyde.isFright())
                  clyde.drawBlue(g);
               else
                  clyde.drawMe(g);
            }
            if(!fruit.isEaten())
               fruit.drawMe(g, pac, scoreboard);
               
            g.setColor(Color.BLACK);
            g.fillRect(centerX - 18, centerY - 18, 36, 36);
            g.setFont(new Font("Monospaced", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString(score + "", centerX - 18, centerY + 5);
            
            panel.updateUI();
         }
         else {
            pause.stop();
            animation.start();
            if(sound)
               siren.start();
         }
      }
   }
   
   /**
   The CollideListener class implements the ActionListener class to animate
   the dying sequence of the PacMan game, once PacMan hits a ghost.
   */
   private class CollideListener implements ActionListener
   {
      /** Time in milliseconds (since January 1st, 1970) when the dying sequence
      concludes. */
      private long timeToStop;
      /** Reference to the original PacManPanel, to update and display 
      the changes in the panel. */
      private JPanel p;
      /** Whether the game has ended and all lives have been lost or not */
      private boolean gameEnds;
      /** Whether the player can enter their score into the leaderboards or not;
      prevents repeated asking */
      private boolean canEnterScore = true;
      /**
      Constructs a new CollideListener with specified reference back to   
      PacManPanel to update animation.
      @param panel	assigned to p; reference to PacManPanel
      @param b assigned to gameEnds; whether all lives have been lost
      */
      public CollideListener(JPanel panel, boolean b)
      {
         timeToStop = System.currentTimeMillis() + 2000;
         p = panel;
         gameEnds = b;
      }
      /**
      Draws PacMan at decreasing sizes of arcs every eighth of a second until
      the specified stopping point and it disappears as the dying animation.
      When the pre-determined stopping point is reached, if the game has ended,
      "Game Over" is displayed and the player is asked to input their score.
      Once asked, they cannot input their score again. If the game has not
      ended, half a second is waited until the introduction Timer is again
      started.
      @param e event that determines whether method is called; required
      parameter for implementation of ActionListener interface
      */
      public void actionPerformed(ActionEvent e){
         
         drawBackground(false);
         drawPellets();
         g.setColor(Color.YELLOW);
         g.fillOval(pac.getX() - 16, pac.getY() - 16, 32, 32);
      
         if(System.currentTimeMillis() < timeToStop - 1750){
            pac.drawDying(g, pac.getX(), pac.getY(), 68, 44);
         }
         else if(System.currentTimeMillis() < timeToStop - 1500){
            pac.drawDying(g, pac.getX(), pac.getY(), 45, 60);
         }
         else if(System.currentTimeMillis() < timeToStop - 1250){
            pac.drawDying(g, pac.getX(), pac.getY(), 22, 136);
         }
         else if(System.currentTimeMillis() < timeToStop - 1000){
            pac.drawDying(g, pac.getX(), pac.getY(), 0, 180);
         }
         else if(System.currentTimeMillis() < timeToStop - 750){
            pac.drawDying(g, pac.getX(), pac.getY(), -22, 225);
         }
         else if(System.currentTimeMillis() < timeToStop - 500){
            pac.drawDying(g, pac.getX(), pac.getY(), -45, 270);
         }
         else if(System.currentTimeMillis() < timeToStop - 250){
            pac.drawDying(g, pac.getX(), pac.getY(), -68, 316);
         }
         else if(System.currentTimeMillis() < timeToStop){
            pac.drawDying(g, pac.getX(), pac.getY(), -90, 360); 
         }
         else if(System.currentTimeMillis() >= timeToStop){
            g.setColor(Color.BLACK);
            g.fillOval(pac.getX() - 16, pac.getY() - 16, 32, 32);
            
            if(gameEnds){
               pac.drawDying(g, pac.getX(), pac.getY(), -90, 360);
               g.setColor(Color.RED.darker());
               g.setFont(new Font("Monospaced", Font.BOLD, 30));
               g.drawString("GAME   OVER", 237, 429);
               
               p.updateUI();
               
               if(canEnterScore){
                  canEnterScore = false;
                  try{Thread.sleep(1500);}
                  catch(InterruptedException b){}
               
                  scoreboard.enterHighScore();
               }
            }
            else{
               pac.reset();
               ready.setDuration(System.currentTimeMillis() + 2000);
               try{Thread.sleep(500);}
               catch(InterruptedException b){}
               intro.start();
               collider.stop();
            }
         }
         p.updateUI();
      }
   }


  /**
  Updates current state of board to the next frame available. 
  If the width of PacMan's mouth has reached a certain value - either too large
  or too small - the width is set to decrease or increase. 
  The background is drawn, and PacMan moves one frame and is then redrawn.
  If any ghost is dead and sound is wanted, the siren and frightened sound is
  stopped and the dead ghost sound is started, if it has not started already.
  If PacMan has collided with a power pellet, all ghosts are set to be
  frightened and to slow down. The counter of pellets collected since a new
  level and since a new level or the player has died is increased. If sound is
  wanted, he siren sound is stopped, and the frightened sound begins. The score
  is increased by 50. 
  If PacMan has collided with a pac-dot, the counter of pellets collected since
  a new level and since a new level or the player has died is increased. The
  score is increased by 10.
  If PacMan has collided with a pac-dot or is in the path of collecting pellets
  and the sound has not previously been started, the pac-dot eating sound
  begins. This only occurs if sound is wanted.
  If all pellets have been collected, the level is reset as a new level. 
  The pellets are drawn. 
  If the pellets collected since a new level is 70 or 170, a fruit appears in
  the center. If the fruit is being drawn, it is checked for collisions. If
  collided with PacMan, the score is increased by the amount specific to that
  type of fruit, and that score is displayed as the animation is paused for a 
  second. If sound is wanted, the fruit sound is also played.
  If the duration has ended, the fruit is not drawn and not checked for 
  collisions. 
  Each ghost is then checked for various characteristics determining their 
  behavior. If a ghost is dead, it moves one frame towards the center of the
  ghost house, where it is set as alive once reached. If a ghost is frightened,
  it checks for collisions with PacMan, where if it is collided with it becomes
  dead and its speed increases drastically. Otherwise, the ghost moves one 
  frame,where it randomly chooses a path to take at each intersection. If a 
  ghost is within the ghost house, parameters are checked to see if the ghost 
  can begin to leave the house. If not met, the ghost bounces back and forth 
  within the house, moving one frame an up or down direction. If met, the ghost
  moves a frame in the process of leaving the ghost house. If none of these
  conditions are met, the ghost moves normally according to its particular AI. 
  If PacMan collides with a ghost, the level is reset as the scenario of a death
  of PacMan. 
  If there isless than two seconds until the ghosts are not frightened any more,
  the ghosts are drawn, alternating between blue and white, once every eighth of
  a second. If the stopping point has been reached of when the ghosts are not
  frightened once more, their speed is set to normal and the siren noise begins,
  if sound is wanted, as the frightened sound ends. 
  The width of PacMan's mouth is either increased or decreased, depending on
  whether it is currently increasing or decreasing.
  */

   private void drawBoard()
   {
      if(chompWidth == 0)
         chompDecreasing = false;
      if(chompWidth == 12)
         chompDecreasing = true;
         
      drawBackground(false);
      
      pac.move(maze);
      pac.drawMe(g, chompWidth);
      
      if(sound){
         if(blinky.isDead() || pinky.isDead() || inky.isDead() || clyde.isDead()){
            siren.stop();
            frightened.stop();
            if(deadGhost == null || !deadGhost.isRunning()){
               try{
                  AudioInputStream deadAIS = AudioSystem.getAudioInputStream(new File("sounds\\dead.wav").getAbsoluteFile());
                  deadGhost = AudioSystem.getClip();
                  deadGhost.open(deadAIS);
                  deadGhost.start();
               }
               catch(Exception e){}
            }
         }
      }
      
      
      
      for(int f = 0; f < pellet.length; f++){
         if(f < powerPellet.length)
            if(powerPellet[f].collide(pac,scoreboard)){
               ghostsEaten = 0;
               blinky.setFrightened(true, blinkyInCorridor);
               pinky.setFrightened(true, pinkyInCorridor);
               inky.setFrightened(true, inkyInCorridor);
               clyde.setFrightened(true, clydeInCorridor);
               frightenedDuration = System.currentTimeMillis() + 6000;
               pelletsCollectedSinceDeath++;
               allPelletsCollected++;
               
               if(sound){
                  try{
                     AudioInputStream frightAIS = AudioSystem.getAudioInputStream(new File("sounds\\frightened.wav").getAbsoluteFile());
                     frightened = AudioSystem.getClip();
                     frightened.open(frightAIS);
                  }
                  catch(Exception e){}
                  frightened.start();
                  siren.stop();
               }
            }
         if(pellet[f].collide(pac, scoreboard)){
            pelletsCollectedSinceDeath++;
            allPelletsCollected++;
            if(sound && !collecting){
               eatSound.start();
               collecting = true;
            }
         }
      }
      if( pac.inDirectionOfPellets(pellet, powerPellet, maze) && sound){
         collecting = false;
         eatSound.stop();
      }
      
      drawPellets();
      
      if(allPelletsCollected ==  70 || allPelletsCollected == 170){
         fruitTimer = System.currentTimeMillis() + 10000;
         fruit.setEaten(false);
      }
      
      if(fruitTimer >= System.currentTimeMillis() && !fruit.isEaten() ){
         if(fruit.drawMe(g, pac, scoreboard)){
            int fruitscore = 0;
            switch(fruit.getType()){
               case 1:
                  fruitscore = 100;
                  break;
               case 2:
                  fruitscore = 300;
                  break;
               case 3:
                  fruitscore = 500;
                  break;
               case 4:
                  fruitscore = 700;
                  break;
               case 5:
                  fruitscore = 1000;
                  break;
            }
            
            if(sound){
               try{
                  AudioInputStream fruitAIS = AudioSystem.getAudioInputStream(new File("sounds\\eatfruit.wav").getAbsoluteFile());
                  fruitS = AudioSystem.getClip();
                  fruitS.open(fruitAIS);
               }
               catch(Exception e){}
               fruitS.start();
            
               siren.stop();
            }
            
            animation.stop();
            fruitTimer = 0;
            frightenedDuration += 1000;
            pauseListener.setCoordinates(336, 420, fruitscore, new Ghost(), System.currentTimeMillis() + 1000);
            pause.start();
         
         }
      }
      else
         fruit.setEaten(true);
      
      if(blinky.isDead() || blinkyGoesUp || !blinkyInCorridor){
         if((blinky.getX() == 324 || blinky.getX() == 348) && blinky.getY() == 276 && !blinkyGoesUp && !blinkyGoesDown){
            blinky.setX(336);
            blinky.setDirection(4);
            blinkyInCorridor = false;
            blinkyGoesDown = true;
         }
         else if(blinky.getX() == 336 && blinky.getY() == 276 && blinkyGoesUp){
            blinky.setDirection(1);
            blinky.setX(blinky.getX() - 4);
            if(!blinky.isFright())
               blinky.setChange(8);
            blinkyInCorridor = true;
            blinkyGoesUp = false;
         }
         else if(blinky.getX() == 336 && blinky.getY() == 348 && blinkyGoesDown){
            blinky.setDirection(2);
            blinky.isAlive();
            if(sound){
               if(pinky.isFright() || inky.isFright() || clyde.isFright())
                  frightened.start();
               else
                  siren.start();
               deadGhost.stop();
            }
            blinkyGoesUp = true;
            blinkyGoesDown = false;
         }
         else if(blinkyGoesDown)
            blinky.setY(blinky.getY() + 6);
         else if(blinkyGoesUp)
            blinky.setY(blinky.getY() - 6);
         else
            blinky.moveHome(maze);
         if(blinky.isDead())
            blinky.drawDead(g);
         else
            blinky.drawMe(g);
      }
      else if(blinky.isFright()){
         blinky.moveFrightened(maze);
         if(blinky.collideGhost(pac, scoreboard, ghostsEaten, sound)){
            ghostsEaten++;
            animation.stop();
            if(sound)
               eatSound.stop();
            frightenedDuration += 1000;
            fruitTimer += 1000;
            pauseListener.setCoordinates(blinky.getX(), blinky.getY(), (int) (Math.pow(2, ghostsEaten) * 100), blinky, System.currentTimeMillis() + 1000);
            pause.start();
         }
         blinky.drawBlue(g);
      }
      else{
         blinky.moveBlinky(pac, maze);
         blinky.drawMe(g);
         if(blinky.collideGhost(pac, scoreboard, ghostsEaten, sound))
            reset(false);
      }
       
       
       
      if(pinky.isDead() && pinkyInCorridor){
         pinky.moveHome(maze);
         if((pinky.getX() == 324 || pinky.getX() == 336) && pinky.getY() == 276){
            pinky.setX(336);
            pinkyInCorridor = false;
            pinky.setDirection(4);
         }
         pinky.drawDead(g);
      }
      else if(pinkyInCorridor && pinky.isFright()){
         pinky.moveFrightened(maze);
         if(pinky.collideGhost(pac, scoreboard, ghostsEaten, sound)){
            ghostsEaten++;
            animation.stop();
            if(sound)
               eatSound.stop();
            frightenedDuration += 1000;
            fruitTimer += 1000;
            pauseListener.setCoordinates(pinky.getX(), pinky.getY(), (int) (Math.pow(2, ghostsEaten) * 100), pinky, System.currentTimeMillis() + 1000);
            pause.start();
         }
         pinky.drawBlue(g);
      }
      else{
         if(pinkyInCorridor){
            pinky.movePinky(pac, maze);
            pinky.drawMe(g);
            if(pinky.collideGhost(pac, scoreboard, ghostsEaten, sound))
               reset(false);
         }
         else if(pinky.getDirection() == 4 && pinky.isDead()){
            pinky.setY(pinky.getY() + 6);
            if(pinky.getY() == 348){
               pinky.setDirection(2);
               pinkyGoesUp = true;
               pinky.isAlive();
               if(sound){
                  if(blinky.isFright() || inky.isFright() || clyde.isFright())
                     frightened.start();
                  else
                     siren.start();
                  deadGhost.stop();
               }
            }
         }
         else if(died && !pinkyGoesUp){
            pinky.bounceInHouse();
            if(pelletsCollectedSinceDeath == 7){
               pinky.setDirection(2);
               pinkyGoesUp = true;
            }
         }
         else if(pinkyGoesUp){
            pinky.setY(pinky.getY() - 6);
            if(pinky.getY() == 276){
               pinkyInCorridor = true;
               pinkyGoesUp = false;
               if(!pinky.isFright())
                  pinky.setChange(8);
               pinky.setDirection(1);
               pinky.setX(pinky.getX() - 4);
            }
         }
          
         if(pinky.isDead())
            pinky.drawDead(g);  
         else if(pinky.isFright())
            pinky.drawBlue(g);
         else
            pinky.drawMe(g);
      }
      
      if(inky.isDead() && inkyInCorridor){
         inky.moveHome(maze);
         if((inky.getX() == 324 || inky.getX() == 348) && inky.getY() == 276){
            inky.setX(336);
            inkyInCorridor = false;
            inky.setDirection(4);
         }
         inky.drawDead(g);
      }
      else if(inkyInCorridor && inky.isFright()){
         inky.moveFrightened(maze);
         if(inky.collideGhost(pac, scoreboard, ghostsEaten, sound)){
            ghostsEaten++;
            animation.stop();
            if(sound)
               eatSound.stop();
            frightenedDuration += 1000;
            fruitTimer += 1000;
            pauseListener.setCoordinates(inky.getX(), inky.getY(), (int) (Math.pow(2, ghostsEaten) * 100), inky, System.currentTimeMillis() + 1000);
            pause.start();
         }
         inky.drawBlue(g);
      }
      else if(inkyInCorridor){
         inky.moveInky(pac, blinky, maze);
         inky.drawMe(g);
         if(inky.collideGhost(pac, scoreboard, ghostsEaten, sound))
            reset(false);
      }
      else{
         if(inky.getX() == 288)
            inky.bounceInHouse();
         
         if(died && inkyNoReturn){
            if(pelletsCollectedSinceDeath == 17){
               inky.setDirection(3);
               inkyNoReturn = false;
            }
         }
         else if(!died && inkyNoReturn){
            if(level == 1){
               if(pelletsCollectedSinceDeath == 30){
                  inky.setDirection(3);
                  inkyNoReturn = false;
               }
            }
            else if(inky.getY() == 336){
               inky.setDirection(3);
               inkyNoReturn = false;
            } 
         }
         else if(inky.getDirection() == 4){
            inky.setY(inky.getY() + 6);
            if(inky.getY() == 348){
               inky.setDirection(2);
               inky.isAlive();
               if(sound){
                  if(blinky.isFright() || pinky.isFright() || clyde.isFright())
                     frightened.start();
                  else
                     siren.start();
                  deadGhost.stop();
               }
            }
         }
         else if(inky.getDirection() == 3){
            inky.setX(inky.getX() + 6);
            if(inky.getX() == 336)
               inky.setDirection(2);
         }
         else if(inky.getDirection() == 2){
            inky.setY(inky.getY() - 6);
            if(inky.getY() == 276){
               inkyInCorridor = true;
               inky.setDirection(1);
               if(!inky.isFright())
                  inky.setChange(8);
               inky.setX(inky.getX() - 4);
            }
         }
         if(inky.isDead())
            inky.drawDead(g);  
         else if(inky.isFright())
            inky.drawBlue(g);
         else
            inky.drawMe(g);
      }
         
         
         
      if(clyde.isDead() && clydeInCorridor){
         clyde.moveHome(maze);
         if((clyde.getX() == 324 || clyde.getX() == 348) && clyde.getY() == 276){
            clyde.setX(336);
            clydeInCorridor = false;
            clyde.setDirection(4);
         }
         clyde.drawDead(g);
      }
      else if(clydeInCorridor && clyde.isFright()){
         clyde.moveFrightened(maze);
         if(clyde.collideGhost(pac, scoreboard, ghostsEaten, sound)){
            ghostsEaten++;
            animation.stop();
            if(sound)
               eatSound.stop();
            frightenedDuration += 1000;
            fruitTimer += 1000;
            pauseListener.setCoordinates(clyde.getX(), clyde.getY(), (int) (Math.pow(2, ghostsEaten) * 100), clyde, System.currentTimeMillis() + 1000);
            pause.start();
         }
         clyde.drawBlue(g);
      }
      else if(clydeInCorridor){
         clyde.moveClyde(pac, maze);
         clyde.drawMe(g);
         if(clyde.collideGhost(pac, scoreboard, ghostsEaten, sound))
            reset(false);
      }
      else{
         if(clyde.getX() == 384)
            clyde.bounceInHouse();
            
         if(died && clydeNoReturn){
            if(pelletsCollectedSinceDeath == 32){
               clyde.setDirection(1);
               clydeNoReturn = false;
            }
         }
         else if(!died && clydeNoReturn){
            if(level == 1){
               if(pelletsCollectedSinceDeath == 90){
                  clyde.setDirection(1);
                  clydeNoReturn = false;
               }
            }
            else if(level == 2){
               if(pelletsCollectedSinceDeath == 50){
                  clyde.setDirection(1);
                  clydeNoReturn = false;
               }
            } 
            else if(clyde.getY() == 336){
               clyde.setDirection(1);
               clydeNoReturn = false;
            } 
         }
         else if(clyde.getDirection() == 4){
            clyde.setY(clyde.getY() + 6);
            if(clyde.getY() == 348){
               clyde.setDirection(2);  
               if(sound){
                  if(blinky.isFright() || pinky.isFright() || inky.isFright())
                     frightened.start();
                  else
                     siren.start();
                  deadGhost.stop();
               }
               clyde.isAlive();
            }
         }
         else if(clyde.getDirection() == 1){
            clyde.setX(clyde.getX() - 6);
            if(clyde.getX() == 336)
               clyde.setDirection(2);
         }
         else if(clyde.getDirection() == 2){
            clyde.setY(clyde.getY() - 6);
            if(clyde.getY() == 276){
               clydeInCorridor = true;
               clyde.setDirection(1);
               if(!clyde.isFright())
                  clyde.setChange(8);
               clyde.setX(clyde.getX() - 4);
            }
         }
         if(clyde.isDead())
            clyde.drawDead(g);  
         else if(clyde.isFright())
            clyde.drawBlue(g);
         else
            clyde.drawMe(g);
      }
      
   
      
      for(long x = frightenedDuration - 2000; x <= frightenedDuration - 125; x += 125)
      {
         if(System.currentTimeMillis() > x && System.currentTimeMillis() < x + 125){
            if(((x - frightenedDuration) * -1) % 250 == 0){
               if(blinky.isFright() && !blinky.isDead()){
                  blinky.drawWhite(g);
               }
               if(pinky.isFright() && !pinky.isDead())
                  pinky.drawWhite(g);
               if(inky.isFright() && !inky.isDead())
                  inky.drawWhite(g);
               if(clyde.isFright() && !clyde.isDead())
                  clyde.drawWhite(g);
            }
            else{
               if(blinky.isFright() && !blinky.isDead())
                  blinky.drawBlue(g);
               if(pinky.isFright() && !pinky.isDead())
                  pinky.drawBlue(g);
               if(inky.isFright() && !inky.isDead())
                  inky.drawBlue(g);
               if(clyde.isFright() && !clyde.isDead())
                  clyde.drawBlue(g);
            }
            updateUI();
         }
      }
      repaint();
      
      if(allPellets(pellet, powerPellet))
         reset(true);
           
      if(frightenedDuration <= System.currentTimeMillis() && (blinky.isFright() || pinky.isFright() || inky.isFright() || clyde.isFright()) ){
         blinky.setFrightened(false, blinkyInCorridor);
         pinky.setFrightened(false, pinkyInCorridor);
         inky.setFrightened(false, inkyInCorridor);
         clyde.setFrightened(false, clydeInCorridor);
         ghostsEaten = 0;
         if(sound){
            frightened.stop();
            if(!(blinky.isDead() || pinky.isDead() || inky.isDead() || clyde.isDead()))
               siren.start();
         }
      }
      
      if(pac.canGo(pac.getDirection(), maze))
         if(chompDecreasing)
            chompWidth -= 3;
         else
            chompWidth += 3;
   }
}