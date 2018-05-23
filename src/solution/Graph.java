package solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.*;
import research.initial.Label;

public class Graph {
	
	private Map<Operation, Node> nodes;
	
	private List<Edge> disjuncEdges;
	private List<Edge> conjuncEdges;
	
	private boolean changed; // to assert the change in graph's data
	
	/**
	 * Default constructor
	 */
	public Graph(Map<Operation, Node> nodes, List<Edge> disjunctives, List<Edge> conjunctives) {
		this.nodes = nodes;
		this.disjuncEdges = disjunctives;
		this.conjuncEdges = conjunctives;
		
	}
	
	public Node getDisjunctiveFather(Node current ) {
		for (Edge edge : this.disjuncEdges) 
			if (edge.getSuccessor().equals(current)) 
				return edge.getPredecessor();
		return null;
	}
	
	public Node getConjunctiveFather(Node current) {
		for(Edge edge: this.conjuncEdges) 
			if(edge.getSuccessor().equals(current)) 
				return edge.getPredecessor();
		return null;
	}
	
	private Node getNode(Operation operation) {
		return nodes.get(operation);	
	}
	
	public CriticalPath getCriticalPath() {
		
		
		List<Edge> edges = new ArrayList <Edge>();
		List<Node> currentNodes = new ArrayList <Node>();
		List<Node> nodesFrom = new ArrayList<Node>();
		List<Integer> cost = new ArrayList<Integer>();
		edges.addAll(conjuncEdges);
		edges.addAll(disjuncEdges); 
		edges.sort(new EdgeValueComparator());
		int previousCost=0;
		

		
		for (Edge iteredge : edges) {
			if (!(nodesFrom.contains(iteredge.from)&&currentNodes.contains(iteredge.to))) {
				nodesFrom.add(iteredge.from);
				currentNodes.add(iteredge.to);
				for (Node nd : currentNodes) {
					if (nd==iteredge.from) {
						previousCost=cost.get(currentNodes.indexOf(nd)); 
					}
				}
				cost.add(iteredge.value+previousCost);
			}
		}
		
		int maxcost =0;
		int index =0;
		int maxIndex=0;;
		boolean endOfList=false;
		
		
		
		for (int currentCost : cost) {
			if (currentCost > maxcost) {
				maxcost=currentCost;
				maxIndex=index;
			}
			index++;
		}
			 
		while (!endOfList) {
			edges.add(new Edge(nodesFrom.get(maxIndex), currentNodes.get(maxIndex), cost.get(maxIndex)));
			maxIndex=currentNodes.indexOf(nodesFrom.get(maxIndex));
			if (maxIndex<0) {
				endOfList=true;
			}
			
		}
		
		CriticalPath criticalPath = new CriticalPath(edges,maxcost);
		
		return criticalPath; 

	}
	
	
	
	public void update(Operation opA, Operation opB) {
		Node savNd= this.getNode(opB);
		for (Edge iteredge : this.conjuncEdges) {
			if (iteredge.from.equals(savNd)) {
				
			}
		}
		
	}
	
	private class EdgeValueComparator implements Comparator<Edge> {
		
		@Override
		public int compare(Edge edge1, Edge edge2) {
			return edge1.value - edge2.value;
			
		}
	}
	
	public static void main(String args[]) {
//Test Bellman Ford 
        
        
        
        
        Operation op00=new Operation(0, 0, 1);
        Operation op01=new Operation(0, 1, 1);
        Operation op02=new Operation(0, 2, 1);
        Operation op10=new Operation(1, 0, 1);
        Operation op11=new Operation(1, 1, 1);
        Operation op20=new Operation(2, 0, 1);
        Operation op21=new Operation(2, 1, 1);
        Operation op22=new Operation(2, 2, 1);
        Operation opEnd=new Operation(-1, -1, 1);
        
        Node nd00=new Node(op00, 1);
        Node nd01=new Node(op01, 3);
        Node nd02=new Node(op02, 2);
        Node nd10=new Node(op10, 3);
        Node nd11=new Node(op11, 4);
        Node nd20=new Node(op20, 1);
        Node nd21=new Node(op21, 4);
        Node nd22=new Node(op22, 2);
        Node ndEnd=new Node(opEnd, -1);
        
        
        Map<Operation, Node> nodes=new HashMap<Operation, Node>();
        nodes.put(op00, nd00);
        nodes.put(op01, nd01);
        nodes.put(op02, nd02);
        nodes.put(op10, nd10);
        nodes.put(op11, nd11);
        nodes.put(op20, nd20);
        nodes.put(op21, nd21);
        nodes.put(op22, nd22);
        nodes.put(opEnd, ndEnd);
        
        Edge conj1 = new Edge(nd00,nd01,10);
        Edge conj2 = new Edge(nd01,nd02,4);
        Edge conj3 = new Edge(nd02,ndEnd,5);
        Edge conj4 = new Edge(nd10,nd11,7);
        Edge conj5 = new Edge(nd11,ndEnd,15);
        Edge conj6 = new Edge(nd20,nd21,11);
        Edge conj7 = new Edge(nd21,nd22,12);
        Edge conj8 = new Edge(nd22,ndEnd,10);
        
        List<Edge> conjunctives=new ArrayList<Edge>();
        conjunctives.add(conj1);
        conjunctives.add(conj2);
        conjunctives.add(conj3);
        conjunctives.add(conj4);
        conjunctives.add(conj5);
        conjunctives.add(conj6);
        conjunctives.add(conj7);
        conjunctives.add(conj8);
        
        Edge disj1 = new Edge(nd00,nd20,10);
        Edge disj2 = new Edge(nd10,nd01,7);
        Edge disj3 = new Edge(nd11,nd21,15);
        Edge disj4 = new Edge(nd02,nd22,5);
        
        List<Edge> disjunctives=new ArrayList<Edge>();
        disjunctives.add(disj1);
        disjunctives.add(disj2);
        disjunctives.add(disj3);
        disjunctives.add(disj4);
        
        
        Graph gr = new Graph(nodes, disjunctives,conjunctives);
        CriticalPath critical = gr.getCriticalPath();
        
        
        System.out.println("done");
        
	}
}
