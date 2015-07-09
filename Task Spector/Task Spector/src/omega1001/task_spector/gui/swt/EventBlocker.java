package omega1001.task_spector.gui.swt;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class EventBlocker extends Dialog {


	protected Shell shlEventBlocker;
	private String eventName;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EventBlocker(Shell parent, int style, String eventName) {
		super(parent, style);
		setText("SWT Dialog");
		this.eventName = eventName;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public void open() {
		createContents();
		shlEventBlocker.open();
		shlEventBlocker.layout();
		Display display = getParent().getDisplay();
		while (!shlEventBlocker.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlEventBlocker = new Shell(getParent(), getStyle());
		shlEventBlocker.setSize(167, 135);
		shlEventBlocker.setText("Event Blocker");
		
		Label lblRunningEvent = new Label(shlEventBlocker, SWT.NONE);
		lblRunningEvent.setBounds(10, 10, 100, 15);
		lblRunningEvent.setText("Running Event:");
		
		Label lblevent = new Label(shlEventBlocker, SWT.NONE);
		lblevent.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		lblevent.setBounds(20, 31, 129, 35);
		lblevent.setText(eventName);
		
		Button btnFinish = new Button(shlEventBlocker, SWT.NONE);
		btnFinish.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shlEventBlocker.dispose();
			}
		});
		btnFinish.setBounds(10, 72, 75, 25);
		btnFinish.setText("Finish");

	}
}
