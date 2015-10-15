import java.awt.*;
import java.util.Random;

public class ItemStorage {  //represents the characters inventory
  Item arr[][];
  int x,y;  //the size of the inventory
  private Random rand = new Random();
  
  public ItemStorage(int x,int y) {
    arr = new Item[x][y];
    this.x = x;
    this.y = y;
    arr[0][0] = Item.newItem(5,3);  //gives the player potions to start
    arr[0][1] = Item.newItem(6,3);
  }
  public ItemStorage() {
  }
  public void draw(int x,int y,Graphics2D g) {
    g.setColor(new Color(183,94,0));  //draws inventory slots
    for (int i = 0;i < this.x;i++) 
      for (int j = 0;j < this.y;j++)
        g.fillRect(x + j * 60,y + i * 60,40,40);
    drawItems(x,y,g);
  }
  public void drawItems(int x,int y,Graphics2D g) {
    for (int i = 0;i < this.x;i++) 
      for (int j = 0;j < this.y;j++)
        if (arr[i][j] != null) arr[i][j].draw(x + 60 * j,y + 60 * i,g); //draws inventory items
  }
  public Item itemAt(int x,int y) {
    try {  
      return arr[x][y];  //returns the item at the inventory position
    } catch(ArrayIndexOutOfBoundsException e) { System.out.println("itemAt screwed up"); }  //sometimes it goes out of bounds
    return arr[0][0];  //return generic item if it fails
  }
  public void setItem(int x,int y,Item item) {
    arr[x][y] = item; //sets the item at the inventory position
  }
  public void createItem(Monster m) {  //creates an item based on a killed monster
    if (m == null) return; 
    for (int i = 0;i < x;i++)
      for (int j = 0;j < y;j++)
        if (arr[i][j] == null) {
          if (m.boss) {  //if the monster was a boss
            arr[i][j] = Item.newItem(rand.nextInt(5),m.level > 14 ? 7 : m.level / 2);  //uses the monsters level to provide an item of appropriate power
          }
          else {  //if it was a normal monster
            int num = rand.nextInt(12);
            if (num == 0) arr[i][j] = Item.newItem(5,m.level);
            if (num == 1) arr[i][j] = Item.newItem(6,m.level);
            if (num == 2) arr[i][j] = Item.newItem(rand.nextInt(5),m.level > 14 ? 7 : m.level / 2);
          }
          return;
        }
  }
}
