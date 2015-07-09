package omega1001.task_spector.core;

import java.util.ArrayList;
import java.util.List;

import omega1001.task_spector.basis.Event;

public class EventQuey {
	
	private static List<Event> eventList = new ArrayList<>();

	public static Event getNextEvent (){
		if (eventList.isEmpty()){
			return null;
		}
		Event res = eventList.get(0);
		eventList.remove(0);
		return res;
	}

	/**
	 * @param eventList the eventList to set
	 */
	public static void addEvent(Event event) {
		eventList.add(event);
	}
	
}
