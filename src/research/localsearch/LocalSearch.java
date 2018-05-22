package research.localsearch;

import java.util.Map;

import data.FlexibleJobShop;
import data.Operation;
import research.initial.Machine;
import solution.Solution;

public class LocalSearch {

	public LocalSearch(Solution S, FlexibleJobShop context) {
		
		LocalSearchDij(S,context);
		boolean ok;
		Solution tmp_solution; //not used ? 
		int currentMachine;
		Operation op=S.getCriticalPath().getLast().getOperation();
		int id_job=S.getCriticalPath().getLast().getOperation().getIdJob(); 
		int[]localMs=S.getMs();
		
		while (op!=null) {
			if(context.getJobs().get(id_job).getOperations().get(op.getId()).getTuples().size()>1) { //check if the operation has more than 1 candidate machine 
				ok=false;
				tmp_solution=S;
				currentMachine=localMs[op.getIndex()]; //get the currently assigned machine
				Map<Integer, Integer> machines = context.getJobs().get(id_job).getOperations().get(op.getId()).getTuples(); //get list of candidates machines
				machines.remove(currentMachine);
				for(int machine : machines.values()) {
					localMs[op.getIndex()]=machine;
					S.setMs(localMs); //modify machine assignments vector
					ok=LocalSearchDij(S,context);
					
					if (ok) {
						op=S.getCriticalPath().getLast().getOperation();
					}else {
						op=S.getCriticalPath().get(S.getCriticalPath().indexOf(op)-1).getOperation();	
					}
				}
				
				
			}else {
				op=S.getCriticalPath().get(S.getCriticalPath().indexOf(op)-1).getOperation();
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
				disjunctiveFather= S.getGraphe().getnode(op).getDisjunctiveFather;
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
