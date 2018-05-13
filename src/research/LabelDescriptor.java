package research;

import java.util.Stack;

import output.pdflatex.IDescriptor;

public class LabelDescriptor implements IDescriptor {

	private Stack<Label> criticalNodes;
	private int totalTime;
	private InitialSolution is;
	
	/**
	 * the last node of the critical path
	 * @param lastNode
	 */
	public LabelDescriptor(Label lastNode, InitialSolution is) {
		totalTime = lastNode.getFinishTime();
		criticalNodes = new Stack<Label>();
		System.out.println("last node = "+lastNode);
		for(Label node = lastNode.getCriticalFather(); node != null; node = node.getCriticalFather()) {
			criticalNodes.push(node);
		}
		this.is = is;
	}
	
	@Override
	public String toDescription() {
		
		StringBuilder des = new StringBuilder();
		des.append("Total processing time = "+totalTime+"\\\\")
		  .append("Critical path : ");
		
		System.out.println("critical nodes = " + criticalNodes);
		
		while(!criticalNodes.isEmpty()) {
			String node = is.convertNode(criticalNodes.pop());
			des.append(node).append(" \\arrow ");
		}
		return des.toString();
	}

}
