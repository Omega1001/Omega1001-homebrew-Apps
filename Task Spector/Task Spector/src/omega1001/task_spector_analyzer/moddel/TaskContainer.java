package omega1001.task_spector_analyzer.moddel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ibm.icu.text.SimpleDateFormat;

public class TaskContainer {

	private long duration;
	private String taskName;
	private List<String> comments = new ArrayList<>();
	private Date date;

	public TaskContainer(String name, Date date) {
		this.taskName = name;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.date = c.getTime();
	}

	public void merge(TaskContainer container) {
		this.duration = this.duration + container.duration;
		this.comments.addAll(container.getComments());
	}

	public double getDuration() {
		long houres = TimeUnit.MILLISECONDS.toHours(duration);
		long buffer = duration - TimeUnit.HOURS.toMillis(houres);
		double minutesBuffer = TimeUnit.MILLISECONDS.toMinutes(buffer);
		double minutes;
		if (minutesBuffer != 0) {
			minutes = minutesBuffer / (60.0 / 100);
			minutes = minutes / 100;
			if (!(minutes == 0.0 || minutes == 0.25 || minutes == 0.5 || minutes == 0.75)) {
				double min;
				double max;
				double thersold;
				boolean nextHoure = false;
				if (minutes > 0.0 && minutes < 0.25) {
					min = 0.0;
					max = 0.25;
					thersold = 0.125;
				} else if (minutes > 0.25 && minutes < 0.5) {
					min = 0.25;
					max = 0.5;
					thersold = 0.375;
				} else if (minutes > 0.5 && minutes < 0.75) {
					min = 0.5;
					max = 0.75;
					thersold = 0.625;
				} else {
					min = 0.75;
					max = 0.0;
					thersold = 0.875;
					nextHoure = true;
				}

				if (minutes < thersold) {
					minutes = min;
				} else {
					minutes = max;
				}
				if (nextHoure) {
					houres++;
				}
			}
		} else {
			minutes = 0.0;
		}
		if (houres == 0.0 && minutes == 0.0) {
			return 0.25;
		} else {
			return minutes + houres;
		}
	}

	public String getTaskName() {
		return taskName;
	}

	public List<String> getComments() {
		return comments;
	}

	public void addTime(Date start, Date end) {
		duration = +(end.getTime() - start.getTime());
	}

	public void addComment(String comment) {
		this.comments.add(comment);
	}

	public void addComment(List<String> comment) {
		this.comments.addAll(comment);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + (int) (duration ^ (duration >>> 32));
		result = prime * result
				+ ((taskName == null) ? 0 : taskName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskContainer other = (TaskContainer) obj;
		if (!taskName.equals(other.taskName) || !date.equals(other.date))
			return false;
		return true;
	}

	public Date getDate() {
		return date;
	}

	public String getExactDuration() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setTimeInMillis(c.getTimeInMillis() + duration);
		return new SimpleDateFormat("HH:mm:ss").format(c.getTime());
	}

}
