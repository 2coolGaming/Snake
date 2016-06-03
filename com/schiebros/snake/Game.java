package com.schiebros.snake;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = -229397690579876143L;
	public static final int WIDTH = 20;
	public static final int HEIGHT = WIDTH;
	public static final int SCALE = 30;
	public static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public static int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public final String title;
	public JFrame frame;
	public boolean running = false;
	public boolean gameOver = false;
	public int fruitsEaten = 0;

	public Position fruitPosition = new Position(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT));
	public SnakePiece snakePosition = getSnakeStarter();
	public Direction snakeDirection = null;

	public Game(String title) {
		this.title = title;
		System.out.println("Starting with title of " + title);
		frame = new JFrame();
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		setSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle(title + " | FPS: 0 UPS: 0");
		/*
		 * try { frame.setIconImage(ImageIO.read(new File("icon.png"))); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		addKeyListener(this);
		frame.addKeyListener(this);
		running = true;
		new Thread(this).start();
	}

	public SnakePiece getSnakeStarter() {
		SnakePiece piece = new SnakePiece(new Position(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT)));
		if (piece.currentPosition.x == fruitPosition.x && piece.currentPosition.y == fruitPosition.y) {
			return getSnakeStarter();
		}
		return piece;
	}

	public static void main(String[] args) {
		new Game("Snake Game 1.0");
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 10.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | FPS: " + frames + " UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	public int tickCounter = 0;

	public void tick() {
		if (snakeDirection == Direction.LEFT) {
			snakePosition.removeX(1);
		}
		if (snakeDirection == Direction.RIGHT) {
			snakePosition.addX(1);
		}
		if (snakeDirection == Direction.UP) {
			snakePosition.removeY(1);
		}
		if (snakeDirection == Direction.DOWN) {
			snakePosition.addY(1);
		}
		if (snakePosition.currentPosition.x == fruitPosition.x && snakePosition.currentPosition.y == fruitPosition.y) {
			eatFruit();
		}
		tickCounter++;
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		{
			renderPixels();
			g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
			if (snakePosition.currentPosition.x < 0) {
				System.exit(0);
			}
			if (snakePosition.currentPosition.x > WIDTH - 1) {
				System.exit(0);
			}
			if (snakePosition.currentPosition.y > HEIGHT - 1) {
				System.exit(0);
			}
			if (snakePosition.currentPosition.y < 0) {
				System.exit(0);
			}
		}
		g.dispose();
		bs.show();
	}

	public void renderPixels() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (x == snakePosition.currentPosition.x && y == snakePosition.currentPosition.y)
					pixels[x + y * WIDTH] = 0xFFFFFF;
				else if (x == fruitPosition.x && y == fruitPosition.y)
					pixels[x + y * WIDTH] = 0xAAAAAA;
				else
					pixels[x + y * WIDTH] = 0x000000;
			}

		}
	}

	public void eatFruit() {
		fruitsEaten++;
		fruitPosition = new Position(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT));
		System.out.println("Total Score: " + fruitsEaten);	
	}

	public void keyTyped(KeyEvent e) {
	}

	/*
	 * W = 87 A = 65 S = 83 D = 68
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 87) {
			snakeDirection = Direction.UP;
		}
		if (e.getKeyCode() == 83) {
			snakeDirection = Direction.DOWN;
		}
		if (e.getKeyCode() == 68) {
			snakeDirection = Direction.RIGHT;
		}
		if (e.getKeyCode() == 65) {
			snakeDirection = Direction.LEFT;
		}
	}

	public void keyReleased(KeyEvent e) {
	}
	
	public enum Direction {

		LEFT(1), RIGHT(2), UP(3), DOWN(4);

		private int id;

		Direction(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public static Direction getRandomDirection() {
			switch (new Random().nextInt(4)) {
			case 0:
				return Direction.LEFT;
			case 1:
				return Direction.RIGHT;
			case 2:
				return Direction.UP;
			case 3:
				return Direction.DOWN;
			default:
				return null;
			}
		}
	}

}
