import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Inventory {  //this represents the character's equipment
  Item arr[][];
  int x,y;
  
  public Inventory() {
    arr = new Item[1][5];
    x = 1;
    y = 5;
    arr[0][0] = Item.newItem(0,0);  //gives the player a weapon to start
  }
  public void draw(int x,int y,Graphics2D g) {
    g.setColor(new Color(183,94,0));  //draws inventory slots
    for (int i = 0;i < this.x;i++) 
      for (int j = 0;j < this.y;j++)
        g.fillRect(x + j * 60,y + i * 60,40,40);
    drawItems(x,y,g);  //draws items
  }
  public void drawItems(int x,int y,Graphics2D g) {
    for (int i = 0;i < 5;i++)
      if (arr[0][i] == null) {
        g.drawImage(Item.allItems.getSprite(i * 40, 0, 40, 40),x + 60 * i,y,null);  //if no item is equipped, draws a placeholder icon
      }
      else arr[0][i].draw(x + 60 * i,y,g);  //draws the item sprite
  }
  public Item itemAt(int x,int y) {
    try {  
      return arr[x][y];  //returns the item at the inventory position
    } catch(ArrayIndexOutOfBoundsException e) { System.out.println("itemAt screwed up"); }  //sometimes it goes out of bounds
    return arr[0][0];  //return generic item if it fails
  }
  public void setItem(int x,int y,Item item) {
    arr[x][y] = item;  //sets the item at the inventory position
  }
}
