package com.magiscrita.minefield;

import com.magiscrita.minefield.model.Board;
import com.magiscrita.minefield.view.ConsoleBoard;

public class Application {
	public static void main(String[] args) {
		Board board = new Board(10, 10, 9);
		new ConsoleBoard(board);
	}
}
