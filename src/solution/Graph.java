package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.*;

public class Graph {
	
	private Map<Operation, Node> nodes;
	
	private List<Arc> disjuncArcs;
	private List<Arc> conjuncArcs;
	
	private boolean changed; // to assert the change in graph's data
	
	/**
	 * Default constructor
	 */
	private Graph() {
		nodes = new TreeMap<Operation, Node>();
		disjuncArcs = new ArrayList<Arc>();
		conjuncArcs = new ArrayList<Arc>();
	}
	
	public Graph(int[] ms, int[] os, FlexibleJobShop context) {
		
		
		
	}
	
	private Node getNode(Operation operation) {
		for()
	}
	
	
	
	public void update() {
		
	}
}
