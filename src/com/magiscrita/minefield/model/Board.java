package com.magiscrita.minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Board implements FieldObserver {
	
	private final int lines;
	private final int columns;
	private final int mines;
	
	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<Boolean>>  observers = new ArrayList<>();
	
	public Board(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		mapNeighbors();
		sortMines();
	}
	
	public void fieldByField(Consumer<Field> function) {
		fields.forEach(function);
	}
	
	public void registerObserver(Consumer<Boolean> observer) {
		this.observers.add(observer);
	}
	
	public void notifyObservers(boolean result) {
		observers.stream().forEach(observer -> observer.accept(result));
	}

	public void openField(int line, int column) {
		fields.parallelStream()
		.filter(field -> field.getX() == line && field.getY() == column)
		.findFirst()
		.ifPresent(field -> field.open());
	}
	
	
	public void changeMarkingField(int line, int column) {
		fields.parallelStream()
		.filter(field -> field.getX() == line && field.getY() == column)
		.findFirst()
		.ifPresent(field -> field.changeMarking());
	}

	private void generateFields() {
		for (int line = 0; line < this.lines; line++) {
			for (int column = 0; column < this.columns; column++) {
				Field field = new Field(line, column);
				field.registerObserver(this);
				fields.add(field);
			}
		}
	}
	
	private void mapNeighbors() {
		for(Field field1: fields) {
			for(Field field2: fields) {
				field1.addNeighbor(field2);
			}
		}
	}
	

	private void sortMines() {
		long minesOnBoard = 0;
		Predicate<Field> mined = field -> field.isMined();
		
		do {
			int any = (int) (Math.random() * fields.size());
			fields.get(any).mine();
			
			minesOnBoard = fields.stream().filter(mined).count();
		} while (minesOnBoard < this.mines);
	}
	
	public boolean goalCompleted() {
		return fields.stream().allMatch(field -> field.goalCompleted());
	}
	
	public void reset() {
		fields.stream().forEach(field -> field.reset());
		this.sortMines();
	}
	
	public void handleEvent(Field field, FieldEvent fieldEvent) {
		if(fieldEvent == FieldEvent.EXPLODE) {
			showMines();
			notifyObservers(false);
		}else if(goalCompleted()) {
			System.out.println("Win!");
			notifyObservers(true);
		}
		
	}
	
	private void showMines() {
		fields.stream()
			.filter(field -> field.isMined() && !field.isMarked())
			.forEach(field -> field.setIsOpen(true));
	}

	public int getLines() {
		return lines;
	}

	public int getColumns() {
		return columns;
	}

	public int getMines() {
		return mines;
	}
	
	
	
}
