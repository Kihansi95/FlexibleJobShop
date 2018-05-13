package output.swiftgantt;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/* swift grant special import*/
import org.swiftgantt.*;
import org.swiftgantt.common.*;
import org.swiftgantt.model.*;
import org.swiftgantt.ui.TimeUnit;

public class SwiftGanttWriter {

	public static void main(String[] args) {
		
		// chart
		GanttChart gantt = new GanttChart();
		gantt.setTimeUnit(TimeUnit.Month);
		
		
		// config chart
		Config config = gantt.getConfig();
		config.setWorkingTimeBackColor(Color.YELLOW);//Set background color for working time.
		config.setTimeUnitWidth(50);//Set width for time unit
		config.setWorkingDaysSpanOfWeek(new int[]{Calendar.MONDAY, Calendar.THURSDAY});//Set span of working days in each week
		config.setAllowAccurateTaskBar(true);//Set true if you want to show accurate task bar.
		
		// model
		GanttModel model = new GanttModel();
		model.setKickoffTime(new Time(new GregorianCalendar(2008, 0, 1)));
		model.setDeadline(new Time(new GregorianCalendar(2008, 0, 30)));
		
		// Create basic element for Gantt Chart
		Task taskGroup = new Task("My Work 1", new Time(2008, 1, 1), new Time(2008, 1, 30));
		Task task1 = new Task("Sub-task 1", new Time(2008, 1, 1), new Time(2008, 1, 5));
		Task task2 = new Task();
		task2.setName("Sub-task 2");
		task2.setStart(new Time(2008, 1, 6));
		task2.setEnd(new Time(2008, 1, 18));// Since version 0.3.0, the end time set to a task is included in duration of the task

		taskGroup.add(new Task[]{task1, task2});
		
		// Attach the dependency between tasks.
		task2.addPredecessor(task1);
		
		model.addTask(taskGroup);
		gantt.setModel(model);
		
		// save as image
		try {
			
			synchronized(gantt){
				gantt.generateImageFile("tmp/gant.jpg");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
