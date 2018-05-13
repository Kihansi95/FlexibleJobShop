package research.arraysolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.transformation.SortedList;

public class MachineAS {
	
	private int id;
	private List<OperationAS> operations;
	private static final ScheduleComparator comparator = new ScheduleComparator();
	
	public MachineAS(int id) {
		this.id = id;
		operations = new ArrayList<OperationAS>();
	}
	
	public List<IdleAS> getIdlesAreas() {
		
		Iterator<OperationAS> it = operations.iterator();
		List<IdleAS> idles = new LinkedList<IdleAS>();
		
		if(!it.hasNext()) 
			return idles; // no operation so no idle time
		
		OperationAS current = null, next = null;
		
		while(it.hasNext()) {
			next = it.next();
	
			
			if(current == null && next.startingTime > 0) {
				
				// check if machine has idle time before starting an operation
				idles.add(new IdleAS(0, next.startingTime));
			} else {
				int startIdle = current.completionTime; // machine start idle when finish the last task
				int endIdle = next.startingTime;
				idles.add(new IdleAS(startIdle, endIdle));
			}
			
			current = next; // update current
		}
		
		return idles;
	}

	public int checkIdleArea(int allowableStartTime, int processingTime) {
		List<IdleAS> idles = this.getIdlesAreas();
		for(IdleAS idle : idles) {
			int startTime = Math.max(idle.start, allowableStartTime);
			if(startTime + processingTime <= idle.end)
				return startTime;
		}
		
		// no admitted idle available
		return -1; 
	}

	public void addOperation(OperationAS operation) {
		this.operations.add(operation);
		operations.sort(comparator);
	}
	

	public int getLastCompletionTime() {
		return operations.get(operations.size() - 1).completionTime;
	}
	
	private static final class ScheduleComparator implements Comparator<OperationAS> {

		@Override
		public int compare(OperationAS o1, OperationAS o2) {
			return o1.processingTime - o2.processingTime;
		}
		
	}

}
