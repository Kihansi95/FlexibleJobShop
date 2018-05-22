package solution;

import data.Operation;

public class Node {
	
	private int job;
	private int operation;
	private int machine;
	private int start; 	// starting time
	private int end;	// end time
	
	/**
	 * Default constructor
	 */
	private Node() {
		this.job = Integer.MIN_VALUE;
		this.operation = Integer.MIN_VALUE;
		this.machine = Integer.MIN_VALUE;
	}
	
	public Node(Operation operation, int machine, int startTime, int finishTime) {
		this();
		this.job = operation.getIdJob();
		this.operation = operation.getId();
		this.machine = machine;
		this.start = startTime;
		this.end = finishTime;
	}
	
}
