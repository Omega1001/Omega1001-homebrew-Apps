package omega1001.task_spector_analyzer.gui.filter.impl;

import java.util.Calendar;

import omega1001.task_spector_analyzer.gui.filter.FilterCriteria;
import omega1001.task_spector_analyzer.moddel.TaskContainer;

public class DateFilter implements FilterCriteria {
	public enum Criteria {
		EQUALS,NOT_EQUALS,BEFORE,AFTER,IN;
	}
	private int dayOfMonth;
	private Criteria criterium;
	
	@Override
	public boolean filter(TaskContainer container) {
		Calendar c = Calendar.getInstance();
		c.setTime(container.getDate());
		if (c.get(Calendar.DAY_OF_MONTH) == dayOfMonth)
			return true;
		else
			return false;
	}

	/**
	 * @param dayOfMonth
	 * @param criterium 
	 */
	public DateFilter(int dayOfMonth, Criteria criterium) {
		this.dayOfMonth = dayOfMonth;
		this.criterium = criterium;
	}

}
