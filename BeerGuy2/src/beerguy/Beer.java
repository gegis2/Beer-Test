
/**
 * we only need beer name, all other beer info is redundant 
 */
package beerguy;

/**
 *
 * @author minda
 */
public class Beer {
    String name;
    public Beer(String name){
        this.name = name;
    }
    
    
    public String getName(){
        return this.name;
    }
}
