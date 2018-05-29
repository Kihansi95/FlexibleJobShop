package solution.graph;

public class SpecialNode extends Node {
	private boolean start; // true == start node, false == end node
	
	public SpecialNode(boolean start) {
		super();
		this.start = start;
	}
	
	public String toString() {
		return start ? "start" : "end";
	}
	
	public boolean equals(Object node) {
		return super.equals(node) && node instanceof SpecialNode && ((SpecialNode) node).start == this.start;
	}
}
