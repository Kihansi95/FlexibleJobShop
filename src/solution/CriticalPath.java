package solution;

import java.util.ArrayList;
import java.util.List;

import solution.graph.Edge;
import solution.graph.Node;

public class CriticalPath {
	private List<Edge> edges;
	private int makespan;
	
	public CriticalPath(List<Edge> edges, int makespan) {
		this.edges = edges;
		this.makespan = makespan;
	}
	
	/**
	 * Clone constructor
	 * @param criticalPath
	 */
	public CriticalPath(CriticalPath criticalPath) {
		this.makespan = criticalPath.makespan;
		this.edges = new ArrayList<Edge>(criticalPath.edges);
	}

	public int getMakespan() {
		return makespan;
	}
	
	public Node getLastNode() {
		//return edges.get(edges.size() - 1).getSuccessor();
		return edges.get(edges.size() - 1).getPredecessor(); // skip end node
	}
	
	/**
	 * Check if 2 edges are linked by node in critical path
	 * @return
	 */
	public boolean isValid() {
		Edge last = null;
		for(Edge edge : edges) {
			if(last != null && !last.getSuccessor().equals(edge.getPredecessor())) {
				return false;
			} 
			last = edge;
		}
		
		return true;
	}
	
	public String toString() {
		StringBuilder msg = new StringBuilder("{\n");
		msg.append("\tmakespan: "+makespan+",\n");
		msg.append("\t" + edges.get(0).getPredecessor());
		for(Edge edge : edges) {
			msg.append(" -(" + edge.getValue() + ")-> "+edge.getSuccessor());
		}
		msg.append("\n}");
		return msg.toString(); 
	}
	
	public Node getPredecessor(Node node) {
		for(Edge edge : this.edges) {
			if(edge.getSuccessor().equals(node)) {
				return edge.getPredecessor();
			}
		}
		return null;
	}

	public List<Edge> getEdges() {
		return this.edges;
	}	
	
}
