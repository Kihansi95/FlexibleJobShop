package solution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import data.Operation;
import research.initial.Label;
import research.initial.Machine;
import data.FlexibleJobShop;
import data.Job; 

public class Solution {
	
	private int[] ms ;
	private int[] os ;
	private Graph graph;
	private CriticalPath criticalPath;
	
	private int endTime; 
	
	/**
	 * Initialize solution
	 * @param ms
	 * @param os
	 * @param context
	 */
	public Solution(int[] ms, int[] os, FlexibleJobShop context) {
		
		List<Job> jobs = context.getJobs();
		
		// init array ms and os
		int nbOps = context.getNbOperation();
		this.ms = new int[nbOps];
		this.os = new int[nbOps];
		
		// fill os by assign all job number and the same case
		int id = 0;
		for(Job job : jobs) {
			for(int i = 0; i < job.getNbOperation(); i++) {
				this.os[id++] = job.getId();
			}
		}

		// fill ms by choosing randomly
		for(Job job : jobs) {
			for(Operation op: job.getOperations()) {
				
				// list all possible machine to assign for this op
				List<Integer> possible_assigned_machines = context.getMachines(op);
				
				// randomly pick a machine
				int rd_index = (int) (Math.random() * (possible_assigned_machines.size() - 1));
				int chosen_machine = possible_assigned_machines.get(rd_index);
				
				// assign to ms
				ms[op.getIndex()] = chosen_machine;
			}
		}
		
		graph = new Graph(this.ms, this.os, context);
	}
	
	public Solution(int[] ms, int[] os, Graph graph) {
		this.ms = ms;
		this.os = os;
		this.graph = graph;
	}

	public void setMs(int[] ms) {
		this.ms = ms;
	}
	
	public int[] getMs() {
		return ms;
	}

	public int[] getOs() {
		return os;
	}


	public CriticalPath getCriticalPath() {
		return this.criticalPath;
	}


	public Graph getGraphe() {
		return graph;
	}


	public int getEndTime() {
		return endTime;
	}
	
	public int getMakespan() {
		return this.getCriticalPath().getMakespan();
	}
	
	
	public void permute (Operation opA, Operation opB) {
		int indexA=opA.getIndex();
		int indexB=opB.getIndex();
		this.os[indexA]=opB.getId();
		this.os[indexB]=opA.getId();
		this.graph.update(opA,opB);
	}
	

}
