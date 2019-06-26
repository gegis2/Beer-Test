/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author minda
 */
public class Tour {
    
    private ArrayList tour = new ArrayList<Brewery>();
    
    private double fitness = 0;
    private double distance = 0;
    private int beerCount=0;

    public Tour(){
        for (int i = 0; i < TourManager.numberOfBreweries(); i++) {
            tour.add(null);
        }
    }
    
    public Tour(ArrayList tour){
        this.tour = tour;
    }
    
    
    public void generateIndividual() {
        // Loop through all our destinations and add them to our tour
        for (int i = 0; i < TourManager.numberOfBreweries(); i++) {
          setBrewery(i, TourManager.getBrewery(i));
        }
        // Randomly reorder the tour
        Collections.shuffle(tour);
    }
    
    public Brewery getBrewery(int tourPosition) {
        return (Brewery)tour.get(tourPosition);
    }
    
    public void setBrewery(int tourPosition, Brewery brew) {
        tour.set(tourPosition, brew);
        
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }
    public boolean containsBrewery(Brewery brew){
        return tour.contains(brew);
    }
    
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }
    
    public double getDistance(){
        if (distance == 0) {
            double tourDistance = 0;
            // Loop through our tour's breweries
            for (int i=0; i < tourSize(); i++) {
                // Get brewery we're travelling from
                Brewery fromBrewery = getBrewery(i);
                // brewery we're travelling to
                Brewery destinationBrewery;
                // Check we're not on our tour's last brewery, if we are set our 
                // tour's final destination brewery to our starting brewery
                if(i+1 < tourSize()){
                    destinationBrewery = getBrewery(i+1);
                }
                else{
                    destinationBrewery = getBrewery(0);
                }
                // Get the distance between the two breweries
                tourDistance += fromBrewery.distanceTo(destinationBrewery);
            }
            distance = tourDistance;
        }
        return distance;
    }
    
    public int getBeerCount(){
        if (beerCount == 0) {
            int tourBeerCount = 0;
            // Loop through our tour's breweries
            for (int i=0; i < tourSize(); i++) {
                // Get brewery we're travelling from
                Brewery brewery = getBrewery(i);
                // Get the distance between the two brewery
                tourBeerCount += brewery.n;
            }
            beerCount = tourBeerCount;
        }
        return beerCount;
    }
    
    public int tourSize() {
        return tour.size();
    }

}
