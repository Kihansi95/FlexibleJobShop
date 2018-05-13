package research.arraysolution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import data.Job;
import data.Operation;

public class JobAS {
	public int id;
	public Queue<OperationAS> operations;
	
	public JobAS(Job job) {
		id = job.getId();
		operations = new LinkedList<OperationAS>();
		
		// add first operation
		Operation opData = job.getFirstOperation();
		OperationAS opAS = new OperationAS(this, opData, null);
		operations.add(opAS);
		
		while(opData.getNext() != null) {
			OperationAS precedent = opAS;
			opData = opData.getNext();
			opAS = new OperationAS(this, opData, precedent);
		}
	}
	
	public void addOperation(OperationAS op) {
		operations.add(op);
	}

	public int getMsIndex() {
		return (id - 1) * operations.size();
	}
	
	public OperationAS pollOperation() {
		return operations.poll();
	}
	
	public List<OperationAS> getAllOperation() {
		return new ArrayList<OperationAS>(this.operations);
	}
}
