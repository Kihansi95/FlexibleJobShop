package solution.helper;

import java.util.List;

import data.FlexibleJobShop;
import data.Operation;
import exception.AlgorithmLogicException;
import solution.graph.Node;

public class Task extends Node {
	
	public int startingTime;
	public int completionTime;
	public int processingTime;
	
	private TaskMachine machine;
	
	public Task(Operation operation, int machine, int startingTime, int completionTime, int processingTime) {
		super(operation, machine);
		this.startingTime = startingTime;
		this.processingTime = processingTime;
		this.completionTime = completionTime;
	}
	
	/*
	public Task(Node node, FlexibleJobShop context) throws AlgorithmLogicException {
		super(node);
		this.startingTime = Integer.MAX_VALUE;
		this.completionTime = Integer.MAX_VALUE;
		this.processingTime = context
								.getJobs().get(this.getJob())
								.getOperations().get(this.getOperation())
								.getProcessingTime(this.getMachine());
		machine = null ;
	}
	*/

	public void updateCompletionTime() {
		this.completionTime = this.startingTime + this.processingTime;
	}

	public void setMachine(List<TaskMachine> machines) throws AlgorithmLogicException {
		this.machine = machines.get(this.getMachine() - 1);		
	}
	
	public TaskMachine getTaskMachine() throws AlgorithmLogicException {
		if(machine == null) {
			throw new AlgorithmLogicException("TaskMachine has never been assigned to this task "+this);
		}
		return machine;
	}
	
	public String toString() {
		return "{"+startingTime+"-"+completionTime+"}";
	}
	
}
