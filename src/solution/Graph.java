package solution;

import java.util.ArrayList;
import java.util.Comparator;
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
	
	/*public Graph(int[] ms, int[] os, FlexibleJobShop context) {
		
		
		
	}*/
	
	public Node getDisjunctiveFather(Node current ) {
		for (Edge iteredge : this.disjuncEdges) {
			if (iteredge.to==current) {
				return iteredge.from;
			}
		}
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
			if (maxIndex==0) {
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
}
