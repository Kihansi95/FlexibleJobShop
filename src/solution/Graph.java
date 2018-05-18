package solution;

import java.util.ArrayList;
import java.util.List;

import data.*;

public class Graph {
	
	private List<Node> nodes;
	
	private List<Arc> disjuncArcs;
	private List<Arc> conjuncArcs;
	
	private boolean changed; // to assert the change in graph's data
	
	/**
	 * Default constructor
	 */
	private Graph() {
		nodes = new ArrayList<Node>();
		disjuncArcs = new ArrayList<Arc>();
		conjuncArcs = new ArrayList<Arc>();
	}
	
	public Graph(int[] ms, int[] os, FlexibleJobShop context) {
		
		
		for(Job job : context.getJobs()) {
			
			for(Operation op : job.getOperations()) {
				
				// init node for op
				int index_op = index(op, context);	// calculate index in the ms string
				int machine = ms[index_op];			// get the assigned machine id
				nodes.add(new Node(op, machine));	// create new node corresponding this assignment
				
				// init conjunctive arcs for op
				Operation successor = op.getNext();
				if(successor != null) {
					this.conjuncArcs.add(new Arc(op, successor, op.getProcessingTime(machine)));
				}
			}
			
			for(Operation op : job.getOperations()) {
				
				nodes.get()
			}
				
		}
	}
	
	private Node getNode(Operation operation) {
		for()
	}
	
	private int index(Operation operation, FlexibleJobShop context) {
		List<Job> jobs = context.getJobs();
		int index = 0;
		for(int i = 0; i < operation.getIdJob(); i++) {
			index += jobs.get(i).getNbOperation();
		}
		
		return index + operation.getId();
	}
	
	public void update() {
		
	}
}
