package jobshopflexible;
import data.DataFactory;
import data.FlexibleJobShop;
import research.InitialSolution;

public class Main {
	
    public static void main(String args[]) {
    	
    	// load configuration
    	Configuration conf = new Configuration();
    		
    	// initiate research context
        FlexibleJobShop shop = DataFactory.initiateJobShop(conf.DataPath);
        
        // Process the initial solution
        
        InitialSolution is = new InitialSolution(shop);
        is.start();
        
        // Visualize the solution
        //System.out.println(is.getSolution());
        is.visualizeSolution();
        
        System.out.println("fin");
    }
    
}
