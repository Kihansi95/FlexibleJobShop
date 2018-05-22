package solution.helper;

import solution.Node;

public class TaskNode extends Node {
	
	int startingTime;
	int completionTime;
	//int processing;
	
	//private TaskMachine machine;
	
	public TaskNode(Node node) {
		super(node);
		this.startingTime = Integer.MAX_VALUE;
		this.completionTime = Integer.MAX_VALUE;
	}
	
	public void setCompletionTime(int processingTime) {
		this.completionTime = this.startingTime + processingTime;
	}
}
