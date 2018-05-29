package jobshopflexible;

import data.DataFactory;
import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import output.pdflatex.PdfWriter;
import research.initial.Converter;
import research.initial.InitialSolution;
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
        PdfWriter pdfWriter = new PdfWriter(conf);
        
        // initiate algorithm
        LocalSearch ls = new LocalSearch(shop);
        
        // Process the initial solution
        InitialSolution is = new InitialSolution(conf, shop);
        is.start();

        // Visualize the solution
        // System.out.println(is.getSolution());
        // is.visualize(new PdfWriter(conf)); 		// test initial solution
        
        Solution sol = Converter.convert(is, shop);
        // sol.visualize(new PdfWriter(conf));		// test graph visualization
        
        try {
			sol.updateGraph(shop);
			// sol.visualize(pdfWriter);			// test solution representation
		
			ls.start(sol);
			sol.visualize(pdfWriter);
			
		} catch (AlgorithmLogicException e) {
			e.printStackTrace();
		}
        
        System.out.println("done");
    }
    
}
