package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.*;
import output.PdfWriter;

public class InitialSolution {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Label> availableOperations;
	private AssignmentComparator comparator;
	
	// output
	private List<Label> assignments;
	
	//comparator that helps sort  
	private class AssignmentComparator implements Comparator<Label> {

		@Override
		public int compare(Label label1, Label label2) {
			return label1.getProcessingTime() - label2.getProcessingTime();
		}
	}
	
	public InitialSolution(FlexibleJobShop context) {
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Label>();
		this.context = context; // TODO to copy context or to reference
		this.comparator = new AssignmentComparator();
		this.assignments = new LinkedList<Label>();
		
		// init machine from context
		int nbMachines = context.getNbMachine();		
		for(int i = 0; i < nbMachines; i++) {
			machines.add(new Machine(i + 1)); 
		}
		
		// init available activities
		for(Job job: context.getJobs()) {
			Operation waitingOperation = job.getFirstOperation();
			availableOperations.add(new Label(waitingOperation));	// get the first activity from each job
		}
	}
	
	public void start() {
		//TODO using thread?
		
		int time = 0;
		List<Label> candidateList = new LinkedList<Label>();
		List<Label> chosenCandidate = new LinkedList<Label>();
		
		for(int i = 0; i < 3; i++)	{
		//while(!availableOperations.isEmpty()) {
			
			// make candidate list

			
			candidateList.clear();
			for(Label operation: this.availableOperations) {
				Map<Integer, Integer> tuples = operation.getOperation().getTuples();
				
				for(Integer idMachine: tuples.keySet()) {
					
					Machine machine = machines.get(idMachine - 1);
					if(machine.isReady()) {
						Label candidate = new Label(operation, machine);
						candidateList.add(candidate);
					}
					
				}
			}
			
			System.out.println("availOp = "+availableOperations);
			System.out.println("candidate list = "+candidateList);
			
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
								
				availableOperations.remove(chosenOp);
				Operation nextOp = chosenOp.getNext();
				if(nextOp != null)
					availableOperations.add(new Label(nextOp, best));	// memory the best as its father
			}
			
			// update candidate's father
			for(Label candidate: chosenCandidate) {
				
				Label father = candidate.getCriticalFather();
				
				// if label doesn't have father yet, its the first time we process algo
				// so no need to update the father
				if(father != null) {
					
					if(father.getFinishTime() < time) {
						candidate.setCriticalFatherByMachine();
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
			
			// TODO create option VERBOSE
			System.out.println("solution = "+ assignments);
		}
		
		
	}
	
	public List<Label> getSolution()	{
		return assignments;
	}
	
	public void visualizeSolution() {
		PdfWriter pdfOutput = new PdfWriter("tmp", "is");
		
		String startNode = "start"; //TODO can put this into conf file
		String endNode = "end";		//TODO can put this into conf file
		
		// add nodes
		pdfOutput.addNode(startNode, true, false);
		//System.out.println(getSolution());
		for(Label label: getSolution()) {
			
			// add actual node
			String node = convertNode(label);
			pdfOutput.addNode(node);
			
			// add path by actual node
			System.out.println(label + " has father:  " + label.getFathers());
			for(Label father : label.getFathers()) {
				String from = convertNode(father);
				pdfOutput.addPath(from, node, father.getProcessingTime());
			}
		}
		pdfOutput.addNode(endNode, false, true);
		
		// add paths:
		/*
		for(Label label : getSolution()) {
			
		}
		*/
		
		// TODO: check if we can add node and path at the same time
		
		pdfOutput.write();
	}
	
	private String convertNode(Label label) {
		return label.getOperation().getIdJob() + "." + label.getOperation().getId() + "." + label.getMachine();
	}
	
}

