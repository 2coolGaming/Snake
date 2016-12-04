package com.schiebros.snake;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = -229397690579876143L;
	public static final int WIDTH = 35;
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
	public Position easyPosition = new Position(12, HEIGHT / 2 - 2);
	public Position mediumPosition = new Position(15, HEIGHT / 2 - 2);
	public Position hardPosition = new Position(18, HEIGHT / 2 - 2);
	public Position superHardPosition = new Position(21, HEIGHT / 2 - 2);
	public Position last
	public Direction snakeDirection = null;
	public boolean locked = false;

	public List<Integer> password = new ArrayList<Integer>();
	public int difficulty = 1;
	public boolean selectingDifficulty = true;
	public double amountOfTicks;
	public double ns;
	public List<Position> tail = new ArrayList<Position>();

	public Game(String title, boolean locked) {
		this.title = title;
		this.locked = locked;
		System.out.println("Starting with title of " + title);
		frame = new JFrame();
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		setSize(new Dimension(WIDTH * SCALE - SCALE / 2, HEIGHT * SCALE - SCALE / 2));
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle(title + " | FPS: 0 UPS: 0");
		frame.setResizable(false);
		/*
		 * try { frame.setIconImage(ImageIO.read(new File("icon.png"))); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		addKeyListener(this);
		frame.addKeyListener(this);
		running = true;

		password.add(8);
		password.add(5);
		password.add(3);
		password.add(4);
		password.add(9);

		if (locked) {
			lock();
		}

		new Thread(this).start();
	}

	public void lock() {
		frame.setTitle("Please enter password using numberpad to continue.");

		while (password.size() > 0) {
			System.out.println("Password digits remaining: " + password.size());
		}
		System.out.println("Password entered");
	}

	public static void main(String[] args) {
		new Game("Snake Game 1.0", false);
	}

	public void run() {
		long lastTime = System.nanoTime();
		amountOfTicks = 5.0;
		ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				String ds = "";
				switch (difficulty) {
				case 1:
					ds = "Easy";
					break;
				case 2:
					ds = "Medium";
					break;
				case 3:
					ds = "Hard";
					break;
				case 4:
					ds = "Super hard";
					break;
				}
				frame.setTitle(
						title + " | FPS: " + frames + " SCR: " + fruitsEaten + (difficulty != -1 ? " D: " + ds : ""));
				frames = 0;
			}

		}
	}

	public int tickCounter = 0;

	public void tick() {
		amountOfTicks = 5.0 * difficulty;
		if (difficulty == 4) {
			amountOfTicks = 30;
		}
		ns = 1000000000 / amountOfTicks;
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
				restart();
			}
			if (snakePosition.currentPosition.x > WIDTH - 1) {
				restart();
			}
			if (snakePosition.currentPosition.y > HEIGHT - 1) {
				restart();
			}
			if (snakePosition.currentPosition.y < 0) {
				restart();
			}
		}
		g.dispose();
		bs.show();
	}

	public void restart() {
		frame.dispose();
		new Game("Snake Game 1.0", false);
	}

	public void renderPixels() {

		if (selectingDifficulty) {
			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					if (x == easyPosition.x && y == easyPosition.y)
						pixels[x + y * WIDTH] = 0xF4F142;
					else if (x == mediumPosition.x && y == mediumPosition.y)
						pixels[x + y * WIDTH] = 0xF4B042;
					else if (x == hardPosition.x && y == hardPosition.y)
						pixels[x + y * WIDTH] = 0xF45042;
					else if (x == superHardPosition.x && y == superHardPosition.y)
						pixels[x + y * WIDTH] = 0x5B1010;
					else
						pixels[x + y * WIDTH] = 0x000000;
						
				}
			}
			if (difficulty == 1) {
				pixels[easyPosition.x + (easyPosition.y + 3) * WIDTH] = 0xFFFFFF;
			}
			
			if (difficulty == 2) {
				pixels[mediumPosition.x + (mediumPosition.y + 3) * WIDTH] = 0xFFFFFF;
			}
			
			if (difficulty == 3) {
				pixels[hardPosition.x + (hardPosition.y + 3) * WIDTH] = 0xFFFFFF;
			}
			
			if (difficulty == 4) {
				pixels[superHardPosition.x + (superHardPosition.y + 3) * WIDTH] = 0xFFFFFF;
			}
			
			
		} else {
			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					if (x == snakePosition.currentPosition.x && y == snakePosition.currentPosition.y) {
						pixels[x + y * WIDTH] = 0xFFFFFF;
					} else if (x == fruitPosition.x && y == fruitPosition.y) {
						pixels[x + y * WIDTH] = 0xAAAAAA;
					} else {
						boolean tailB = false;
						for (Position p : tail) {
							if (x == p.x && y == p.y) {
								pixels[x + y * WIDTH] = 0xFFFFFF;
								tailB = true;
							}
						}
						if (!tailB) {
							pixels[x + y * WIDTH] = 0x000000;
						}
					}
				}

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

		if (password.size() != 0) {
			switch (e.getKeyCode()) {

			case KeyEvent.VK_NUMPAD0:
				if (password.get(0) == 0) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;

			case KeyEvent.VK_NUMPAD1:
				if (password.get(0) == 1) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;

			case KeyEvent.VK_NUMPAD2:
				if (password.get(0) == 2) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD3:
				if (password.get(0) == 3) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD4:
				if (password.get(0) == 4) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD5:
				if (password.get(0) == 5) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD6:
				if (password.get(0) == 6) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD7:
				if (password.get(0) == 7) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;
			case KeyEvent.VK_NUMPAD8:
				if (password.get(0) == 8) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;

			case KeyEvent.VK_NUMPAD9:
				if (password.get(0) == 9) {
					password.remove(0);
				} else {
					password.clear();
					password.add(8);
					password.add(5);
					password.add(3);
					password.add(4);
					password.add(9);
				}
				break;

			}
		}

		if (e.getKeyCode() == 87 || e.getKeyCode() == KeyEvent.VK_UP) {

			if (selectingDifficulty) {
				return;
			}

			if (snakeDirection == Direction.DOWN) {
				return;
			}
			snakeDirection = Direction.UP;
			return;
		}
		if (e.getKeyCode() == 83 || e.getKeyCode() == KeyEvent.VK_DOWN) {

			if (selectingDifficulty) {
				return;
			}

			if (snakeDirection == Direction.UP) {
				return;
			}
			snakeDirection = Direction.DOWN;
			return;
		}
		if (e.getKeyCode() == 68 || e.getKeyCode() == KeyEvent.VK_RIGHT) {

			if (selectingDifficulty) {
				if (difficulty == 4) {
					difficulty = 1;
				} else {
					difficulty++;
				}
				return;
			}

			if (snakeDirection == Direction.LEFT) {
				return;
			}
			snakeDirection = Direction.RIGHT;
			return;
		}
		if (e.getKeyCode() == 65 || e.getKeyCode() == KeyEvent.VK_LEFT) {

			if (selectingDifficulty) {
				if (difficulty == 1) {
					difficulty = 4;
				} else {
					difficulty--;
				}
				return;
			}

			if (snakeDirection == Direction.RIGHT) {
				return;
			}
			snakeDirection = Direction.LEFT;
			return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER && selectingDifficulty) {
			selectingDifficulty = false;
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
