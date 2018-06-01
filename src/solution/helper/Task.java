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
	
	public void resetTime() {
		this.startingTime = Integer.MIN_VALUE;
		this.completionTime = Integer.MIN_VALUE;
	}

	public void updateCompletionTime() {
		this.completionTime = this.startingTime + this.processingTime;
	}

	public void setMachine(List<TaskMachine> machines, FlexibleJobShop context) throws AlgorithmLogicException {
		this.machine = machines.get(this.getMachine() - 1);	
		this.processingTime = context.getJobs().get(this.job)
									.getOperations().get(this.operation).getProcessingTime(machine.getId());
	}
	
	public TaskMachine getTaskMachine() throws AlgorithmLogicException {
		if(machine == null) {
			throw new AlgorithmLogicException("TaskMachine has never been assigned to this task "+this);
		}
		return machine;
	}
	
	/*
	public String getString() {
		return "{"+startingTime+"-"+completionTime+"}";
	}*/

	public String getSchedule() {
		return "{"+this.toString()+" : ["+startingTime +" - "+completionTime +"]}";
	}
	
	public boolean equals(Object task) {
		return super.equals(task);
	}
}
