package research;

public class Machine {

    private int id;
    private Label lastAssignment;
    private boolean ready;
    
    public Machine(int id) {
    	this.id = id;
    	this.lastAssignment = null;
    	this.ready = true;
    }
    
    public int getId() {
    	return id;
    }
    
    public void memoryLabel(Label assignment) {
    	lastAssignment = assignment;
    }
    
    public void setState(boolean state)	{
    	this.ready = state;
    }
    
    public boolean isReady() {
    	return ready;
    }
    
    @Override
	public boolean equals(Object other) {
		return other instanceof Machine && this.id == ((Machine) other).id;
	}

	public Label getLastAssignment() {
		return lastAssignment;
	}
}
