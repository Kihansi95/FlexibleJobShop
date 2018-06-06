package research.localsearch;

import java.util.List;

import com.sun.org.glassfish.external.statistics.impl.StatsImpl;

import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import jobshopflexible.Configuration;
import solution.CriticalPath;
import solution.Solution;
import solution.graph.Edge;
import solution.graph.Node;
import utility.Verbose;

public class LocalSearch extends Verbose {

	final private FlexibleJobShop context;
	private Solution optimizedSolution;

	public LocalSearch(Configuration conf, FlexibleJobShop context) {
		
		super(conf, "localsearch");
		this.context = context;
		this.optimizedSolution = null;
	}
	
	public Solution getSolution() {
		return this.optimizedSolution;
	}
	
	public void start(Solution solution) throws AlgorithmLogicException {
		
		Solution first_improved_solution = localSearchDij(solution);
		if(first_improved_solution != null) {
			solution = first_improved_solution;
			first_improved_solution = null; // try to free memory
		}
		
		int current_Machine;
		
		Node node = solution.getCriticalPath().getLastNode();
				
		int[] ma = solution.getMa();
			
		while (!node.equals(solution.getGraph().getStartNode())) {
			
			//check if the operation has more than 1 candidate machine 
			List<Integer> machines = context.getMachines(node.getJob(), node.getOperation());
			if(machines.size() > 1)	{ 
				
				// case opeartion can have more than one machine choice, we search all possible assignments
				Solution neighbor_ma = solution; 
				boolean improved_neighbor = false;
				
				//get list of candidates machines
				ma = solution.getMa();
				current_Machine = ma[node.getIndex()]; //get the currently assigned machine
				machines.remove(new Integer(current_Machine));
				
				if(this.verbose)
					System.out.println("[Local search] Exploring ma neighbor on node " + node);
				
				for(int machine : machines) {
														
					//modify machine sequence
					Solution tmp_solution = new Solution(solution);
					tmp_solution.setMa(context, node.getIndex(), machine); 
					
					Solution neighbor_os = localSearchDij(tmp_solution);
					
					if (neighbor_os != null && neighbor_os.getMakespan() < neighbor_ma.getMakespan()) {
						
						neighbor_ma = neighbor_os;
						improved_neighbor = true;
					} 
					
				}
				
				if(improved_neighbor) {
					solution = neighbor_ma;
					node = solution.getCriticalPath().getLastNode();
					if(this.verbose) {
						System.out.println("[Local search] =====================================");
						System.out.println("[Local search] A better solution found: "+solution);
						System.out.println("[Local serach] restart to the sink node "+node);
					}
					
				} else {
					node = solution.getCriticalPath().getPredecessor(node);
				}
				
			} else {
				
				// when operation has no choice on machine => advance to the predecessor operation
				node = solution.getCriticalPath().getPredecessor(node);
			}
			
		}
		
		this.optimizedSolution = solution;
	}
	
	private Solution localSearchDij(final Solution solution)throws AlgorithmLogicException {
				
		boolean solution_updated = false;
		
		Solution new_solution = solution;
		CriticalPath critical_path = solution.getCriticalPath();
		
		for(Edge edge : critical_path.getEdges()) {
			
			// check if disjunctive
			if(edge.isDisjunctive()) {
				
				Solution neighbor = new Solution(solution); // clone
				
				neighbor.permute(edge.getSuccessor(), edge.getPredecessor(), context);
				
				if(this.verbose)
					System.out.println("[local search] Try to permute " + edge.getPredecessor() + " <-> " + edge.getSuccessor() +" : makespan = " + neighbor.getMakespan());
				
				if (neighbor.getMakespan() < new_solution.getMakespan()) {
					
					solution_updated = true;
					new_solution = neighbor;
					if(this.verbose)
						System.out.println("[Local search] found a better neighbor solution: " + new_solution);
					
				}
				
			}
		}
		
		if(solution_updated)
			return new_solution;	
		return null;
	}
}
