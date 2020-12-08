package io.gameoftrades.student43.snelstepad;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.*;


import java.util.*;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme{

    private ArrayList<Node> openList;
    private ArrayList<Node> closedList;
    private ArrayList<Richting> richtingList;
    private Comparator<Node> terreinSorter;
    private Node current;

    public SnelstePadAlgoritmeImpl(){
        this.openList = new ArrayList<>();
        this.closedList = new ArrayList<>();
        this.richtingList = new ArrayList<>();
    }

    //returns het snelste Pad, van Coordinaat start naar Coordinaat eind.
    @Override
    public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {

        if (start.getY() < 0 || start.getX() < 0 || kaart.getTerreinOp(start).getTerreinType() == TerreinType.ZEE){
            return null;
        }

        this.terreinSorter = (o1, o2) -> {
            if(o2.calculatefCost(eind) < o1.calculatefCost(eind)){
                return 1;
            }
            if(o2.calculatefCost(eind) > o1.calculatefCost(eind)){
                return -1;
            }
            return 0;
        };

        openList.add(new Node(null, kaart.getTerreinOp(start)));


        while (openList.size() >= 1){
            current = getLowestfCostNode();

            openList.remove(current);
            closedList.add(current);

            Richting[] mogelijkeRichtingen = current.getTerrein().getMogelijkeRichtingen();

            for (Richting richting: mogelijkeRichtingen) {

                Node node = new Node(current, kaart.kijk(current.getTerrein(), richting));

                // Checkt of de node naast de huidige node minder kost als deze al in de open lijst zit.
                // Overbodig zonder diagonale beweegrichtingen.
                if (checkOpenList(node) && !checkClosedList(node)){
                    for(int i = 0; i < this.openList.size(); i++){
                        if(this.openList.get(i).getCoordinaat().equals(node.getCoordinaat())){
                            if (this.openList.get(i).getgCost() > node.getgCost()){
                                node.setParent(current);
                                node.setgCost(node.calculategCost());
                            }
                        }
                    }
                }

                if (!checkClosedList(node) && !checkOpenList(node)){
                    openList.add(node);
                }
            }

            if (current.getCoordinaat().equals(eind)){
//                Pad pad = retracePath();
//                System.out.println("Kortste pad: " + pad.getTotaleTijd());
                return retracePath();
            }
        }
        return null;
    }

    // return Node met laagste fCost, dit is de volgende Node die gecheckt moet worden.
    private Node getLowestfCostNode(){
        Collections.sort(openList, terreinSorter);
        return openList.get(0);
    }

    //Leegt alle lijsten die gebruikt worden.
    private void clearLists(){
        openList.clear();
        closedList.clear();
        richtingList.clear();
    }

    // Loopt het gevonden pad na.
    // returns een Pad met de gevonden looprichtingen en de totale tijd.
    private Pad retracePath(){
        int totaleTijd = current.getgCost();

        while (current.hasParent()){
            richtingList.add(Richting.tussen(current.getCoordinaat(), current.getParent().getCoordinaat()).omgekeerd());
            current = current.getParent();
        }

        Collections.reverse(richtingList);
        Richting[] richtingen = new Richting[richtingList.size()];

        for (int i = 0; i < richtingList.size(); i++) {
            richtingen[i] = richtingList.get(i);
        }

        clearLists();

//        System.out.println("Aantal Stappen: " +  richtingen.length + "\nTotale tijd: " + totaleTijd );

        return new PadImpl(totaleTijd, richtingen);
    }

    //checkt of de gegeven Node al in de openList zit.
    private boolean checkOpenList(Node node){
        for(int i = 0; i < this.openList.size(); i++){
            if(this.openList.get(i).getCoordinaat().equals(node.getCoordinaat())){
                return true;
            }
        }
        return false;
    }

    //checkt of de gegeven Node al in de closedList zit.
    private boolean checkClosedList(Node node){
        for(int i = 0; i < this.closedList.size(); i++){
            if(this.closedList.get(i).getCoordinaat().equals(node.getCoordinaat())){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return "A* Algoritme";
    }

}
