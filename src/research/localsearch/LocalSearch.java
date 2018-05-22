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
		int currentMachine;
		CriticalPath critical_path = solution.getCriticalPath();
		Node node = critical_path.getLastNode();
		int[]ms = solution.getMs();
		
		while (node !=null) {
			
			//check if the operation has more than 1 candidate machine 
			if(context.getJobs().get(node.getJob()).getOperations().get(node.getOperation()).getTuples().size()>1) { 
				
				ok = false;
				tmp_solution = solution;
				currentMachine = ms[node.getIndex()]; //get the currently assigned machine
				
				//get list of candidates machines
				List<Integer> machines = new ArrayList<Integer>(context.getJobs().get(node.getJob()).getOperations().get(node.getOperation()).getTuples().keySet());
				machines.remove(currentMachine);
				
				for(int machine : machines) {
					ms[node.getIndex()]=machine;
					
					//modify machine assignments vector
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
	
	
	
	boolean LocalSearchDij(Solution S, FlexibleJobShop context) {
		
		boolean ok=false;
		Operation op;
		Operation saveOp;
		Operation savePrec; 
		Operation disjunctiveFather;
		Solution newS;
		while(!ok) {
			
			op=S.getCriticalPath().getLast().getOperation();
			while (op!=null) {
				disjunctiveFather= S.getGraphe().getDisjunctiveFather(op);
				if (disjunctiveFather!=null) {
					saveOp=op;
					savePrec=disjunctiveFather;
					ok=true;
				}
				op=S.getCriticalPath().get(S.getCriticalPath().indexOf(op)-1).getOperation();
			}
			if (ok) {
				newS=S;
				newS.permute(saveOp,savePrec); //swap in OA
				newS.update();
				if (newS.makespan()<S.makespan()) {
					S=newS;
					ok=false;
				}
			}
		}
		
		return ok;
	}
	
	
	

}
