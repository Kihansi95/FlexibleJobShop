package solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import data.*;
import output.pdflatex.PdfWriter;
import research.initial.Label;
import utility.Verbose;

public class Graph {
	
	private Map<Operation, Node> nodes;
	private Node startNode, endNode;
	
	private List<Edge> disjuncEdges;
	private List<Edge> conjuncEdges;
	
	private boolean changed; // to assert the change in graph's data
	
	/**
	 * Default constructor
	 */
	public Graph(Map<Operation, Node> nodes, List<Edge> disjunctives, List<Edge> conjunctives, Node start, Node end) {
		this.nodes = nodes;
		this.disjuncEdges = disjunctives;
		this.conjuncEdges = conjunctives;
		this.startNode = start;
		this.endNode = end;
	}
	
	public Node getDisjunctiveFather(Node current) {
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
	
	public CriticalPath getCriticalPath2() {
		List<Edge> edges = new ArrayList <Edge>(conjuncEdges);
		edges.addAll(disjuncEdges); 					//fusion of both disjunctive and conjunctive edges
		edges.sort(new EdgeValueComparator()); 			//sort by descending edge cost
		
		List<Edge> critical_segments = new ArrayList<Edge>();
		
		Set<Node> visited = new HashSet<Node>();
		int makespan = 0;
		while(critical_segments.size() + 1 < nodes.size()) {
			
			Edge edge = edges.remove(0);
			boolean ok = false;
			if(!visited.contains(edge.getPredecessor())) {
				ok = true;
				visited.add(edge.getPredecessor());
			}
			
			if(!visited.contains(edge.getSuccessor())) {
				ok = true;
				visited.add(edge.getSuccessor());
			}
			
			if(ok) {
				critical_segments.add(edge);
				makespan += edge.value;
			}
		}
		
		return new CriticalPath(critical_segments, makespan);
	}
	
	public CriticalPath getCriticalPath() {
		
		List<Node> currentNodes = new ArrayList <Node>(); //successors
		List<Node> nodesFrom = new ArrayList<Node>(); //predecessors
		List<Integer> cost = new ArrayList<Integer>(); //cumulated cost for corresponding edge

		List<Edge> edges = new ArrayList <Edge>(conjuncEdges);
		edges.addAll(disjuncEdges); //fusion of both disjunctive and conjunctive edges
		edges.sort(new EdgeValueComparator()); //sort by descending edge cost
		
		int previousCost=0;
		
		for (Edge iteredge : edges) {
			if (!(nodesFrom.contains(iteredge.from)&&currentNodes.contains(iteredge.to))) { //checking if edge has already been inserted in the tree
				nodesFrom.add(iteredge.from);
				currentNodes.add(iteredge.to);
				for (Node nd : currentNodes) {
					if (nd==iteredge.from) {
						previousCost=cost.get(currentNodes.indexOf(nd)); //getting the cost of preceding edge to add it to the current edge cost
					}
				}
				cost.add(iteredge.value+previousCost); //previousCost=0 if no preceding edge found : PROBLEM 
			}
		}
		
		int maxcost =0;
		int index =0;
		int maxIndex=0;;
		boolean endOfList=false;
		
		
		
		for (int currentCost : cost) { //in this loop, getting the makespan(maxcost) and its index to know the corresponding node
			if (currentCost > maxcost) {
				maxcost=currentCost;
				maxIndex=index;
			}
			index++;
		}
			 
		while (!endOfList) { //building the critical path riding up edges
			edges.add(new Edge(nodesFrom.get(maxIndex), currentNodes.get(maxIndex), cost.get(maxIndex))); //adding edge to critical path
			maxIndex=currentNodes.indexOf(nodesFrom.get(maxIndex)); //finding index of predecessor which belongs to the preceding edge
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
	
	public void visualize(PdfWriter pdfOutput) {
		
		// print nodes
		pdfOutput.addStartNode(this.startNode.toString());
		List<Node> ordered_nodes = new ArrayList<Node>(this.nodes.values());
		ordered_nodes.sort(new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				int diff = n1.getJob() - n2.getJob();
				diff = diff == 0 ? n1.getOperation() - n2.getOperation() : diff;
				diff = diff == 0 ? n1.getMachine() - n2.getMachine() : diff;
				return diff;
			}
			
		});
		
		for(Node node : ordered_nodes) {
			String param = convertParam(node);
			pdfOutput.addNode(node.toString(), param);
			
		}
		
		int middle_job = ordered_nodes.get(ordered_nodes.size() - 1).getJob() / 2;
		Node last_op = null;
		for(Node node : ordered_nodes) {
			if(node.getJob() == middle_job) {
				last_op = last_op == null ? node : last_op.getOperation() < node.getOperation() ? node : last_op;
			}
		}
		
		pdfOutput.addEndNode(this.endNode.toString(), "right of=" + last_op.toString() );
		
		// print edges
		for(Edge edge : this.conjuncEdges) {
			pdfOutput.addPath(edge.getPredecessor().toString(), edge.getSuccessor().toString(), edge.getValue());
		}
		
		for(Edge edge : this.disjuncEdges) {
			pdfOutput.addPath(edge.getPredecessor().toString(), edge.getSuccessor().toString(), edge.getValue());
		}
		
		// clean template and write down
		pdfOutput.write();
		
	}
	
	private String convertParam(Node node) {
		
		// for the first job's operation line
		if(node.getJob() == 0) {
			if(node.getOperation() == 0)
				return "above right of=start";
			
			
		}
		
		// for other operation
		if(node.getOperation() == 0) {
			for(Node upper_node : nodes.values()) {
				if(upper_node.getOperation() == 0 && upper_node.getJob() == node.getJob() - 1) {
					return "below " + (node.getJob() % 2 == 0 ? "left" : "right") + " of=" + upper_node;
				}
			}
		}
		
		// get father of the same job
		for(Edge conjunc : conjuncEdges) {
			if(conjunc.getSuccessor().equals(node)) {
				return "right of=" + conjunc.getPredecessor();
			}
		}
		
		// how can you reach here ???
		throw new RuntimeException("Can't be here");
	}
	
	public static void main(String args[]) {
		//Test Bellman Ford 
        Job j0 = new Job(0);
        Job j1 = new Job(1);
        Job j2 = new Job(2);
        
        Operation op00 = new Operation(j0, 0, 1);
        Operation op01 = new Operation(j0, 1, 1);
        Operation op02 = new Operation(j0, 2, 1);
        Operation op10 = new Operation(j1, 0, 1);
        Operation op11 = new Operation(j1, 1, 1);
        Operation op20 = new Operation(j1, 0, 1);
        Operation op21 = new Operation(j2, 1, 1);
        Operation op22 = new Operation(j2, 2, 1);
        // Operation opEnd=new Operation(-1, -1, 1);
        
        // init nodes
        Node nd00 = new Node(op00, 1);
        Node nd01 = new Node(op01, 3);
        Node nd02 = new Node(op02, 2);
        Node nd11 = new Node(op11, 4);
        Node nd10 = new Node(op10, 3);
        Node nd20 = new Node(op20, 1);
        Node nd21 = new Node(op21, 4);
        Node nd22 = new Node(op22, 2);
        
        Node end = new SpecialNode(false);
        Node start = new SpecialNode(true);
        
        Map<Operation, Node> nodes=new HashMap<Operation, Node>();
        nodes.put(op00, nd00);
        nodes.put(op01, nd01);
        nodes.put(op02, nd02);
        nodes.put(op10, nd10);
        nodes.put(op11, nd11);
        nodes.put(op20, nd20);
        nodes.put(op21, nd21);
        nodes.put(op22, nd22);
        nodes.put(null, start);
        nodes.put(null, start);
        nodes.put(null, start);
        
        // init conjunctives
        List<Edge> conjunctives=new ArrayList<Edge>();
        conjunctives.add(new Edge(nd00,nd01,10));
        conjunctives.add(new Edge(nd01,nd02,4));
        conjunctives.add(new Edge(nd02,end,5));
        conjunctives.add(new Edge(nd10,nd11,7));
        conjunctives.add(new Edge(nd11,end,15));
        conjunctives.add(new Edge(nd20,nd21,11));
        conjunctives.add(new Edge(nd21,nd22,12));
        conjunctives.add(new Edge(nd22,end,10));
        
        // init disjunctives
        List<Edge> disjunctives=new ArrayList<Edge>();
        disjunctives.add(new Edge(nd00,nd20,10));
        disjunctives.add(new Edge(nd10,nd01,7));
        disjunctives.add(new Edge(nd11,nd21,15));
        disjunctives.add(new Edge(nd02,nd22,5));
        
        
        Graph gr = new Graph(nodes, disjunctives,conjunctives, start, end);
        CriticalPath critical = gr.getCriticalPath();
        
        
        System.out.println("done");
        
        System.out.println("Check if valid : "+critical.isValid());
        
        System.out.println("Makespan : "+critical.getMakespan()+" Path : ");
        
        Node currentNode = critical.getLastNode();
        
        while (currentNode != null) {
        	System.out.println("J : "+currentNode.getJob()+" / Op : "+currentNode.getOperation()+" / M : "+currentNode.getMachine());
        	currentNode=critical.getPredecessor(currentNode);
        }
        
	}

	public List<Node> getNodes() {
		return new ArrayList<Node>(this.nodes.values());
	}

	public void setDisjunctiveEdges(List<Edge> disjuncs) {
		this.disjuncEdges = disjuncs;		
	}
}
