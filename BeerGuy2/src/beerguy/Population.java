/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

/**
 *
 * @author minda
 */
public class Population {
    
    Tour[] tours;
    
    public Population(int populationSize, boolean initialise) {
        tours = new Tour[populationSize];
        // If we need to initialise a population of tours do so
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize; i++) {
                Tour newTour = new Tour();
                newTour.generateIndividual();
                saveTour(i, newTour);
            }
        }
    }
    
    
    public void saveTour(int index, Tour tour) {
        tours[index] = tour;
    }
    
    public Tour getTour(int index) {
        return tours[index];
    }
    
    public int populationSize() {
        return tours.length;
    }
    
    public Tour getFittest() {
        Tour fittest = tours[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getTour(i).getFitness()) {
                fittest = getTour(i);
            }
        }
        return fittest;
    }
}
