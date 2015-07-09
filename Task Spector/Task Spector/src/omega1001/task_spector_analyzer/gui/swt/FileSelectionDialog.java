package omega1001.task_spector_analyzer.gui.swt;

import java.io.File;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class FileSelectionDialog extends Dialog {

	protected File result;
	protected Shell shell;
	private Table table;
	private File[] files;
	private Button btnClose;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FileSelectionDialog(Shell parent, int style, File current) {
		super(parent, style);
		setText("Select a file");
		files = current.getAbsoluteFile().getParentFile().listFiles();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public File open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(215, 295);
		shell.setText(getText());
		
		Button btnSelect = new Button(shell, SWT.NONE);
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (table.getSelectionIndex() >=0){
					result = files[table.getSelectionIndex()];
					shell.dispose();
				}
			}
		});
		btnSelect.setBounds(10, 237, 75, 25);
		btnSelect.setText("Select");
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 185, 221);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnFiles = new TableColumn(table, SWT.NONE);
		tblclmnFiles.setText("Files");
		tblclmnFiles.setWidth(181);
		
		btnClose = new Button(shell, SWT.NONE);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnClose.setBounds(120, 237, 75, 25);
		btnClose.setText("close");
		for (File f : files){
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(f.getName());
		}

	}
}
