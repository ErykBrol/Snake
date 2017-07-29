package waffles.snake.mob;

import java.awt.Color;
import java.awt.Rectangle;

public class BodyPart {
	public int x;
	public int y;
	
	public int xvel, yvel;
	
	public int lastX, lastY;

	public int bodySize = 35;
	public Color bodyColor = new Color(65,105,225);
	
	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.bodySize, this.bodySize);
	}
}