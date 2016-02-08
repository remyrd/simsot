package simsot;

import java.awt.Rectangle;

public class Tato extends Enemy {

	protected int movementParam;

	public Tato(int centerX, int centerY) {
		super(centerX,centerY);
		health = 3;
		movementParam = ((int) (Math.random() * 50));
		rectX = new Rectangle(getCenterX() - 25, getCenterY() - 20, 50, 40);
		rectY = new Rectangle(getCenterX() - 20, getCenterY() - 25, 40, 50);
		R = new Rectangle(getCenterX()-20, getCenterY()-20,40,40);

		characterStayPath = "data/tato1.png";
		characterMove1Path = "data/tato2.png";
		characterMove2Path = "data/tato3.png";
		characterDiePath = "data/tatoDie.png";
	}

	// Behavioral Methods
	@Override
	public void callAI() {

		// centerX += speedX;
		// centerY += speedY;

		if (alive == true) {
			// Collision
			rectX.setBounds(getCenterX() - 25, getCenterY() - 20, 50, 40);
			rectY.setBounds(getCenterX() - 20, getCenterY() - 25, 40, 50);
			R.setBounds(getCenterX()-20, getCenterY()-20,40,40);
			// AI
			if (movementTime % 400 == movementParam) {
				setSpeedX(2);
				setSpeedY(0);
			} else if (movementTime % 400 == movementParam + 75) {
				setSpeedX(0);
				setSpeedY(0);
			} else if (movementTime % 400 == movementParam + 100) {
				setSpeedX(-2);
				setSpeedY(0);
			} else if (movementTime % 400 == movementParam + 175) {
				setSpeedX(0);
				setSpeedY(0);
			} else if (movementTime % 400 == movementParam + 200) {
				setSpeedX(0);
				setSpeedY(-2);
			} else if (movementTime % 400 == movementParam + 275) {
				setSpeedX(0);
				setSpeedY(0);
			} else if (movementTime % 400 == movementParam + 300) {
				setSpeedX(0);
				setSpeedY(2);
			} else if (movementTime % 400 == movementParam + 375) {
				setSpeedX(0);
				setSpeedY(0);
			} else if (movementTime == 999) {
				movementTime = 0;
			}
			movementTime++;
			if (getCenterY() > 30 && getCenterY() < 450) {
				int difX = player.getCenterX() - getCenterX();
				int difY = player.getCenterY() - getCenterY();
				int absdifX = Math.abs(difX);
				int absdifY = Math.abs(difY);
				if (absdifX < 80 && difY > 0 && difY < 400) {
					setSpeedX(0);
					setSpeedY(0);
					if (movementTime % 50 == 0) {
						shootDown();
					}
				} else if (absdifX < 80 && difY < 0 && difY > -400) {
					setSpeedX(0);
					setSpeedY(0);
					if (movementTime % 50 == 0) {
						shootUp();
					}
				} else if (absdifY < 80 && difX > 0 && difX < 400) {
					setSpeedX(0);
					setSpeedY(0);
					if (movementTime % 50 == 0) {
						shootRight();
					}
				} else if (absdifY < 80 && difX < 0 && difX > -400) {
					setSpeedX(0);
					setSpeedY(0);
					if (movementTime % 50 == 0) {
						shootLeft();
					}
				}
			}
		}
	}

}
