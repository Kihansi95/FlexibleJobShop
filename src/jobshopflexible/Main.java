package jobshopflexible;

import data.DataFactory;
import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import output.pdflatex.PdfWriter;
import research.glutton.Converter;
import research.glutton.GluttonSearch;
import research.localsearch.LocalSearch;
import solution.CriticalPath;
import solution.Solution;

public class Main {
	
    public static void main(String args[]) {
    	
    	// load configuration
    	Configuration conf = new Configuration();
    		
    	// initiate research context
        FlexibleJobShop shop = DataFactory.instance(conf).initiateJobShop(conf);
        
        // initiate output method
        //PdfWriter pdfWriter = new PdfWriter(conf);
        
        // initiate algorithm
        LocalSearch local_s = new LocalSearch(shop);
        
        // Process the initial solution
        GluttonSearch glutton_s = new GluttonSearch(conf, shop);
        glutton_s.start();

        // Visualize the solution
        // System.out.println(is.getSolution());
        // is.visualize(new PdfWriter(conf)); 		// test initial solution
        
        Solution sol = Converter.convert(glutton_s, shop);
        // sol.visualize(new PdfWriter(conf));		// test graph visualization
        
        try {
			sol.updateGraph(shop);
			//sol.visualize(pdfWriter);			// test solution representation
		
			local_s.start(sol);
			sol = local_s.getSolution();
			//sol.visualize(pdfWriter);
			System.out.println("Final solution: " + sol);
			
		} catch (AlgorithmLogicException e) {
			e.printStackTrace();
		}
        
        System.out.println("done");
    }
    
}
