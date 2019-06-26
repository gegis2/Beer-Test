/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.util.ArrayList;

/**
 *
 * @author minda
 */
public class TourManager {
    
    // list of breweries
    private static ArrayList destinationBreweries = new ArrayList<Brewery>();
    
    public static ArrayList tour()
    {
        return destinationBreweries;
    }
    
    public static void addBrewery(Brewery brew) {
        destinationBreweries.add(brew);
    }
    
    public static void removeBrewery(Brewery brew) {
        destinationBreweries.remove(brew);
    }
    
    public static Brewery getBrewery(int index){
        return (Brewery)destinationBreweries.get(index);
    }
    
    public static int numberOfBreweries(){
        return destinationBreweries.size();
    }
}
