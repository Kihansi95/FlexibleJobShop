package solution.graph;

public class Edge {
	
	Node from;
	Node to;
	int value;

	public Edge(Node from, Node to, int value) {
		this.from = from;
		this.to = to;
		this.value = value;
		
		/*
		from.addSuccessor(to, this);
		to.addPredecessor(from, this);
		*/
	}
	
	public Node getSuccessor() {
		return to;
	}

	public Node getPredecessor() {
		return from;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return "{" + from + "->" + to +  ", "+value+"}";
	}
}
