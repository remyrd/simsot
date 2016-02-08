package simsot;

public abstract class Stuff {

	int speedX = 0;
	int speedY = 0;
	int centerX;
	int centerY;
	Player player = StartingClass.getPlayer();
	
	public Stuff(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public int getSpeedX() {
		return speedX;
	}
	
	public int getSpeedY() {
		return speedY;
	}
	
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public void update() {
		centerX += speedX;
		centerY += speedY - player.getScrollingSpeed();
	}
	
	public int getCenterX() {
		return centerX;
	}
	
	public int getCenterY() {
		return centerY;
	}
	
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
}
