package solution.helper;

import java.util.LinkedList;
import java.util.Queue;

import data.Job;
import research.arraysolution.OperationAS;

public class TaskGroup { // == job
	public int id;
	public Queue<Task> tasks;
	
	public TaskGroup(Job job) {
		id = job.getId();
		tasks = new LinkedList<Task>();
		
		// add first operation
		Operation opData = job.getFirstOperation();
		Task opAS = new OperationAS(this, opData, null);
		operations.add(opAS);
		
		while(opData.getNext() != null) {
			OperationAS precedent = opAS;
			opData = opData.getNext();
			opAS = new OperationAS(this, opData, precedent);
		}
	}
}
