package simsot;

import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;

import simsot.framework.Animation;

public abstract class Enemy extends Stuff {

	private int maxHealth, currentHealth, power;
	protected int health;
	protected boolean alive = true;
	protected URL base;
	protected boolean isMoving = false;
	protected int walkCounter = 1;

	private Background bg = StartingClass.getBg1();
	public Rectangle rectX;
	public Rectangle rectY;
	public Rectangle R;
	protected int movementTime = ((int) Math.random() * 100) + 50;

	protected Player player = StartingClass.getPlayer();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	protected Animation anim;
	
	public Image characterStay, characterMove1, characterMove2, characterDie, currentSprite;
	public String characterStayPath, characterMove1Path, characterMove2Path, characterDiePath, currentSpritePath;

	public Enemy(int centerX, int centerY) {
		super(centerX, centerY);
	}

	public void checkCollision(Enemy e) {
		if (rectX.intersects(e.R)) {
			if (e.getCenterX() - getCenterX() >= 0 && getSpeedX() > 0) {
				setSpeedX(0);
			}
			if (e.getCenterX() - getCenterX() <= 0 && getSpeedX() < 0) {
				setSpeedX(0);
			}
		}
		if (rectY.intersects(e.R)) {
			if (e.getCenterY() - getCenterY() >= 0 && getSpeedY() > 0) {
				setSpeedY(0);
			}
			if (e.getCenterY() - getCenterY() <= 0 && getSpeedY() < 0) {
				setSpeedY(0);
			}
		}
	}

	
	public void checkEnemyCollisions() {
		for (Enemy e : StartingClass.getEnemyarray()) {
			if (!e.equals(this))
				checkCollision(e);
		}
	}
	
	// Behavioral Methods
	@Override
	public void update() {

		super.update();
		
		if (alive == true) {
			
			// Prevents going beyond X coordinate of 0 or 800
			if (centerX + speedX <= 60) {
				centerX = 61;
				setSpeedX(2);
			} else if (centerX + speedX >= 800) {
				centerX = 799;
				setSpeedX(-2);
			}
			
			if (getSpeedX() != 0 || getSpeedY() !=0){
				isMoving = true;
			} else {
				isMoving = false;
			}
			
			walkCounter++;

			// Collision
			// rectX.setRect(getCenterX() - 55, getCenterY() - 55, 50, 40);
			// rectY.setRect(getCenterX() - 50, getCenterY() - 60, 40, 50);

			// AI
		}
	}

	public abstract void callAI();/* {
		if (alive == true){
			setSpeedX(2);
		}
	}*/
	
	public void die() {
		alive = false;
		setSpeedX(0);
		setSpeedY(0);
	}

	public void attack() {
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	/*
	 * public int getMovementParam() { return movementParam; }
	 * 
	 * public void setMovementParam(int movementParam) { this.movementParam =
	 * movementParam; }
	 */

	public Background getBg() {
		return bg;
	}

	public void setBg(Background bg) {
		this.bg = bg;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void shootUp() {
		Projectile p = new Projectile(centerX, centerY, 0, -10);
		projectiles.add(p);
	}

	public void shootDown() {
		Projectile p = new Projectile(centerX, centerY, 0, 10);
		projectiles.add(p);
	}

	public void shootLeft() {
		Projectile p = new Projectile(centerX, centerY, -10, 0);
		projectiles.add(p);
	}

	public void shootRight() {
		Projectile p = new Projectile(centerX, centerY, 10, 0);
		projectiles.add(p);
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

}
