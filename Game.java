package waffles.snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import waffles.snake.input.Keyboard;
import waffles.snake.mob.Snake;

public class Game extends Canvas {
	private static final long serialVersionUID = 1L;

	public static int width = 300;
	public static int height = width / 16 * 10;
	public static int scale = 3;

	public static int currentIn;
	
	private static Color bckg = new Color(176,224,230); //

	public static String title = "Snake";

	private static boolean running = false;

	Keyboard key = new Keyboard();
	JFrame frame = new JFrame();
	Snake snake = new Snake();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		frame.addKeyListener(key);
	}

	public void run() throws IOException {		
		running = true;
		
		long timeLast = System.nanoTime();
		double timer = System.currentTimeMillis();
		final double nanoConstant = 1000000000.0 / 60.0;
		double delta = 0;

		int updates = 0;
		int frames = 0;
		
		snake.spawnHead();
		snake.spawnBp();
		snake.spawnBp();
		snake.spawnBp();
		snake.spawnBp();
		snake.spawnEgg(getWidth(), getHeight());
		
		// Main game loop
		while (running) {			
			long timeNow = System.nanoTime();
			delta += (timeNow - timeLast) / nanoConstant;
			timeLast = timeNow;

			// Making sure we run at a pre-set rate. See nanoConstant.
			while (delta >= 1) {
				
				update();
				render();
				updates++;
				frames++;

				delta--;
			}

			// FPS and UPS counter
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates + " ups " + frames + " fps ");
				updates = 0;
				frames = 0;
			}
			
			// If dead then stop game
			if (snake.dead) {
				break;
			}

		}
		// Fail-safe for accidentally exiting game loop
		stop();
	}

	public void stop() {
		running = false;
	}
	
	public void keyIn() {
		// "Buffered" key input
		if (key.up) {
			currentIn = 1;
		}
		if (key.down){
			currentIn = 2;
		}
		if (key.left) {
			currentIn = 3;
		}
		if (key.right) {
			currentIn = 4;
		}
	}

	@SuppressWarnings("static-access")
	public void keyActions() {
		// Determines what to do when specific key is in "buffer"
		if (currentIn == 1 && snake.bpList.get(0).x % snake.target == 0 && snake.bpList.get(0).y % snake.target == 0) {
				snake.bpList.get(0).xvel = 0;
				snake.bpList.get(0).yvel = -snake.vel;
		}
		if (currentIn == 2 && snake.bpList.get(0).x % snake.target == 0 && snake.bpList.get(0).y % snake.target == 0) {
				snake.bpList.get(0).xvel = 0;
				snake.bpList.get(0).yvel = snake.vel;
		}
		if (currentIn == 3 && snake.bpList.get(0).x % snake.target == 0 && snake.bpList.get(0).y % snake.target == 0) {
				snake.bpList.get(0).yvel = 0;
				snake.bpList.get(0).xvel = -snake.vel;
		}
		if (currentIn == 4 && snake.bpList.get(0).x % snake.target == 0 && snake.bpList.get(0).y % snake.target == 0) {
				snake.bpList.get(0).yvel = 0;
				snake.bpList.get(0).xvel = snake.vel;
		}
	}

	public void update() {
		// Update everything from keyboard input to movement
		key.update();
		keyIn();
		keyActions();
		snake.updateHead(getHeight(), getWidth());
		snake.updateBp();
	}

	@SuppressWarnings("static-access")
	public void render() throws IOException {
		// Create new buffer strategy for beautiful drawing
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		// Set background color here
		g.setColor(bckg);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Set some variables locally to avoid using huge draw statements for
		// snake
		Color headColor = snake.bpList.get(0).bodyColor;
		int x = snake.bpList.get(0).x;
		int y = snake.bpList.get(0).y;
		int size = snake.bpList.get(0).bodySize;

		// ----------------- DRAW THINGS HERE ---------------------

		// Draw snake head
		g.setColor(headColor);
		g.fillRect(x, y, size, size);
		
		// Iterate through and draw all body parts
		for (int i = 1; i < snake.bpList.size(); i++) {
			g.setColor(snake.bpList.get(i).bodyColor);
			g.fillRect(snake.bpList.get(i).x, snake.bpList.get(i).y, snake.bpList.get(i).bodySize, snake.bpList.get(i).bodySize);
		}

		// ------------------ GRID --------------------
		/*
		for (int j = 30; j < (getHeight()); j += 30) {
			g.setColor(Color.white);
			g.drawLine(0, j, getWidth(), j);
		}
		for (int j = 30; j < (getWidth()); j += 30) {
			g.setColor(Color.white);
			g.drawLine(j, 0, j, getWidth());
		}
		*/
		
		// Draw eggs
		for (int i = 0; i < snake.eggList.size(); i++) {
			g.setColor(new Color(255,165,0));
			g.fillRect(snake.eggList.get(i).x,snake.eggList.get(i).y,snake.eggList.get(i).size,snake.eggList.get(i).size );
		}
		
		int scoreX = 5;
		int scoreY = 25;
		
		if (!snake.dead) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 30)); 
			g.drawString("Score: ", scoreX, scoreY);
			g.drawString(String.valueOf(snake.score), scoreX + 90, scoreY);
		} else {
			g.setColor(Color.gray);
			g.setFont(new Font("Arial", Font.PLAIN, 90)); 
			g.drawString("GAME OVER!", 160, 250);
			g.setFont(new Font("Arial", Font.PLAIN, 70)); 
			g.drawString("Final Score: " + String.valueOf(snake.score), 225, 350);
		}
		
		// Throw out any graphics we aren't using anymore. No memory leaks :)
		g.dispose();

		bs.show();
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		// Slap everything together in our main and initiate launch
		// via game.run
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.add(game);
		game.frame.setTitle(game.title);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.run();
	}
}