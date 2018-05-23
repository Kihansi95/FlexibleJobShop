package research.arraysolution;

import java.util.ArrayList;
import java.util.List;

import data.FlexibleJobShop;
import data.Job;
import jobshopflexible.Configuration;
import utility.Verbose;

/**
 * The class that will process function for solutions with 
 * presentation with 2 array MA and OA
 */
public class SolutionHelper extends Verbose {
	
	public SolutionHelper(Configuration conf) {
		super(conf, "sh");
	}
	
	public List<MachineAS> decode(FlexibleJobShop context, int[] ms, int[] os) {
	
		List<JobAS> jobs = new ArrayList<JobAS>();
		List<MachineAS> machines = new ArrayList<MachineAS>();
		List<OperationAS> operations = new ArrayList<OperationAS>();
		
		// prepare machines
		int nbMachine = context.getNbMachine();
		for(int idMachine = 1; idMachine <= nbMachine; idMachine++) {
			machines.add(new MachineAS(idMachine));
		}
		
		// prepare jobs and operations
		for(Job j : context.getJobs()) {
			
			// create empty job-AS
			JobAS job = new JobAS(j);
			operations.addAll(job.getAllOperation());
			jobs.add(job);
		}
		
		
		// generate machine for each operation based on MS
		for(OperationAS op : operations) {
			int indexOp = op.getMsIndex();
			MachineAS machine = machines.get(ms[indexOp]);
			op.machine = machine;
		}
		
		// Calculation of starting and completion time
		for(int idJob : os) {
			
			JobAS job = jobs.get(idJob - 1); // TODO check if we use jobs[idJob - 1] for something else to be sure that we can get the correct job here
			OperationAS operation = job.pollOperation();
			MachineAS machine = operation.machine;
			
			int allowable_starting_time = -1;
			if(operation.precedent == null) {
				
				// the first operation of the job
				allowable_starting_time = 0;
			} else {
				
				allowable_starting_time = operation.precedent.completionTime;
			}
			
			operation.startingTime = machine.checkIdleArea(allowable_starting_time, operation.processingTime);
			
			if(operation.startingTime < 0) {
				// it means no idle area possible
				operation.startingTime = Math.max(allowable_starting_time, machine.getLastCompletionTime());
			}
			
			// update operation's completion time
			operation.updateCompletionTime();
			
			// update machine state
			machine.addOperation(operation);
		}
		
		return machines;
		
	}
}
