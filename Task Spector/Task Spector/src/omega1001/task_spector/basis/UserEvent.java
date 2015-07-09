package omega1001.task_spector.basis;

public class UserEvent extends Event {
	
	private String eventName;
	
	public UserEvent(EventType type,String eventName) {
		super(type);
		this.eventName = eventName;
	}

	public String getEventName() {
		return eventName;
	}

}
