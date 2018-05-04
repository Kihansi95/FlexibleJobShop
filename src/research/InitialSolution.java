package research;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.*;
import javafx.collections.transformation.SortedList;

public class InitialSolution {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Label> availableOperations;
	
	// output
	private List<Label> assignments;
	
	public InitialSolution(FlexibleJobShop context) {
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Label>();
		this.context = context; // TODO to copy context or to reference
		
		// init machine from context
		int nbMachines = context.getNbMachine();		
		for(int i = 0; i < nbMachines; i++) {
			machines.add(new Machine(i)); 
		}
		
		// init available activities
		for(Job job: context.getJobs()) {
			Label waitingOperation = new Label(job.getOperation());
			availableOperations.add(waitingOperation);	// get the first activity from each job
		}
	}
	
	public void start() {
		//TODO using thread?
		
		int time = 0;
		List<Label> candidateList = new LinkedList<Label>();
		
		while(!availableOperations.isEmpty()) {
			
			// make candidate list
			candidateList.clear();
			for(Label avail: this.availableOperations) {
				Operation operation = avail.getOperation();
				Map<Integer, Integer> tuples = operation.getTuples();
				for(Integer idMachine: tuples.keySet()) {
					
					Machine machine = machines.get(idMachine);
					if(machine.isReady()) {
						candidateList.add(new Label(operation, machine, null));
					}
					
				}
			}
			
			// choose the best candidate(s) but be careful with duplicata assignments
			
			// update candidate's father and save it into solution
			
			// advance time cost and update machine state (ready)
		}
		
		
	}
	
	
}

class AssignmentComparator implements Comparator<Label> {

	@Override
	public int compare(Label label1, Label label2) {
		return label1.getProcessingTime() - label2.getProcessingTime();
	}
}