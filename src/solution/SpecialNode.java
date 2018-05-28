package solution;

public class SpecialNode extends Node {
	private boolean start; // true == start node, false == end node
	
	public SpecialNode(boolean start) {
		super();
		this.start = start;
	}
	
	public String toString() {
		return start ? "start" : "end";
	}
}
