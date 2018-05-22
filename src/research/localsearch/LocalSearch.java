package research.localsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.FlexibleJobShop;
import data.Operation;
import research.initial.Machine;
import solution.CriticalPath;
import solution.Node;
import solution.Solution;

public class LocalSearch {

	public LocalSearch(Solution solution, FlexibleJobShop context) {
		
		LocalSearchDij(solution,context);
		
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
					ok = LocalSearchDij(solution,context);
					
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
	
	
	
	boolean LocalSearchDij(Solution solution, FlexibleJobShop context) {
		
		boolean ok = false;
		
		Node op;
		Node saveOp;
		Node savePrec; 
		
		Node disjunctiveFather;
		Solution new_solution;
		CriticalPath critical_path = solution.getCriticalPath();
		
		while(!ok) {
			
			op = critical_path.getLastNode();
			
			while (op!=null) {
				
				disjunctiveFather = solution.getGraphe().getDisjunctiveFather(op);
				
				if (disjunctiveFather!=null) {
					saveOp=op;
					savePrec=disjunctiveFather;
					ok=true;
				}
				
				op = critical_path.getPredecessor(op);
			}
			
			if (ok) {
				
				new_solution = solution;
				
				new_solution.permute(saveOp,savePrec); //swap in OA
				
				if (new_solution.getMakespan() < solution.getMakespan()) {
					
					solution = new_solution;
					ok = false;
					
				}
			}
		}
		
		return ok;
	}
	
	
	

}
