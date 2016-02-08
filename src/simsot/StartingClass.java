package simsot;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import simsot.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener {

	private static Player player;
	private Image image, character1, character2, characterMove1, characterMove2, currentSprite, background;
	public static Image tileTree, tileGrass;
	private int walkCounter = 1;
	private URL base;
	private Graphics second;
	private static Background bg1, bg2;
	private Animation anim;
	
	enum GameState {
		Running, Dead, Paused
	}

	GameState state = GameState.Running;

	private ArrayList<Tile> tilearray = new ArrayList<Tile>();
	public static ArrayList<Enemy> enemyarray = new ArrayList<Enemy>();

	@Override
	public void init() {
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("SIMSOT");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
		}
		/*

		// Image Setups
		character1 = getImage(base, "data/character1.png");
		character2 = getImage(base, "data/character2.png");
		characterMove1 = getImage(base, "data/characterwalk1.png");
		characterMove2 = getImage(base, "data/characterwalk2.png");
		background = getImage(base, "data/background.png");
		tileTree = getImage(base, "data/tree.png");
		tileGrass = getImage(base, "data/grass.png");

		anim = new Animation();
		anim.addFrame(character1, 1250);
		anim.addFrame(character2, 50);
		for (int i = 0; i < getEnemyarray().size(); i++) {
			Enemy e = getEnemyarray().get(i);
			e.characterStay = getImage(base, e.characterStayPath);
			e.characterMove1 = getImage(base, e.characterMove1Path);
			e.characterMove2 = getImage(base, e.characterMove2Path);
			e.anim.addFrame(e.characterStay, 100);
			e.currentSprite = e.anim.getImage();
		}

		currentSprite = anim.getImage();
		*/
	}

	@Override
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		player = new Player();
		bg1 = new Background(0, -800);
		bg2 = new Background(0, 800);
		// Initialize Tiles
		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*getEnemyarray().add(new Tato(200, 360));
		getEnemyarray().add(new Tato(600, 360));
		getEnemyarray().add(new Tato(300, 250));
		getEnemyarray().add(new Tato(500, 250));
		getEnemyarray().add(new Tato(100, 420));
		getEnemyarray().add(new Tato(400, 420));*/
	}

	private void loadMap(String filename) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;

		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while (null != (line = reader.readLine())) {
			// no more lines to read
			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		reader.close();
		height = lines.size();

		for (int j = 0; j < height; j++) {
			line = lines.get(j);
			for (int i = 0; i < width; i++) {
				if (i < line.length()) {
					char ch = line.charAt(i);
					if (Tile.isTileTypeSupported(ch)) {
						Tile t = new Tile(i, j, ch);
						tilearray.add(t);
					}
					if (EnemyFactory.isTileTypeSupported(ch)) {
						getEnemyarray().add(EnemyFactory.getEnemy(i, j, ch));
					}
				}
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void run() {
		if (state == GameState.Running) {
			while (true) {
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Animation
				if (player.isMovingHor() == true || player.isMovingVer() == true) {
					if (walkCounter % 30 == 0) {
						currentSprite = characterMove2;
					} else if (walkCounter % 15 == 0) {
						currentSprite = characterMove1;
					}
				} else if (player.isMovingVer() == false && player.isMovingHor() == false) {
					currentSprite = anim.getImage();
				}

				for (int i = 0; i < getEnemyarray().size(); i++) {
					Enemy e = getEnemyarray().get(i);

					if (e.alive == true) {
						if (e.isMoving == true) {
							if (walkCounter % 30 == 0) {
								e.currentSprite = getImage(base, e.characterMove1Path);
							} else if (walkCounter % 15 == 0) {
								e.currentSprite = getImage(base, e.characterMove2Path);
							}
						} else if (e.isMoving == false) {
							e.currentSprite = getImage(base, e.characterStayPath);
						}
						if (e.walkCounter > 1000) {
							e.walkCounter = 0;
						}
					}
				}
				updatePlayer();
				
				callEnemiesAIs();
				checkTileCollisions();
				checkEnemiesCollision();
				updateEnemies();
				
				
				bg1.update();
				bg2.update();
				animate();
				updateTiles();
				repaint(); // this calls paint
				if (walkCounter > 1000) {
					walkCounter = 0;
				}
				walkCounter++;
			}
		}
	}

	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (state == GameState.Running) {
			g.drawImage(background, bg1.getCenterX(), bg1.getCenterY(), this);
			g.drawImage(background, bg2.getCenterX(), bg2.getCenterY(), this);
			paintTiles(g);
			ArrayList<Projectile> projectiles = player.getProjectiles();
			for (int i = 0; i < getEnemyarray().size(); i++) {
				Enemy e = getEnemyarray().get(i);
				g.drawImage(e.currentSprite, e.getCenterX() - 31, e.getCenterY() - 31, this);
				for (int j = 0;  j < e.getProjectiles().size(); j++) {
					Projectile p = e.getProjectiles().get(j);
					g.setColor(Color.YELLOW);
					g.fillRect(p.getR().x, p.getR().y, p.getR().width, p.getR().height);
				}
			}
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile p = projectiles.get(i);
				g.setColor(Color.YELLOW);
				g.fillRect(p.getR().x, p.getR().y, p.getR().width, p.getR().height);
			}
			g.drawImage(currentSprite, player.getCenterX() - 31, player.getCenterY() - 31, this);
			g.setColor(Color.BLACK);
			g.fillRect(47, 37, 20, 20);
			g.setColor(Color.WHITE);
			g.drawString(Integer.toString(player.getHealth()),50,50);
		} else if (state == GameState.Dead) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 480);
			g.setColor(Color.WHITE);
			g.drawString("Dead", 360, 240);
		}

	} 
	
	private void callEnemiesAIs() {
		for (Enemy e : getEnemyarray()) {
			e.callAI();
		}
	}
	
	private void checkEnemiesCollision() {
		for (Enemy e : getEnemyarray()) {
			e.checkEnemyCollisions();
		}
	}
	
	private void checkTileCollisions() {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = tilearray.get(i);
			t.checkCollisions();
		}
	}
	
	private void updatePlayer() {
		ArrayList<Projectile> projectiles = player.getProjectiles();
		int i = 0;
		while (i < projectiles.size()) {
			Projectile p = projectiles.get(i);
			if (p.isVisible() == true) {
				p.update();
				for (int j = 0; j < getEnemyarray().size(); j++) {
					Enemy e = getEnemyarray().get(j);
					if (e.alive == true) {
						if (p.checkCollision(e) == true) {
							e.setHealth(e.getHealth() - p.damage);
							if (e.getHealth() < 1) {
								e.currentSprite = getImage(base, e.characterDiePath);
								e.die();
								// enemyarray.remove(e);
							}
						}
					}
				}
				for (int j = 0; j < tilearray.size(); j++) {
					p.checkCollision(tilearray.get(j));
				}
				i++;
			} else {
				projectiles.remove(i);
			}
		}
		player.update();
	}
	
	private void updateEnemies() {
		for (int j = 0; j < getEnemyarray().size(); j++) {
			Enemy e = getEnemyarray().get(j);
			e.update();
			int i = 0;
			while (i<e.getProjectiles().size()) {
				Projectile p = e.getProjectiles().get(i);
				if (p.isVisible() == true) {
					p.update();
					if (p.checkCollision(player)) {
						player.setHealth(player.getHealth() - p.damage);
						if (player.getHealth() < 1)
							state = GameState.Dead;
					}
					for (int k = 0; k < tilearray.size(); k++) {
						p.checkCollision(tilearray.get(k));
					}
					i++;
				} else {
					e.getProjectiles().remove(i);
				}
			}
		}
	}

	private void updateTiles() {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = tilearray.get(i);
			t.update();
		}
	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getCenterX()-31, t.getCenterY()-31, this);
		}
	}

	public void animate() {
		anim.update(10);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			player.moveUp();
			break;

		case KeyEvent.VK_S:
			player.moveDown();
			break;

		case KeyEvent.VK_Q:
			player.moveLeft();
			break;

		case KeyEvent.VK_D:
			player.moveRight();
			break;

		case KeyEvent.VK_UP:
			if (0 == player.isShooting()) {
				player.setShooting(2);
			}
			break;

		case KeyEvent.VK_DOWN:
			if (0 == player.isShooting()) {
				player.setShooting(4);
			}
			break;

		case KeyEvent.VK_LEFT:
			if (0 == player.isShooting()) {
				player.setShooting(1);
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (0 == player.isShooting()) {
				player.setShooting(3);
			}
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z: case KeyEvent.VK_S:
			player.stopVer();
			player.isColliding = false;
			break;
		case KeyEvent.VK_Q: case KeyEvent.VK_D:
			player.stopHor();
			player.isColliding = false;
			break;
		case KeyEvent.VK_UP: case KeyEvent.VK_DOWN: case KeyEvent.VK_LEFT: case KeyEvent.VK_RIGHT:
			player.setShooting(0);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static Background getBg1() {
		return bg1;
	}

	public static void setBg1(Background bg1) {
		StartingClass.bg1 = bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

	public static void setBg2(Background bg2) {
		StartingClass.bg2 = bg2;
	}

	public static Player getPlayer() {
		return player;
	}

	/*
	public void setPlayer(Player player) {
		this.player = player;
	}*/

	public static ArrayList<Enemy> getEnemyarray() {
		return enemyarray;
	}
/*
	public void setEnemyarray(ArrayList<Enemy> enemyarray) {
		this.enemyarray = enemyarray;
	}*/

	public ArrayList<Tile> getTilearray() {
		return tilearray;
	}

	public void setTilearray(ArrayList<Tile> tilearray) {
		this.tilearray = tilearray;
	}

}
