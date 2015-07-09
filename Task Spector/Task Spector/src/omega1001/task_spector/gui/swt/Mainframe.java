package omega1001.task_spector.gui.swt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import omega1001.task_spector.basis.CommentEvent;
import omega1001.task_spector.basis.Event;
import omega1001.task_spector.basis.EventType;
import omega1001.task_spector.basis.IniFile;
import omega1001.task_spector.basis.TaskAddEvent;
import omega1001.task_spector.basis.TaskChangeEvent;
import omega1001.task_spector.basis.UserEvent;
import omega1001.task_spector.basis.UserEventAddEvent;
import omega1001.task_spector.core.CentralCore;
import omega1001.task_spector.core.EventQuey;
import omega1001.task_spector.moddel.Task;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Mainframe {

	protected Shell shlTaskSpector;
	private Table table;
	private CentralCore core;
	private static final String DEFAULT_LOG_PATH = "taskLog"
			+ new SimpleDateFormat("MMMMM.yyyy").format(new Date()) + ".txt";
	private File logPath;

	private IniFile iniDatas;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Mainframe window = null;
		if (args.length != 0 && args.length >= 2) {
			for (int i = 0; i < args.length; i++) {
				String s;
				if ((s = args[i]).contains("--path:\"")) {
					s.replace("--path:", "").replace("\"", "");
					File f = new File(s);
					if (f.getParentFile().exists() && !f.isDirectory()) {
						window = new Mainframe(f);
					} else {
						window = new Mainframe(new File(DEFAULT_LOG_PATH));
					}
				}
				if (window == null) {
					window = new Mainframe(new File(DEFAULT_LOG_PATH));
				}
			}
		} else {
			window = new Mainframe(new File(DEFAULT_LOG_PATH));
		}
		try {

			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mainframe(File f) {
		logPath = f;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlTaskSpector.open();
		shlTaskSpector.layout();
		while (!shlTaskSpector.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			update();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTaskSpector = new Shell();
		shlTaskSpector.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				e.doit = false;
				dispose();
			}
		});
		core = new CentralCore(logPath);
		try {
			core.ini();
			iniDatas = core.getIniDatas();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		shlTaskSpector.setSize(338, 316);
		shlTaskSpector.setText("Task Spector");

		table = new Table(shlTaskSpector, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 302, 235);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(221);
		tblclmnNewColumn.setText("Task");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(72);
		tblclmnNewColumn_1.setText("Active");

		Menu menu = new Menu(shlTaskSpector, SWT.BAR);
		shlTaskSpector.setMenuBar(menu);

		MenuItem mntmTask = new MenuItem(menu, SWT.CASCADE);
		mntmTask.setText("Task");

		Menu menu_1 = new Menu(mntmTask);
		mntmTask.setMenu(menu_1);

		MenuItem mntmNewTask = new MenuItem(menu_1, SWT.NONE);
		mntmNewTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddSimpleDialog dialog = new AddSimpleDialog(shlTaskSpector,
						SWT.APPLICATION_MODAL, "New Task", "Task Name");
				Task t = new Task(dialog.open());
				if (t != null)
					EventQuey.addEvent(new TaskAddEvent(t));
			}
		});
		mntmNewTask.setText("New Task");

		MenuItem mntmStartTask = new MenuItem(menu_1, SWT.NONE);
		mntmStartTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() == 1) {
					TableItem item = table.getSelection()[0];
					EventQuey.addEvent(new TaskChangeEvent(EventType.TASK_STAT,
							item.getText(0)));
				}
			}
		});
		mntmStartTask.setText("Start Task");

		MenuItem mntmEditTask = new MenuItem(menu_1, SWT.NONE);
		mntmEditTask.setText("Edit Task");

		MenuItem mntmEndTask = new MenuItem(menu_1, SWT.NONE);
		mntmEndTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() == 1) {
					TableItem item = table.getSelection()[0];
					EventQuey.addEvent(new TaskChangeEvent(EventType.TASK_STAT,
							item.getText(0)));
				}
			}
		});
		mntmEndTask.setText("End Task");

		MenuItem mntmDeleteTask = new MenuItem(menu_1, SWT.NONE);
		mntmDeleteTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionCount() == 1) {
					TableItem item = table.getSelection()[0];
					EventQuey.addEvent(new TaskChangeEvent(
							EventType.TASK_DELETE, item.getText(0)));
				}
			}
		});
		mntmDeleteTask.setText("Delete Task");

		MenuItem mntmEvent = new MenuItem(menu, SWT.CASCADE);
		mntmEvent.setText("Event");

		Menu menu_2 = new Menu(mntmEvent);
		mntmEvent.setMenu(menu_2);

		MenuItem mntmStartEvent = new MenuItem(menu_2, SWT.NONE);
		mntmStartEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectEvent sev = new SelectEvent(shlTaskSpector,
						SWT.APPLICATION_MODAL, iniDatas.getEvents());
				UserEvent event = sev.open();
				if (event != null) {
					EventQuey.addEvent(event);
					EventBlocker blocker = new EventBlocker(shlTaskSpector,
							SWT.APPLICATION_MODAL, event.getEventName());
					blocker.open();
					EventQuey.addEvent(new UserEvent(EventType.USER_EVENT_END,
							event.getEventName()));
				}
			}
		});
		mntmStartEvent.setText("Start Event");

		MenuItem mntmAddEvent = new MenuItem(menu_2, SWT.NONE);
		mntmAddEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddSimpleDialog eventNameDialog = new AddSimpleDialog(
						shlTaskSpector, SWT.APPLICATION_MODAL, "New Event",
						"Event Name");
				String newEvent = eventNameDialog.open();
				if (newEvent != null) {
					Event event = new UserEventAddEvent(newEvent);
					EventQuey.addEvent(event);
				}
			}
		});
		mntmAddEvent.setText("Add Event");

		MenuItem mntmDeleteEvent = new MenuItem(menu_2, SWT.NONE);
		mntmDeleteEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectEvent sev = new SelectEvent(shlTaskSpector,
						SWT.APPLICATION_MODAL, iniDatas.getEvents());
				UserEvent event = sev.open();
				if (event != null && event.getEventName() != null) {
					String eventName = event.getEventName();
					EventQuey.addEvent(new UserEvent(EventType.USER_EVENT_DEL,
							eventName));
				}
			}
		});
		mntmDeleteEvent.setText("Delete Event");
		
		MenuItem mntmLogAnalyzer = new MenuItem(menu, SWT.NONE);
		mntmLogAnalyzer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				omega1001.task_spector_analyzer.gui.swt.Mainframe
				.start(logPath);
			}
		});
		mntmLogAnalyzer.setText("Log Analyzer");

		update();

	}

	private void update() {
		if (!core.isUpdate()) {
			return;
		}
		table.removeAll();
		List<Task> tasks = core.getTasks();
		for (Task t : tasks) {
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(0, t.getName());
			ti.setText(1, (t.isActiv() ? "Active" : "Inactive"));
		}
	}

	private void dispose() {
		core.shutdown();
		shlTaskSpector.dispose();
	}
}
