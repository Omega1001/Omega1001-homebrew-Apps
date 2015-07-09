package omega1001.task_spector.basis;

public class TaskChangeEvent extends Event {
	
	private String taskName;
	
	public TaskChangeEvent(EventType type ,String taskName) {
		super(type);
		this.taskName = taskName;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}


}
