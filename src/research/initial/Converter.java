package research.initial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.FlexibleJobShop;
import data.Job;
import data.Operation;
import solution.Edge;
import solution.Graph;
import solution.Node;
import solution.Solution;

public class Converter {

	public static Solution convert(InitialSolution init, FlexibleJobShop context) {
		
		// create ms and os
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
		Map<Operation, Node> nodes = new TreeMap<Operation, Node>();
		for(Label assign: initial_assigments) {
			nodes.put(assign.getOperation(), new Node(assign.getOperation(), assign.getMachine().getId()));
		}
		
		List<Edge> conjunctives = new ArrayList<Edge>();
		List<Edge> disjunctives = new ArrayList<Edge>();
		for(Label assign : initial_assigments) {
			for(Label father : assign.getFathers()) {
				
				Node from = nodes.get(father.getOperation());
				Node to = nodes.get(assign.getOperation());
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
		
		Graph graph = new Graph(nodes, disjunctives, conjunctives);
		
		return new Solution(ms, os, graph);
	}
	
	
	
	
	
}
