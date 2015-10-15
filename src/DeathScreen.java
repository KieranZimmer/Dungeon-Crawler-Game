import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class DeathScreen { //draws the game over screen
  static int alpha = 255;
  public static void draw(Graphics2D g,Map map) {
    /*if (alpha > 0) {
      g.setColor(new Color(0,0,0,alpha));
      alpha--;
      g.fillRect(0,0,1144,800);
    }*/
    g.setColor(Color.black);
    g.fillRect(0,0,1144,800);
    g.setColor(new Color(180,0,0));
    g.setFont(new Font("Helvetica", Font.BOLD, 100));
    g.drawString("GAME OVER",275,200);
    g.setColor(Color.white);
    g.setFont(new Font("Default", Font.PLAIN, 30));
    g.drawString("You made it to level " + map.level + " before dying",350,500);
    g.drawString("Thank you for playing!",420,550);
    long t = System.currentTimeMillis();
    g.setColor(new Color((int)(t % 255),(int)((t + 50) % 255),(int)((t + 100) % 255)));
    g.drawString("Press any key to return to menu",365,750);
  }
}
