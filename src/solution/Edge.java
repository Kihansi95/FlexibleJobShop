package solution;

public class Edge {
	
	Node from;
	Node to;
	int value;

	public Edge(Node from, Node to, int value) {
		this.from = from;
		this.to = to;
		this.value = value;
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

}
