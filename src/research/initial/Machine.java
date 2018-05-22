package research.initial;

public class Machine {

    private int id;
    private Lable lastAssignment;

	public Machine(int id) {
    	this.id = id;
    	this.lastAssignment = null;
    }
    
    public int getId() {
    	return id;
    }
    
    public void memoryLabel(Lable assignment) {
    	lastAssignment = assignment;
    }
    
    public boolean isReady(int currentTime) {
    	if(lastAssignment == null)
    		return true;
    	return lastAssignment.getFinishTime() <= currentTime; 
    }
    
    @Override
	public boolean equals(Object other) {
		return other instanceof Machine && this.id == ((Machine) other).id;
	}

	public Lable getLastAssignment() {
		return lastAssignment;
	}

	public Lable getMemory() {
		return lastAssignment;
	}
}
