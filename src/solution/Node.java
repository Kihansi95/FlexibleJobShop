package solution;

import data.Operation;

public class Node {
	
	int job;
	int operation;
	int machine;
	
	/**
	 * Default constructor
	 */
	private Node() {
		this.job = Integer.MIN_VALUE;
		this.operation = Integer.MIN_VALUE;
		this.machine = Integer.MIN_VALUE;
	}
	
	public Node(Operation operation, int machine) {
		this();
		this.job = operation.getIdJob();
		this.operation = operation.getId();
		this.machine = machine;
	}
	
}
