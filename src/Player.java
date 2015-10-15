import static java.lang.Math.abs;

import java.awt.image.*;
import java.awt.*;
import java.util.ArrayList;

public class Player {
  int dir,anim,shootDir,shootX,shootY,maxHealth,maxMana,curHealth,curMana,armor;  //ShootX/Y dont do anything
  double spd,atkSpd;
  long upMove,upAnim;
  
  ItemStorage inventory;
  Inventory equipment;
  String name = "Awesome Wizard Guy";
  Point loc;
  Attack atk;
  BufferedImage sprites[][] = new BufferedImage[4][3],def,curSprite; //holds sprites for directions
  
  public Player(double spd) {
    this.spd = spd;  //sets how fast the character walks
    atkSpd = 0.8;
    sprites[0] = new BufferedImage[4];
    sprites[1] = new BufferedImage[4];
    equipment = new Inventory();
    inventory = new ItemStorage(6,5);
    atk = new Attack(0.5,0,10);
    dir = anim = armor = 0;
    shootDir = shootX = shootY = -1;
    maxHealth = maxMana = curHealth = curMana = 100;
    calculateStats();
  }
  public Point getLoc() {
    return loc;
  }
  public void setLoc(int x,int y) {
    loc = new Point(x,y);
  }
  public void setUpSprite(BufferedImage bi) {  //sets the character sprites
    sprites[0][0] = bi;
  }
  public void setDownSprite(BufferedImage bi) {
    sprites[1][0] = bi;
  }
  public void setLeftSprite(BufferedImage bi) {
    sprites[2][0] = bi;
  }
  public void setRightSprite(BufferedImage bi) {
    sprites[3][0] = bi;
  }
  public void setUpMove1Sprite(BufferedImage bi) {
    sprites[0][1] = bi;
  }
  public void setUpMove2Sprite(BufferedImage bi) {
    sprites[0][2] = bi;
  }
  public void setDownMove1Sprite(BufferedImage bi) {
    sprites[1][1] = bi;
  }
  public void setDownMove2Sprite(BufferedImage bi) {
    sprites[1][2] = bi;
  }
  public void setLeftMoveSprite(BufferedImage bi) {
    sprites[2][1] = bi;
  }
  public void setRightMoveSprite(BufferedImage bi) {
    sprites[3][1] = bi;
  }
  public void setUpAttackSprite(BufferedImage bi) {
    sprites[0][3] = bi;
  }
  public void setDownAttackSprite(BufferedImage bi) {
    sprites[1][3] = bi;
  }
  public void setLeftAttackSprite(BufferedImage bi) {
    sprites[2][2] = bi;
  }
  public void setRightAttackSprite(BufferedImage bi) {
    sprites[3][2] = bi;
  }
  public void setDefaultSprite(BufferedImage bi) {
    def = bi;
  }
  public void setCurrentSprite(BufferedImage bi) {
    curSprite = bi;
  }
  public void setAttack(Attack atk) {  //sets character's attack
    this.atk = atk;
  }
  public void shootDirection(int n,int m) {  //sets shootDirection
    int x = 400 - n,y = 400 - m;
    if (abs(x) <= y) shootDir = 0;  //shootDirection used to display the correct attack sprite
    else if (abs(x) <= y * -1) shootDir =  1;
    else if (x > abs(y)) shootDir = 2;
    else if (-1 * x > abs(y)) shootDir = 3;
    shootX = n;
    shootY = m;
  }
  public void useItem(int y,int x) {  //uses an item
    Item temp = inventory.itemAt(x,y);
    if (temp == null) return;
    else if (temp.itemID < 5) {  //equips equipnent
      inventory.setItem(x, y, equipment.itemAt(0,temp.itemID));
      equipment.setItem(0, temp.itemID, temp);
      calculateStats();  //recalculates player stats
    }
    else if (temp.itemID == 5) { //uses health potion
      inventory.setItem(x, y, null);
      healHealth((int)(80 + maxHealth * 0.2)); 
    }
    else if (temp.itemID == 6) {  //uses mana potion
      inventory.setItem(x, y, null);
      healMana((int)(80 + maxMana * 0.2));
    }
  }
  public void useHealthPotion() {
    boolean b = false;
    for (int i = 0;i < 6;i++) {
      for (int j = 0;j < 5;j++) {
        if (inventory.itemAt(i, j) != null && inventory.itemAt(i, j).itemID == 5) {
          inventory.setItem(i, j, null);
          healHealth((int)(80 + maxHealth * 0.2)); 
          break;
        }
      }
      if (b) break;
    }
  }
  public void useManaPotion() {
    boolean b = false;
    for (int i = 0;i < 6;i++) {
      for (int j = 0;j < 5;j++) {
        if (inventory.itemAt(i, j) != null && inventory.itemAt(i, j).itemID == 6) {
          inventory.setItem(i, j, null);
          healMana((int)(80 + maxMana * 0.2));
          break;
        }
      }
      if (b) break;
    }
  }
  public void healMana(int n) {  //heals mana
    curMana = curMana + n > maxMana ? maxMana : curMana + n;
  }
  public void healHealth(int n) {  //heals health
    curHealth = curHealth + n > maxHealth ? maxHealth : curHealth + n;
  }
  public void calculateStats() {  //calculates stats based on equipped items
    if (equipment.itemAt(0,0) != null) atk.damage = equipment.itemAt(0, 0).value1;
    if (equipment.itemAt(0,1) != null) atkSpd = ((double)equipment.itemAt(0, 1).value1 / 100.0 + 1) * 0.8;
    if (equipment.itemAt(0,2) != null) {
      armor = equipment.itemAt(0, 2).value1;
      maxHealth = 100 + equipment.itemAt(0, 2).value2;
    }
    if (equipment.itemAt(0,3) != null) {
      armor += equipment.itemAt(0, 3).value1;
      maxMana = 100 + equipment.itemAt(0, 3).value2;
    }
    if (equipment.itemAt(0,4) != null) {
      maxHealth += equipment.itemAt(0, 4).value1;
      atk.damage += equipment.itemAt(0, 4).value2;
    }
  }
  public boolean hit(Bullet b) {
    if (b.ally == 0) return false; //if the bullet was by the player
    long time = System.currentTimeMillis();
    int t = (int)(time - b.startTime);
    if (b.loc.x + b.x * t >= loc.x - sprites[0][0].getWidth() / 2 && b.loc.x + b.x * t <= loc.x + sprites[0][0].getWidth() / 2 &&
        b.loc.y + b.y * t >= loc.y - sprites[0][0].getHeight() / 2 && b.loc.y + b.y * t <= loc.y + sprites[0][0].getHeight() / 2) {  //if the bullets current location is on the player
      curHealth -= b.damage - armor;  //player takes damage
      if (curHealth < 0) curHealth = 0;
      return true;
    }
    return false;
  }
  public String toString() {  //returns the player's stats in a string
    return atk.damage + " attack + " + atkSpd + " attack speed + "+ armor + " armor + " + maxHealth + " hp + " + maxMana + " mana";
  }
}
