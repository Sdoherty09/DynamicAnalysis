package dynamicAnalysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class SelectProcess2 extends Shell
{
	private Table table;

	/**
	 * Launch the application.
	 * @param args
	 */
	/*
	public static void main(String args[])
	{
		try
		{
			Display display = Display.getDefault();
			SelectProcess shell = new SelectProcess(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed())
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the shell.
	 * @param display
	 */
	public SelectProcess2(Display display, String[] names, int[] pids)
	{
		super(display, SWT.SHELL_TRIM);
		setLayout(new GridLayout(1, false));
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
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
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Select");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents()
	{
		setText("SWT Application");
		setSize(317, 506);

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

}
