package solution.graph;

import java.util.HashMap;
import java.util.Map;

import data.Operation;

public class Node {
	
	protected int job;
	protected int operation;
	protected int machine;
	
	private int index;
	
	/**
	 * Empty node, initialize with default value. Can be use for end node and start node
	 */
	public Node() {
		this.job = Integer.MIN_VALUE;
		this.operation = Integer.MIN_VALUE;
		this.machine = Integer.MIN_VALUE;
		this.index = Integer.MIN_VALUE;
		/*
		this.successors = new HashMap<Node, Edge>();
		this.predecessors = new HashMap<Node, Edge>();*/
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
	
	public int getMachine() {
		return this.machine;
	}

	public void setMachine(int machine) {
		this.machine = machine;
	}
	
	@Override
	public boolean equals(Object node) {
		return node instanceof Node
				&& ((Node) node).index == this.index
				&& ((Node) node).job == this.job
				&& ((Node) node).machine == this.machine
				&& ((Node) node).operation == this.operation;
	}

}
