package data;

import java.util.LinkedList;
import java.util.Queue;

import data.Operation;

public class Job {

	/**
	 * All the required operations for a job
	 */
    private Queue<Operation> operations;
    
    private int id;

    public Job(int id){
    	this.id = id;
        this.operations = new LinkedList<Operation>();	// TODO if multithreading, use ConcurrentLinkedQueue<>
    }

    /**
     * Add an operation into the job
     * @param opeartion
     * @return
     */
    public boolean addActivity(Operation operation){
        return this.operations.add(operation);
    }
    
    /**
     * Get the first activity available from the job. 
     * This method does not guarantee whether the previous operation has been done, 
     * therefore the availability of the given activity must be checked.
     * @example Job J1 has Activity A1 and A2. 
     * 		Activity A1 = J1.popActivity(); 
     * 		Activity A2 = J1.popActivity(); // we are not sure whether A1 has been done before getting A2
     * @return Activity
     */
    public Operation getFirstOperation() {
    	// return this.operations.remove(); // == .poll(), to see if need
    	return this.operations.peek();
    }
    
    public int getNbOperation() {
    	return this.operations.size();
    }
    
    /**
     * Check if this job has been finished. 
     * A job is finished when all its required activities has been done. 
     * So we check if the list of activity is empty
     * @return true if finished, false if not
     */
    public boolean isFinished() {
    	return this.operations.isEmpty();	// TODO to be removed because this will be processed in algorithm
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder("{ Job "+ id +" has " + this.operations.size() + " operations: [");
    	for(Operation operation: operations) {
    		builder.append(operation).append(", ");
    	}
    	builder.replace(builder.length() - 2, builder.length(), "]}"); // replace ", " by "]}"
    	return builder.toString();
    }

}


