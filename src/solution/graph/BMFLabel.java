package solution.graph;

import java.util.Map;
import java.util.Map.Entry;

import solution.Graph;

public class BMFLabel {
	
	private int startTime;
	private Edge edgeFromFather;
	private BMFLabel criticalFather;

	public BMFLabel(Node node, Map<Node, BMFLabel> dicts, Graph graph) {
		
		Map<Node, Edge> fathers = graph.getPredecessor(node);
		startTime = Integer.MIN_VALUE;
				
		for(Entry<Node, Edge> f : fathers.entrySet()) {
			
			BMFLabel fl = dicts.get(f.getKey());
			
			Edge edge = f.getValue();
						
			if(fl.startTime + edge.getValue() > this.startTime) {
				this.startTime = fl.startTime + edge.getValue();
				this.edgeFromFather = edge;
				this.criticalFather = fl;
			}
				
		}
		
		if(this.startTime < 0) {
			startTime = 0;
			edgeFromFather = null;
			criticalFather = null;
		}
	}
	
	public BMFLabel getFather() {
		return criticalFather;
	}
	
	public Edge getEdge() {
		return edgeFromFather;
	}

	public int getStartTime() {
		return startTime;
	}
	
	@Override
	public String toString() {
		return "{start time : "+startTime+", edge : "+edgeFromFather+"}";
	}

}
