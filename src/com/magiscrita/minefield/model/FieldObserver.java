package com.magiscrita.minefield.model;

@FunctionalInterface
public interface FieldObserver {
	public void handleEvent(Field field, FieldEvent fieldEvent);
}
