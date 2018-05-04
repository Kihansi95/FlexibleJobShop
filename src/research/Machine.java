package research;

import java.util.List;

import data.*;

public class Machine {

    private int id;
    private Label lastAssignment;
    private boolean ready;
    
    public Machine(int id) {
    	this.id = id;
    	lastAssignment = null;
    }
    
    public int getId() {
    	return id;
    }
    
    public void memoryLabel(Label assignment) {
    	lastAssignment = assignment;
    }
    
    public boolean isReady() {
    	return ready;
    }
    
}
