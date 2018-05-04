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
	
	public Label getFather()	{
		return this.father;
	}
	
	public void setMachineState(boolean state) {
		this.machine.setState(state);
	}
	
	public void setMachineMemory(Label assignment)	{
		this.machine.memoryLabel(assignment);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Label
				&& ( (this.operation.equals(((Label) other).operation)
				|| this.machine.equals(((Label) other).machine)) );
	}
	
	@Override
	public String toString() {
		return "{" + operation+ "-"+this.machine.getId()+"-"+this.getProcessingTime()+"}";
	}

	public int getFinishTime() {
		return this.finishTime;
	}

	public void setFatherByMachine() {
		this.father = this.machine.getLastAssignment();		
	}

	public void updateFinishTime(int currentTime) {
		int assign_machine = machine.getId();
		finishTime = currentTime + operation.getProcessingTime(assign_machine);	
	}
}
