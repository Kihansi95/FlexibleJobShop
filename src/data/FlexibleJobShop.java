package data;
import java.util.List;


public class FlexibleJobShop {

	private int nbMachine;
    private List<Job> jobs;
    private int nbOperation;

    public FlexibleJobShop(List<Job> jobs, int nbMachine){
    	this.nbMachine = nbMachine;
        this.jobs = jobs;
        
        this.nbOperation = 0;
        for(Job job : jobs) {
        	nbOperation += job.getNbOperation();
        }
    }

    public int getNbMachine()	{
    	return nbMachine;
    }

    public List<Job> getJobs()	{
    	return jobs;
    }

	public List<Integer> getMachines(int job, int operation) {
		return jobs.get(job).getOperations().get(operation).getMachines();
	}

	public int getNbOperation() {
		return nbOperation;
	}

	/**
	 * Return the complexity of a dataset.
	 * o(n) = nb_machine + S(nb_operation in each job) + S(nb_tuple in each operation)
	 * @return int
	 */
	public int getComplexity() {
		
		int o = nbMachine;	// o(n)
		
		for(Job job : jobs) {
			o += job.getNbOperation();
			for(Operation operation : job.getOperations()) {
				o += operation.getTuples().size();
			}
		}
		
		return o;
	}
}
