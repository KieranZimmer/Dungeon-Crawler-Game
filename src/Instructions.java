import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class Instructions {
  public static void draw(Graphics2D g) {  //Draws the instructions page
    g.setColor(Color.black);
    g.fillRect(0,0,1144,800);
    g.setColor(new Color(100,100,100,100));
    g.fillRect(380, 665, 400, 50);
    g.fillRect(380, 740, 400, 50);
    g.setColor(new Color(180,0,0));
    g.setFont(new Font("Helvetica", Font.PLAIN, 30));
    g.drawString("Play Game",517,700);
    g.drawString("Exit",560,775);
    g.setColor(Color.white);
    g.drawString("Welcome to my Dungeon Crawler game.  This game is composed of", 50, 50);
    g.drawString("infinite levels of increasing difficulty.  You navigate a dungeon", 50, 75);
    g.drawString("filled with monsters who drop loot for you to use.  Killing the", 50, 100);
    g.drawString("boss of each dungeon level unlocks the next. To get to the next", 50, 125);
    g.drawString("level, walk onto the dungeon entrance in the boss' room.", 50, 150);
    //
    g.drawString("Controls:", 50, 200);
    g.drawString("WASD or arrow keys to move", 50, 225);
    g.drawString("Mouse left click to shoot", 50, 250);
    g.drawString("Mouse right click to use or equip inventory items", 50, 275);
    g.drawString("F to use a health potion, V to use a mana potion", 50, 300);
    //
    g.drawString("Loot from monsters will automatically be placed in your inventory.", 50, 350);
    g.drawString("To see the item, mouseover it.  To equip it, right click.", 50, 375);
    g.drawString("Armor values decrease damage by that value.", 50, 400);
    g.drawString("Attack values increase damage by that value.", 50, 425);
    g.drawString("You cannot attack without mana. If you run out, use a potion.", 50, 450);
  }
}
