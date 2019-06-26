/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerguy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *Whilst solving this problem, I tried my first attempt at genetic programming, thereore its a bit rusty and 
 * very... very slow, however, it does improve the collected beer count by about 50% in most cases so i'd say it was a good attempt
 * @author minda
 */
public class BeerGuy {

    static double kmL=2000;
    static double lat=51.355468;
    static double lon=11.100790; 
    static long  startTime;
//    static double lat=51.74250300;
//    static double lon=19.43295600;
    static ArrayList<Brewery> breweries = new ArrayList(); 
    
    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        if(args.length==2)
        {
            try{
            lat=Double.parseDouble(args[0]);
            lon=Double.parseDouble(args[1]);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        readBrewery();
        readGeo();
        readBeer();
        Brewery start = new Brewery(0,"{{{{HOME}}}}"); start.setGeo(lat, lon);
        breweries.add(0, start);
        removeUnreachable();
        
        
        searchInsurance();
        
        
        
    }
    
    public static ArrayList<Brewery>  searchGenetic(ArrayList<Brewery> iBrew){
        for (int i = 0; i < iBrew.size(); i++) {
            TourManager.addBrewery(iBrew.get(i));
        }
        
        Population pop = new Population(100, true);
        
        double currDistance=pop.getFittest().getDistance();
        int index=0;
        
        GA ga = new GA();
        while(index!=500)
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
       return iBrew;
    }
    
    
    
    /**
     * unoptimized algorythm path search
     */
    public static void searchInsurance()
    {
        InsuranceAlgorythm IA = new InsuranceAlgorythm(breweries,lat,lon,kmL);
        
        ArrayList<Brewery> iBrew =IA.pathFinding();
        
        int total=0;
        double dist=0;
        dist+=iBrew.get(0).distanceTo(iBrew.get(1));
        dist+=iBrew.get(iBrew.size()-1).distanceTo(iBrew.get(0));
        
        for (int i = 1; i < iBrew.size(); i++) {
            if(i+1!=iBrew.size()){
            dist+=iBrew.get(i).distanceTo(iBrew.get(i+1));
            }
        }
        
            System.out.println();
            System.out.println();
       System.out.println(iBrew.size()-1+" breweries found without optimization: ");
        write(iBrew, dist);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        iBrew=searchGenetic(iBrew);
        
        BeerOptimization opt = new BeerOptimization(breweries,iBrew,kmL);
        iBrew = opt.changePath();
        
        System.out.println(iBrew.size()-1+" breweries found with optimization: ");
        dist=0;
        
        dist+=iBrew.get(iBrew.size()-1).distanceTo(iBrew.get(0));
        for (int i = 0; i < iBrew.size(); i++) {
            if(i+1!=iBrew.size()){
            dist+=iBrew.get(i).distanceTo(iBrew.get(i+1));
            }
        }
        write(iBrew, dist);
        stopTime = System.currentTimeMillis();
        long elapsedTime2 = stopTime - startTime;
        System.out.println("Fast part ran for: "+elapsedTime+" ms");
        System.out.println("Full code ran for: "+elapsedTime2+" ms");
    }
    
    private static void write(ArrayList<Brewery> iBrew, double dist){
        for (int i = 0; i < iBrew.size(); i++) {
            if(i==0)
                System.out.println("    <-->"+iBrew.get(i).toString()+" Distance: 0Km");
            else
            {
                System.out.println("    --->"+iBrew.get(i).toString()+" Distance: "+ iBrew.get(i-1).distanceTo(iBrew.get(i))+" km");
            }
            if(i+1==iBrew.size())
                System.out.println("    <---"+iBrew.get(0).toString()+" Distance: "+ iBrew.get(i).distanceTo(iBrew.get(0))+" km");
        }
        
            System.out.println("Total Distance: "+dist);
            System.out.println();
            System.out.println();
            System.out.println("Beers collected");
            int count=0;
        for (int i = 0; i < iBrew.size(); i++) {
            count+=iBrew.get(i).n;
            for (int j = 0; j < iBrew.get(i).n; j++) {
                System.out.println("     -->"+iBrew.get(i).beers.get(j).name);
            }
        }
        
            System.out.println("Total:  "+ count);
    }
    
    /**
     * to reduce complexity, removing all breweries that cannot be reached with distance limiter
     * and sorting, so the adding would choose richest beer paths first
     */
    public static void removeUnreachable()
    {
        
        ArrayList<Brewery> temp = new ArrayList(); 
        for (int i = 0; i < breweries.size(); i++) {
            if(breweries.get(0).distanceTo(breweries.get(i))<kmL/2)
            {
                
                temp.add(breweries.get(i));
            }
        }  
         
        int n = temp.size(); 
        for (int i = 1; i < n; i++) { 
            Brewery key = temp.get(i); 
            int j = i - 1; 
            
            while (j >= 1 && (key.n >  temp.get(j).n)) { 
                temp.set(j+1, temp.get(j));
                j = j - 1; 
                
            } 
            temp.set(j+1,key); 
        } 
        
        breweries=temp;
    }
    
    /**
     * reading beers from... fixed text file
     */
    private static void readBeer(){
        List<List<String>> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("beers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");            
                records.add(Arrays.asList(values));            
            }
        }
        catch(Exception ex){ex.printStackTrace();}

