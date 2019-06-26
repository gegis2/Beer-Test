/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.util.ArrayList;

/**
 * this one works 100% but has close to 0 optimization
 * @author minda
 */
public class InsuranceAlgorythm {
    
    ArrayList<Brewery> breweries = new ArrayList(); 
    double startX;
    double startY;
    double limit;
    
    public InsuranceAlgorythm(ArrayList<Brewery> breweries, double x, double y, double limit)
    {
        this.breweries=breweries;
        startX=x;
        startY=y;
        this.limit=limit;
    }
    
    public ArrayList<Brewery> pathFinding()
    {
        
        boolean next = true;
        ArrayList<Brewery> visited = new ArrayList();
        visited.add(breweries.get(0));
        while(next){
            double minFit=0;        // breweries that are close to the last brewery, but not too far from previous brewery and distance is proportional beercount
            double bCount=0;
            double min=-1;
            int minI=0;
            for (int i = 0; i < breweries.size(); i++) {
                if(min==-1 && !visited.contains(breweries.get(i))){
                   min=visited.get(visited.size()-1).distanceTo(breweries.get(i));
                   minFit=(visited.get(visited.size()-1).distanceTo(breweries.get(i))+(breweries.get(i).distanceTo(breweries.get(0))/2))/breweries.get(i).n;
                   bCount=0.01;
                   minI=i;
                }
                else if(visited.get(visited.size()-1).distanceTo(breweries.get(i))<min && !visited.contains(breweries.get(i))&& 
                        ((min+(minFit/2))/bCount>((visited.get(visited.size()-1).distanceTo(breweries.get(i))+(breweries.get(i).distanceTo(breweries.get(0))/2))/breweries.get(i).n)))
                {
                    min = visited.get(visited.size()-1).distanceTo(breweries.get(i));
                    minFit=(visited.get(visited.size()-1).distanceTo(breweries.get(i))+(breweries.get(i).distanceTo(breweries.get(0))/2))/breweries.get(i).n;
                    bCount=breweries.get(i).n;
                    minI=i;
                }
            }
            
            if(limit-min>=distance(breweries.get(minI))){
            limit-=min;
            visited.add(breweries.get(minI));
            //System.out.println(min+" "+ limit);
            }
            else
            {
                
                next=false;
            }
            
        }
        return visited;
    }
    
    public double distance(Brewery brew){
        if ((startX == brew.getX()) && (startY == brew.getY())) {
            return 0;
	}
	else {
		double theta = startY - brew.getY();
		double dist = Math.sin(Math.toRadians(startX)) * Math.sin(Math.toRadians(brew.getX())) + Math.cos(Math.toRadians(startX)) * Math.cos(Math.toRadians(brew.getX())) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
		return (dist);
	}
    }
}
