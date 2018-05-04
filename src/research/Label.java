package research;

import data.Operation;

public class Label {

	/**
	 * Activity with whom this label assoc with
	 */
	private Operation operation;
	
	private Machine machine;
	
	/**
	 * The last activity it has to wait in order to be executed
	 */
	private Label father;
	
	/**
	 * Recorded time when finished
	 */
	private int finishTime;
	
	/**
	 * By default, each operation will follow the precedent operation in the same job
	 * @param operation
	 * @param precedentLabel
	 */
	public Label(Operation operation, Machine machine, Label precedentLabel) {
		this.operation = operation;
		this.father = precedentLabel;
		this.finishTime = -1;
		this.machine = machine;
	}
	
	/**
	 * Constructor for the first label, so it does not has father
	 * @param operation
	 */
	public Label(Operation operation) {
		this(operation, null);
	}
	
	/**
	 * Constructor for assignment candidate
	 * @param operation2
	 * @param machine2
	 */
	public Label(Operation operation, Label precedentLabel) {
		this(operation, null, precedentLabel);
	}

	public Operation getOperation() {
		return this.operation;
	}
	
	public int getProcessingTime() {
		return this.operation.getProcessingTime(this.machine.getId());
	}
}
