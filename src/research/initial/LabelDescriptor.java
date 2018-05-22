package research.initial;

import java.util.Stack;

import output.pdflatex.IDescriptor;

public class LabelDescriptor implements IDescriptor {

	private Stack<Lable> criticalNodes;
	private int totalTime;
	private InitialSolution is;
	
	/**
	 * the last node of the critical path
	 * @param lastNode
	 */
	public LabelDescriptor(Lable lastNode, InitialSolution is) {
		totalTime = lastNode.getFinishTime();
		criticalNodes = new Stack<Lable>();
		for(Lable node = lastNode; node != null; node = node.getCriticalFather()) {
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
