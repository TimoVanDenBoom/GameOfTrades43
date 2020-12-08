package io.gameoftrades.ui;

import io.gameoftrades.student43.HandelaarImpl;


/**
 * Toont de visuele gebruikersinterface.
 * 
 * Let op: dit werkt alleen als je de WereldLader hebt geimplementeerd (Anders krijg je een NullPointerException).
 */
public class StudentUI {

	public static void main(String[] args) {
		MainGui.toon(new HandelaarImpl(), "src/test/resources/kaarten/westeros-kaart.txt");

	}
}
