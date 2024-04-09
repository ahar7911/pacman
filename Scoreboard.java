import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

/**
The Scoreboard class displays the player’s current score and highscore at the
top of the screen, the player’s lives, in the form of multiple PacMans on
the bottom left of the screen, the fruit collected in the bottom right of
the screen, and a PacManPanel object, or the actual game, in the center of
the Panel.
@author Aidan Harbison
@since 5-29-2018
*/
public class Scoreboard extends JPanel
{
   /** PacManPanel object that will display the game. */
   private PacManPanel p;
   /** Array of visual representation of lives and fruits on bottom of
   the scoreboard. */
   private LifePanel[] lives;
   /** Displays current score of the player. */
   private JLabel scoring;
   /** Displays current highscore of the game since first played */
   private JLabel highscore;
   /** Counter for current score. */
   private int score = 0;
   /** Counter for current highscore. */
   private int hscore;
   /** Result from the asking of player for sound; 0 represents Yes and 1
   represents No. */
   private int sound;
   
   /**
   Instantiates a Scoreboard object, with a subpanel in the north to hold 
   JLabels to display the highscore (read from a text file) and current score,
   and an array of fruit/lives panels in the south to display current lives
   and all collected fruits. JLabels are instantiated and displayed in the top.
   An array of LifePanels are displayed along the bottom, and the leftmost three
   are set to display a life. The player is asked if they would like sound, and a
   PacManPanel is instantiated to be held in the center using that information.
   @throws Exception removes complication of scanning text files.
   */
   public Scoreboard() throws Exception
   {
      setLayout(new BorderLayout());
      
      Scanner i = new Scanner(new File("leaderboards.txt"));
      i.nextLine();
      
      JPanel north = new JPanel();
      add(north,  BorderLayout.NORTH);
      north.setLayout(new GridLayout(2, 3));
      north.setBackground(Color.BLACK);
      north.setSize(900, 100);
      
      Font f = new Font("Monospaced", Font.BOLD, 20);
      
      JLabel oneup = new JLabel();
      oneup.setForeground(Color.WHITE);
      oneup.setFont(f);
      north.add(oneup);
      
      JLabel h = new JLabel("HIGHSCORE", SwingConstants.CENTER);
      h.setFont(f);
      h.setForeground(Color.WHITE);
      north.add(h);
      
      north.add(new JLabel());
      
      scoring = new JLabel("   00", SwingConstants.CENTER);
      scoring.setFont(f);
      scoring.setForeground(Color.WHITE);
      north.add(scoring);
      
      hscore = i.nextInt();
      highscore = new JLabel("" + hscore, SwingConstants.CENTER);
      highscore.setFont(f);
      highscore.setForeground(Color.WHITE);
      north.add(highscore);
      
      JPanel south = new JPanel();
      south.setLayout(new GridLayout(1, 18));
      south.setBackground(Color.BLACK);
      south.setPreferredSize(new Dimension(900, 40));
      
      lives = new LifePanel[18];
      for(int x = 0; x < lives.length; x++){
         if(x < 3)
            lives[x] = new LifePanel(true);
         else
            lives[x] = new LifePanel(false);
         lives[x].setSize(50, 50);
         south.add(lives[x]);
      }
      add(south, BorderLayout.SOUTH);
      
      JPanel west = new JPanel();
      west.setBackground(Color.BLACK);
      west.setPreferredSize(new Dimension(5, 744));
      add(west, BorderLayout.WEST);
      
      JPanel east = new JPanel();
      east.setBackground(Color.BLACK);
      east.setPreferredSize(new Dimension(5, 744));
      add(east, BorderLayout.EAST);
      
      sound = JOptionPane.showConfirmDialog(null, "Would you like to play with sound?", "Sound", JOptionPane.YES_NO_OPTION);
      p = new PacManPanel(this, sound == 0);
      add(p, BorderLayout.CENTER);
   }
   
   /**
   Updates the current player's by the given amount, adding a specified
   value, as well as updating the highscore, if needed, and checking
   for new lives.
   @param num  value to be added to the players score.
   */
   public void updateScore(int num)
   {
      score += num;
      scoring.setText("" + score);
      if(score > hscore)
         highscore.setText("" + score);
      checkNewLife();
   }
   
   /** 
   Determines if the player has reached a score of 10,000, 
   where a new life is given to the player and a jingle plays.
   */
   public void checkNewLife()
   {
      if(score == 10000){
         for(int x = 0; x < 4; x++)
            if(!lives[x].isOn()){
               lives[x].setLife(true);
               lives[x].updateUI();
            }
       
         if(sound == 0)
            try{
               AudioInputStream newLifeAIS = AudioSystem.getAudioInputStream(new File("sounds\\newlife.wav").getAbsoluteFile());
               Clip newLife = AudioSystem.getClip();
               newLife.open(newLifeAIS);
               newLife.start();
            } 
            catch(Exception e){}   
      }     
   }
   
   /**
   Adds 10 points to the score once a pellet is collected, and
   adjusts the display of the highscore 
   and current score accordingly.
   */
   public void scorePellet(){
      updateScore(10);
   }
   
