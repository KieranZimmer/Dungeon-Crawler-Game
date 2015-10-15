import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu {  //draws the menu
  static BufferedImage background;
  static {
    try {
        background = ImageIO.read(new File("Game Background.jpg"));
    } catch (IOException e) {System.out.println("Could not load background image for menu");}
  }
  public static void draw(Graphics2D g) {
    g.drawImage(background,0,0,null);
    g.setColor(new Color(100,100,100,100));
    g.fillRect(380, 665, 400, 50);
    g.fillRect(380, 740, 400, 50);
    g.setColor(new Color(180,0,0));
    g.setFont(new Font("Helvetica", Font.PLAIN, 30));
    g.drawString("Play Game",517,700);  //buttons for playing/instructions
    g.drawString("Instructions",515,775);
  }
}
