package simsot;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Player {

	final int MOVESPEED = 4;

	private int centerX = 400;
	private int centerY = 100;
	private int speedX = 0;
	private int speedY = 0;
	private int scrollingSpeed = 0;
	private int health = 10;
	private boolean isMovingVer = false;
	private boolean isMovingHor = false;
	public boolean isColliding = false;
	public Rectangle rectX = new Rectangle(0, 0, 0, 0);
	public Rectangle rectY = new Rectangle(0, 0, 0, 0);
	private int shootingcounter = 0;
	private int shootingFrequency = 10;

	// 0 = not, 1 = left, 2 = top, 3 = right, 4 = bottom
	private int isShooting = 0;

	/*
	private static Background bg1 = StartingClass.getBg1();
	private static Background bg2 = StartingClass.getBg2();*/

	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public void update() {

		// Moves Character or Scrolls Background accordingly.
		if (speedY != 0) {
			centerY += speedY;
		}

		// Updates X Position
		centerX += speedX;
		
		/*
		if (speedY > 0 && centerY > 200) {
			bg1.setSpeedY(0);
			bg2.setSpeedY(0);
		} else if (speedY == 0) {
			bg1.setSpeedY(0);
			bg2.setSpeedY(0);
		} else if (speedY < 0 && centerY < 50) {
			bg1.setSpeedY(0);
			bg2.setSpeedY(0);
			setSpeedY(0);
			setCenterY(getCenterY() + 2);
		}*/

		// Prevents going beyond X coordinate of 0 or 800
		if (centerX + speedX <= 30) {
			centerX = 31;
		} else if (centerX + speedX >= 769) {
			centerX = 768;
		}

		// Prevents going beyond Y coordinate of 150 and 330
		if (centerY + speedY <= 150) {
			centerY = 149;
			scrollingSpeed = 2*speedY;
		} else if (centerY + speedY >= 280) {
			centerY = 279;
			scrollingSpeed = 2*speedY;
		}

		// Collision
		rectX.setRect(centerX - 25, centerY - 20, 50, 40);
		rectY.setRect(centerX - 20, centerY - 25, 40, 50);
		if (isShooting > 0) {
			if (shootingcounter % shootingFrequency == 0) {
				switch (isShooting) {
				case 1:
					shootLeft();
					break;
				case 2:
					shootUp();
					break;
				case 3:
					shootRight();
					break;
				case 4:
					shootDown();
					break;
				}
			}
			shootingcounter++;
			if (shootingcounter == 1000)
				shootingcounter = 0;
		}
		
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

	public int isShooting() {
		return isShooting;
	}

	public void setShooting(int isShooting) {
		this.isShooting = isShooting;
		if (0 == isShooting)
			shootingcounter = 0;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void moveRight() {
		if (isColliding == false) {
			speedX = MOVESPEED;
			setMovingHor(true);
		}
	}

	public void moveLeft() {
		if (isColliding == false) {
			speedX = -MOVESPEED;
			setMovingHor(true);
		}
	}

	public void moveUp() {
		if (isColliding == false) {
			this.setSpeedY(-MOVESPEED);
			setMovingVer(true);
		}
	}

	public void moveDown() {
		if (isColliding == false) {
			this.setSpeedY(MOVESPEED);
			setMovingVer(true);
		}
	}

	public void stopHor() {
		speedX = 0;
		setMovingHor(false);
	}

	public void stopVer() {
		this.setSpeedY(0);
		setMovingVer(false);
	}

	public boolean isMovingVer() {
		return isMovingVer;
	}

	public void setMovingVer(boolean isMovingVer) {
		this.isMovingVer = isMovingVer;
	}

	public boolean isMovingHor() {
		return isMovingHor;
	}

	public void setMovingHor(boolean isMovingHor) {
		this.isMovingHor = isMovingHor;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getSpeedY() {
		return speedY;
	}
	
	public int getScrollingSpeed() {
		return this.scrollingSpeed;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY/2;
		this.scrollingSpeed = speedY/2; 
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
