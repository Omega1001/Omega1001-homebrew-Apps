package omega1001.task_spector.gui.swt;

import omega1001.task_spector.moddel.Task;

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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.core.databinding.DataBindingContext;

public class NewTaskDialog extends Dialog {

	protected Task result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewTaskDialog(Shell parent, int style) {
		super(parent, style);
		setText("New Task");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Task open() {
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
		shell.setText(getText());
		
		Label lblTaskName = new Label(shell, SWT.NONE);
		lblTaskName.setBounds(10, 10, 89, 15);
		lblTaskName.setText("Task Name");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 31, 156, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					result = new Task(text.getText());
					shell.dispose();
				}
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (text.getText() != null && !text.getText().equals(""))
					result = new Task(text.getText());
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
