package solution;

import data.Operation;
import solution.helper.TaskMachine;

public class Node {
	
	private int job;
	private int operation;
	private int machine;
	
	private int index;
	
	/**
	 * Empty node, initialize with default value. Can be use for end node and start node
	 */
	public Node() {
		this.job = Integer.MIN_VALUE;
		this.operation = Integer.MIN_VALUE;
		this.machine = Integer.MIN_VALUE;
		this.index = Integer.MIN_VALUE;
	}
	
	/**
	 * Node correspond to Label
	 * @param operation
	 * @param machine
	 */
	public Node(Operation operation, int machine) {
		this();
		this.job = operation.getIdJob();
		this.operation = operation.getId();
		this.index = operation.getIndex();
		this.machine = machine;
	}
	
	public Node(Node node) {
		this.job = node.job;
		this.operation = node.operation;
		this.index = node.index;
		this.machine = node.machine;
	}
	
	@Override
	public boolean equals(Object another) {
		Node other = (Node) another;
		return another instanceof Node
				&& this.job == other.job 
				&& this.operation == other.operation 
				&& this.machine == other.machine;
	}
	
	@Override
	public String toString() {
		
		final String SEPARATOR = "/";
		return job + SEPARATOR + operation + SEPARATOR + machine;
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
	
	protected int getMachine() {
		return this.machine;
	}
}
