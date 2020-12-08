package io.gameoftrades.student43.genetic;

import io.gameoftrades.model.kaart.Stad;

import java.util.List;

public class Population {

    private Individual[] individuals;


    public Population(int populationSize, boolean initialise, List<Stad> steden){
        individuals = new Individual[populationSize];

        if (initialise){
            for (int i = 0; i < individuals.length; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual(steden);
                individuals[i] = newIndividual;
            }
        }
    }

    public Individual getIndividual (int i){
        return individuals[i];
    }

    //Retourneert de fitste individu uit de populatie.
    public Individual getFittest(){
        Individual fittest = individuals[0];

        for (int i = 0; i < individuals.length; i++) {
            if(fittest.getFitness() >= getIndividual(i).getFitness()){
                fittest = individuals[i];
            }
        }

        return fittest;
    }

    public double getAverageFitness(){
        double total = 0;

        for (int i = 0; i < individuals.length; i++) {
            total = total + individuals[i].getFitness();
        }

        return total / individuals.length;
    }

    public int size(){
        return individuals.length;
    }

    public void saveIndividual(int i, Individual individual){
        individuals[i] = individual;
    }
}
