package com.magiscrita.minefield.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.magiscrita.minefield.model.Board;

@SuppressWarnings("serial")
public class BoardView extends JPanel{
	public BoardView(Board board) {
		setLayout(new GridLayout(board.getLines(), board.getColumns()));
		
		board.fieldByField(field -> add(new FieldButton(field)));
		board.registerObserver(isWin -> {
			SwingUtilities.invokeLater(() -> {
				if(isWin) {
					JOptionPane.showMessageDialog(this, "WIN!");
				} else {
					JOptionPane.showMessageDialog(this, "GAME OVER!");
				}
				
				board.reset();
			});
		});
	}
}
