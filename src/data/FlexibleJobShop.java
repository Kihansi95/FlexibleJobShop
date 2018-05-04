package data;
import java.util.List;


public class FlexibleJobShop {

	private int nbMachine;
    private List<Job> jobs;

    public FlexibleJobShop(List<Job> jobs, int nbMachine){
    	this.nbMachine = nbMachine;
        this.jobs = jobs;
    }

    public int getNbMachine()	{
    	return nbMachine;
    }

    public List<Job> getJobs()	{
    	return jobs;
    }

}
