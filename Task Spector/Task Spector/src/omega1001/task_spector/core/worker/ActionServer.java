package omega1001.task_spector.core.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import omega1001.task_spector.basis.CommentEvent;
import omega1001.task_spector.basis.Event;
import omega1001.task_spector.basis.EventType;
import omega1001.task_spector.basis.TaskAddEvent;
import omega1001.task_spector.basis.TaskChangeEvent;
import omega1001.task_spector.basis.UserEvent;
import omega1001.task_spector.basis.UserEventAddEvent;
import omega1001.task_spector.core.EventQuey;
import omega1001.task_spector.moddel.Task;

public class ActionServer implements Runnable {

	private List<Task> tasks = new ArrayList<Task>();
	private Recorder recorder;
	private IniServer iniServer = null;
	private boolean update = false;
	private boolean shutdown = false;

	/**
	 * @param recorder
	 */
	public ActionServer(Recorder recorder) {
		super();
		this.recorder = recorder;
	}

	public ActionServer(Recorder recorder, IniServer iniServer) {
		super();
		this.recorder = recorder;
		this.iniServer = iniServer;
	}

	@Override
	public void run() {
		iniTasks();
		update = true;
		Event e = null;
		while (!shutdown) {
			e = EventQuey.getNextEvent();
			if (e != null) {
				if (e.getType().equals(EventType.TASK_ADD)
						&& e instanceof TaskAddEvent) {
					TaskAddEvent ev = (TaskAddEvent) e;
					Task task = ev.getTaskToAdd();
					tasks.add(task);
					iniServer.addTask(task.getName());
				} else if (e instanceof TaskChangeEvent) {
					TaskChangeEvent ev = (TaskChangeEvent) e;
					int taskId = findTask(ev.getTaskName());
					int activeId = findActiveTask();
					if (ev.getType().equals(EventType.TASK_STAT)
							&& taskId != -1) {
						if (activeId != -1) {
							EventQuey.addEvent(new TaskChangeEvent(
									EventType.TASK_STOP, tasks.get(activeId)
											.getName()));
							EventQuey.addEvent(ev);
							continue;
						}
						tasks.get(taskId).setActiv(true);
					} else if (ev.getType().equals(EventType.TASK_STOP)
							&& taskId != -1) {
						tasks.get(taskId).setActiv(false);
					} else if (ev.getType().equals(EventType.TASK_DELETE)
							&& taskId != -1) {
						if (taskId == activeId) {
								recorder.printTaskChangeToLog(ev.getTaskName(),
										EventType.TASK_STOP);
						}
						tasks.remove(taskId);
						iniServer.delTask(ev.getTaskName());
						update = true;
						continue;
					} else {
						// TODO Logging
						continue;
					}
						recorder.printTaskChangeToLog(ev.getTaskName(),
								ev.getType());

				} else if (e instanceof UserEvent) {
					UserEvent ev = (UserEvent) e;
					String activeTask = tasks.get(findActiveTask()).getName();
					if (ev.getType().equals(EventType.USER_EVENT_BEGIN)) {
							recorder.printTaskChangeToLog(activeTask,
									EventType.TASK_STOP);
							recorder.printTaskChangeToLog(ev.getEventName(),
									EventType.USER_EVENT_BEGIN);
					} else if (ev.getType().equals(EventType.USER_EVENT_END)) {
						synchronized (recorder) {
							recorder.printTaskChangeToLog(ev.getEventName(),
									EventType.USER_EVENT_END);
							recorder.printTaskChangeToLog(activeTask,
									EventType.TASK_STAT);
						}
					}else if (ev.getType().equals(EventType.USER_EVENT_DEL)){
						iniServer.delEvent(ev.getEventName());
					}
				} else if (e.getType().equals(EventType.COMMENT)) {
						recorder.printComment(((CommentEvent) e).getComment());
				}else if (e instanceof UserEventAddEvent){
					iniServer.addEvent(((UserEventAddEvent)e).getEvent());
				}
				update = true;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				break;
			}

		}
		int activeId = findActiveTask();
		if (activeId != -1) {
				recorder.printTaskChangeToLog(tasks.get(activeId).getName(),
						EventType.TASK_STOP);
		}

	}

	private void iniTasks() {
		Set<String> datas = iniServer.getIniDatas().getTasks();
		for (String s : datas){
			tasks.add(new Task(s));
		}
		
	}

	private int findTask(String name) {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private int findActiveTask() {
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).isActiv()) {
				return i;
			}
		}
		return -1;
	}

	public void shutdown() {
		shutdown = true;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		boolean up = update;
		update = false;
		return up;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Recorder getRecorder() {
		return recorder;
	}

	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}

}
