package solution;

import java.util.List;

public class CriticalPath {
	private List<Edge> edges;
	private int makespan;
	
	public CriticalPath(List<Edge> edges, int makespan) {
		this.edges = edges;
		this.makespan = makespan;
	}
	
	public int getMakespan() {
		return makespan;
	}
	
	public Node getLastNode() {
		return edges.get(edges.size() - 1).getSuccessor();
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
		return "{makespan = "+makespan+", "+edges+"}"; 
	}
	
	public Node getPredecessor(Node node) {
		for(Edge edge : this.edges) {
			if(edge.getSuccessor().equals(node)) {
				return edge.getPredecessor();
			}
		}
		return null;
	}	
	
}
