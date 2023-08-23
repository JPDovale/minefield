package com.magiscrita.minefield.model;

import java.util.ArrayList;
import java.util.List;

import com.magiscrita.minefield.exception.ExplosionException;

public class Field {
	private final int X;
	private final int Y;
	
	private boolean isMined = false;
	private boolean isOpen = false;
	private boolean isMarked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	
	Field(int X, int Y){
		this.X = X;
		this.Y = Y;
	};
	
	boolean addNeighbor(Field neighbor) {
		boolean xIsDiff = this.X != neighbor.X;
		boolean yIsDiff = this.Y != neighbor.Y;
		boolean isDiagonal = yIsDiff && xIsDiff;
		
		int diffBetweenX = Math.abs(this.X - neighbor.X);
		int diffBetweenY = Math.abs(this.Y - neighbor.Y);
		int diffBetweenFields = diffBetweenX + diffBetweenY;
		
		if(diffBetweenFields == 1 && !isDiagonal) {
			neighbors.add(neighbor);
			return true;
		}else if(diffBetweenFields == 2 && isDiagonal) {
			neighbors.add(neighbor);
			return true;
		}
		
		return false;
	}
	
	void changeMarking() {
		if(!isOpen) {
			this.isMarked = !this.isMarked;
		}
	}
	
	void mine() {
		if(!isOpen) {
			this.isMined = true;
		}
	}
	
	boolean open() {
		if(!isOpen && !isMarked) {
			this.isOpen = true;
			
			if(this.isMined) {
				throw new ExplosionException();
			}
			
			if(this.neighborsIsSafe()) {
				neighbors.forEach(neighbor -> neighbor.open());
			}
			
			return true;
		}
		
		return false;
	}
	
	boolean neighborsIsSafe() {
		return neighbors.stream().noneMatch(neighbor -> neighbor.isMined);
	}
	
	boolean goalCompleted() {
		boolean isOpenSafe = !isMined && isOpen;
		boolean isProtected = isMined && isMarked;
		return isOpenSafe || isProtected;
	}
	
	long minesOnNeighbor() {
		return this.neighbors.stream().filter(neighbor -> neighbor.isMined).count();
	}
	
	void reset() {
		this.isOpen = false;
		this.isMarked = false;
		this.isMined = false;
	}
	
	public String toString() {
		if(this.isMarked) {
			return "X";
		}else if(isOpen && isMined) {
			return "*";
		}else if(isOpen && minesOnNeighbor() > 0) {
			return Long.toString(minesOnNeighbor());
		}else if(isOpen) {
			return " ";
		}else {
			return "?";
		}
	}
	
	public boolean isMarked() {
		return this.isMarked;
	}
	
	void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public boolean isOpen() {
		return this.isOpen;
	}
	
	public boolean isMined() {
		return this.isMined;
	}
	
	public int getX() {
		return this.X;
	}
	
	public int getY() {
		return this.Y;
	}
}

