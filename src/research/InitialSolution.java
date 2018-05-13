package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import data.*;
import jobshopflexible.Configuration;
import output.pdflatex.PdfWriter;
import utility.Verbose;

public class InitialSolution extends Verbose {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Label> availableOperations;
	private ProcessingTimeComparator comparator;
	
	// output
	private List<Label> assignments;	// solution
	private PdfWriter pdfOutput;		// solution visualizer
	
	// comparator based on processing time
	private class ProcessingTimeComparator implements Comparator<Label> {

		@Override
		public int compare(Label label1, Label label2) {
			return label1.getProcessingTime() - label2.getProcessingTime();
		}
	}
	
	// comparator based on operation's id
	private class IdOperationComparator implements Comparator<Label> {
		
		@Override
		public int compare(Label label1, Label label2) {
			int diff = label1.getOperation().getIdJob() - label2.getOperation().getIdJob();
			diff = diff == 0 ? label1.getOperation().getId() - label2.getOperation().getId() : diff;
			diff = diff == 0 ? label1.getMachine() - label2.getMachine() : diff;
			return diff;
			
		}
	}
	
	public InitialSolution(Configuration conf, FlexibleJobShop context) {
		
		super(conf, "is");
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Label>();
		this.context = context; // TODO to copy context or to reference
		this.comparator = new ProcessingTimeComparator();
		this.assignments = new LinkedList<Label>();
		pdfOutput = new PdfWriter(conf);
		
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
		
		//for(int i = 0; i < 3; i++)	{
		while(!availableOperations.isEmpty()) {
			
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
				availableOperations.remove(best);
				
				Operation nextOp = best.getOperation().getNext();
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
					candidate.updateMachineMemory();
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
			chosenCandidate.clear(); 
			
			if(verbose)
				System.out.println("solution = "+ assignments);
		}
		
		
	}
	
	public List<Label> getSolution()	{
		return assignments;
	}
	
	public void visualizeSolution() {
		
		assignments.sort(new IdOperationComparator());
		System.out.println(assignments);
		
		
		// start node
		pdfOutput.addStartNode("start");
		
		// assignment nodes
		for(Label label: getSolution()) {
			
			// add actual node
			String node = convertNode(label);
			String param = convertParam(label);
			pdfOutput.addNode(node, param);
			
			// add path by actual node
			for(Label father : label.getFathers()) {
				String from = convertNode(father);
				pdfOutput.addPath(from, node, father.getProcessingTime());
			}
			
			// if there ain't father == first op of the job
			if(label.getFathers().isEmpty())
				pdfOutput.addPath("start", node, 0);
			
		}
		
		// end node		
		for(Job job : context.getJobs()) {
			Operation lastOp = job.getLastOperation();
			
			for(Label label: assignments) {
				if(label.getOperation().equals(job.getLastOperation())) {
					pdfOutput.addPath(convertNode(label), "end", label.getProcessingTime());
					
					if(lastOp.getIdJob() == (context.getJobs().size()/2))
						pdfOutput.addNode("end", "right of=" + convertNode(label));
				}
					
			}
			
		}
		
		
		// clean template and write down
		pdfOutput.write();
	}
	
	private String convertNode(Label label) {
		final String SEPARATOR = "/";
		return label.getOperation().getIdJob() + SEPARATOR + label.getOperation().getId() + SEPARATOR + label.getMachine();
	}
	
	private String convertParam(Label label) {
		
		int job = label.getOperation().getIdJob();
		int op = label.getOperation().getId();
		
		// for the first job's operation line
		if(job == 0) {
			if(op == 0)
				return "above right of=start";
			
			// get father of the same job
			for (Label father : label.getFathers())
				if(father.getOperation().getIdJob() == job)
					return "right of="+ convertNode(father);
		}
		
		// for other operation
		// TODO dirty method
		for(Label upperLabel : this.assignments) {
			if(upperLabel.getOperation().getIdJob() == (job - 1) && upperLabel.getOperation().getId() == op) {
				
				// alternate left / right for each line
				return "below " + (job % 2 == 0 ? "left" : "right") + " of=" + convertNode(upperLabel);
				
			}
		}
		
		// how can you reach here ???
		if(verbose)
			System.err.println("error at label "+label);
		throw new RuntimeException("Can't be here");
		
	}
	
}
