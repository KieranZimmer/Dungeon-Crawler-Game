import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.*;

public class Monster {
  double spd = 2.5;
  int health,level,updateMove,updateShoot;
  boolean boss;
  private Random rand = new Random();
  
  BufferedImage sprite;
  Point loc;
  Attack atk;
  static SpriteSheet monsters = new SpriteSheet("Game Sprites 2.png");  //spritesheet used to get monster sprites
  
  public Monster(boolean boss,int level,Point p,BufferedImage monster,BufferedImage attack) {
    int m = 0;
    for (int i = level - 1;i > 0;i--)
      m += i;
    if (!boss) {
      health = 30 + 10 * (level - 1) + m * 5; //scales boss health
      atk = new Attack(0.4,1,10 + 5 * (level - 1));  //scales boss attack
    }
    else {
      health = 100 + 30 * (level - 1) + m * 10;  //scales monster health
      atk = new Attack(0.5,1,20 + 5 * (level - 1));  //scales monster attack
    }
    this.level = level - 1;
    atk.bullet = attack;
    loc = p;
    sprite = monster;
    this.boss = boss;
    updateMove = updateShoot = 0;
    MonsterSpawner.monsterList.add(this);  //adds the monster to the monster Arraylist
  }
  public void shoot(Point p) {
    if (loc.distance(p) < 600) {  //if the monster is close enough to the player
      atk.bullet((int)p.x, (int)p.y, loc);  //monster attacks
    }
  }
  public void move(Point p,Map map) {
    if (loc.distance(p) < 600) {  //if the monster is close enough to the player
      double x,y;
      x = p.x - loc.x;  //move towards player
      y = p.y - loc.y;
      if (abs(x) > spd) {
        x = x > 0 ? spd : -1 * spd;  //reduce movement to maximum speed
      }
      if (abs(y) > spd) {
        y = y > 0 ? spd : -1 * spd;  //reduce movement to maximum speed
      }
      loc = map.walk(loc,x,y);  //move monster
    }
  }
  public boolean hit(Bullet b) {
    if (b.ally == 1) return false;  //if the bullet came from another monster
    long time = System.currentTimeMillis();
    int t = (int)(time - b.startTime);
    if (b.loc.x + b.x * t >= loc.x - sprite.getWidth() / 2 && b.loc.x + b.x * t <= loc.x + sprite.getWidth() / 2 &&
        b.loc.y + b.y * t >= loc.y - sprite.getHeight() / 2 && b.loc.y + b.y * t <= loc.y + sprite.getHeight() / 2) {  //if the current location of the bullet is on a monster
      health -= b.damage;  //monster takes damage
      return true;
    }
    return false;
  }
  public void draw(Graphics2D g, Point p) {
    g.drawImage(sprite,(int)(400 + loc.x - p.x - sprite.getWidth() / 2),(int)(400 + loc.y - p.y - sprite.getHeight() / 2),null);  //draw monster relative to player
  }
}
