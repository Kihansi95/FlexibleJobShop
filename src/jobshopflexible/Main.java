package jobshopflexible;
import data.DataFactory;
import data.FlexibleJobShop;
import research.InitialSolution;

public class Main {
	
    public static void main(String args[]) {
    	
    	// load configuration
    	Configuration conf = new Configuration();
    		
    	// initiate research context
        FlexibleJobShop shop = DataFactory.instance(conf).initiateJobShop(conf);
        
        // Process the initial solution
        
        InitialSolution is = new InitialSolution(conf, shop);
        is.start();
        
        // Visualize the solution
        //System.out.println(is.getSolution());
        is.visualizeSolution();
        
        System.out.println("done");
    }
    
}
