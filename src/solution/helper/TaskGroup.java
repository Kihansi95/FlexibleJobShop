package solution.helper;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import data.Job;

public class TaskGroup { // == job
	private int id;
	private List<Task> tasks;
	private boolean sorted;
	
	private ListIterator<Task> pointer; // get the current element of the list
	
	public TaskGroup(Job job) {
		id = job.getId();
		tasks = new LinkedList<Task>();
		
		pointer = null;
		
		sorted = false;
	}
	
	public boolean add(Task task) {
		
		if(task.getJob() != this.id) {
			throw new IllegalArgumentException("Task "+task+" does not belong to the job "+ this);
		}
		
		sorted = false;
		return this.tasks.add(task);
	}
	
	public Task poll() {
		if(!isSorted()) {
			sort();
		}
		
		return pointer.next();
	}
	
	public boolean isSorted() {
		return sorted;
	}
	
	public void sort() {
		this.tasks.sort(new Comparator<Task>() {

			@Override
			public int compare(Task t1, Task t2) {
				return t1.getOperation() - t2.getOperation();
			}
			
		});
		this.pointer = tasks.listIterator();
	}
	
	@Override
	public String toString() {
		return "{id : "+ id + "}";
	}

	public boolean hasPrevious(Task task) {
		return pointer.hasPrevious();
	}
	
	public Task previous(Task task) {
		return pointer.previous();
	}
}
