package omega1001.task_spector.gui.swt;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.core.databinding.DataBindingContext;

public class AddSimpleDialog extends Dialog {

	protected String result;
	protected Shell shell;
	private Text text;
	private String caption;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddSimpleDialog(Shell parent, int style, String title, String caption) {
		super(parent, style);
		setText(title);
		this.caption = caption;
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
		shell.setSize(178, 118);
		
		Label lblTaskName = new Label(shell, SWT.NONE);
		lblTaskName.setBounds(10, 10, 89, 15);
		lblTaskName.setText(caption);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 31, 156, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (text.getText() != null && !text.getText().equals(""))
					result = text.getText();
				else
					result = null;
				shell.dispose();
			}
		});
		btnNewButton.setBounds(10, 58, 75, 25);
		btnNewButton.setText("OK");
		shell.setDefaultButton(btnNewButton);
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				result = null;
				shell.dispose();
			}
		});
		btnNewButton_1.setBounds(91, 58, 75, 25);
		btnNewButton_1.setText("Cancel");
		shell.setTabList(new Control[]{text, btnNewButton, btnNewButton_1});

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
