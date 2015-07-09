package omega1001.task_spector.gui.swt;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class CommentForm extends Dialog {

	protected String result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CommentForm(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open() {
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
		shell.setSize(293, 120);
		shell.setText(getText());
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 31, 269, 21);
		
		Label lblComment = new Label(shell, SWT.NONE);
		lblComment.setBounds(10, 10, 55, 15);
		lblComment.setText("Comment");
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				result = text.getText();
				shell.dispose();
			}
		});
		btnOk.setBounds(10, 58, 75, 25);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(91, 58, 75, 25);
		btnCancel.setText("Cancel");
		
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				result = null;
				shell.dispose();
			}
		});

	}
}
