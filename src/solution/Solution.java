package solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.AlgorithmLogicException;
import output.pdflatex.PdfWriter;
import solution.helper.TaskGroup;
import solution.helper.TaskMachine;
import solution.graph.Edge;
import solution.graph.Node;
import solution.helper.Task;
import data.FlexibleJobShop;
import data.Job; 

public class Solution {
	
	private int[] ma ;
	private int[] os ;
	private Graph graph;
	private CriticalPath criticalPath;
	
	private int endTime; 
	
	public Solution(int[] ma, int[] os, Graph graph) {
		this.ma = ma;
		this.os = os;
		this.graph = graph;
	}

	/**
	 * Clone construtor
	 * @param solution
	 */
	public Solution(Solution solution) {
		this.ma = solution.ma.clone();
		this.os = solution.os.clone();
		this.graph = new Graph(solution.graph);
		this.criticalPath = this.graph.getCriticalPath();
	}

	public void setMa(int[] ma, FlexibleJobShop context) throws AlgorithmLogicException {
		for(int i = 0; i < this.ma.length; i++)
			if(this.ma[i] != ma[i]) {
				graph.updateMachineOnNode(i, ma[i]);
			}
		this.ma = ma;
		this.updateGraph(context);
	}
	
	//TODO better ma?
	public void setMa(FlexibleJobShop context, int index, int machine) throws AlgorithmLogicException {
		this.ma[index] = machine;
		graph.updateMachineOnNode(index, machine);
		this.updateGraph(context);
	}
	
	public int[] getMa() {
		return ma;
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
	
	private int getOsIndex(Node node) throws AlgorithmLogicException {
		int op = 0;
		for(int i = 0; i < this.os.length; i++) {
			if(os[i] == node.getJob()) {
				if(op == node.getOperation())
					return i;
				else
					op++;
			}
		}
		
		throw new AlgorithmLogicException("Node not found on os : "+ node);
	}
	
	public void permute (Node operation, Node precedent, FlexibleJobShop context ) throws AlgorithmLogicException {
		
		// find index in os:
		int index_op = getOsIndex(operation);
		int index_prec = getOsIndex(precedent);
		
		// permute the job of the two operation	in os	
		this.os[index_op] = precedent.getJob();
		this.os[index_prec] = operation.getJob();
		
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
			task.resetTime();
			jobs.get(task.getJob()).add(task);
			
			// generate machine for each operation based on MA
			//TaskMachine machine = machines.get(ma[task.getIndex()]);
			task.setMachine(machines, context); // we supposed that machine has been saved into node
			
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
				
		// update the critical path as disjunctives have been changed
		this.criticalPath = graph.getCriticalPath();
	}

	public void visualize(PdfWriter pdfWriter) {
		
		this.graph.visualize(pdfWriter);
		
		pdfWriter.addDescription(new SolutionDescriptor(this.criticalPath));
		
		pdfWriter.write();
	}
	
	@Override
	public String toString() {
		return "{\n\tmakespan : " + this.criticalPath.getMakespan() + ",\n\tcritical path: "+ this.criticalPath + "\n}";
	}
	
}
