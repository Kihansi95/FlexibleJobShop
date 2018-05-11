package data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jobshopflexible.Configuration;

public class DataFactory {

	private static final String[] DATAPATH = {
				"data.path", 		// param name
				"conf/fjs.conf"		// default value if not found
			};
	private static final String VERBOSE = "data.verbose";
	
	public static FlexibleJobShop initiateJobShop(Configuration conf){
    
    	List<Job> jobs = new ArrayList<Job>();
    	Scanner input;
    	boolean verbose = false;
    	
		try {
        
			input = new Scanner(new FileReader(conf.getParam(DATAPATH[0], DATAPATH[1])));
			
			String v =  conf.getParam(VERBOSE);
			verbose = v != null && !v.toUpperCase().equalsIgnoreCase("FALSE");
			
			
			// scan the first line
	        int nb_job = input.nextInt();
	        int nb_machine = input.nextInt(); //TODO do we need it?
	        int avg_machine_per_operation = input.nextInt(); // really dont need
	        
	        // for each following line, we get a new job
	        for(int job = 0; job < nb_job; job++) {
	        	jobs.add(scanJob(job, input));
	        }
	        
	        if(verbose) {
	        	// check if all the job has been correctly instantiated
		        System.out.println("The problem has "+nb_job+" jobs, "+nb_machine+" machines:");
		        for(Job job: jobs) {
		        	System.out.println(job);
		        }
	        }
	        
	        
			input.close();
			
			FlexibleJobShop context = new FlexibleJobShop(jobs, nb_machine);
			return context;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}   
	}

    
    private static Job scanJob(int idJob, Scanner input) {
    	int nb_activities = input.nextInt();
    	Job job = new Job(idJob);
    	Operation lastCreatedOp = null;	// memory the last one in order to set next
    	
    	for(int operation = 0; operation < nb_activities; operation++) {

    		int nb_machine = input.nextInt(); //get nb machine allowed for this operation
    		Operation op = new Operation(idJob,operation, nb_machine);
    		
    		if(operation > 0)
    			lastCreatedOp.setNext(op);
    		    		
    		for(int machine = 0; machine < nb_machine; machine++) {
    		
    			int id_machine = input.nextInt();
    			int duration = input.nextInt();
    			
    			op.addTuple(id_machine, duration);
    			// act.addTuple(input.nextInt(), input.nextInt()); shorthand
    		}
    		
    		job.addActivity(op);
    		lastCreatedOp = op;
    	}
    	
    	return job;
    }
}
