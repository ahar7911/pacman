import javax.swing.JFrame;

/**
The PacManDriver class runs PacMan, utilizing a frame to hold the
graphical aspects of the game.
@author Ajay Prabhakar
@since 5-29-2018
*/
public class PacManDriver
{
   /**
   The main method runs PacMan, using the construction of a
   JFrame to hold a instance of a Scoreboard.
   @param args  found in all main methods.
   @throws Exception  removes complication of scanning text files.
   */
   public static void main(String[] args) throws Exception
   {
      JFrame frame = new JFrame("CS Final Project: PacMan");
      frame.setSize(750, 1000);
      frame.setLocation(700, 20);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new Scoreboard());
      frame.setVisible(true);
   }
}