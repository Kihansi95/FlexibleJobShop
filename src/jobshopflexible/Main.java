package jobshopflexible;
import java.util.List;
import java.util.Map;

import data.DataFactory;
import solution.Edge;
import solution.Graph;
import solution.Node;
import data.FlexibleJobShop;
import data.Operation;
import research.initial.InitialSolution;

public class Main {
	
    public static void main(String args[]) {
    	
    	// load configuration
    	Configuration conf = new Configuration();
    		
    	// initiate research context
        FlexibleJobShop shop = DataFactory.instance(conf).initiateJobShop(conf);
        
        // Process the initial solution
        
        InitialSolution is = new InitialSolution(conf, shop);

        // Visualize the solution
        //System.out.println(is.getSolution());
        is.visualizeSolution();
        
        System.out.println("done");
    }
    
}
