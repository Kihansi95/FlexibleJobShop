package research.initial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.*;
import jobshopflexible.Configuration;
import output.pdflatex.PdfWriter;
import solution.Solution;
import utility.Verbose;

public class InitialSolution extends Verbose {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Label> availableOperations;
	
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
			diff = diff == 0 ? label1.getMachine().getId() - label2.getMachine().getId() : diff;
			return diff;
			
		}
	}
	
	private class FinishTimeComparator implements Comparator<Label> {

		@Override
		public int compare(Label label1, Label label2) {
			return label1.getFinishTime() - label2.getFinishTime();
		}
		
	}
	
	private class ReversedFinishTimeComparator implements Comparator<Label> {
		
		@Override
		public int compare(Label label1, Label label2) {
			return label2.getFinishTime() - label1.getFinishTime();
		}
	}
	
	public InitialSolution(Configuration conf, FlexibleJobShop context) {
		
		super(conf, "is");
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Label>();
		this.context = context; // TODO to copy context or to reference
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
		// new algo 
		int time = 0;
		
		List<Label> candidates = new ArrayList<Label>();
		List<Label> waiting = this.availableOperations;
		List<Label> processing = new ArrayList<Label>();
		
		int step = 0;
		
		while(!waiting.isEmpty() || !processing.isEmpty()) {
			
			candidates.clear();
			
			// for all waiting operation
			for(Label operation : waiting) {
				Map<Integer, Integer> tuples = operation.getOperation().getTuples();
				
				// for all machine that can process this operation
				for(Integer idMachine: tuples.keySet()) {
					Machine machine = machines.get(idMachine - 1);
					
					// check if machine is ready (does not have any other operation running on it)
					if(machine.isReady(time)) {
						Label candidate = new Label(operation, machine);
						candidates.add(candidate);
					}
					
				}
			}
			
			// choose best candidates base on processing time
			candidates.sort(new ProcessingTimeComparator());
			while(!candidates.isEmpty()) {
				
				// choose the best
				Label best = candidates.remove(0);
				
				// move from waiting to processing list
				waiting.remove(best);
				processing.add(best);
				
				// update all processing information
				best.updateFinishTime(time);
				best.setStartTime(time);
				best.addFatherFromMachine();
				
				// check father
				Label father = best.getCriticalFather();
				
				if(father == null && best.getMachine().getMemory() != null) {
					best.setCriticalFatherByMachine();
				}
				
				if(father != null && father.getFinishTime() < time) {						
					best.setCriticalFatherByMachine();
				}
				
				// update machine after check with father
				best.updateMachineMemory();
				
				// remove duplicata machine || operation from candidate list
				while(candidates.remove(best));
			}
			
			// update time to the soonest finished operation in processing
			processing.sort(new FinishTimeComparator());
			time = processing.get(0).getFinishTime();
			
			// after the process, remove from the processing list and enqueue the next op
			//for(Label finished : processing) {
			
			for(Iterator<Label> it = processing.iterator(); it.hasNext(); ) {
				Label finished = it.next();
			
				if(finished.getFinishTime() <= time) {
					
					it.remove();
					//processing.remove(finished);	// check if we accidentally remove another operation
													// due to the usage of same machine, which is illogical
					Operation nextOp = finished.getOperation().getNext();
					if(nextOp != null)
						waiting.add(new Label(nextOp, finished));	// memory the best as its father
					
					// save this one into solution
					this.assignments.add(finished);
				}
			}
			
			if(verbose)
				System.out.println("[IS - " + (step++) + ", time = "+ time +"] Solution = "+ assignments);
		}
	}

	public void visualizeSolution() {
		
		// copy so that not modify it
		List<Label> solution = getAssignments();
		solution.sort(new IdOperationComparator());
		System.out.println(solution);
		
		// start node
		pdfOutput.addStartNode("start");
		
		// assignment nodes
		for(Label label: solution) {
			
			// add actual node
			String node = convertNode(label);
			String param = convertParam(label);
			pdfOutput.addNode(node, param);
			
			// add path by actual node
			for(Label father : label.getFathers()) {
				String from = convertNode(father);
				pdfOutput.addPath(from, node, father.getProcessingTime());
			}
			
			// start link to first op of all job
			if(label.getOperation().getId() == 0)
				pdfOutput.addPath("start", node, 0);
			
		}
		
		// end node		
		for(Job job : context.getJobs()) {
			Operation lastOp = job.getLastOperation();
			
			for(Label label: solution) {
				if(label.getOperation().equals(job.getLastOperation())) {
					pdfOutput.addPath(convertNode(label), "end", label.getProcessingTime());
					
					if(lastOp.getIdJob() == (context.getJobs().size()/2))
						pdfOutput.addNode("end", "right of=" + convertNode(label));
				}
					
			}
			
		}
		
		// add description
		solution.sort(new ReversedFinishTimeComparator());
		Label last_label_in_critical_path = solution.get(0);
		System.out.println("Last label critical path : " + last_label_in_critical_path);
		pdfOutput.addDescription(new LabelDescriptor(last_label_in_critical_path, this));
		
		// clean template and write down
		pdfOutput.write();
	}
	
	public String convertNode(Label label) {
		final String SEPARATOR = "/";
		return label.getOperation().getIdJob() + SEPARATOR + label.getOperation().getId() + SEPARATOR + label.getMachine().getId();
	}
	
	public String convertParam(Label label) {
		
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
		for(Label upperLabel : this.getAssignments()) {
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

	public List<Label> getAssignments() {
		return new ArrayList<Label>(this.assignments);
	}
	
}
