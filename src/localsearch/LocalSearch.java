package localsearch;

import data.FlexibleJobShop;
import data.Operation;
import solution.Solution;

public class LocalSearch {

	public LocalSearch(Solution S, FlexibleJobShop context) {
		
		LocalSearchDij(S,context);
		boolean ok;
		Solution tmp_solution;
		boolean endOfCriticalPath; 
		int op=S.getCriticalPath().getLast().getOperation().getId(); 
		int id_job=S.getCriticalPath().getLast().getOperation().getIdJob(); 
		
		while (!endOfCriticalPath) {
			if(context.getJobs().get(id_job).getOperations().get(op).getTuples().size()>1) {
				ok=false;
				tmp_solution=S;
				
			};
		}
		
	}
	

}
