package research.localsearch;

import java.util.List;

import data.FlexibleJobShop;
import exception.AlgorithmLogicException;
import solution.CriticalPath;
import solution.Solution;
import solution.graph.Node;

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
		
		int[] ms = solution.getMs();
		
		while (node !=null) {
			
			//check if the operation has more than 1 candidate machine 
			List<Integer> machines = context.getMachines(node.getJob(), node.getOperation());
			if(machines.size() > 1)	{ 
				
				ok = false;
				tmp_solution = solution;
				current_Machine = ms[node.getIndex()]; //get the currently assigned machine
				
				//get list of candidates machines
				machines.remove(current_Machine);
				
				for(int machine : machines) {
					
					ms[node.getIndex()] = machine;
					
					//modify machine sequence
					solution.setMs(ms); 
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
		Node saveOp=null;
		Node savePrec=null;
		
		Node disjunctiveFather;
		Solution new_solution;
		CriticalPath critical_path = solution.getCriticalPath();
		
		while(!ok) {
			
			op = critical_path.getLastNode();
			
			while (op!=null) {
				
				disjunctiveFather = solution.getGraph().getDisjunctiveFather(op);
				
				if (disjunctiveFather!=null) {
					saveOp=op;
					savePrec=disjunctiveFather;
					ok=true;
				}
				
				op = critical_path.getPredecessor(op);
			}
			
			if (ok) {
				
				new_solution = solution;
				
				new_solution.permute(saveOp,savePrec,context); //swap in OA
				
				if (new_solution.getMakespan() < solution.getMakespan()) {
					
					solution = new_solution;
					ok = false;
					
				}
			}
		}
		
		return ok;
	}
	
}
