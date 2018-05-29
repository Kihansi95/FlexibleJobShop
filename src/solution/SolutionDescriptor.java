package solution;

import output.pdflatex.IDescriptor;
import solution.graph.Edge;

public class SolutionDescriptor implements IDescriptor {
	
	private CriticalPath path;
	
	public SolutionDescriptor(CriticalPath path) {
		this.path = path;
	}

	@Override
	public String toDescription() {
		StringBuilder des = new StringBuilder();
		des.append("Makespan = "+ path.getMakespan() +"\\\\")
		  .append("Critical path : ");
		
		boolean first = true;
		for(Edge edge : path.getEdges()) {
			if(first) {
				des.append("("+edge.getPredecessor()+")");
				first = false;
			}
				
			des.append(" $\\\\rightarrow$  ("+edge.getSuccessor()+ ")");
		}
		return des.toString();
	}

}
