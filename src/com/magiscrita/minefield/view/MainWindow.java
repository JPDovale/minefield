package com.magiscrita.minefield.view;

import javax.swing.JFrame;

import com.magiscrita.minefield.model.Board;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	public MainWindow() {	
		Board board = new Board(32, 60, 120);
		BoardView boardView = new BoardView(board);
		
		add(boardView);
		
		setTitle("Minedfield");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
