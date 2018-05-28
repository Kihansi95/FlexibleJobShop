package solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import data.Operation;
import exception.AlgorithmLogicException;
import output.pdflatex.PdfWriter;
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


	public Graph getGraph() {
		return graph;
	}


	public int getEndTime() {
		return endTime;
	}
	
	public int getMakespan() {
		return this.getCriticalPath().getMakespan();
	}
	
	
	public void permute (Node opA, Node opB, FlexibleJobShop context )throws AlgorithmLogicException {
		int indexA=opA.getOperation();
		int indexB=opB.getOperation();
		this.os[indexA]=opB.getOperation();
		this.os[indexB]=opA.getOperation();
		this.updateGraph(context);
	}
	
	public void updateGraph(FlexibleJobShop context) throws AlgorithmLogicException {
		
		List<TaskGroup> jobs = new ArrayList<TaskGroup>();
		List<TaskMachine> machines = new ArrayList<TaskMachine>();
		
		// prepare machines
		int nbMachine = context.getNbMachine();
		for(int idMachine = 1; idMachine <= nbMachine; idMachine++) {
			machines.add(new TaskMachine(idMachine));
		}
		
		// prepare jobs and operations	
		for(Job job : context.getJobs()) {
			jobs.add(new TaskGroup(job));
		}
		for(Node node : graph.getNodes()) {
			Task task = (Task) node;
			jobs.get(task.getJob()).add(task);
			
			// generate machine for each operation based on MS
			//TaskMachine machine = machines.get(ms[task.getIndex()]);
			task.setMachine(machines); // we supposed that machine has been saved into node
			
		}
		
		
		// calculation of starting and completion time
		for(int id_job : this.os) {
			
			TaskGroup job = jobs.get(id_job); // TODO check if we use jobs[idJob] for something else to be sure that we can get the correct job here
			
			Task task = job.poll();			
			TaskMachine machine = task.getTaskMachine();
			
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
		List<Edge> disjuncs = new ArrayList<Edge>();
		for(TaskMachine machine : machines) {
			List<Task> tasks = machine.schedule();
			for(int i = 0; i < tasks.size() - 1; i++) {
				
				Task from = tasks.get(i), to = tasks.get(i + 1);
				
				if(from.getJob() != to.getJob()) // avoid to duplicate with conjunctive edges
					disjuncs.add(new Edge(tasks.get(i), tasks.get(i+1), tasks.get(i).processingTime));
			}
		}
		
		graph.setDisjunctiveEdges(disjuncs);
	}

	public void visualize(PdfWriter pdfWriter) {
		this.graph.visualize(pdfWriter);
		
	}
	
	
}
