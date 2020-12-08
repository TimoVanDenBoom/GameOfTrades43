package io.gameoftrades.student43.snelstepad;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

public class Node {

    private Node parent;
    private int gCost;
    private Terrein terrein;

    public Node(Node parent, Terrein terrein){
        this.parent = parent;
        this.terrein = terrein;
        this.gCost = calculategCost();

    }

    public boolean hasParent(){
        return this.parent != null;
    }

    public int calculategCost(){
        if(!hasParent()){
            return 0;
        }
        else{
            return terrein.getTerreinType().getBewegingspunten() + parent.getgCost();
        }
    }



    public double calculatefCost(Coordinaat eind){
        return this.terrein.getCoordinaat().afstandTot(eind) + this.gCost;
    }

    public Coordinaat getCoordinaat(){
        return this.terrein.getCoordinaat();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public Terrein getTerrein() {
        return terrein;
    }

    public void setTerrein(Terrein terrein) {
        this.terrein = terrein;
    }
}