        for(int i =1; i< records.size(); i++){
            int id=0;
            String name="";
            Beer temp;
            boolean numeric = true;

            /**
             * check initializer
             */
            try {
                Double num = Double.parseDouble(records.get(i).get(0));
                Double num2 = Double.parseDouble(records.get(i).get(1));
            } catch (NumberFormatException e) {
                numeric = false;
            }


            if(numeric){
                id=Integer.parseInt(records.get(i).get(1));
                name=records.get(i).get(2);
                temp= new Beer(name);
                for (int j = 0; j < breweries.size(); j++) {
                    if(breweries.get(j).id==id)
                    {
                        breweries.get(j).addBeer(temp);
                    }
                }
            }
        }

        /**
         * removing empty breweries
         */
        for (int j = 0; j < breweries.size(); j++) {
            if(breweries.get(j).n==0){

                breweries.remove(j);
                j--;
            }
        }
    }
    
    /**
     * read breweries from CSV
     */
    private static void readBrewery(){
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("breweries.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        }
        catch(Exception ex){ex.printStackTrace();}

        for(int i =1; i< records.size(); i++){
            int id=0;
            String name="";
            Brewery temp;
            boolean numeric = true;

            /**
             * check initializer
             */
            try {
                Double num = Double.parseDouble(records.get(i).get(0));
            } catch (NumberFormatException e) {
                numeric = false;
            }

            /**
             * what even is this database line seperation logic?!?!?!?!?!
             */
            if(records.get(i).size()>1 && !records.get(i).get(0).contains("*") && numeric){
                

                id=Integer.parseInt(records.get(i).get(0));
                name=records.get(i).get(1);
                temp=new Brewery(id,name);
                breweries.add(temp);
            }
        }
    }
    
    /**
     * add coordinates to breweries
     */
    private static void readGeo(){
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("geocodes.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
        }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    
        for(int i =1; i< records.size(); i++){
            int id=0;
            double x;
            double y;
            
            /**
             *  input format check initializer
             */
            boolean numeric = true;

            try {
                Double num = Double.parseDouble(records.get(i).get(0));
                Double num2 =Double.parseDouble(records.get(i).get(2));
                Double num3 =Double.parseDouble(records.get(i).get(3));
            } catch (NumberFormatException e) {
                numeric = false;
            }
            
            /**
             * input check
             */
            if(numeric){

                id=Integer.parseInt(records.get(i).get(1));
                x=Double.parseDouble(records.get(i).get(2));
                y=Double.parseDouble(records.get(i).get(3));
                for (int j = 0; j < breweries.size(); j++) {
                    if(breweries.get(j).id==id)
                    {
                        breweries.get(j).setGeo(x, y);
                    }
                }
            }
        }
        
        /**
         * removing unknown location Breweries
         */
        for (int j = 0; j < breweries.size(); j++) {
            if(breweries.get(j).getX()==-1 && breweries.get(j).getY()==-1){
                
                breweries.remove(j);
                j--;
            }
        }
    }
    
}