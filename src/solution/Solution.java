package solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import data.Operation;
import exception.AlgorithmLogicException;
import research.arraysolution.MachineAS;
import research.initial.Label;
import research.initial.Machine;
import solution.helper.TaskGroup;
import solution.helper.TaskMachine;
import solution.helper.Task;
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
	
	private void updateGraph(FlexibleJobShop context) throws AlgorithmLogicException {
		
		List<TaskGroup> jobs = new ArrayList<TaskGroup>();
		List<TaskMachine> machines = new ArrayList<TaskMachine>();
		
		// prepare machines
		int nbMachine = context.getNbMachine();
		for(int idMachine = 1; idMachine <= nbMachine; idMachine++) {
			machines.add(new TaskMachine(idMachine));
		}
		
		// prepare jobs and operations		
		for(Node node : graph.getNodes()) {
			Task task = new Task(node, context);
			jobs.get(node.getJob() - 1).add(task);
			
			// generate machine for each operation based on MS
			//TaskMachine machine = machines.get(ms[task.getIndex()]);
			task.setMachine(machines); // we supposed that machine has been saved into node
			
		}
		
		
		// calculation of starting and completion time
		for(int id_job : this.os) {
			
			TaskGroup job = jobs.get(id_job - 1); // TODO check if we use jobs[idJob - 1] for something else to be sure that we can get the correct job here
			Task task = job.poll();
			TaskMachine machine = task.getMachine();
			
			int allowable_starting_time = -1;
			if(job.hasPrevious(task)) {
				allowable_starting_time = job.previous(task).completionTime;
			} else {
				allowable_starting_time = 0;
			}
			
			task.startingTime = machine.checkIdleArea(allowable_starting_time, task.processingTime);
			if(task.startingTime < 0) {
				// this means no idle area possible
				task.startingTime = Math.max(allowable_starting_time, machine.getLastCompletionTime());
			}
			
			// update completion time of task
			task.updateCompletionTime();
			
			// update machine's memory
			machine.addTask(task);
		}
		
		// update disjunctive edges
		for(TaskMachine machine : machines) {
			for(Task task : machine.schedule()) {
				
			}
		}
	}
	
	
}
