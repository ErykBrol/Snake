package waffles.snake.mob;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class Egg {
	public static Random rand2 = new Random();
	
	public int x = (rand2.nextInt(24) + 0) * 30;
	public int y = (rand2.nextInt(18) + 0) * 30;
	
	public int size = 30;
	public Color eggColor = new Color(0,0,0);
	
	public int numEggs = 0;
	public int score;
	
	public boolean eaten = false;
	
	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.size, this.size);
	}
}