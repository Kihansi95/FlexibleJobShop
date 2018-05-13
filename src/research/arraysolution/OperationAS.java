package research.arraysolution;

import data.Operation;

public class OperationAS  {
	private JobAS job;
	private int id;
	
	public int startingTime;
	public int completionTime;
	public int processingTime;
	
	public MachineAS machine;
	public OperationAS precedent;
	
	public OperationAS(JobAS job, Operation operation, OperationAS precedent) {
		this.job = job;
		this.id = operation.getId();
		this.precedent = precedent;
		
		this.machine = null;
		this.startingTime = -1;
		this.processingTime = -1;
		this.completionTime = -1;
	}

	public int getMsIndex() {
		return job.getMsIndex() + this.id;
	}

	public void updateCompletionTime() {
		this.completionTime = this.startingTime + this.processingTime;		
	}
	
}
