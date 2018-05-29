package solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import data.*;
import jobshopflexible.Configuration;
import output.pdflatex.PdfWriter;
import solution.graph.BMFLabel;
import solution.graph.Edge;
import solution.graph.Node;
import solution.graph.SpecialNode;
import solution.helper.Task;
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
	
	public List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>(this.conjuncEdges);
		edges.addAll(this.disjuncEdges);
		return edges;
	}
	
	public Node getEndNode() {
		return endNode;
	}
	
	public Node getStartNode() {
		return startNode;
	}
		
	/*
	public CriticalPath getCriticalPath() {
		List<Edge> edges = new ArrayList <Edge>(conjuncEdges);
		edges.addAll(disjuncEdges); 					//fusion of both disjunctive and conjunctive edges
		edges.sort(new EdgeValueComparator()); 			//sort by descending edge cost
		
		List<Edge> spanning_tree = new ArrayList<Edge>();
		
		List<Set<Node>> cfc = new ArrayList<Set<Node>>();
		
		// build spanning tree
		while(spanning_tree.size() < nodes.size()) {
			
			Edge edge = edges.remove(0);
			Set<Node> cfc_prec = null, cfc_succ = null;
			
			for(Set<Node> composant : cfc) {
				if(composant.contains(edge.getPredecessor())) {
					cfc_prec = composant;
				}
				
				if(composant.contains(edge.getSuccessor())) {
					cfc_succ = composant;
				}
			}
			
			if(cfc_succ == null && cfc_prec == null) {
				
				Set<Node> composant = new HashSet<Node>();
				composant.add(edge.getPredecessor());
				composant.add(edge.getSuccessor());
				cfc.add(composant);
				spanning_tree.add(edge);
				
			} else if( cfc_succ != null && cfc_prec == null) {
				cfc_succ.add(edge.getPredecessor());
				spanning_tree.add(edge);
				
			} else if(cfc_succ == null && cfc_prec != null) {
				
				cfc_prec.add(edge.getSuccessor());
				spanning_tree.add(edge);
			} else {
				// cfc_succ != null && cfc_prec != null
				
				if(cfc_succ != cfc_prec) {
					cfc_succ.addAll(cfc_prec);
					cfc.remove(cfc_prec);
					spanning_tree.add(edge);
				}
			}
		}
		
		// build critical path from spanning tree
		Node successor = this.endNode;
		List<Edge> critical_path = new ArrayList<Edge>();
		int makespan = 0;
		while(!successor.equals(startNode)) {
			for(Edge edge : spanning_tree) {
				
				if(edge.getSuccessor().equals(successor)) {
					critical_path.add(edge);
					successor = edge.getPredecessor();
					makespan += edge.value;
					System.out.println(edge);
					break;
				}
				
			}
		}
		
		return new CriticalPath(critical_path, makespan);
	}
		*/
	
	public CriticalPath getCriticalPath() {
		Stack<Node> nodes = sortTopology();
		Map<Node, BMFLabel> dicts = new HashMap<Node, BMFLabel>();
		
		while(!nodes.isEmpty()) {
			BMFLabel label = new BMFLabel(nodes.pop(), dicts);
		}
		
		BMFLabel label = dicts.get(endNode);
		List<Edge> critical_path = new ArrayList<Edge>();
		int makespan = label.getStartTime();
		
		while(!label.equals(dicts.get(startNode))) {
			critical_path.add(label.getEdge());
			System.out.println(label.getEdge());
			label = label.getFather();
			if(label == null)
				System.out.println("toto");
		}
		
		Collections.reverse(critical_path);
		return new CriticalPath(critical_path, makespan);
	}
	
	private Stack<Node> sortTopology() {
		List<Node> all_nodes = this.getNodes();
		all_nodes.add(startNode);
		all_nodes.add(endNode);
		
		Stack stack = new Stack<Node>();
		Map<Node, Boolean> visited = new HashMap<Node, Boolean>();
		
		// mark all node as not visited yet
		for(Node n : all_nodes) {
			visited.put(n, false);
		}
		
		// call recursively the sub sort to store 
		
		for(Node node : all_nodes) {
			if(!visited.get(node))
				recursiveTopoSort(node, visited, stack);
		}
		return stack;
	}
	
	private void recursiveTopoSort(Node node, Map<Node, Boolean> visited, Stack<Node> stack) {
		visited.replace(node, true);
		for(Node succ : node.getSuccessors().keySet()) {			
			if(!visited.get(succ)) {
				recursiveTopoSort(succ, visited, stack);
			}
		}
		
		stack.push(node);
	}
	
	private class EdgeValueComparator implements Comparator<Edge> {
		
		@Override
		public int compare(Edge edge1, Edge edge2) {
			return edge2.value - edge1.value;
			
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
        
        Operation op20 = new Operation(j2, 0, 1);
        Operation op21 = new Operation(j2, 1, 1);
        Operation op22 = new Operation(j2, 2, 1);
        
        op00.setIndex(0);
        op01.setIndex(1);
        op02.setIndex(2);
        op10.setIndex(3);
        op11.setIndex(4);
        op20.setIndex(5);
        op21.setIndex(6);
        op22.setIndex(7);
        
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
        nodes.put(null, end);
        
        // init conjunctives
        List<Edge> conjunctives=new ArrayList<Edge>();
        
        conjunctives.add(new Edge(start,nd00,0));
        conjunctives.add(new Edge(nd00,nd01,10));
        conjunctives.add(new Edge(nd01,nd02,4));
        conjunctives.add(new Edge(nd02,end,5));
        
        conjunctives.add(new Edge(start,nd10,0));
        conjunctives.add(new Edge(nd10,nd11,7));
        conjunctives.add(new Edge(nd11,end,15));
        
        conjunctives.add(new Edge(start,nd20,0));
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
        // gr.visualize(new PdfWriter(new Configuration()));
        
        CriticalPath critical = gr.getCriticalPath();
        
        
        
        System.out.println("done");
        
        System.out.println("Check if valid : "+critical.isValid());
        
        System.out.println("Critical path : "+ critical);
                
	}

	public List<Node> getNodes() {
		return new ArrayList<Node>(this.nodes.values());
	}

	public void setDisjunctiveEdges(List<Edge> disjuncs) {
		this.disjuncEdges = disjuncs;		
	}
}
