import java.awt.*;
import java.awt.image.*;
import java.util.*;
import static java.lang.Math.*;

public class Map {
  BufferedImage map;
  private byte rooms[][] = new byte[8][8], roomDesigns[][] = new byte[200][200],rmnum;  //holds the room designs
  private Random rand = new Random();
  int minx,maxx,miny,maxy,startx,starty,level = 0;
  boolean bossDead;
  
  public void newMap() {
    rooms = new byte[8][8];
    roomDesigns = new byte[200][200];
    rmnum = 0;
    level++;
    startx = starty = 0;
    minx = miny = 8;
    maxx = maxy = -1;
    bossDead = false;
    MonsterSpawner.monsterList.clear();  //clears all monsters and bullets
    Attack.bulletList.clear();
    fillRooms(rand.nextInt(8),rand.nextInt(8));  //designs and draws the map
    createRooms();
    paintMap();
  }
  private void fillRooms(int x,int y) {  //randomizes room locations
    if (rmnum >= 10) return; //max 10 rooms
    if (x < minx) minx = x;
    if (x > maxx) maxx = x;
    if (y < miny) miny = y;
    if (y > maxy) maxy = y;
    int adjoining,n,m;
    double dir;
    rmnum++;
    if (rmnum == 1) rooms[x][y] = 2;  //first room 
    else if (rmnum == 3) rooms[x][y] = 3;  //boss room
    else rooms[x][y] = 1;  
    if (rmnum == 1) adjoining = rand.nextInt(4) + 1;  //setting amount of adjoining rooms
    else if (rmnum < 4) adjoining = rand.nextInt(3) + 1;
    else adjoining = rand.nextInt(4);
    for (int i = 0;i < adjoining;i++) {
      dir = rand.nextInt(4) * PI / 2.0;  //randomise a direction
      n = (int)cos(dir);
      m = (int)sin(dir);
      if (rmnum < 3) {
        if (x + n >= 0 && x + n < 8 && y + m >= 0 && y + m < 8) {  
          if (rooms[x + n][y + m] == 0) {
            if (rmnum < 10) createHall(x,y,x + n,y + m);  //create hallways between rooms  
            fillRooms(x + n,y + m);  //recurse
          }
          else i--;
        }
        else i--;
      }
      else if (x + n >= 0 && x + n < 8 && y + m >= 0 && y + m < 8) {
        if (rooms[x + n][y + m] == 0) {
          if (rmnum < 10) createHall(x,y,x + n,y + m);
          fillRooms(x + n,y + m);
        }
      }
      else i--;
    }
    setStartPoint();
  }
  public void setStartPoint() { //finds the character's starting point based on the map
    for (int i = 0;i < 8;i++)
      for (int j = 0;j < 8;j++)
        if (rooms[i][j] == 2) {
          starty = 375 + (i - minx) * 750 + 30;
          startx = 375 + (j - miny) * 750 + 30;  //30 compensates for offset
        }
  }
  public void createHall(int startx,int starty,int endx,int endy) {  //creates hallways between rooms
    if (startx == endx) {
      if (starty > endy) {  //switches values
        starty = starty ^ endy;
        endy = starty ^ endy;
        starty = starty ^ endy;
      }
      for (int i = endx * 25 + 12;i <= endx * 25 + 14;i++)  //creates hall
        for (int j = starty * 25 + 13;j < starty * 25 + 38;j++)
          roomDesigns[i][j] = 1;
    }
    else if (starty == endy) {  //switches values
      if (startx > endx) {
        startx = startx ^ endx;
        endx = startx ^ endx;
        startx = startx ^ endx;
      }
      for (int i = endy * 25 + 12;i <= endy * 25 + 14;i++) //creates hall
        for (int j = startx * 25 + 13;j < startx * 25 + 38;j++)
          roomDesigns[j][i] = 1;  
    }
  }
  public void createRooms() {  //designs the rooms
    int x1,y1,x2,y2,r;
    boolean shp;
    for (int i = 0;i < 8;i++)
      for (int j = 0;j < 8;j++)
        if (rooms[i][j] != 0) {  //if a room exists there
          shp = rand.nextBoolean();
          if (shp) {  //rectangular rooms
            x1 = rand.nextInt(7); //randomise values
            y1 = rand.nextInt(7);
            x2 = rand.nextInt(8) + 17;
            y2 = rand.nextInt(8) + 17;
            for (int k = i * 25 + x1;k < i * 25 + x2;k++) //fill room in
              for (int l = j * 25 + y1;l < j * 25 + y2;l++)
                roomDesigns[k][l] = 1;  
          }
          else {  //circular rooms
            r = rand.nextInt(7) + 6;  //randomise value
            for (int k = 0;k < 25;k++)  //fill room in
              for (int l = 0;l < 25;l++)
                if (sqrt(pow(k - 13,2) + pow(l - 13,2)) < r) roomDesigns[k + i * 25][l + j * 25] = 1;
          }
          if (rooms[i][j] == 3) {
            roomDesigns[i * 25 + 13][j * 25 + 13] = 3;  //set next level portal location
          }
        }
  }
  public void paintMap() {  //draws the map
    int exitx = 0,exity = 0;
    map = new BufferedImage(1000 + (maxy - miny + 1) * 750, 1000 + (maxx - minx + 1) * 750, BufferedImage.TYPE_INT_RGB);  //creates bufferedImage
    Graphics2D g = map.createGraphics();
    g.setColor(new Color(145,145,145));
    for (int i = 0;i < (maxx - minx + 1) * 25;i++)
      for (int j = 0;j < (maxy - miny + 1) * 25;j++)
        if (roomDesigns[i + minx * 25][j + miny * 25] == 1) {  //if it is part of a room, paint
          g.fillRect(530 + j * 30,530 + i * 30,30,30); //increased by 30 (530) to compensate for the displacement
          if (rand.nextInt(75) == 0 && new Point(startx,starty).distance(j * 30,i * 30) > 600) MonsterSpawner.monsterList.add(new Monster(false, level, new Point(j * 30,i * 30), Monster.monsters.getSprite(427,160,52,52),Monster.monsters.getSprite(488,34,45,9)));
        }  //randomise monster spawning in rooms
        else if (roomDesigns[i + minx * 25][j + miny * 25] == 3) {  //if the exit portal is here
          /*g.setColor(new Color(150,75,0));
          g.fillRect(530 + j * 30,530 + i * 30,30,30);  //this just draws a brown square
          g.setColor(new Color(145,145,145));*/
          exitx = 519 + j * 30;
          exity = 520 + i * 30;
          MonsterSpawner.monsterList.add(new Monster(true, level, new Point(j * 30,i * 30), Monster.monsters.getSprite(470,55,82,82),Monster.monsters.getSprite(517,178,56,14)));  //spawn boss on exit portal
        }
    g.drawImage(Monster.monsters.getSprite(390,50,52,50),exitx,exity,null);  //draws the exit portal (moved out of loop because it was being drawn on)
  }
  public Point walk(Point loc, double x, double y) {  //moves a point based on map
    if (passable(loc.x + x,loc.y)) loc.x += x;  //if a point is passable, move
    if (passable(loc.x,loc.y + y)) loc.y += y;
    return loc;
  }
  public boolean passable(double x,double y) {  //checks if you can walk through a given square
    if (getPosVal((int)x,(int)y) != 0) return true;  
    return false;
  }
  public int getPosVal(int x,int y) {  //return the array value of a position
    try {
      return roomDesigns[y / 30 + minx * 25][x / 30 + miny * 25];
    } catch(ArrayIndexOutOfBoundsException e) { System.out.println("getPosVal screwed up");} 
    return 1;
  }
  public void test() { //test function, not used
    double avg = 0;
    //for (int i = 0;i < 1000;i++) {
      //rmnum = 0;
      //rooms = new byte[8][8];
      //roomDesigns = new byte[200][200];
      //fillRooms(rand.nextInt(8),rand.nextInt(8));
      //createRooms();
      //avg+=rmnum;
      System.out.println(this.toString());
      //System.out.println(minx + " " + maxx + " " + miny + " " + maxy);
      //System.out.println((startx - 500) + " " + (starty - 500));
      //if (rmnum < 4) System.out.println("ERROR " + rmnum);
    //}
    //System.out.println(avg / 1000);
  }
  public String toString() {  //returns string representation of map arrays
    String s = "";
    for (int i = 0;i < 8;i++) {
      for (int j = 0;j < 8;j++)
        s+=rooms[i][j];
      s+="\n";
    }
    s+="\n";
    for (int i = 0;i < 200;i++) {
      for (int j = 0;j < 200;j++)
        s+=roomDesigns[i][j];
      s+="\n";
    }
    return s;
  }
}
