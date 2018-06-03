package research.localsearch;

import java.util.List;

import com.sun.org.glassfish.external.statistics.impl.StatsImpl;

import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import solution.CriticalPath;
import solution.Solution;
import solution.graph.Edge;
import solution.graph.Node;

public class LocalSearch {

	final private FlexibleJobShop context;
	private Solution optimizedSolution;

	public LocalSearch(FlexibleJobShop context) {
		this.context = context;
		this.optimizedSolution = null;
	}
	
	public Solution getSolution() {
		return this.optimizedSolution;
	}
	
	public void start(Solution solution) throws AlgorithmLogicException {
		
		localSearchDij(solution);
		
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
				
				for(int machine : machines) {
														
					//modify machine sequence
					System.out.println("try machine " + machine + " to node "+node);
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
					System.out.println("[Local search] =====================================");
					System.out.println("[Local search] A better solution found: "+solution);
					System.out.println("[Local serach] restart to the sink node "+node);
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
				
				//TODO debug
				//System.out.println("Disjunctive found : "+edge);
				Solution neighbor = new Solution(solution); // clone
				
				neighbor.permute(edge.getSuccessor(), edge.getPredecessor(), context);
				//System.out.println("[local search disjunctive] Try to permute " + edge.getPredecessor() + " <-> " + edge.getSuccessor() +" : makespan = " + neighbor.getMakespan());
				//System.out.println("Compare : neighbor = "+neighbor.getMakespan()+", new_solution = "+new_solution.getMakespan());
				if (neighbor.getMakespan() < new_solution.getMakespan()) {
					
					solution_updated = true;
					new_solution = neighbor;
					System.out.println("[Local search] found a better neighbor solution: " + new_solution);
					
				}
			}
		}
		
		if(solution_updated)
			return new_solution;	
		return null;
	}
	/*
	private boolean localSearchDij(Solution solution, FlexibleJobShop context)throws AlgorithmLogicException {
		
		boolean ok = false;
		
		Node op;
		Node saveOp = null;
		Node savePrec = null;
		
		Node disjunctiveFather;
		Solution new_solution;
		CriticalPath critical_path = solution.getCriticalPath();
		
		while(!ok) {
			System.out.println("[local search disjunctive] makespan = "+solution.getMakespan());
			
			op = critical_path.getLastNode();
			
			while (op!=null) {
				
				disjunctiveFather = solution.getGraph().getDisjunctiveFather(op);
				
				if (disjunctiveFather != null) {
					saveOp = op;
					savePrec = disjunctiveFather;
					ok = true;
				}
				
				op = critical_path.getPredecessor(op);
			}
			
			if (ok) {
				
				new_solution = new Solution(solution); // clone
				
				System.out.println("[local search disjunctive] Try to permute "+saveOp+" <-> "+savePrec);
				new_solution.permute(saveOp,savePrec,context); //swap in OA
				System.out.println("[local search disjunctive] Candidat solution has makespan = " + new_solution.getMakespan());
				
				if (new_solution.getMakespan() < solution.getMakespan()) {
					
					solution = new_solution;
					ok = false;
					System.out.println("[Local search] accept new solution: "+solution);
					
				}
				
				new_solution = null;
			}
		}
		
		return ok;
	}
	*/
}
