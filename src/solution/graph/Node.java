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
	
	public int getMachine() {
		return this.machine;
	}

	/*
	private Map<Node, Edge> successors, predecessors;
	public void addSuccessor(Node successor, Edge edge) {
		successors.put(successor, edge);
	}

	public void addPredecessor(Node predecessor, Edge edge) {
		predecessors.put(predecessor, edge);
	}
	
	public Map<Node, Edge> getSuccessors() {
		return new HashMap<Node, Edge>(this.successors);
	}
	
	public Map<Node, Edge> getPredecessor() {
		return new HashMap<Node, Edge>(this.predecessors);
	}
	*/
	public void setMachine(int machine) {
		this.machine = machine;
	}

}
