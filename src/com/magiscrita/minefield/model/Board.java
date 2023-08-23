package com.magiscrita.minefield.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.magiscrita.minefield.exception.ExplosionException;

public class Board {
	
	private int lines;
	private int columns;
	private int mines;
	
	private final List<Field> fields = new ArrayList<>();

	public Board(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		mapNeighbors();
		sortMines();
	}

	public void openField(int line, int column) {
		try {
			fields.parallelStream()
			.filter(field -> field.getX() == line && field.getY() == column)
			.findFirst()
			.ifPresent(field -> field.open());
		} catch (ExplosionException e) {
			fields.forEach(field -> field.setIsOpen(true));
			throw e;
		}
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
				fields.add(new Field(line, column));
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  ");
		for (int column = 0; column < this.columns; column++) {
			sb.append(" ");
			sb.append(column);
			sb.append(" ");
		}
		 
		sb.append("\n");
		
		int i = 0;
		for (int line = 0; line < this.lines; line++) {
			sb.append(line);
			sb.append(" ");
			
			for (int column = 0; column < this.columns; column++) {
				sb.append(" ");
				sb.append(fields.get(i));
				sb.append(" ");
				
				i++;
			}
			sb.append("\n");
		}

		
		return sb.toString();
	}
}
