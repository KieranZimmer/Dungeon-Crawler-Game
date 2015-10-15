import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class IntroScreen {  //draws the introductory screen
  public static void draw(Graphics2D g) {
    g.setColor(Color.black);
    g.fillRect(0,0,1144,800);
    g.setColor(new Color(180,0,0));
    g.setFont(new Font("Helvetica", Font.BOLD, 75));
    g.drawString("INFINITE DUNGEON CRAWLER",15,200);
    g.setColor(Color.white);
    g.setFont(new Font("Default", Font.PLAIN, 30));
    g.drawString("Welcome to",485,100);
    g.drawString("By Kieran Zimmer",450,400);
    g.drawString("for Grade 11 Computer Science",360,450);
    long t = System.currentTimeMillis();
    g.setColor(new Color((int)(t % 255),(int)((t + 50) % 255),(int)((t + 100) % 255)));
    g.drawString("Press any key to return to continue",350,750);
  }
}
