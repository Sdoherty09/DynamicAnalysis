package dynamicAnalysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectProcess
{

	protected Shell shell;
	private Table table;
	private String[] names;
	private int[] pids;
	private int pid;

	/*
	public static void main(String[] args)
	{
		SelectProcess selectProcess = new SelectProcess();
		selectProcess.open();
	}*/
	
	/**
	 * Launch the application.
	 * @param args
	 */
	
	public SelectProcess(String[] names, int[] pids)
	{
		setNames(names);
		setPids(pids);
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
	
	public String[] getNames()
	{
		return names;
	}

	public void setNames(String[] names)
	{
		this.names = names;
	}

	public int[] getPids()
	{
		return pids;
	}

	public void setPids(int[] pids)
	{
		this.pids = pids;
	}
	
	public int getPid()
	{
		return pid;
	}

	private void setPid(int pid)
	{
		this.pid = pid;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		try
		{
						
			shell = new Shell();
			shell.setSize(450, 300);
			shell.setText("SWT Application");
			shell.setLayout(new GridLayout(1, false));
			
			Button btnNewButton = new Button(shell, SWT.NONE);
			
			table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event e) {
					setPid(Integer.parseInt(table.getSelection()[0].getText(1)));
					shell.dispose();
				}
			});
			
			int width = 140;
			
			TableColumn tableNames = new TableColumn(table, SWT.CENTER);
			tableNames.setWidth(width);
			tableNames.setText("Name");
			
			TableColumn tablePids = new TableColumn(table, SWT.CENTER);
			tablePids.setWidth(width);
			tablePids.setText("Process ID");
			
			TableItem[] tableItems = new TableItem[names.length];
			
			System.out.println("table items: "+tableItems.length);
			
			for(int index = 0;index < tableItems.length;index++)
			{
				try
				{
					tableItems[index] = new TableItem(table, SWT.NULL);
					tableItems[index].setText(0, getNames()[index]);
					tableItems[index].setText(1, Integer.toString(getPids()[index]));
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
					continue;
				}
			}
			
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			btnNewButton.setText("Select");
			btnNewButton.setEnabled(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
