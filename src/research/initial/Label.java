package research.initial;

import java.util.Stack;

import data.Operation;

public class Label {

	/**
	 * Activity with whom this label assoc with
	 */
	private Operation operation;
	
	private Machine machine;
	
	/**
	 * All the possible fathers
	 */
	private Stack<Label> fathers;
	
	/**
	 * The last activity it has to wait in order to be executed
	 */
	private Label criticalFather;
	
	/**
	 * Recorded time when finished
	 */
	private int finishTime;
	
	/**
	 * Constructor for a standard label
	 * @param operation
	 * @param precedentLabel
	 */
	public Label(Operation operation, Machine machine, Label precedentLabel) {
		this.operation = operation;
		this.fathers = new Stack<Label>();
		if(precedentLabel != null) 		// avoid NullPointerException
			this.fathers.push(precedentLabel); 
		this.criticalFather = precedentLabel;
		this.finishTime = -1;
		this.machine = machine;
	}
	
	/**
	 * Constructor of candidate label
	 * @param other : label from available operation
	 * @param machine
	 */
	public Label(Label other, Machine machine) {
		
		// copy from other
		this.operation = other.operation;
		this.fathers = other.fathers;
		this.criticalFather = other.criticalFather;
		this.finishTime = other.finishTime;
		
		// assign new machine
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
	
	public Machine getMachine() {
		return this.machine;
	}
	
	public int getProcessingTime() {
		return this.operation.getProcessingTime(this.machine.getId());
	}
	
	public Label getCriticalFather()	{
		return this.criticalFather;
	}
	
	public Stack<Label> getFathers()	{
		return this.fathers;
	}
	
	public void updateMachineMemory()	{
		this.machine.memoryLabel(this);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Label
				&& ( (this.operation.equals(((Label) other).operation)
				|| this.machine.equals(((Label) other).machine)) );
	}
	
	@Override
	public String toString() {
		String msg = new String("{");
		msg += operation+ ".";
		msg += this.machine == null? "#=?" : this.machine.getId();
		msg += ", processTime: "+this.getProcessingTime();
		msg += ", finishTime: "+ this.finishTime;
		msg += this.criticalFather == null? "" : ", father:"+ this.criticalFather.operation+"."+this.criticalFather.machine.getId();
		return msg + "}";
		//return "{" + operation+ "."+() + () +"}";
	}

	public int getFinishTime() {
		return this.finishTime;
	}

	public void setCriticalFatherByMachine() {
		this.criticalFather = this.machine.getLastAssignment();		
	}

	public void updateFinishTime(int currentTime) {
		int assign_machine = machine.getId();
		finishTime = currentTime + operation.getProcessingTime(assign_machine);	
	}

	public void addFatherFromMachine() {
		Label fatherFromMachine = this.machine.getLastAssignment();
		if(fatherFromMachine != null && fatherFromMachine.getOperation().getIdJob() != this.operation.getIdJob())
			this.fathers.push(fatherFromMachine);
	}
}
