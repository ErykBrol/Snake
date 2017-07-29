package waffles.snake.mob;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {
	public int x = 180;
	public int y = 180;

	// Make sure head size is divisible by vel, sans remainder
	public int vel = 5;
	public int xvel = 0;
	public int yvel = 0;

	public int headSize = 30;
	public Color headColor = (new Color(65,105,225));

	public int score = 0;

	public int target = headSize;

	public boolean dead = false;

	private boolean alreadyExecuted = false;

	public static List<BodyPart> bpList = new ArrayList<BodyPart>();
	public static List<Egg> eggList = new ArrayList<Egg>();

	Random rand = new Random();

	public void spawnHead() {
		if (!alreadyExecuted) {
			BodyPart head = new BodyPart();
			bpList.add(head);
			
			head.x = this.x;
			head.y = this.y;

			head.xvel = this.vel;

			head.bodySize = this.headSize;
			head.bodyColor = this.headColor;
			
			alreadyExecuted = true;
		}
	}

	public void spawnBp() {
		BodyPart bp = new BodyPart();
		bpList.add(bp);
		
		
		for (int i = 1; i < bpList.size(); i++) {
			bpList.get(i).x = bpList.get(i - 1).lastX;
			bpList.get(i).y = bpList.get(i - 1).lastY;

		}	
		bp.bodySize = this.headSize;
	}

	public void spawnEgg(int width, int height) {
		Egg e = new Egg();
		eggList.add(e);
	}

	public void eggCollision(int width, int height) {
		if (bpList.get(0).getBounds().intersects(eggList.get(0).getBounds())) {
			score += 5;
			spawnBp();

			eggList.get(0).x = (rand.nextInt(24) + 0) * 30;
			eggList.get(0).y = (rand.nextInt(18) + 0) * 30;
			
			for (int i = 1; i < bpList.size(); i++) {
				while (eggList.get(0).getBounds().intersects(bpList.get(i).getBounds())) {
					eggList.get(0).x = (rand.nextInt(24) + 0) * 30;
					eggList.get(0).y = (rand.nextInt(18) + 0) * 30;
				}
			}
		}
	}

	public void updateBp() {
		if (bpList.get(0).x % target == 0 && bpList.get(0).y % target == 0) {
			for (int i = 1; i < bpList.size(); i++) {

				// "Send-out" the lastX and lastY coordinates
				bpList.get(i).lastX = bpList.get(i).x;
				bpList.get(i).lastY = bpList.get(i).y;

				// Actually update the current coordinates to the last ones
				// of previous body part
				bpList.get(i).x = bpList.get(i - 1).lastX;
				bpList.get(i).y = bpList.get(i - 1).lastY;
			}
		}
	}

	public void updateHead(int height, int width) {
		// Increase x and y coordinates by their velocities respectively
		bpList.get(0).x += bpList.get(0).xvel;
		bpList.get(0).y += bpList.get(0).yvel;
		
		// If we reach a grid marker, "send-out" our x and y as lastX and lastY
		// for body parts to be drawn to
		if (bpList.get(0).x % target == 0 && bpList.get(0).y % target == 0) {
			if (bpList.get(0).xvel > 0) {
				bpList.get(0).lastX = bpList.get(0).x;
				bpList.get(0).lastY = bpList.get(0).y;
			} else if (bpList.get(0).xvel < 0) {
				bpList.get(0).lastX = bpList.get(0).x;
				bpList.get(0).lastY = bpList.get(0).y;
			} else if (bpList.get(0).yvel > 0) {
				bpList.get(0).lastX = bpList.get(0).x;
				bpList.get(0).lastY = bpList.get(0).y;
			} else if (bpList.get(0).yvel < 0) {
				bpList.get(0).lastX = bpList.get(0).x;
				bpList.get(0).lastY = bpList.get(0).y;
			}
		}

		// If hits the walls then dead
		if (bpList.get(0).x < 0)
			dead = true;
		if (bpList.get(0).x + bpList.get(0).bodySize > width)
			dead = true;
		if (bpList.get(0).y < 0)
			dead = true;
		if (bpList.get(0).y + bpList.get(0).bodySize > height)
			dead = true;

		// If hits it's own body then dead
		for (int i = 2; i < bpList.size(); i++) {
			if (bpList.get(0).getBounds().intersects(bpList.get(i).getBounds())) {
				dead = true;
			}
		}
		eggCollision(width, height);
	}
}