package solution.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TaskMachine {
	
	private int id;
	private List<Task> tasks;
	
	public TaskMachine(int id) {
		this.id = id;
		tasks = new ArrayList<Task>();
	}
	
	public int getId() {
		return id;
	}
	
	public List<IdleArea> getIdleAreas() {
		Iterator<Task> it = tasks.iterator();
		List<IdleArea> idles = new LinkedList<IdleArea>();
		
		if(!it.hasNext())
			return idles; // no operation so no idle time
		
		Task current = null, next = null;
		
		while(it.hasNext()) {
			next = it.next();
			
			if(current == null) {
				
				// check if machine has idle time before starting an operation
				if(next.startingTime > 0)
					idles.add(new IdleArea(0, next.startingTime));
			} else {
				int startIdle = current.completionTime; // machine start idle when finish the last task
				int endIdle = next.startingTime;
				idles.add(new IdleArea(startIdle, endIdle));
			}
			
			current = next; // update current
		}
		
		return idles;
	}
	
	public int checkIdleArea(int allowableStartTime, int processingTime) {
		List<IdleArea> idles = this.getIdleAreas();
		for(IdleArea idle : idles) {
			int startTime = Math.max(idle.start, allowableStartTime);
			if(startTime + processingTime <= idle.end)
				return startTime;
		}
		
		// no admitted idle available
		return -1; 
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public int getLastCompletionTime() {
		
		if(this.tasks.isEmpty())
			return 0;
		
		List<Task> clone_tasks = new ArrayList<Task>(this.tasks);
		clone_tasks.sort(new Comparator<Task>() {
			@Override
			public int compare(final Task task1, final Task task2) {
				return task2.completionTime - task1.completionTime;
			}
		});
		return clone_tasks.get(0).completionTime;
	}

	public String toString() {
		return "{"+id+": "+tasks+"}";
	}
	
	public List<Task> schedule() {
		return tasks;
	}
}
