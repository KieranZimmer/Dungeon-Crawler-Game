import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DungeonCrawler extends JPanel implements Runnable,KeyListener,MouseListener,ActionListener,MouseMotionListener{
	private MainPanel main;	 //unused...why is this declared?
	private static int pWidth = 1144, pHeight = 800; //width & height of JPanel
	private boolean running; //keeps track of state of the program
	private Thread thread;
	private Graphics2D graphics;
	private Image image;
	private Map map;
	SpriteSheet sprites = new SpriteSheet("Game Sprites 2.png");
	Player player = new Player(3);
	boolean keys[] = new boolean[4],delete;
	private int updater,shootTimer,state,cursorx,cursory; //Don't let the game run for more than 124 days, otherwise updater will overflow
	
	
	Timer t = new Timer(5, this);
	
	public DungeonCrawler(MainPanel main) {
		this.setDoubleBuffered(false); //we'll use our own double buffering method
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(pWidth, pHeight));
		this.setFocusable(true);
		this.requestFocus();
		this.main = main;
	  addKeyListener(this); //checks for keyboard presses for this JPanel
	  addMouseListener(this);
	  addMouseMotionListener(this);
	  state = 3; //starts on the intro screen state
	}

	public static void main(String[] args) {
		new MainPanel();
	}
	
	public void addNotify() {
		super.addNotify();
		startGame();
	}
	
	public void stopGame() {
		running = false;
	}
	
	//Creates a thread and starts it
	public void startGame() {
		if (thread == null || !running) {
			thread = new Thread(this);
		}
		thread.start();
	}
	
	//this is run by the thread once it is created
	public void run() {
		running = true;
	
		while (running) {
		  while (state != 1) {  //if in any state but the game state
		    drawGame();
	      drawScreen();
		  }
		  while (state == 1) {  //if in game state
		    //used for double buffering (creates an Image)
		    if (player.curHealth <= 0) {  //if player is dead
          try {  //paused so that player stops clicking, which skips the game over screen
            Thread.sleep(1500);
          } catch (InterruptedException e) {}
		      state = 4; //death screen state
		      break;
		    }
		    drawGame();
		    
		    //DRAW YOUR STUFF IN THIS METHOD
		    draw();
		  
		    UserInterface.drawInterface(graphics,player,map.level); //draws interface
		    
		    if (cursorx >= 822 && cursorx <= 1122 && cursory >= 280 && cursory <= 620) {  //if the player is mousing over an item
		      graphics.setColor(Color.white);
		      if ((cursorx - 822) % 60 <= 40 && (cursory - 280) % 60 <= 40) {
		        if (player.inventory.itemAt((cursory - 280) / 60,(cursorx - 822) / 60) != null) graphics.drawString(player.inventory.itemAt((cursory - 280) / 60,(cursorx - 822) / 60).toString(),815,700);  //write the item description
		      }
		    }
		    if (cursorx >= 822 && cursorx <= 1122 && cursory >= 200 && cursory <= 240) { //if the player is mousing over an item
		      graphics.setColor(Color.white);
		      if ((cursorx - 822) % 60 <= 40) 
		        if (player.equipment.itemAt(0,(cursorx - 822) / 60) != null) graphics.drawString(player.equipment.itemAt(0,(cursorx - 822) / 60).toString(),815,700); //write the item description
		    }
		    
		    if (delete) {
		      graphics.setColor(Color.red);  //if delete items is on
		      graphics.drawString("Inventory items right clicked will be deleted",815,720);
		    }
		    //used to draw the Image created to display our graphics
	      drawScreen();
		  }
		}
    System.exit(0);
	}
	
	//draw whatever you need to draw here using the Graphics2D object called graphics
	public void init() {
    player = new Player(3);
    keys = new boolean[4];
	  updater = shootTimer = 0;
	  player.setUpSprite(sprites.getSprite(336,2,42,42));  //set player sprites
	  player.setDownSprite(sprites.getSprite(109,4,42,42));
	  player.setLeftSprite(sprites.getSprite(247,2,42,42));
	  player.setRightSprite(sprites.getSprite(7,4,42,42));
	  player.setUpMove1Sprite(sprites.getSprite(378,2,42,42));
	  player.setUpMove2Sprite(sprites.getSprite(428,2,37,42));
	  player.setDownMove1Sprite(sprites.getSprite(156,3,42,42));
	  player.setDownMove2Sprite(sprites.getSprite(205,3,37,42));
	  player.setLeftMoveSprite(sprites.getSprite(290,2,42,42));
	  player.setRightMoveSprite(sprites.getSprite(59,4,37,42));
	  player.setUpAttackSprite(sprites.getSprite(337,52,42,42));
	  player.setDownAttackSprite(sprites.getSprite(109,50,37,42));
	  player.setLeftAttackSprite(sprites.getSprite(231,50,57,42));
	  player.setRightAttackSprite(sprites.getSprite(10,51,57,42));
	  player.setDefaultSprite(player.sprites[1][0]);
	  player.atk.bullet = sprites.getSprite(497,12,26,10);
	  map = new Map();
	  map.newMap();  //randomise a new map
	  player.setLoc(map.startx,map.starty);  //put player at start location
	  delete = false;
	  t.start(); //starts the timer
	}
	public void update() {
	  //MOVEMENT
	  if (updater % 2 == 0) {  //when to update movement
	    double x = 0,y = 0;
	    if (keys[0]) y -= player.spd;  //player is moving in a direction
	    if (keys[1]) y += player.spd;
	    if (keys[2]) x -= player.spd;
	    if (keys[3]) x += player.spd;
	    if (x != 0 && y != 0) {  //if movement is diagonal
	      x*=4.0 / 5.0;
	      y*=4.0 / 5.0;
	    }
	    if (x != 0 || y != 0) player.loc = map.walk(player.loc,x,y);  //move player
	    if (x != 0 || y != 0) if (map.getPosVal((int)player.loc.x, (int)player.loc.y) == 3 && map.bossDead) {  //if player is on an exit portal
	      map.newMap();  //randomise new map
	      player.setLoc(map.startx,map.starty);  //give player new start location
	    }
	  //PLAYER ANIMATION
	    if (updater % 30 == 0) {  //sets player animation based on shooting and moving directions
	      if (player.shootDir == 0) {
	        if (player.anim == 0) player.anim = 3;
	        else player.anim = 0;
	        player.setDefaultSprite(player.sprites[0][player.anim]);
	        player.setDefaultSprite(player.sprites[0][0]);
	      }
	      else if (player.shootDir == 1) {
	        if (player.anim == 0) player.anim = 3;
	        else player.anim = 0;
	        player.setDefaultSprite(player.sprites[1][player.anim]);
	        player.setDefaultSprite(player.sprites[1][0]);
	      }
	      else if (player.shootDir == 2) {
	        if (player.anim == 0) player.anim = 2;
	        else player.anim = 0;
	        player.setDefaultSprite(player.sprites[2][player.anim]);
	        player.setDefaultSprite(player.sprites[2][0]);
	      }
	      else if (player.shootDir == 3) {
	        if (player.anim == 0) player.anim = 2;
	        else player.anim = 0;
	        player.setDefaultSprite(player.sprites[3][player.anim]);
	        player.setDefaultSprite(player.sprites[3][0]);
	      }
	      else if (y > 0) {
	        player.setDefaultSprite(player.sprites[1][0]);
	        if (player.dir == 1) {
	          if (player.anim == 2) player.anim = 1;
	          else player.anim = 2;
	        }
	        else {
	          player.anim = 1;
	          player.dir = 1;
	        }
	        player.setDefaultSprite(player.sprites[1][player.anim]);
	      }
	      else if (y < 0) {
	        player.setDefaultSprite(player.sprites[0][0]);
	        if (player.dir == 0) {
	          if (player.anim == 2) player.anim = 1;
	          else player.anim = 2;
	        }
	        else {
	          player.anim = 1;
	          player.dir = 0;
	        }
	        player.setDefaultSprite(player.sprites[0][player.anim]);
	      }
	      else if (x < 0) {
	        player.setDefaultSprite(player.sprites[2][0]);
	        if (player.dir == 2) player.anim = (player.anim + 1) % 2;
	        else {
	          player.anim = 0;
	          player.dir = 2;
	        }
	        player.setDefaultSprite(player.sprites[2][player.anim]);
	      }
	      else if (x > 0) {
	        player.setDefaultSprite(player.sprites[3][0]);
	        if (player.dir == 3) player.anim = (player.anim + 1) % 2;
	        else {
	          player.anim = 0;
	          player.dir = 3;
	        }
	        player.setDefaultSprite(player.sprites[3][player.anim]);
	      }
	      else player.setCurrentSprite(player.def);
	    }
	  }
	  //PLAYER ATTACKS
	  if ((updater - shootTimer) % (int)(50 / player.atkSpd) == 0)  //if the player can attack
	    if (player.shootDir != -1 && player.curMana > 0) {  //if the player is clicking to attack
	      player.atk.bullet((int)(player.shootX + player.loc.x - 400),(int)(player.shootY + player.loc.y - 400),player.loc);  //attack
	      player.curMana--;  //uses mana
	    }
	}
	public void draw() {
	  //DRAW BULLETS
    for (int i = 0;i < Attack.bulletList.size();i++) {  //loop through bullets
      Bullet temp = Attack.bulletList.get(i);
      if (temp.draw(graphics,map,player.loc)) {  //draws bullet, function returns true if the bullet is out of time
        Attack.bulletList.remove(i);  //if bullet is out of time, it is removed
        continue;  //skips to next bullet
      }
      for (int j = 0;j < MonsterSpawner.monsterList.size();j++) {
        if (MonsterSpawner.monsterList.get(j).hit(temp)) {  //checks collision with monster
          Attack.bulletList.remove(i);  //if bullet collides, it is removed
          break;
        }
      }
      if (player.hit(temp)) Attack.bulletList.remove(i);  //checks collision with player, if the bullet collides, it is removed
    }
    for (int i = 0;i < MonsterSpawner.monsterList.size();i++) {  //loop through monsters
      Monster temp = MonsterSpawner.monsterList.get(i);
      if (temp.health <= 0) {  //if monster is dead
        MonsterSpawner.monsterList.remove(i);  //remove monster
        if (temp.boss) map.bossDead = true;
        player.inventory.createItem(temp);  //spawn loot (always does it twice, no idea)
      }
      else {
        if (updater - temp.updateMove > 2) {  //monster moves
          temp.move(player.loc,map);
          temp.updateMove = updater;
        }
        if (updater - temp.updateShoot > 100) {  //monster attacks
          temp.shoot(player.loc);
          temp.updateShoot = updater;
        }
        temp.draw(graphics,player.loc);  //draw monster
      }
    }
    //DRAW PLAYER
    try {
      if (player.shootDir == 0) graphics.drawImage(player.sprites[0][player.anim],379,379,null); //draws player based on shooting and moving directions
      else if (player.shootDir == 1) graphics.drawImage(player.sprites[1][player.anim],379,379,null);
      else if (player.shootDir == 2) {
        if (player.anim == 2) graphics.drawImage(player.sprites[2][2],379 - 15,379,null);
        else graphics.drawImage(player.sprites[2][0],379,379,null);
      }
      else if (player.shootDir == 3) {
        if (player.anim == 3) player.anim = 2;
        graphics.drawImage(player.sprites[3][player.anim],379,379,null);
      }
      else if (keys[0])
        graphics.drawImage(player.sprites[0][player.anim],379,379,null);
      else if (keys[1])
        graphics.drawImage(player.sprites[1][player.anim],379,379,null);
      else if (keys[2])
        graphics.drawImage(player.sprites[2][player.anim],379,379,null);
      else if (keys[3])
        graphics.drawImage(player.sprites[3][player.anim],379,379,null);
      else graphics.drawImage(player.def,379,379,null);
    } catch(ArrayIndexOutOfBoundsException e) { System.out.println("Draw messed up");}
	}

	//creates an image and gets the graphics object so we can paint on it
	public void drawGame() {
	  int x,y;
		if (image == null) {
			image = createImage(pWidth, pHeight);
			
			if (image == null) {
				System.out.println("Cannot create buffer");
				return;
			}
			else
				graphics = (Graphics2D)image.getGraphics(); //get graphics object from Image
		}
		if (state == 0) {
      Menu.draw(graphics);  //draw menu
		}
		else if (state == 1) {
		  graphics.drawImage(map.map,(int)(player.loc.x * -1) - 125,(int)(player.loc.y * -1) - 125,null);  //draw game map
		  //graphics.drawImage(map.map.getSubimage((int)player.loc.x - 400, (int)player.loc.y - 400, 800, 800),0,0,null);  DOES NOT WORK AT ALL
		}
		else if (state == 2) {
		  Instructions.draw(graphics);  //draw instructions
		}
		else if (state == 3) {
      IntroScreen.draw(graphics);  //draw intro screen
		}
		else if (state == 4) {
      DeathScreen.draw(graphics,map);  //draw death screen
		}
	}
	public void drawScreen() {
		Graphics g;
		try {
			g = this.getGraphics(); //a new image is created for each frame, this gets the graphics for that image so we can draw on it
			if (g != null && image != null) {
				g.drawImage(image, 0, 0, null);
				g.dispose(); //not associated with swing, so we have to free memory ourselves (not done by the JVM)
			}
			image = null;
		}catch(Exception e) {System.out.println("Graphics objects error");}
	}

  @Override
  public void keyPressed(KeyEvent e) {
    if (state == 1) {
      int keyCode = e.getKeyCode(); //gets the code of the key pressed
      keys[0] |= keyCode == e.VK_UP || keyCode == e.VK_W; //if the UP/W key is pressed set it's array location to true
      keys[1] |= keyCode == e.VK_DOWN || keyCode == e.VK_S; //if the DOWN/S key is pressed set it's array location to true
      keys[2] |= keyCode == e.VK_LEFT || keyCode == e.VK_A; //if the LEFT/A key is pressed set it's array location to true
      keys[3] |= keyCode == e.VK_RIGHT || keyCode == e.VK_D; //if the RIGHT/D key is pressed set it's array location to true
      if (keyCode == KeyEvent.VK_F) player.useHealthPotion();
      if (keyCode == KeyEvent.VK_V) player.useManaPotion(); //sometimes consumes multiple potions, no idea
    }
    else if (state == 3 || state == 4) {
      state = 0;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (state == 1) {
      int keyCode = e.getKeyCode();
      keys[0] &= keyCode != e.VK_UP && keyCode != e.VK_W; //if the UP/W key is released set it's array location to false
      keys[1] &= keyCode != e.VK_DOWN && keyCode != e.VK_S; //if the DOWN/S key is released set it's array location to false
      keys[2] &= keyCode != e.VK_LEFT && keyCode != e.VK_A; //if the LEFT/A key is released set it's array location to false
      keys[3] &= keyCode != e.VK_RIGHT && keyCode != e.VK_D; //if the RIGHT/D key is released set it's array location to false
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent me) {
  }

  @Override
  public void mouseEntered(MouseEvent me) {
  }

  @Override
  public void mouseExited(MouseEvent me) {
  }

  @Override
  public void mousePressed(MouseEvent me) {
    int x = me.getX();
    int y = me.getY();
    if (state == 0) {  //menu
      if (x >= 380 && x <= 780 && y >= 665 && y <= 715) {  //if play game is click
        init();  //initialise, start game
        state = 1;
      }
      else if (x >= 380 && x <= 780 && y >= 740 && y <= 790) state = 2;  //if instructions is clicked, go to instructions
    }
    else if (state == 1) {  //game
      if (x <= 800 && x >= 0 && y <= 800 && y >= 0) {  //if player is clicking on screen, set shoot direction
        player.shootDirection(x,y);
        shootTimer = updater + 1;
      }
      else {
        player.shootDir = -1;
        if (x >= 822 && x <= 1122 && y >= 280 && y <= 640) {
          if (me.getButton() == 3) {
            if ((x - 822) % 60 <= 40 && (y - 280) % 60 <= 40) 
              if (delete) player.inventory.setItem((y - 280) / 60,(x - 822) / 60, null);  //if the user is clicking on items, use or delete them
              else player.useItem((x - 822) / 60,(y - 280) / 60);
          }
        }
        if (x >= 1050 && x <= 1100 && y >= 725 && y <= 775) delete = !delete;  //toggle delete
      }
    }
    else if (state == 2) {  //instructions
      if (x >= 380 && x <= 780 && y >= 665 && y <= 715) {  //if play is pressed, init and launch game
        init();
        state = 1;
      }
      else if (x >= 380 && x <= 780 && y >= 740 && y <= 790) state = 0;  //if back is pressed, return to menu
    }
    else if (state == 3 || state == 4) {  //if anything is click in the intro or death pages, return to menu
      state = 0;
    }
  }

  @Override
  public void mouseReleased(MouseEvent me) {
    player.shootDir = -1;  //stop shootng when mouse released
  }

  @Override
  public void mouseDragged(MouseEvent me) {
    int x = me.getX();
    int y = me.getY();
    if (x <= 800 && x >= 0 && y <= 800 && y >= 0) player.shootDirection(x,y);  //if the mouse is on the game
    else player.shootDir = -1;
  }

  @Override
  public void mouseMoved(MouseEvent me) {
    cursorx = me.getX();
    cursory = me.getY();
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    updater++;  //every time timer ticks, update
    update();
  }
}
