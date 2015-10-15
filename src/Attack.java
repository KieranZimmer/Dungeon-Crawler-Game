import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Attack {
  static ArrayList<Bullet> bulletList = new ArrayList<Bullet>();  //holds all bullets in game
  double spd; //bullet flightspeed
  int damage,ally; //ally refers to who the bullet can damage (cant damage yourself)
  BufferedImage bullet;
  
  public Attack(double spd,int ally,int damage) {
    this.spd = spd;
    this.ally = ally;
    this.damage = damage;
  }
  public void bullet(int endx,int endy,Point p) {
    bulletList.add(new Bullet(bullet,endx,endy,spd,damage,ally,p)); //adds a bullet to the arraylist
  }
}