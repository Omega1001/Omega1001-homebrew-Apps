package omega1001.task_spector_analyzer.worker;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import omega1001.task_spector.basis.EventType;
import omega1001.task_spector_analyzer.moddel.TaskContainer;

public class LogParser implements Callable<List<TaskContainer>> {

	private File source;

	public LogParser(File source) {
		this.source = source;
	}

	@Override
	public List<TaskContainer> call() throws Exception {
		List<TaskContainer> result = new ArrayList<>();
		Scanner scanner = new Scanner(source);
		StorrageClass storrage = new StorrageClass();
		while (scanner.hasNextLine()) {
			parse(scanner.nextLine(), storrage);
			if (storrage.isValid()) {
				TaskContainer c = new TaskContainer(storrage.getName(),
						storrage.getStart());
				c.addComment(storrage.getComments());
				c.addTime(storrage.getStart(), storrage.getEnd());

				if (result.contains(c)) {
					for (TaskContainer co : result) {
						if (co.equals(c)) {
							co.merge(c);
						}
					}
				} else {
					result.add(c);
				}
				storrage = new StorrageClass();
			}
		}
		scanner.close();
		return result;
	}

	private void parse(String nextLine, StorrageClass storrage) {
		Date date = null;
		String name = null;
		EventType event = null;
		try {
			date = getDate(nextLine);
			name = getName(nextLine);
			event = getEvent(nextLine);
		} catch (ParseException e) {
			// TODO Do something!
			e.printStackTrace();
		}
		if (storrage.getName() == null || storrage.getName().equals(name)
				|| event.equals(EventType.COMMENT)) {
			switch (event) {
			case TASK_STAT:
			case USER_EVENT_BEGIN:
				storrage.setName(name);
				storrage.setStart(date);
				break;
			case TASK_STOP:
			case USER_EVENT_END:
				storrage.setEnd(date);
				break;
			case COMMENT:
				storrage.addComment(name);
				break;
			default:
				break;
			}
		}

	}

	private Date getDate(String source) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Pattern datePattern = Pattern.compile("\\(.*\\)");
		Matcher dateMatcher = datePattern.matcher(source);
		dateMatcher.find();
		String date = (source.substring(dateMatcher.start() + 1,
				dateMatcher.end() - 1));
		return sdf.parse(date);
	}

	private String getName(String source) throws ParseException {
		Pattern datePattern = Pattern.compile("\\[.*\\]");
		Matcher dateMatcher = datePattern.matcher(source);
		dateMatcher.find();
		return (source
				.substring(dateMatcher.start() + 1, dateMatcher.end() - 1));

	}

	private EventType getEvent(String source) throws ParseException {
		Pattern datePattern = Pattern.compile("\\{.*\\}");
		Matcher dateMatcher = datePattern.matcher(source);
		dateMatcher.find();
		return EventType.valueOf(source.substring(dateMatcher.start() + 1,
				dateMatcher.end() - 1));
	}

}

final class StorrageClass {
	private Date start = null;
	private Date end = null;
	private String name = null;

	/**
	 * 
	 */
	public StorrageClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	private List<String> comments = new ArrayList<>();

	public boolean isValid() {
		if (start != null && end != null && name != null) {
			return true;
		} else {
			return false;
		}
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public String getName() {
		return name;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public void addComment(String comment) {
		this.comments.add(comment);
	}
}
