package research.localsearch;

import java.util.List;

import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import solution.CriticalPath;
import solution.Solution;
import solution.graph.Node;
import solution.graph.SpecialNode;

public class LocalSearch {

	private FlexibleJobShop context;

	public LocalSearch(FlexibleJobShop context) {
		this.context = context;
	}
	
	public void start(Solution solution) throws AlgorithmLogicException {
		
		localSearchDij(solution, context);
		boolean ok;
		Solution tmp_solution; //not used ? 
		int current_Machine;
		CriticalPath critical_path = solution.getCriticalPath();
		Node node = critical_path.getLastNode();
		
		
		int[] ms = solution.getMa();
		
		// skip end node
		node = critical_path.getPredecessor(node);
		
		while (!(node instanceof SpecialNode)) {
			
			//check if the operation has more than 1 candidate machine 
			List<Integer> machines = context.getMachines(node.getJob(), node.getOperation());
			if(machines.size() > 1)	{ 
				
				ok = false;
				tmp_solution = solution;
				current_Machine = ms[node.getIndex()]; //get the currently assigned machine
				
				//get list of candidates machines
				machines.remove(new Integer(current_Machine));
				
				for(int machine : machines) {
					
					ms[node.getIndex()] = machine;
					
					//modify machine sequence
					solution.setMa(ms, context); 
					solution.updateGraph(context);
					
					ok = localSearchDij(solution,context);
					
					if (ok) {
						node = critical_path.getLastNode();
					} else {
						node = critical_path.getPredecessor(node);
					}
				}
				
				
			} else {
				node = critical_path.getPredecessor(node);
			}
		}
	}
	
	private boolean localSearchDij(Solution solution, FlexibleJobShop context)throws AlgorithmLogicException {
		
		boolean ok = false;
		
		Node op;
		Node saveOp = null;
		Node savePrec = null;
		
		Node disjunctiveFather;
		Solution new_solution;
		CriticalPath critical_path = solution.getCriticalPath();
		
		while(!ok) {
			System.out.println("[localSearchDij] makespan = "+solution.getMakespan());
			
			op = critical_path.getLastNode();
			
			while (op!=null) {
				
				disjunctiveFather = solution.getGraph().getDisjunctiveFather(op);
				
				if (disjunctiveFather!=null) {
					saveOp = op;
					savePrec = disjunctiveFather;
					ok = true;
				}
				
				op = critical_path.getPredecessor(op);
			}
			
			if (ok) {
				
				new_solution = solution;
				
				System.out.println("[Local search] Try to permute "+saveOp+" <-> "+savePrec);
				new_solution.permute(saveOp,savePrec,context); //swap in OA
				System.out.println("[Local search] Candidat solution has makespan = " + new_solution.getMakespan());
				
				if (new_solution.getMakespan() < solution.getMakespan()) {
					
					solution = new_solution;
					ok = false;
					System.out.println("[Local search] accept new solution: "+solution);
					
				}
			}
		}
		
		return ok;
	}
	
}
