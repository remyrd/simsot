package simsot;

import java.awt.Rectangle;

public class Projectile extends Stuff {

	private boolean visible;
	private Rectangle rectP;
	private int initX;
	private int initY;
	public int damage = 1;

	public Projectile(int startX, int startY, int speedX, int speedY) {
		super(startX, startY);
		super.setSpeedX(speedX);
		super.setSpeedY(speedY);
		initX = startX;
		initY = startY;
		visible = true;
		rectP = new Rectangle(0, 0, 0, 0);
	}

	@Override
	public void update() {
		super.update();
		rectP.setBounds(getCenterX() - 2, getCenterY() - 2, 5, 5);
		if (Math.abs(this.getCenterX() - initX) > 300) {
			visible = false;
		}
		if (Math.abs(this.getCenterY() - initY) > 300) {
			visible = false;
		}
	}

	boolean checkCollision(Enemy e) {
		if(rectP.intersects(e.R) /*|| rectP.intersects(e.rectY)*/){
			visible = false;
			return true;
		}
		return false;
	}
	
	boolean checkCollision(Tile t) {
		if (t.getType() != '0') {
			if(rectP.intersects(t.getR())){
				visible = false;
				return true;
			}
			return false;
		}
		return false;
	}
	
	boolean checkCollision(Player p) {
		if(rectP.intersects(p.rectX) || rectP.intersects(p.rectY)){
			visible = false;
			return true;
		}
		return false;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Rectangle getR() {
		return rectP;
	}
}
