package solution;

import data.Operation;

public class Node {
	
	private int job;
	private int operation;
	private int machine;
	
	
	private int index;
	
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
		this.index = operation.getIndex();
		this.machine = machine;
	}
	
	public Node(Node node) {
		this.job = node.job;
		this.operation = node.operation;
		this.index = node.operation;
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
		return job+"."+operation+"."+machine;
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
	
<<<<<<< HEAD
	protected int getMachineId() {
		return this.machine;
	}
=======
	public int getMachine() {
		return machine;
	}
	
	
>>>>>>> branch 'master' of https://github.com/Kihansi95/FlexibleJobShop.git
}
