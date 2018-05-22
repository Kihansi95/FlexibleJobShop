package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.*;

public class Graph {
	
	private Map<Operation, Node> nodes;
	
	private List<Edge> disjuncEdges;
	private List<Edge> conjuncArcs;
	
	private boolean changed; // to assert the change in graph's data
	
	/**
	 * Default constructor
	 */
	private Graph() {
		nodes = new TreeMap<Operation, Node>();
		disjuncArcs = new ArrayList<Edge>();
		conjuncArcs = new ArrayList<Edge>();
	}
	
	public Graph(int[] ms, int[] os, FlexibleJobShop context) {
		
		
		
	}
	
	private Node getNode(Operation operation) {
		return nodes.get(operation);	
	}
	
	private List<Edge> getCriticalPath() {
		
		List<Edge> critical_path = new ArrayList <Edge>();
		List<Edge> edges = new ArrayList <Edge>();
		List<Node> currentNodes = new ArrayList <Node>();
		List<Node> nodesFrom = new ArrayList<Node>;
		List<int> cost = new ArrayList<int>;
		edges.addAll(conjuncArcs);
		edges.addAll(disjuncArcs); 
		
		while (!edges.isEmpty()) {
			
		}
		
		
		
		
		
		return edges; 
		
		
		
		
		
	}
	
	
	
	public void update() {
		
	}
}
