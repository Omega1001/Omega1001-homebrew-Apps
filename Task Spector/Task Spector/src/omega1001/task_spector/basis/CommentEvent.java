package omega1001.task_spector.basis;

public class CommentEvent extends Event{
	
	private String comment;

	/**
	 * @param type
	 * @param comment
	 */
	public CommentEvent(String comment) {
		super(EventType.COMMENT);
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

}
