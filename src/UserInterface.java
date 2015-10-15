import java.awt.*;


public class UserInterface {
  public static void drawInterface(Graphics2D g, Player p,int level) {  //draws the interface
    g.setColor(new Color(124,66,0));
    g.fillRect(800,0,344,800);  
    drawStats(g,p);
    g.drawString(p.name,972 - (p.name.length() / 2) * 8,50);
    g.drawString("Dungeon Level " + level, 916, 70);
    p.inventory.draw(822,280,g);  //draws equipment and inventory
    p.equipment.draw(822,200,g);
    g.setColor(new Color(100,100,100));
    g.fillRect(1050, 725, 50, 50);
    g.setColor(Color.red);
    g.drawString("Delete", 1052, 754);
  }
  public static void drawStats(Graphics2D g,Player p) {
    g.setColor(Color.black);
    g.fillRect(822,100,300,20);  //draws health bars
    g.fillRect(822,130,300,20);
    g.setColor(Color.red);
    g.fillRect(822,100,(int)(300 * p.curHealth / p.maxHealth),20);
    g.setColor(Color.blue);
    g.fillRect(822,130,(int)(300 * p.curMana / p.maxMana),20);
    g.setColor(Color.white);
    g.setFont(new Font("SansSerif", Font.BOLD, 15));
    g.drawString("HP",824,115);  //draws stats
    g.drawString(p.curHealth + "/" + p.maxHealth,850,115);
    g.drawString("MP",824,145);
    g.drawString(p.curMana + "/" + p.maxMana,850,145);
    g.drawString("Armor: " + p.armor,822,180);
    g.drawString("Damage: " + p.atk.damage,972,180);
  }
}
