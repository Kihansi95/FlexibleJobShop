package solution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import data.Operation;
import research.Label;
import research.Machine;
import data.FlexibleJobShop; 

public class Solution {
	
	private int[] ms ;
	private int[] os ;
	private LinkedList<Label> critical_path;
	//private LinkedList<Label> graph; class graph to dev (bellman ford)
	private Graphe graphe;
	private int endTime; 
	
	
	
	public Solution(List<Label> listLabels, int nb_machines, int nb_jobs, FlexibleJobShop context) {
		
		this.ms = new int[nb_machines];
		this.os = new int[nb_jobs]; 
		int i=0;
		
		//To be done in the converter
		
		/*listLabels.sort(new IdOperationComparator());
		Label lastLabel; 
		int maxFinishTime=0;
		
		for (Label iterLabel : listLabels) {
			ms[i]=(iterLabel.getMachine());
			os[i]=iterLabel.getOperation().getId();
			context.getJobs().get(iterLabel.getOperation().getIdJob()).getOperations().get(iterLabel.getOperation());
			
			if (iterLabel.getFinishTime()>maxFinishTime) {
				maxFinishTime=iterLabel.getFinishTime();
				lastLabel=iterLabel; 
			}
			i++;
		}
		
		this.endTime=lastLabel.getFinishTime();
		Label currentLabel=lastLabel;
		
		while (currentLabel != null) {
			critical_path.add(currentLabel);
			currentLabel=currentLabel.getCriticalFather();	
		}*/
		
		this.graphe = new Graphe(ms, os, context);
	
		
	}
	
	
	public void setMs(int[] ms) {
		this.ms = ms;
	}


	public int[] getMs() {
		return ms;
	}


	public int[] getOs() {
		return os;
	}


	public LinkedList<Label> getCriticalPath() {
		return critical_path;
	}


	public Graphe getGraphe() {
		return graphe;
	}


	public int getEndTime() {
		return endTime;
	}
	
	public int getIndex(int nJob, int nOp, int nbOps ) {
		int index=((nJob-1)*nbOps)+nOp;
	}
	
	public int makespan() {
		return this.critical_path.getLast().getFinishTime();
	}
	
	public void update() { //recalculate the graph with new assignment vectors
		this.graphe.update(ms,os);
	}
	
	public void permute (Operation opA, Operation opB) {
		int indexA=opA.getIndex();
		int indexB=opB.getIndex();
		this.os[indexA]=opB.getId();
		this.os[indexB]=opA.getId();
	}



	private class IdOperationComparator implements Comparator<Label> {
			
			@Override
			public int compare(Label label1, Label label2) {
				int diff = label1.getOperation().getIdJob() - label2.getOperation().getIdJob();
				diff = diff == 0 ? label1.getOperation().getId() - label2.getOperation().getId() : diff;
				return diff;
				
			}
		}
	

}
