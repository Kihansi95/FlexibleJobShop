package research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.*;
import javafx.collections.transformation.SortedList;
import jdk.nashorn.internal.ir.Assignment;

public class InitialSolution {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Operation> availableOperations;
	private AssignmentComparator comparator;
	
	// output
	private List<Label> assignments;
	
	public InitialSolution(FlexibleJobShop context) {
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Operation>();
		this.context = context; // TODO to copy context or to reference
		this.comparator = new AssignmentComparator();
		this.assignments = new LinkedList<Label>();
		
		// init machine from context
		int nbMachines = context.getNbMachine();		
		for(int i = 0; i < nbMachines; i++) {
			machines.add(new Machine(i)); 
		}
		
		// init available activities
		for(Job job: context.getJobs()) {
			Operation waitingOperation = job.getOperation();
			availableOperations.add(waitingOperation);	// get the first activity from each job
		}
	}
	
	public void start() {
		//TODO using thread?
		
		int time = 0;
		List<Label> candidateList = new LinkedList<Label>();
		List<Label> chosenCandidate = new LinkedList<Label>();
		
		for(int i = 0; i < 2; i++)	{
		//while(!availableOperations.isEmpty()) {
			
			// make candidate list
			candidateList.clear();
			for(Operation operation: this.availableOperations) {
				Map<Integer, Integer> tuples = operation.getTuples();
				for(Integer idMachine: tuples.keySet()) {
					
					Machine machine = machines.get(idMachine - 1);
					if(machine.isReady()) {
						candidateList.add(new Label(operation, machine, null));
					}
					
				}
			}
			
			// choose the best candidate(s) but be careful with duplicata assignments
			Collections.sort(candidateList, comparator);
			while(!candidateList.isEmpty()) {
				
				// choose the best
				Label best = candidateList.remove(0);
				best.setMachineState(false);
				best.updateFinishTime(time);
				chosenCandidate.add(best);
				
				// remove duplicata machine || operation
				while(candidateList.remove(best));	//not the best but the corresponding
				
				// remove the corresponding from waiting available list
				Operation chosenOp = best.getOperation();
				
				System.out.println("Chosen op = " +chosenOp);
				
				availableOperations.remove(chosenOp);
				Operation next = chosenOp.getNext();
				if(next != null)
					availableOperations.add(next);
			}
			
			// update candidate's father
			for(Label candidate: chosenCandidate) {
				
				Label father = candidate.getFather();
				
				// if label doesn't have father yet, its the first time we process algo
				// so no need to update the father
				if(father != null) {
					
					if(father.getFinishTime() < time) {
						candidate.setFatherByMachine();
					}
				}
			}

			// advance time cost and update machine state (ready)
			int min_time = chosenCandidate.get(0).getProcessingTime();
			time += min_time;
			for(Label label : chosenCandidate)	{	// update machine state
				label.setMachineState(time <= label.getFinishTime());
			}
			
			// save chosen candidate into output solution 
			this.assignments.addAll(chosenCandidate);
			chosenCandidate.clear(); // TODO check if all assigned labels are null
			
			System.out.println(availableOperations);
		}
		
		
	}
	
	public List<Label> getSolution()	{
		return assignments;
	}
}

class AssignmentComparator implements Comparator<Label> {

	@Override
	public int compare(Label label1, Label label2) {
		return label1.getProcessingTime() - label2.getProcessingTime();
	}
}