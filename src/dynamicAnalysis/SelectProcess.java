package dynamicAnalysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;

public class SelectProcess
{

	protected Shell shell;
	private Table table;
	private String[] names;
	private int[] pids;

	/**
	 * Launch the application.
	 * @param args
	 */
	public SelectProcess(String[] names, int[] pids)
	{
		
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		int width = 140;
		
		TableColumn tableNames = new TableColumn(table, SWT.CENTER);
		tableNames.setWidth(width);
		tableNames.setText("Name");
		
		TableColumn tablePids = new TableColumn(table, SWT.CENTER);
		tablePids.setWidth(width);
		tablePids.setText("Process ID");
		
		table.setItemCount(names.length);
		TableItem[] tableItems = new TableItem[names.length];
		
		for(int index = 0;index < tableItems.length;index++)
		{
			tableItems[index] = new TableItem(table, SWT.NULL);
			tableItems[index].setText(0, names[index]);
			tableItems[index].setText(1, Integer.toString(pids[index]));
		}
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Select");
		
		
	}

}
