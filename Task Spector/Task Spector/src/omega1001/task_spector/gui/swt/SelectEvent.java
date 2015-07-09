package omega1001.task_spector.gui.swt;

import java.util.Set;

import omega1001.task_spector.basis.EventType;
import omega1001.task_spector.basis.UserEvent;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SelectEvent extends Dialog {

	protected UserEvent result;
	protected Shell shlSelectEvent;
	private Table table;
	private Set<String> events;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectEvent(Shell parent, int style,Set<String> set) {
		super(parent, style);
		setText("Event Selection");
		this.events = set;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public UserEvent open() {
		createContents();
		shlSelectEvent.open();
		shlSelectEvent.layout();
		Display display = getParent().getDisplay();
		while (!shlSelectEvent.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlSelectEvent = new Shell(getParent(), getStyle());
		shlSelectEvent.setSize(184, 255);
		shlSelectEvent.setText("Select Event");

		table = new Table(shlSelectEvent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 155, 178);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnEvent = new TableColumn(table, SWT.NONE);
		tblclmnEvent.setWidth(150);
		tblclmnEvent.setText("Event");

		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText("Pause");

		TableItem tableItem_1 = new TableItem(table, SWT.NONE);
		tableItem_1.setText("Meeting");

		enhanceTable();

		Button btnNewButton = new Button(shlSelectEvent, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (table.getSelectionCount() == 1) {
					TableItem i = table.getSelection()[0];
					result = new UserEvent(EventType.USER_EVENT_BEGIN, i
							.getText(0));
				}else{
					result = null;
				}
				shlSelectEvent.dispose();
			}
		});
		btnNewButton.setBounds(10, 194, 75, 25);
		btnNewButton.setText("OK");

		Button btnNewButton_1 = new Button(shlSelectEvent, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				result = null;
				shlSelectEvent.dispose();
			}
		});
		btnNewButton_1.setBounds(91, 194, 75, 25);
		btnNewButton_1.setText("Cancel");

	}

	private void enhanceTable() {
		for (String event : events){
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(event);
		}
	}
}