   /**
   Adds either 200, 400, 800, or 1600 points to the score once a 
   ghost has been hit, depending on in what order the ghost has 
   been hit, and adjusts the display of the highscore and current 
   score accordingly.
   @param num	        succession of ghost that has been hit
   */
   public void scoreGhost(int num){
      updateScore((int) (100 * Math.pow(2, num)));
   }
   
   /**
   Adds 50 points to the score once a power pellet is collected, 
   and adjusts the display of the highscore and current score accordingly.
   */
   public void scorePowerPellet(){
      updateScore(50);
   }
   
   /**
   Adds a certain number of bonus points to the score once a fruit is 
   collected depending on its type, and adjusts the display of the 
   highscore and current score accordingly. If the fruit is a cherry,
   100 points are added; for a strawberry, 300 points; for an orange,
   500 points, for an apple, 700; for a melon, 1000.
   @param num  	type of fruit
   */
   public void scoreFruit(int num){
      switch(num){
         case 1:
            updateScore(100);
            break;
         case 2:
            updateScore(300);
            break;
         case 3:
            updateScore(500);
            break;
         case 4:
            updateScore(700);
            break;
         case 5:
            updateScore(1000);
            break;
      }
   }
   
   /**
   Upon hitting the ghost, PacMan loses a life and the one PacMan 
   life on the LifePanel array below disappears. If all lives are 
   lost, the lose method of PacManPanel is called.
   */
   public void hitGhost(){
      for(int x = lives.length - 1; x >= 0; x--){
         lives[x].updateUI();
         if(lives[x].isOn()){
            lives[x].setLife(false);
            if(x == 0)
               p.lose();
            break;
         }
      }
   }
   
   /**
   The current top five scores and their associated names are
   printed in the output box. The player is asked if the would like 
   to enter their score into the leaderboards; if yes, they are asked 
   to enter an 8 character name. The entire rankings are displayed once
   more with their new score and attached name included.
   */
   public void enterHighScore(){
      System.out.println("LEADERBOARDS");
      
      Scanner k = null;
      try{k = new Scanner(new File("leaderboards.txt"));}
      catch(Exception e){System.exit(0);}
      
      int place = 1;
      while(place < 6 && k.hasNext()){
         System.out.println(place + ". " + k.nextLine());
         System.out.println("\t" + k.nextLine());
         place++;
      }
      
      int scoreYN = JOptionPane.showConfirmDialog(null, "Would you like to enter your score into the leaderboards?", "Leaderboard", JOptionPane.YES_NO_OPTION);
      if(scoreYN == 0){
         for(int x = 0; x < 10; x++)
            System.out.println();
         
         String name = JOptionPane.showInputDialog("Enter your name. (8 characters)");
         while(name.length() > 8)
            name = JOptionPane.showInputDialog("Your name is too long. Enter your name in a format less than 8 characters.");
            
         Scanner l = null;
         try{l = new Scanner(new File("leaderboards.txt"));}
         catch(Exception e){System.exit(0);}
         
         String[] names = new String[100];
         int[] scores = new int[100];
         
         place = 1;
         boolean noReturn = true;
         while(l.hasNext()){
            names[place - 1] = l.nextLine();
            scores[place - 1] = Integer.parseInt(l.nextLine());
            
            if(scores[place - 1] < score && noReturn){
               noReturn = false;
               names[place] = names[place - 1];
               scores[place] = scores[place - 1];
               
               names[place - 1] = name;
               scores[place - 1] = score;
               
               System.out.println(place + ". " + names[place - 1]);
               System.out.println("\t" + scores[place - 1]);
               
               System.out.println(place + 1 + ". " + names[place]);
               System.out.println("\t" + scores[place]);
               
               place += 2;
            }
            else{
               System.out.println(place + ". " + names[place - 1]);
               System.out.println("\t" + scores[place - 1]);
               place++;
            }
         }
         if(noReturn){
            System.out.println(place + ". " + name);
            System.out.println("\t" + score);
            names[place - 1] = name;
            scores[place - 1] = score;
         }
         
         PrintStream outfile = null;
         try{outfile = new PrintStream(new File("leaderboards.txt"));}
         catch(Exception e){}
         
         for(int x = 0; x < names.length; x++){
            if(names[x] == null)
               break;
            outfile.println(names[x]);
            outfile.println(scores[x]);
         }
      }
   }
   
   /** The fruit specified is displayed in the bottom right corner
   as part of a LifePanel in its appropriate spot corresponding to
   its type. They are displayed in descending point order from left
   to right.
   @param type type of the fruit being displayed */
   public void showFruit(int type)
   {
      lives[18 - type].setFruit(type);
   }
   
   /** The game is restarted through the instantiation of a new
   PacManPanel with the same parameters as the first,
   which is added to the center of the panel. Lives are reset and
   the score is set to zero. */
   public void restart()
   {
      remove(p);
      try{p = new PacManPanel(this, sound == 0);}
      catch(Exception e){System.exit(0);}
      add(p, BorderLayout.CENTER);
      
      for(int x = 0; x < 3; x++)
         lives[x].setLife(true);
     
      score = 0;
      scoring.setText("" + score);
   }
   
}
