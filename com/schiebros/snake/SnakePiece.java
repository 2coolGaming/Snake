package com.schiebros.snake;

public class SnakePiece {

	public Position currentPosition;

	public SnakePiece(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public void addX(int x) {
		Game.setLastSnakePosition(currentPosition);
		System.out.println("Before Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
		this.currentPosition.x += x;
		System.out.println("After  Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
	}

	public void removeX(int x) {
		Game.setLastSnakePosition(currentPosition);
		System.out.println("Before Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
		this.currentPosition.x -= x;
		System.out.println("After  Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
	}

	public void addY(int y) {
		Game.setLastSnakePosition(currentPosition);
		System.out.println("Before Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
		this.currentPosition.y += y;
		System.out.println("After  Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
	}

	public void removeY(int y) {
		Game.setLastSnakePosition(currentPosition);
		System.out.println("Before Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
		this.currentPosition.y -= y;
		System.out.println("After  Last Position: " + Game.getLastSnakePosition().x + ", " + Game.getLastSnakePosition().y);
	}

}
