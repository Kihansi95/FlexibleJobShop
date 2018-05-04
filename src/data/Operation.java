package data;

import java.util.HashMap;
import java.util.Map;

import research.Machine;

public class Operation {
    
	private int idJob;
	private int id;
    private Map<Integer, Integer> tuples; // <machine , processingtime>
    private Operation next;

    public Operation(int idJob, int id, int nb_machines){
    	this.idJob = idJob;
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
    	return this.idJob+"."+this.id;
    }
    
    @Override
	public boolean equals(Object other) {
		return other instanceof Operation 
				&& this.idJob == ((Operation) other).id 
				&& this.id == ((Operation) other).id;
	}
}

