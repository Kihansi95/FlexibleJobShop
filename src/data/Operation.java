package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operation {
    
	private Job job;
	private int id;
    private Map<Integer, Integer> tuples; // <machine , processingtime>
    private Operation next;
    private int index; //index in os solution vector 

    public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Operation(Job job, int id, int nb_machines){
    	this.job = job;
    	this.id = id;
        this.tuples = new HashMap<Integer, Integer>();
        this.next = null;
    }
    
    public void setNext(Operation next) {
    	this.next = next;
    }
    
    public Operation getNext() {
    	return next;
    }

    public void addTuple(int num_machine, int process_time){
        this.tuples.put(num_machine, process_time);
    }
    
    public Map<Integer, Integer> getTuples() {
    	return this.tuples; //TODO clones before return
    }
    
    public Job getJob() {
    	return job;
    }
    
    public int getIdJob() {
    	return job.getId();
    }
    
    public int getId() {
    	return id;
    }
    
    /**
     * Get le temps 
     * @param machine
     * @return
     */
    public int getProcessingTime(int machine) {
    	Integer processing = tuples.get(new Integer(machine));  
    	return processing == null? -1 : processing;
    }
    
    @Override
    public String toString() {
    	return this.job.getId()+"."+this.id;
    }
    
    @Override
	public boolean equals(Object other) {
		return other instanceof Operation 
				&& this.job.getId() == ((Operation) other).job.getId()
				&& this.id == ((Operation) other).id;
	}

	public List<Integer> getMachines() {
		return new ArrayList<Integer>(this.tuples.keySet());
	}

	public boolean isLast() {
		return this.equals(this.job.getLastOperation());
	}
}

