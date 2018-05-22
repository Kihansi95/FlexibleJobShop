package research.initial;

import java.util.List;

import data.FlexibleJobShop;
import data.Job;
import data.Operation;
import solution.Arc;
import solution.Node;
import solution.Solution;

public class Converter {

	public static Solution convert(InitialSolution init, FlexibleJobShop context) {
		
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
		
		
		
		Graph graph = new Graph();
		
		return new Solution(ms, os, graph);
	}
	
	
	
	
	
}
