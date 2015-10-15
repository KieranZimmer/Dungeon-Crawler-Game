import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class Item {
  BufferedImage sprite;
  int itemID,value1,value2;  //values mean different things based on item type
  private static Random rand = new Random();
  static SpriteSheet allItems = new SpriteSheet("Item Sprites.png");
  
  public Item(BufferedImage bi,int ID,int val1,int val2) {
    sprite = bi;
    itemID = ID;
    value1 = val1;
    value2 = val2;
  }
  public void draw(int x,int y,Graphics2D g) {
    g.drawImage(sprite,x,y,null);  //draws the item
  }
  public static Item newItem(int ID,int level) {  //creates an item based on the itemID and the level of the item
    //int ID = rand.nextInt(5);
    if (ID == 0) { //weapons
      if (level == 0) return new Item(allItems.getSprite(960, 400, 40, 40), ID, 10, 0);
      if (level == 1) return new Item(allItems.getSprite(800, 400, 40, 40), ID, 15, 0);
      if (level == 2) return new Item(allItems.getSprite(120, 440, 40, 40), ID, 25, 0);
      if (level == 3) return new Item(allItems.getSprite(80, 440, 40, 40), ID, 35, 0);
      if (level == 4) return new Item(allItems.getSprite(200, 440, 40, 40), ID, 50, 0);
      if (level == 5) return new Item(allItems.getSprite(280, 440, 40, 40), ID, 65, 0);
      if (level == 6) return new Item(allItems.getSprite(360, 440, 40, 40), ID, 85, 0);
      if (level == 7) return new Item(allItems.getSprite(400, 440, 40, 40), ID, 110, 0);
    }
    if (ID == 1) { //spellbooks
      if (level == 0) return new Item(allItems.getSprite(240, 320, 40, 40), ID, 10, 0);
      if (level == 1) return new Item(allItems.getSprite(280, 320, 40, 40), ID, 20, 0);
      if (level == 2) return new Item(allItems.getSprite(320, 320, 40, 40), ID, 30, 0);
      if (level == 3) return new Item(allItems.getSprite(360, 320, 40, 40), ID, 40, 0);
      if (level == 4) return new Item(allItems.getSprite(400, 320, 40, 40), ID, 50, 0);
      if (level == 5) return new Item(allItems.getSprite(440, 320, 40, 40), ID, 60, 0);
      if (level == 6) return new Item(allItems.getSprite(480, 320, 40, 40), ID, 80, 0);
      if (level == 7) return new Item(allItems.getSprite(520, 320, 40, 40), ID, 100, 0);  
    }
    if (ID == 2) { //helms
      if (level == 0) return new Item(allItems.getSprite(480, 400, 40, 40), ID, 5, 5);
      if (level == 1) return new Item(allItems.getSprite(520, 400, 40, 40), ID, 6, 10);
      if (level == 2) return new Item(allItems.getSprite(560, 400, 40, 40), ID, 7, 15);
      if (level == 3) return new Item(allItems.getSprite(600, 400, 40, 40), ID, 8, 20);
      if (level == 4) return new Item(allItems.getSprite(640, 400, 40, 40), ID, 10, 25);
      if (level == 5) return new Item(allItems.getSprite(680, 400, 40, 40), ID, 12, 35);
      if (level == 6) return new Item(allItems.getSprite(720, 400, 40, 40), ID, 16, 50);
      if (level == 7) return new Item(allItems.getSprite(760, 400, 40, 40), ID, 20, 75);  
    }
    if (ID == 3) { //chestpieces
      if (level == 0) return new Item(allItems.getSprite(560, 360, 40, 40), ID, 5, 10);
      if (level == 1) return new Item(allItems.getSprite(600, 360, 40, 40), ID, 6, 20);
      if (level == 2) return new Item(allItems.getSprite(640, 360, 40, 40), ID, 7, 30);
      if (level == 3) return new Item(allItems.getSprite(760, 360, 40, 40), ID, 8, 40);
      if (level == 4) return new Item(allItems.getSprite(960, 360, 40, 40), ID, 10, 55);
      if (level == 5) return new Item(allItems.getSprite(800, 360, 40, 40), ID, 12, 70);
      if (level == 6) return new Item(allItems.getSprite(120, 400, 40, 40), ID, 16, 90);
      if (level == 7) return new Item(allItems.getSprite(40, 400, 40, 40), ID, 20, 120);  
    }
    if (ID == 4) { //rings
      if (level == 0) return new Item(allItems.getSprite(680, 240, 40, 40), ID, 5, 5);
      if (level == 1) return new Item(allItems.getSprite(0, 280, 40, 40), ID, 10, 10);
      if (level == 2) return new Item(allItems.getSprite(320, 280, 40, 40), ID, 15, 15);
      if (level == 3) return new Item(allItems.getSprite(640, 280, 40, 40), ID, 20, 20);
      if (level == 4) return new Item(allItems.getSprite(960, 280, 40, 40), ID, 25, 30);
      if (level == 5) return new Item(allItems.getSprite(200, 320, 40, 40), ID, 35, 40);
      if (level == 6) return new Item(allItems.getSprite(120, 320, 40, 40), ID, 50, 55);
      if (level == 7) return new Item(allItems.getSprite(160, 320, 40, 40), ID, 75, 75);  
    }
    if (ID == 5) return new Item(allItems.getSprite(880, 160, 40, 40), ID, 80 ,20);  //health pot
    return new Item(allItems.getSprite(920, 160, 40, 40), ID, 80, 20);  //mana pot
  }
  public String toString() {
    if (this == null) return "";  //creates strings based on the ItemID and the values of the item
    else if (itemID == 0) return "Deals " + value1 + " damage per attack";
    else if (itemID == 1) return "Increases attack speed by " + value1 + " percent";
    else if (itemID == 2) return "Increase armor by " + value1 + ", increases health by " + value2;
    else if (itemID == 3) return "Increase armor by " + value1 + ", increases mana by " + value2;
    else if (itemID == 4) return "Increase health by " + value1 + ", increases damage by " + value2;
    else if (itemID == 5) return "Heals 80 + 20% of max health";
    return "Heals 80 + 20% of max mana";
  }
} 
