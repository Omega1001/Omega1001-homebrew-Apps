package omega1001.task_spector_analyzer.gui.filter.impl;

import omega1001.task_spector_analyzer.gui.filter.FilterCriteria;
import omega1001.task_spector_analyzer.moddel.TaskContainer;

public class NameFilter implements FilterCriteria {
	
	public enum Criteria {
		CONTAINS,EQUALS,NOT_CONTAINS,NOT_EQUALS;
	}
//	public static Criteria CONTAINS = Criteria.CONTAINS;
//	public static Criteria EQUALS = Criteria.EQUALS;
//	public static Criteria NOT_CONTAINS = Criteria.NOT_CONTAINS;
//	public static Criteria NOT_EQUALS = Criteria.NOT_EQUALS;
	private Criteria criterium;
	private String pattern;

	/**
	 * @param criterium
	 * @param pattern
	 */
	public NameFilter(Criteria criterium, String pattern) {
		this.criterium = criterium;
		this.pattern = pattern;
	}

	@Override
	public boolean filter(TaskContainer container) {
		String name = container.getTaskName();
		switch (criterium){
		case CONTAINS:
			return name.toLowerCase().contains(pattern.toLowerCase());
		case EQUALS:
			return name.equalsIgnoreCase(pattern);
		case NOT_CONTAINS:
			return !name.toLowerCase().contains(pattern.toLowerCase());
		case NOT_EQUALS:
			return !name.equalsIgnoreCase(pattern);
		}
		return false;
	}

}


