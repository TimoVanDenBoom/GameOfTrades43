package io.gameoftrades.student43.genetic;

import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual {

    private List<Stad> stedenList;
    private List<Path> paden;
    private int fitness;

    public Individual (){
        this.paden = new ArrayList<>();
        this.stedenList = new ArrayList<>();
        this.fitness = 0;
    }

    // Maakt een willekeurige stedenlijst aan voor een individu.
    public void generateIndividual(List<Stad> steden){
        this.stedenList = new ArrayList<>(steden);
        Collections.shuffle(this.stedenList);
        generatePaths();
    }

    //Haalt de paden van een individu op uit de reeds berekende lijst van paden.
    public void generatePaths(){
        for (int i = 0; i < stedenList.size() - 1; i++) {
            paden.add(PathChecker.getPath(stedenList.get(i).getCoordinaat(), stedenList.get(i + 1).getCoordinaat()));
        }
    }


    public Stad getGene(int i){
        return this.stedenList.get(i);
    }

    public void setGene(int i, Stad stad){
        this.stedenList.set(i, stad);
        this.fitness = 0;
    }

    public void addGene(Stad stad){
        this.stedenList.add(stad);
    }

    public int size(){
        return stedenList.size();
    }

    public int getFitness(){
        if (fitness == 0){
            fitness = paden.stream().map(Path::getLength).reduce(0, (i, j) -> i + j);
        }
        return fitness;
    }

    public List<Stad> getStedenList() {
        return stedenList;
    }

    public List<Path> getPaden() {
        return paden;
    }

    //Checkt of een stad in de lijst van steden van een individu zit.
    public boolean listContains(Stad stad){
        for (Stad s : stedenList){
            if(s.getCoordinaat().equals(stad.getCoordinaat())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }


}