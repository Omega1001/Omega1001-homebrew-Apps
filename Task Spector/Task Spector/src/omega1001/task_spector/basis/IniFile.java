package omega1001.task_spector.basis;

import java.util.HashSet;
import java.util.Set;

public class IniFile {
	
	private Set<String> events = new HashSet<>();
	private Set<String> tasks = new HashSet<>();
	

	public IniFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Set<String> getEvents() {
		return events;
	}
	public Set<String> getTasks() {
		return tasks;
	}
	public void setEvents(Set<String> events) {
		this.events = events;
	}
	public void setTasks(Set<String> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "IniFile [events=" + events + ", tasks=" + tasks + "]";
	}
}
