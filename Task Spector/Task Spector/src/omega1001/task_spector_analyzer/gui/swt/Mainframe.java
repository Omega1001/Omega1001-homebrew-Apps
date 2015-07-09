package omega1001.task_spector_analyzer.gui.swt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import omega1001.task_spector_analyzer.gui.filter.FilterCriteria;
import omega1001.task_spector_analyzer.moddel.TaskContainer;
import omega1001.task_spector_analyzer.worker.LogParser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class Mainframe extends Shell {
	private Table table;
	private java.util.List<TaskContainer> sources;
	private java.util.List<TaskContainer> current = new ArrayList<>();
	private java.util.List<FilterCriteria>  filter = new ArrayList<>();
	private Table table_1;
	private Text exactDurationInput;
	private Text taskNameInput;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void start (File filesource) {
		try {
			Display display = Display.getDefault();
			Mainframe shell = new Mainframe(display,filesource);
			
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildFrame() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		boolean needFilter = !this.filter.isEmpty();
		table.removeAll();
		current.clear();
		for (TaskContainer c : sources){
			if (needFilter && !filter(c)){
				continue;
			}else{
				current.add(c);
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(c.getTaskName());
				tableItem.setText(1, String.valueOf(c.getDuration()));
				tableItem.setText(2, sdf.format(c.getDate()));
			}
		}
	}
	
	private boolean filter (TaskContainer container){
		for (FilterCriteria fi : filter){
			if (!fi.filter(container)){
				return false;
			}
		}
		return true;		
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public Mainframe(Display display, File filesource) {
		super(display, SWT.SHELL_TRIM);
		LogParser parser = new LogParser(filesource);
		try {
			sources = parser.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TaskContainer sou = current.get(table.getSelectionIndex());
				java.util.List<String> container = sou.getComments();
				table_1.removeAll();
				taskNameInput.setText(sou.getTaskName());
				exactDurationInput.setText(sou.getExactDuration());
				for (String s : container){
					TableItem tableItem = new TableItem(table_1, SWT.NONE);
					tableItem.setText(s);
				}
			}
		});
		table.setBounds(10, 10, 315, 236);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnTaskName = new TableColumn(table, SWT.NONE);
		tblclmnTaskName.setWidth(155);
		tblclmnTaskName.setText("Task Name");
		
		TableColumn tblclmnDuration = new TableColumn(table, SWT.NONE);
		tblclmnDuration.setWidth(66);
		tblclmnDuration.setText("Duration");
		
		TableColumn tblclmnDate = new TableColumn(table, SWT.NONE);
		tblclmnDate.setWidth(89);
		tblclmnDate.setText("Date");
		
		Button btnLoadDifferentFile = new Button(this, SWT.NONE);
		btnLoadDifferentFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FileSelectionDialog dialog = new FileSelectionDialog(getShell(), SWT.APPLICATION_MODAL, filesource);
				File res = dialog.open();
				if (res != null){
					LogParser parser = new LogParser(res);
					try {
						sources = parser.call();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					table.removeAll();
					buildFrame();
				}
			}
		});
		btnLoadDifferentFile.setBounds(10, 252, 113, 25);
		btnLoadDifferentFile.setText("Load different File");
		
		Button btnFilter = new Button(this, SWT.NONE);
		btnFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FilterDialog dialog = new FilterDialog(getShell(), SWT.NONE);
				filter = dialog.open();
				buildFrame();
			}
		});
		btnFilter.setBounds(129, 252, 75, 25);
		btnFilter.setText("Filter");
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(331, 10, 197, 236);
		
		TabItem tbtmAllgemein = new TabItem(tabFolder, SWT.NONE);
		tbtmAllgemein.setText("Allgemein");
		
		Group grpInfo = new Group(tabFolder, SWT.NONE);
		tbtmAllgemein.setControl(grpInfo);
		
		exactDurationInput = new Text(grpInfo, SWT.BORDER);
		exactDurationInput.setEditable(false);
		exactDurationInput.setBounds(103, 37, 76, 21);
		
		Label lblTaskName = new Label(grpInfo, SWT.NONE);
		lblTaskName.setBounds(10, 13, 64, 15);
		lblTaskName.setText("Task Name");
		
		taskNameInput = new Text(grpInfo, SWT.BORDER);
		taskNameInput.setEnabled(false);
		taskNameInput.setBounds(103, 10, 76, 21);
		
		Label lblExactDuration = new Label(grpInfo, SWT.NONE);
		lblExactDuration.setBounds(10, 40, 83, 15);
		lblExactDuration.setText("Exact Duration");
		
		TabItem tbtmComments = new TabItem(tabFolder, SWT.NONE);
		tbtmComments.setText("Comments");
		
		Group group = new Group(tabFolder, SWT.NONE);
		tbtmComments.setControl(group);
		
		table_1 = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLocation(0, 0);
		table_1.setSize(189, 208);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnComments = new TableColumn(table_1, SWT.NONE);
		tblclmnComments.setText("Comments");
		tblclmnComments.setWidth(183);
		createContents();
		buildFrame();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Task Inspector");
		setSize(554, 324);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
