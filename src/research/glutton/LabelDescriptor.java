package research.glutton;

import java.util.Stack;

import output.pdflatex.IDescriptor;

public class LabelDescriptor implements IDescriptor {

	private Stack<Label> criticalNodes;
	private int totalTime;
	private GluttonSearch is;
	
	/**
	 * the last node of the critical path
	 * @param lastNode
	 */
	public LabelDescriptor(Label lastNode, GluttonSearch is) {
		totalTime = lastNode.getFinishTime();
		criticalNodes = new Stack<Label>();
		for(Label node = lastNode; node != null; node = node.getCriticalFather()) {
			criticalNodes.push(node);
		}
		this.is = is;
	}
	
	@Override
	public String toDescription() {
		
		StringBuilder des = new StringBuilder();
		des.append("Total processing time = "+totalTime+"\\\\")
		  .append("Critical path : start $\\rightarrow$ ");
		
		while(!criticalNodes.isEmpty()) {
			String node = is.convertNode( criticalNodes.pop());
			des.append("("+ node + ")").append(" $\\rightarrow$ ");
		}
		return des.append("end").toString();
	}

}
