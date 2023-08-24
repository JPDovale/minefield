package com.magiscrita.minefield.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.magiscrita.minefield.model.Field;
import com.magiscrita.minefield.model.FieldEvent;
import com.magiscrita.minefield.model.FieldObserver;

@SuppressWarnings("serial")
public class FieldButton extends JButton implements FieldObserver, MouseListener{
	private final Color BG_DEFAULT = new Color(184, 184, 184);
	private final Color BG_MARKED = new Color(8, 179, 247);
	private final Color BG_EXPLODED = new Color(189, 66, 68);
	private final Color GREEN_TEXT = new Color(0, 100, 0);
	
	private Field field;
	
	public FieldButton(Field field) {
		this.field = field;
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		setOpaque(true);
		
		addMouseListener(this);
		field.registerObserver(this);
	}
	
	@Override
	public void handleEvent(Field field, FieldEvent fieldEvent) {
		switch (fieldEvent) {
			case OPEN: {
				apllyOpenStyle();
				break;
			}
			
			case MARK: {
				applyMarkedStyle();
				break;
			}
			
			case EXPLODE: {
				apllyExplodeStyle();
				break;
			}
			
			default: {
				apllyDefaultStyle();
				break;
			}
		}
	}

	private void apllyDefaultStyle() {
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void apllyExplodeStyle() {
		setBackground(BG_EXPLODED);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void applyMarkedStyle() {
		setBackground(BG_MARKED);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void apllyOpenStyle() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(field.isMined()) {
			setBackground(BG_EXPLODED);
			return;
		}
		
		setBackground(BG_DEFAULT);
		
		switch (field.minesOnNeighbor()) {
		case 1: {
			setForeground(GREEN_TEXT);
			break;
		}
		
		case 2: {
			setForeground(Color.BLUE);
			break;
		}
		
		case 3: {
			setForeground(Color.YELLOW);
			break;
		}
		
		case 4:
		case 5:
		case 6: {
			setForeground(Color.RED);
			break;
		}
		
		default:
			setForeground(Color.PINK);
			break;
		}
		
		String valueOnLabel = !field.neighborsIsSafe() ? field.minesOnNeighbor() + "" : "";
		setText(valueOnLabel);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == 1) {
			field.open();
		}else {
			field.changeMarking();
		}
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
