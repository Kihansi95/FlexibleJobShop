package data;

import java.util.HashMap;
import java.util.Map;

public class Operation {
     
    private Map<Integer, Integer> tuples; // <machine , processingtime>

    public Operation(int nb_machines){
        this.tuples = new HashMap<Integer, Integer>();
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
    	return this.tuples.toString(); // dont know how
    }
}

