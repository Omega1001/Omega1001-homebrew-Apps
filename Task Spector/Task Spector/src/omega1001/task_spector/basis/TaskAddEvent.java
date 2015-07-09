/**
 * 
 */
package omega1001.task_spector.basis;

import omega1001.task_spector.moddel.Task;

/**
 * @author j.adam
 *
 */
public class TaskAddEvent extends Event {
	
	private Task taskToAdd;
	
	/**
	 * @param type
	 */
	public TaskAddEvent(Task task) {
		super(EventType.TASK_ADD);
		this.taskToAdd = task;
	}

	public Task getTaskToAdd() {
		return taskToAdd;
	}

}
