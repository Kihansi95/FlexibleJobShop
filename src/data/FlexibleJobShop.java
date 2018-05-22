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

}
