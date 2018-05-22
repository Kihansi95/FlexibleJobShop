package solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import data.Operation;
import research.arraysolution.MachineAS;
import research.initial.Label;
import research.initial.Machine;
import solution.helper.TaskMachine;
import solution.helper.TaskNode;
import data.FlexibleJobShop;
import data.Job; 

public class Solution {
	
	private int[] ms ;
	private int[] os ;
	private Graph graph;
	private CriticalPath criticalPath;
	
	private int endTime; 
	
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
	
	private void updateGraph(FlexibleJobShop context) {
		List<TaskJob> jobs = new ArrayList<TaskJob>();
		List<TaskMachine> machines = new ArrayList<TaskMachine>();
		List<TaskNode> tasks = new ArrayList<TaskNode>();
		
		// prepare machines
		int nbMachine = context.getNbMachine();
		for(int idMachine = 1; idMachine <= nbMachine; idMachine++) {
			machines.add(new TaskMachine(idMachine));
		}
		
		// TODO not complete
		for(Node node : graph.getNodes()) {
			tasks.add(new TaskNode(node));
		}
		
		// generate machine for each operation based on MS
		for(TaskNode task : tasks) {
			TaskMachine machine = machines.get(ms[task.getIndex()]);
			task.machine = machine;
		}
		
		// calculation of starting and completion time
		for(int job : this.os) {
			
		}
	}
}
