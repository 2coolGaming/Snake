package com.schiebros.snake;

public class SnakePiece {

	public Position currentPosition;
	public Position lastPosition;
	
	public SnakePiece(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public SnakePiece(Position currentPosition, Position lastPosition) {
		this.currentPosition = currentPosition;
		this.lastPosition = lastPosition;
	}
	
	public void addX(int x) {
		lastPosition = currentPosition;
		this.currentPosition.x += x;
	}
	
	public void removeX(int x) {
		lastPosition = currentPosition;
		this.currentPosition.x -= x;
	}
	
	public void addY(int y) {
		lastPosition = currentPosition;
		this.currentPosition.y += y;
	}
	
	public void removeY(int y) {
		lastPosition = currentPosition;
		this.currentPosition.y -= y;
	}

}
