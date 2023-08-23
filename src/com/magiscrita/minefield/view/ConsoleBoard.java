package com.magiscrita.minefield.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import com.magiscrita.minefield.exception.CloseException;
import com.magiscrita.minefield.exception.ExplosionException;
import com.magiscrita.minefield.model.Board;

public class ConsoleBoard {
	private Board board;
	private Scanner console = new Scanner(System.in);
	
	public ConsoleBoard(Board board) {
		this.board = board;
		
		executeGame();
	}
	
	private void executeGame() {
		try {
			
			boolean continueRuuning = true;
			
			while(continueRuuning) {
				tick();
				
				System.out.println("Outra partida? (Y/n) ");
				String response = console.nextLine();
				
				if(response.equalsIgnoreCase("n")) {
					continueRuuning = false;
				}else {
					board.reset();
				}
			}
			
		} catch (CloseException e) {
			System.out.println("Tchal!!");
		} finally {
			console.close();
		}
	}
	
	private void tick() {
		try {
			
			while(!board.goalCompleted()) {
				System.out.println(board);
				
				String response = makeQuestion("Digite (x, y): ");
				
				Iterator<Integer> xy = Arrays
						.stream(response.split(","))
						.map(el -> Integer.parseInt(el.trim()))
						.iterator();
				
				response = makeQuestion("1 para Abrir | 2 para (Des)marcar: ");
				
				if (response.equalsIgnoreCase("1")) {
					board.openField(xy.next(), xy.next());
				}else if(response.equalsIgnoreCase("2")) {
					board.changeMarkingField(xy.next(), xy.next());
				}
			}
			
			System.out.println(board);
			System.out.println("Você ganou!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("Você perdeu!");
		}
	}
	
	private String makeQuestion(String question) {
		System.out.print(question);
		String response = console.nextLine();
		
		if(response.equalsIgnoreCase("sair")) {
			throw new CloseException();
		}
		
		return response;
	}
}
