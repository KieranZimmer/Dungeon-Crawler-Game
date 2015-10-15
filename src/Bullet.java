import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import static java.lang.Math.*;

public class Bullet {
  BufferedImage bullet;
  Point loc;
  double x,y,angle;
  int damage,ally,lifeTime = 2000; //lifetime of all bullets is 2 seconds
  long startTime,lastTime = -1; //not currently using lastTime
  
  public Bullet(BufferedImage bullet) {  //as yet unused
    this.bullet = bullet;
  }
  public Bullet(BufferedImage bullet,double endx,double endy,double spd,int damage,int ally,Point p) {
    loc = new Point(p);  //bullet's starting location
    double hyp = loc.distance(endx,endy);  //used for trig
    this.bullet = bullet;
    if (hyp != 0) {
      x = spd * (endx - loc.x) / hyp;  //setting x and y speeds of bullet
      y = spd * (endy - loc.y) / hyp;
      angle = asin((endy - loc.y) / hyp); //setting rotation angle
      if (endx < loc.x && endy <= loc.y) angle = -1 * PI - angle;  //allowing the bullet to go in 360 degrees
      else if (endx < loc.x && endy > loc.y) angle = PI - angle;
    }
    else {  //if the bullets starting and fired at points are the same
      x = spd; 
      y = 0;
      angle = 0;
    }
    this.damage = damage;
    this.ally = ally;
    startTime = System.currentTimeMillis(); //set start time
  }
  public boolean draw(Graphics2D g,Map map,Point p) {  //map is used to check collision, point is used to draw bullet relative to player 
    long time = System.currentTimeMillis();  
    int t = (int)(time - startTime);
    if (t > lifeTime) return true;  //making sure the bullet doesnt last forever
    if (map.getPosVal((int)(x * t + loc.x), (int)(y * t + loc.y)) == 0) return true;  //if the bullet is off the map
    g.setTransform(new AffineTransform());  //setting rotation
    g.translate(400 + x * t + loc.x - p.x, 400 + y * t + loc.y - p.y);  //drawing relative to player
    g.rotate(angle);
    g.drawImage(bullet,-1 * bullet.getWidth() / 2,-1 * bullet.getHeight() / 2,null);
    g.setTransform(new AffineTransform());
    return false;
  }
}
