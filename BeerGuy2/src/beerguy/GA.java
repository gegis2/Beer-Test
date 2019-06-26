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
public class GA {
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;
    
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.populationSize(), false);
        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveTour(0, pop.getFittest());
            elitismOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            // Select parents
            Tour parent1 = tournamentSelection(pop);
            Tour parent2 = tournamentSelection(pop);
            // Crossover parents
            Tour child = crossover(parent1, parent2);
            // Add child to new population
            newPopulation.saveTour(i, child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            mutate(newPopulation.getTour(i));
        }

        return newPopulation;
    }
    
    public static Tour crossover(Tour parent1, Tour parent2) {
        // Create new child tour
        Tour child = new Tour();

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * parent1.tourSize());
        int endPos = (int) (Math.random() * parent1.tourSize());

        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.tourSize(); i++) {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setBrewery(i, parent1.getBrewery(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setBrewery(i, parent1.getBrewery(i));
                }
            }
        }

        // Loop through parent2's breweries tour
        for (int i = 0; i < parent2.tourSize(); i++) {
            // If child doesn't have the brewery add it
            if (!child.containsBrewery(parent2.getBrewery(i))) {
                // Loop to find a spare position in the child's tour
                for (int ii = 0; ii < child.tourSize(); ii++) {
                    // Spare position found, add brewery
                    if (child.getBrewery(ii) == null) {
                        child.setBrewery(ii, parent2.getBrewery(i));
                        break;
                    }
                }
            }
        }
        return child;
    }
    
    private static void mutate(Tour tour) {
        // Loop through tour breweries
        for(int tourPos1=0; tourPos1 < tour.tourSize(); tourPos1++){
            // Apply mutation rate
            if(Math.random() < mutationRate){
                // Get a second random position in the tour
                int tourPos2 = (int) (tour.tourSize() * Math.random());

                // Get the breweries at target position in tour
                Brewery brew1 = tour.getBrewery(tourPos1);
                Brewery brew2 = tour.getBrewery(tourPos2);

                // Swap them around
                tour.setBrewery(tourPos2, brew1);
                tour.setBrewery(tourPos1, brew2);
            }
        }
    }
    
    private static Tour tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.saveTour(i, pop.getTour(randomId));
        }
        // Get the fittest tour
        Tour fittest = tournament.getFittest();
        return fittest;
    }
}
