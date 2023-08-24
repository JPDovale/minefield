package com.magiscrita.minefield.model;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private final int X;
	private final int Y;
	
	private boolean isMined = false;
	private boolean isOpen = false;
	private boolean isMarked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	private List<FieldObserver> observers = new ArrayList<>();
	
	Field(int X, int Y){
		this.X = X;
		this.Y = Y;
	};
	
	public void registerObserver(FieldObserver observer) {
		this.observers.add(observer);
	}
	
	private void notifyObservers(FieldEvent event) {
		this.observers.stream().forEach(observer -> observer.handleEvent(this, event));
	}
	
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
	
	public void changeMarking() {
		if(!isOpen) {
			this.isMarked = !this.isMarked;
			
			if(isMarked) {
				notifyObservers(FieldEvent.MARK);
			} else {
				notifyObservers(FieldEvent.UNMARK);
			}
		}
	}
	
	void mine() {
		if(!isOpen) {
			this.isMined = true;
		}
	}
	
	public boolean open() {
		if(!isOpen && !isMarked) {
			if(this.isMined) {
				notifyObservers(FieldEvent.EXPLODE);
				return true;
			}
			
			setIsOpen(true);
			
			if(this.neighborsIsSafe()) {
				neighbors.forEach(neighbor -> neighbor.open());
			}
			
			return true;
		}
		
		return false;
	}
	
	public boolean neighborsIsSafe() {
		return neighbors.stream().noneMatch(neighbor -> neighbor.isMined);
	}
	
	boolean goalCompleted() {
		boolean isOpenSafe = !isMined && isOpen;
		boolean isProtected = isMined && isMarked;
		return isOpenSafe || isProtected;
	}
	
	public int minesOnNeighbor() {
		return (int) this.neighbors.stream().filter(neighbor -> neighbor.isMined).count();
	}
	
	void reset() {
		this.isOpen = false;
		this.isMarked = false;
		this.isMined = false;
		notifyObservers(FieldEvent.RESET);
	}
	
	public boolean isMarked() {
		return this.isMarked;
	}
	
	void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
		
		if(isOpen) {
			notifyObservers(FieldEvent.OPEN);
		}
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

