/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.util.ArrayList;

/**
 *since i cant think of a way to make fitness in to beer while limiting distance traveling
 * i just add  additional destinations and re-do genenetic TSP till the limiter is reached and nothing else can be added
 * I could of course check every possibilitty but that would make complexity exponential... and therefor unpractical
 * @author minda
 */
public class BeerOptimization {
    
    static ArrayList<Brewery> breweries; 
    static ArrayList<Brewery> path ; 
    double limit;
    
    public BeerOptimization(ArrayList<Brewery> breweries, ArrayList<Brewery> path, double limiter){
        this.breweries=breweries;
        this.path=path;
        limit=limiter;
    }
    
    
    public ArrayList<Brewery> changePath(){
        Tour best= new Tour();
        ArrayList<Brewery> temp=path;
        int pos=0;
            for (int j = 0; j < breweries.size(); j++) {
                
                if(!temp.contains(breweries.get(j))){
                    temp.add(breweries.get(j));
                    
                    Tour t =searchGenetic(temp);
                    if(t.getDistance()>limit)
                    {
                        TourManager.removeBrewery(breweries.get(j));
                        
                    }
                    else
                    {
                        best= t;
                    }
                }
                
            }
        temp= new ArrayList<Brewery>();
        double total=0;
        for (int i = 0; i < best.tourSize(); i++) {
            if(best.getBrewery(i).equals(breweries.get(0))){
                pos=i;
            }
        }
        
        int indexer=0;
        for (int i = 0; i <best.tourSize(); i++) {
            if(i+pos<best.tourSize())
                temp.add(best.getBrewery(i+pos));  
            else{
                temp.add(best.getBrewery(indexer++));
            }
               
        }
         path = temp;
         
         return path;
    }
    
    public static Tour  searchGenetic(ArrayList<Brewery> iBrew){
            TourManager.addBrewery(iBrew.get(iBrew.size()-1));
        
        Population pop = new Population(iBrew.size()*2, true);
        
        double currDistance=pop.getFittest().getDistance();
        int index=0;
        
        GA ga = new GA();
        
        /**
         * 150 repetitions means we found optimal enough path
         */
        while(index!=50)
        {
            pop = GA.evolvePopulation(pop);
            if(pop.getFittest().getDistance()<currDistance)
            {
                currDistance=pop.getFittest().getDistance();
                index=0;
            }
            else
            {
                index++;
            }
        }
        
        for (int i = 0; i < pop.getFittest().tourSize(); i++) {
            iBrew.set(i, pop.getFittest().getBrewery(i));
            
        }
        
       return pop.getFittest();
    }
    
}
