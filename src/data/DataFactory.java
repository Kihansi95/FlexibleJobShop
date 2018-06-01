package data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import jobshopflexible.Configuration;
import utility.Verbose;

public final class DataFactory extends Verbose {

	private static final String[] DATAPATH = {
				"data.path", 		// param name
				"conf/fjs.conf"		// default value if not found
			};
	
	
	
	// singleton
	private static DataFactory _instance;
	private DataFactory(Configuration conf) {
		super(conf, "data");
	}
	public final static DataFactory instance(Configuration conf) {
		if(_instance == null)
			_instance = new DataFactory(conf);
		return _instance;
	}
	
	public FlexibleJobShop initiateJobShop(Configuration conf){
		
    	List<Job> jobs = new ArrayList<Job>();
    	Scanner input;
    	
		try {
        
			input = new Scanner(new FileReader(conf.getParam(DATAPATH[0], DATAPATH[1])));
			input.useLocale(Locale.US);
			
			// scan the first line
	        int nb_job = input.nextInt();
	        int nb_machine = input.nextInt();
	        float avg_machine_per_operation = input.nextFloat(); // really dont need
	        
	        // for each following line, we get a new job
	        int index = 0;
	        for(int job = 0; job < nb_job; job++) {
	        	Job new_job = scanJob(job, input, index);
	        	index += new_job.getNbOperation();
	        	jobs.add(new_job);
	        	
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

    
    private Job scanJob(int idJob, Scanner input, int job_index) {
    	int nb_activities = input.nextInt();
    	Job job = new Job(idJob);
    	Operation lastCreatedOp = null;	// memory the last one in order to set next
    	
    	for(int operation = 0; operation < nb_activities; operation++) {

    		int nb_machine = input.nextInt(); //get nb machine allowed for this operation
    		Operation op = new Operation(job, operation, nb_machine, job_index + operation);
    		
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
