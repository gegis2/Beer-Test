/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.util.ArrayList;

/**
 *x=lattitude
 *y=longitude
 *n= brewery beer count
 *  data not included from database is redundant 
 * @author minda
 */
public class Brewery {
    int id;
    String name;
    double x=-1;
    double y=-1;
    int n=0;
    ArrayList<Beer> beers=new ArrayList();
    public Brewery(int id,String name){
        this.id=id;
        this.name=name;
    }
    
    public void addBeer(Beer beer){
        beers.add(beer);
        n++;
    }
    
    public void setGeo(double x, double y)
    {
        
        this.x=x;
        this.y=y;
    }
    
     public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    
    public double distanceTo(Brewery brew){
//         double R = 6371; // metres
//        double φ1= Math.toRadians(x);
//        double φ2 = Math.toRadians(x);
//        double Δφ = Math.toRadians(brew.getX()-x);
//        double Δλ = Math.toRadians((brew.getY()-y));
//
//        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
//                Math.cos(φ1) * Math.cos(φ2) *
//                Math.sin(Δλ/2) * Math.sin(Δλ/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//
//        double d = R * c;
//        return d;

        //this one calculates more accurately 
        if ((x == brew.getX()) && (y == brew.getY())) {
            return 0;
	}
	else {
		double theta = y - brew.getY();
		double dist = Math.sin(Math.toRadians(x)) * Math.sin(Math.toRadians(brew.getX())) + Math.cos(Math.toRadians(x)) * Math.cos(Math.toRadians(brew.getX())) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
		return (dist);
	}
    }
    
    @Override
    public String toString(){
        return name+" Beer count "+n+" Coordinates: "+getX()+", "+getY();
    }
}
