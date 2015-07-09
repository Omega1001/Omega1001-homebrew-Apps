package omega1001.task_spector_analyzer.gui.swt;

import java.util.ArrayList;
import java.util.List;

import omega1001.task_spector_analyzer.gui.filter.FilterCriteria;
import omega1001.task_spector_analyzer.gui.filter.impl.DateFilter;
import omega1001.task_spector_analyzer.gui.filter.impl.NameFilter;
import omega1001.task_spector_analyzer.gui.filter.impl.NameFilter.Criteria;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class FilterDialog extends Dialog {

	protected List<FilterCriteria> result;
	protected Shell shlFilter;
	private Text nameSearchText;
	private Button dateEnabler;
	private Spinner dayOfMonthSelector;
	private Button nameEnabler;
	private CCombo nameSearchMode;
	private CCombo dateSearchMode;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FilterDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		result = new ArrayList<>();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public List<FilterCriteria> open() {
		createContents();
		shlFilter.open();
		shlFilter.layout();
		Display display = getParent().getDisplay();
		while (!shlFilter.isDisposed()) {
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
		shlFilter = new Shell(getParent(), getStyle());
		shlFilter.setSize(450, 246);
		shlFilter.setText("Filter");
		
		TabFolder tabFolder = new TabFolder(shlFilter, SWT.NONE);
		tabFolder.setBounds(10, 10, 424, 158);
		
		TabItem tbtmByDate = new TabItem(tabFolder, SWT.NONE);
		tbtmByDate.setText("by Date");
		
		Group group = new Group(tabFolder, SWT.NONE);
		tbtmByDate.setControl(group);
		
		Label dateLableDayOfMonth = new Label(group, SWT.NONE);
		dateLableDayOfMonth.setBounds(89, 28, 82, 15);
		dateLableDayOfMonth.setText("Day of Month");
		
		dayOfMonthSelector = new Spinner(group, SWT.BORDER);
		dayOfMonthSelector.setEnabled(false);
		dayOfMonthSelector.setMaximum(31);
		dayOfMonthSelector.setMinimum(1);
		dayOfMonthSelector.setBounds(299, 28, 47, 22);
		
		dateEnabler = new Button(group, SWT.CHECK);
		dateEnabler.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean val = dateEnabler.getSelection();
				dayOfMonthSelector.setEnabled(val);
			}
		});
		dateEnabler.setBounds(20, 30, 63, 16);
		dateEnabler.setText("Enable");
		
		dateSearchMode = new CCombo(group, SWT.BORDER);
		dateSearchMode.setText("equals");
		dateSearchMode.setItems(new String[] {"equals", "contains", "not equals", "not contains"});
		dateSearchMode.setEnabled(false);
		dateSearchMode.setBounds(177, 28, 116, 21);
		
		TabItem tbtmByName = new TabItem(tabFolder, SWT.NONE);
		tbtmByName.setText("by Name");
		
		Group group_1 = new Group(tabFolder, SWT.NONE);
		tbtmByName.setControl(group_1);
		
		nameEnabler = new Button(group_1, SWT.CHECK);
		nameEnabler.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean val = nameEnabler.getSelection();
				nameSearchMode.setEnabled(val);
				nameSearchText.setEnabled(val);
			}
		});
		nameEnabler.setBounds(10, 25, 68, 16);
		nameEnabler.setText("Enabled");
		
		nameSearchMode = new CCombo(group_1, SWT.BORDER);
		nameSearchMode.setEnabled(false);
		nameSearchMode.setText("equals");
		nameSearchMode.setItems(new String[] {"equals", "contains", "not equals", "not contains"});
		nameSearchMode.setBounds(84, 25, 116, 21);
		
		nameSearchText = new Text(group_1, SWT.BORDER);
		nameSearchText.setEnabled(false);
		nameSearchText.setBounds(206, 25, 200, 21);
		
		Button btnOk = new Button(shlFilter, SWT.NONE);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
//				if (dateEnabler.getSelection()){
//					String selection = nameSearchMode.getText();
//					omega1001.task_spector_analyzer.gui.filter.impl.DateFilter.Criteria criterium = null;
//					if (selection.equals("equals"))
//						criterium = Criteria.EQUALS;
//					else if (selection.equals("contains"))
//						criterium = Criteria.CONTAINS;
//					else if (selection.equals("not equals"))
//						criterium = Criteria.NOT_EQUALS;
//					else if (selection.equals("not contains"))
//						criterium = Criteria.NOT_CONTAINS;
//					else
//						criterium = Criteria.EQUALS;
//					result.add(new DateFilter(dayOfMonthSelector.getSelection(),criterium));
//				}
				if (nameEnabler.getSelection()){
					String selection = nameSearchMode.getText();
					Criteria criterium = null;
					if (selection.equals("equals"))
						criterium = Criteria.EQUALS;
					else if (selection.equals("contains"))
						criterium = Criteria.CONTAINS;
					else if (selection.equals("not equals"))
						criterium = Criteria.NOT_EQUALS;
					else if (selection.equals("not contains"))
						criterium = Criteria.NOT_CONTAINS;
					else
						criterium = Criteria.EQUALS;
					result.add(new NameFilter(criterium, nameSearchText.getText()));
				}	
				
				shlFilter.dispose();
			}
		});
		btnOk.setBounds(10, 183, 75, 25);
		btnOk.setText("OK");

	}
}
