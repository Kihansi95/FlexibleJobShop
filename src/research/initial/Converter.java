package research.initial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.FlexibleJobShop;
import data.Job;
import data.Operation;
import solution.Graph;
import solution.Solution;
import solution.graph.Edge;
import solution.graph.Node;
import solution.graph.SpecialNode;
import solution.helper.Task;

public class Converter {

	public static Solution convert(InitialSolution init, FlexibleJobShop context) {
		
		// 1. create ms and os
		int nbMachine = context.getNbMachine();
		int nbOp = context.getNbOperation();
		
		int ms[] = new int[nbOp];
		int os[] = new int[nbOp];
		
		List<Label> initial_assigments = init.getAssignments();
		int index = 0;
		for(Label assign : initial_assigments) {
			int index_op = assign.getOperation().getIndex();
			ms[index_op] = assign.getMachine().getId();
			os[index++] = assign.getOperation().getIdJob();
		}
		
		// create associated graph
		Map<Operation, Node> nodes = new HashMap<Operation, Node>();
		for(Label assign: initial_assigments) {
			
			// create normal Node for corresponding label
			Node n = new Task(
					assign.getOperation(), 
					assign.getMachine().getId(),
					assign.getStartTime(),
					assign.getFinishTime(),
					assign.getProcessingTime()
					);
			nodes.put(assign.getOperation(), n);
		}
		
		// create special node for end node and start node
		Node end_node = new SpecialNode(false);
		Node start_node = new SpecialNode(true);
		
		List<Edge> conjunctives = new ArrayList<Edge>();
		List<Edge> disjunctives = new ArrayList<Edge>();
		for(Label assign : initial_assigments) {
			
			Node to = nodes.get(assign.getOperation());
			
			// case first node of the job
			// add conjunctive edge (start_node -> first_node)
			if(assign.getOperation().getId() == 0) {
				conjunctives.add(new Edge(start_node, to, 0));
			}
			
			// case last node of the job
			// add conjunctive edge (last_node -> end_node)
			if(assign.getOperation().isLast()) {
				Node last = to;
				conjunctives.add(new Edge(last, end_node, 0));
			}
			
			// other case add edge for every father found
			for(Label father : assign.getFathers()) {
				
				Node from = nodes.get(father.getOperation());
				int value = father.getProcessingTime();
				
				// linked by the same job constraint -> conjunctive
				if(father.getOperation().getIdJob() == assign.getOperation().getIdJob()) {
					
					// assert for security
					if(father.getOperation().getId() != assign.getOperation().getId() - 1)
						throw new RuntimeException("Error");
					
					conjunctives.add(new Edge(from, to, value));
				} else {
					disjunctives.add(new Edge(from, to, value));
				}
			}
		}
		
		Graph graph = new Graph(nodes, disjunctives, conjunctives, start_node, end_node);
		
		return new Solution(ms, os, graph);
	}
	
	
	
	
	
}
