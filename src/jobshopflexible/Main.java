package jobshopflexible;
import data.DataFactory;
import data.FlexibleJobShop;
import research.InitialSolution;

public class Main {
	
	public static final String PATH = "./data/mt10c1.fjs";


    public static void main(String args[]) {

    	// initiate research context
        FlexibleJobShop shop = DataFactory.initiateJobShop(PATH);
        
        // Process the initial solution
        InitialSolution is = new InitialSolution(shop);
        
        // Visualize the solution

        System.out.println("fin");
    }
}
