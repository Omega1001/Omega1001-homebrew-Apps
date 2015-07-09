package omega1001.task_spector.gui.swt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import omega1001.task_spector.basis.CommentEvent;
import omega1001.task_spector.basis.EventType;
import omega1001.task_spector.basis.IniFile;
import omega1001.task_spector.basis.TaskAddEvent;
import omega1001.task_spector.basis.TaskChangeEvent;
import omega1001.task_spector.basis.UserEvent;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class Mainframe {

	protected Shell shlTaskSpector;
	private Table table;
	private CentralCore core;
	private static final String DEFAULT_LOG_PATH = "taskLog"+new SimpleDateFormat("MMMMM.yyyy").format(new Date())+".txt";
	private File logPath;
	
	private IniFile iniDatas;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Mainframe window = null;
		if (args.length != 0 && args.length>=2){
			for (int i=0;i<args.length;i++){
				String s;
				if ((s = args[i]).contains("--path:\"")){
					s.replace("--path:", "").replace("\"", "");
					File f = new File(s);
					if(f.getParentFile().exists() && !f.isDirectory()){
						window = new Mainframe(f);
					}else{
						window = new Mainframe(new File (DEFAULT_LOG_PATH));
					}
				}
				if (window == null){
					window = new Mainframe(new File (DEFAULT_LOG_PATH));
				}
			}
		}else{
			window = new Mainframe(new File (DEFAULT_LOG_PATH));
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
		shlTaskSpector.setSize(338, 291);
		shlTaskSpector.setText("Task Spector");
		
		table = new Table(shlTaskSpector, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 221, 235);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(145);
		tblclmnNewColumn.setText("Task");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(72);
		tblclmnNewColumn_1.setText("Active");
		
		Button btnNewTask = new Button(shlTaskSpector, SWT.NONE);
		btnNewTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				NewTaskDialog dialog = new NewTaskDialog(shlTaskSpector, SWT.APPLICATION_MODAL);
				Task t = dialog.open();
				if (t != null)
					EventQuey.addEvent(new TaskAddEvent(t));
			}
		});
		btnNewTask.setBounds(237, 10, 75, 25);
		btnNewTask.setText("New Task");
		
		Button btnNewButton = new Button(shlTaskSpector, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (table.getSelectionCount() == 1){
					TableItem item = table.getSelection()[0];
					EventQuey.addEvent(new TaskChangeEvent(EventType.TASK_STAT, item.getText(0)));
				}
			}
		});
		btnNewButton.setBounds(237, 41, 75, 25);
		btnNewButton.setText("Start Task");
		
		Button btnNewButton_1 = new Button(shlTaskSpector, SWT.NONE);
		btnNewButton_1.setBounds(237, 72, 75, 25);
		btnNewButton_1.setText("End Task");
		
		Button btnDeleteTask = new Button(shlTaskSpector, SWT.NONE);
		btnDeleteTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem item = table.getSelection()[0];
				EventQuey.addEvent(new TaskChangeEvent(EventType.TASK_DELETE, item.getText(0)));
			}
		});
		btnDeleteTask.setBounds(237, 103, 75, 25);
		btnDeleteTask.setText("Delete Task");
		
		Button btnNewButton_2 = new Button(shlTaskSpector, SWT.NONE);
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				SelectEvent sev = new SelectEvent(shlTaskSpector, SWT.APPLICATION_MODAL, iniDatas.getEvents());
				UserEvent event = sev.open();
				if (event != null){
					EventQuey.addEvent(event);
					EventBlocker blocker = new EventBlocker(shlTaskSpector, SWT.APPLICATION_MODAL, event.getEventName());
					blocker.open();
					EventQuey.addEvent(new UserEvent(EventType.USER_EVENT_END, event.getEventName()));
				}
			}
		});
		btnNewButton_2.setBounds(237, 134, 75, 25);
		btnNewButton_2.setText("Start Event");
		
		Button btnComment = new Button(shlTaskSpector, SWT.NONE);
		btnComment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				CommentForm c = new CommentForm(new Shell(shlTaskSpector.getDisplay()), SWT.APPLICATION_MODAL);
				String s = c.open();
				if (s != null){
					EventQuey.addEvent(new CommentEvent(s));
				}
			}
		});
		btnComment.setBounds(237, 165, 75, 25);
		btnComment.setText("Comment");
		
		Button btnLogAnalyzer = new Button(shlTaskSpector, SWT.NONE);
		btnLogAnalyzer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				omega1001.task_spector_analyzer.gui.swt.Mainframe.start(logPath);
			}
		});
		btnLogAnalyzer.setBounds(237, 196, 75, 25);
		btnLogAnalyzer.setText("Log Analyzer");
		shlTaskSpector.setTabList(new Control[]{btnNewTask, btnNewButton, btnNewButton_1});
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (table.getSelectionCount() == 1){
					TableItem item = table.getSelection()[0];
					EventQuey.addEvent(new TaskChangeEvent(EventType.TASK_STOP, item.getText(0)));
				}
			}
		});
		
		update();
		

	}
	
	private void update (){
		if (!core.isUpdate()){
			return;
		}
		table.removeAll();
		List<Task> tasks = core.getTasks();		
		for (Task t : tasks){
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(0,t.getName());
			ti.setText(1,(t.isActiv() ? "Active":"Inactive"));
		}
	}
	
	private void dispose(){
		core.shutdown();
		shlTaskSpector.dispose();
	}
}
