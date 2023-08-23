package com.magiscrita.minefield.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.magiscrita.minefield.exception.ExplosionException;

public class FieldTest {

	private Field field;

	@BeforeEach
	void initializer() {
		field = new Field(3, 3);
	}

	@Test
	void testRealNeifhborLeft() {
		Field neighbor = new Field(3, 2);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborRight() {
		Field neighbor = new Field(3, 4);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborUp() {
		Field neighbor = new Field(2, 3);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborDown() {
		Field neighbor = new Field(3, 4);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborUpLeft() {
		Field neighbor = new Field(2, 2);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborUpRight() {
		Field neighbor = new Field(4, 2);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}
	
	@Test
	void testRealNeifhborDownLeft() {
		Field neighbor = new Field(2, 4);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}

	@Test
	void testRealNeifhborDownRight() {
		Field neighbor = new Field(4, 4);
		boolean isAdded = field.addNeighbor(neighbor);
		assertTrue(isAdded);
	}
	
	@Test
	void testSobreposition() {
		Field neighbor = new Field(3, 3);
		boolean isAdded = field.addNeighbor(neighbor);
		assertFalse(isAdded);
	}
	
	@Test
	void testUnrealNeighbor() {
		Field neighbor = new Field(4, 5);
		boolean isAdded = field.addNeighbor(neighbor);
		assertFalse(isAdded);
	}
	
	@Test
	void testDefaultIsMarked() {
		assertFalse(field.isMarked());
	}
	
	@Test
	void testChangeMarking() {
		field.changeMarking();
		assertTrue(field.isMarked());
	}
	
	@Test
	void testChangeMarkingTowTimes() {
		field.changeMarking();
		field.changeMarking();
		assertFalse(field.isMarked());
	}
	
	@Test
	void testOpenNotMarkedAndNotMined() {
		assertTrue(field.open());
	}
	
	@Test
	void testOpenMarkedAndNotMined() {
		field.changeMarking();
		assertFalse(field.open());
	}
	
	@Test
	void testOpenMarkedAndMined() {
		field.mine();
		field.changeMarking();
		assertFalse(field.open());
	}
	
	@Test
	void testOpenNotMarkedAndMined() {
		field.mine();
		
		assertThrows(ExplosionException.class, () -> {
			field.open();
		});
	}
	
	@Test
	void testOpenWithNeighbors() {
		Field field11 = new Field(1, 1);
		Field field22 = new Field(2, 2);
		
		field22.addNeighbor(field11);
		field.addNeighbor(field22);
		field.open();
		
		assertTrue(field11.isOpen() && field22.isOpen() && field.isOpen());
	}
	
	@Test
	void testOpenWithNeighborsMined() {
		Field field11 = new Field(1, 1);
		Field field12 = new Field(1, 2);
		field12.mine();
		
		Field field22 = new Field(2, 2);
		
		field22.addNeighbor(field11);
		field22.addNeighbor(field12);
		
		field.addNeighbor(field22);
		field.open();
		
		assertTrue(!field11.isOpen() && !field12.isOpen() && field22.isOpen() && field.isOpen());
	}
	
}
