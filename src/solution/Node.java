package solution;

import data.Operation;

public class Node {
	
	private int job;
	private int operation;
	private int machine;
	
	private int start; 	// starting time
	private int end;	// end time
	
	private int index;
	
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
		this.index = operation.getIndex();
		this.machine = machine;
		this.start = startTime;
		this.end = finishTime;
	}

	public int getStartTime() {
		return start;
	}
	
	@Override
	public boolean equals(Object another) {
		Node other = (Node) another;
		return another instanceof Node
				&& this.job == other.job 
				&& this.operation == other.operation 
				&& this.machine == other.machine;
	}
	
	public int getIndex() {
		return index;
	}

	public int getOperation() {
		return this.operation;
	}

	public int getJob() {
		return this.job;
	}
	
	
}
