package omega1001.task_spector.basis;

public class UserEventAddEvent extends Event {

	private String event;
	
	public UserEventAddEvent(String event) {
		super(EventType.USER_EVENT_ADD);
		this.event = event;
	}

	public String getEvent() {
		return event;
	}

}
