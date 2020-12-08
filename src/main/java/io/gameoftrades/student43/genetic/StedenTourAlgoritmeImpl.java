package io.gameoftrades.student43.genetic;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.List;

public class StedenTourAlgoritmeImpl implements StedenTourAlgoritme, Debuggable {

    private static final double PERCENTAGE = 0.5;
    private static final double RARE_MUTATION_RATE = 0.015;
    private static final double NORMAL_MUTATION_RATE = 0.075;
    private static final int TOURNAMENT_SIZE = 5;
    private static final boolean ELITISM = true;
    private static final int TRIES = 25;
    private static final int POPULATION_SIZE = 50;
    private static final int SAME_LOWEST = 50;

    private List<Stad> steden;
    private Debugger debugger;

    //Retourneert een stedenlijst met de steden in volgorde van de tour.
    @Override
    public List<Stad> bereken(Kaart kaart, List<Stad> steden) {

        new PathChecker(steden, kaart);
        this.steden = new ArrayList<>(steden);

        Individual lowestOverall = null;
        Individual lowestCurrent;
        int lowest = -1;

        for (int i = 0; i < TRIES; i++) {
            Population population = new Population(POPULATION_SIZE, true, steden);

            int generationCount = 0;
            int sameLowest = 0;

            while(sameLowest < SAME_LOWEST){
                generationCount++;

                lowestCurrent = population.getFittest();
                population = evolvePopulation(population);

                if (lowest == -1 || population.getFittest().getFitness() < lowestOverall.getFitness()){
                    lowestOverall = population.getFittest();
                    lowest++;
                }

                if (lowestCurrent.getFitness() > lowestOverall.getFitness() + 30 && generationCount >= 40 && i > 15){
                    break;
                }

                if (population.getFittest().getFitness() == lowestCurrent.getFitness()){
                    sameLowest++;
                }
            }
            System.out.println(generationCount + " Generations completed.     Fittest: " + population.getFittest().getFitness());
        }

        System.out.println("Lowest fitness: " + lowestOverall.getFitness());
        System.out.println(lowestOverall.getStedenList());
//        debugger.debugSteden(kaart, lowestOverall.getStedenList());
        return lowestOverall.getStedenList();
    }

    //Retourneert een nieuwe populatie die op basis van de beste individuen van de oude populatie is gemaakt.
    private Population evolvePopulation(Population pop){
        Population newPopulation = new Population(POPULATION_SIZE, false, steden);

        if (ELITISM) newPopulation.saveIndividual(0, pop.getFittest());

        int elitismOffset;

        elitismOffset = ELITISM ? 1 : 0;

        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        for (int i = elitismOffset; i < newPopulation.size(); i++) mutate(newPopulation.getIndividual(i));

        for (int i = 0; i < newPopulation.size(); i++) newPopulation.getIndividual(i).generatePaths();

        return newPopulation;
    }

    //Combineert twee van de beste individuen, deze worden geselecteerd in een toernooi.
    private Individual crossover(Individual indiv1, Individual indiv2){
        Individual newSol = new Individual();

        for (int i = 0; i < indiv1.size(); i++) {

            if (Math.random() <= PERCENTAGE){
                getCityFrom(newSol, indiv1, indiv2, i);
            }
            else {
                getCityFrom(newSol, indiv2, indiv1, i);
            }
        }
        return newSol;
    }

    //Helper method bij crossover. Pakt op basis van een willekeurig getal een stad van of de eerste, of de tweede individu
    //en voegt deze toe aan de nieuwe individu. Als ze alletwee al in de lijst zitten van de nieuwe individu, wordt er een
    //stad gepakt uit de originele lijst, die nog niet in de lijst van de nieuwe individu zit.
    private void getCityFrom(Individual newSol, Individual first, Individual second, int i){
        if (!newSol.listContains(first.getGene(i))){
            newSol.addGene(first.getGene(i));
        }
        else if (!newSol.listContains(second.getGene(i))){
            newSol.addGene(second.getGene(i));
        }
        else {
            steden.stream().filter(s -> !newSol.listContains(s)).forEach(newSol::addGene);
        }
    }

    //Op basis van een willekeurig getal wordt gekeken of een bepaalde individu een specifieke aanpassing ondergaat.
    private void mutate(Individual indiv){
        for (int i =  0; i < indiv.size() - 1; i++) {
            if (Math.random() <= RARE_MUTATION_RATE) {
                int random = (int) (Math.random() * ((indiv.size() - 1)));

                Stad stad1 = indiv.getStedenList().get(random);
                Stad stad2 = indiv.getStedenList().get(i);
                indiv.setGene(i, stad1);
                indiv.setGene(random, stad2);
            }

            if (Math.random() <= NORMAL_MUTATION_RATE){
                Stad stad1 = indiv.getStedenList().get(i + 1);
                Stad stad2 = indiv.getStedenList().get(i);
                indiv.setGene(i, stad1);
                indiv.setGene(i + 1, stad2);
            }
        }
    }

    //Retourneert een individu die wordt gekozen uit een vooraf ingesteld getal aan willekeurige individuen. De beste wordt gekozen.
    private Individual tournamentSelection(Population pop){
        Population tournament = new Population(TOURNAMENT_SIZE, false, steden);

        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }

        return tournament.getFittest();
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    public String toString(){
        return "Genetisch Algoritme";
    }
}

