/*
 * 
 */
package dynamicAnalysis;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * The window to choose a currently running process to hook into. Displays a list of all running processes
 */
public class SelectProcess
{

	/** The shell for the process selection window. */
	protected Shell shell;
	
	/** The table to display the currently running processes. */
	private Table table;
	
	/** The names of the processes. */
	private String[] names;
	
	/** The unique identifiers of the processes. */
	private int[] pids;
	
	/** The memory that the processes are using. */
	private String[] memory;
	
	/** The unique identifier to be selected by the user. */
	private int pid;
	
	/** The X value to position the window horizontally. */
	private int x;
	
	/** The Y value to position the window vertically. */
	private int y;
	
	/** The text field to search for a process. */
	private Text text;
	
	/** The table items that populate the GUI table. */
	private TableItem[] tableItems;
	
	/**
	 * Launch the file selection window.
	 *
	 * @param names the names of the processes
	 * @param pids the PIDs of the processes
	 * @param memory the memory that the processes use
	 * @param x the X value for positioning
	 * @param y the Y value for positioning
	 */
	
	public SelectProcess(String[] names, int[] pids, String[] memory, int x, int y)
	{
		setNames(names);
		setPids(pids);
		setMemory(memory);
		setX(x);
		setY(y);
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
	 * Gets the names of the processes.
	 *
	 * @return the names of the processes
	 */
	public String[] getNames()
	{
		return names;
	}

	/**
	 * Sets the names of the processes.
	 *
	 * @param names the new names of the processes
	 */
	public void setNames(String[] names)
	{
		this.names = names;
	}

	/**
	 * Gets the unique identifiers of the processes.
	 *
	 * @return the unique identifiers of the processes
	 */
	public int[] getPids()
	{
		return pids;
	}

	/**
	 * Sets the unique identifiers of the processes.
	 *
	 * @param pids the new unique identifiers of the processes
	 */
	public void setPids(int[] pids)
	{
		this.pids = pids;
	}
	
	/**
	 * Gets the unique identifier of a process.
	 *
	 * @return the unique identifier of a process
	 */
	public int getPid()
	{
		return pid;
	}

	/**
	 * Sets the unique identifier of a process.
	 *
	 * @param pid the new unique identifier of a process
	 */
	private void setPid(int pid)
	{
		this.pid = pid;
	}
	
	/**
	 * Gets the memory used by the processes.
	 *
	 * @return the memory used by the processes
	 */
	public String[] getMemory()
	{
		return memory;
	}

	/**
	 * Sets the memory used by the processes.
	 *
	 * @param memory the new memory used by the processes
	 */
	public void setMemory(String[] memory)
	{
		this.memory = memory;
	}

	/**
	 * Gets the X value.
	 *
	 * @return the X value
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Sets the X value.
	 *
	 * @param x the new X value
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Gets the Y value.
	 *
	 * @return the Y value
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Sets the Y value.
	 *
	 * @param y the new Y value
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Search through the running processes. Filters any processes that do not match
	 *
	 * @param names the names of the processes
	 * @param toSearch the value to search for
	 * @return an index array of values that match
	 */
	private int[] search(String[] names, String toSearch)
	{
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for(int index = 0; index < names.length; index++)
		{
			if(names[index].toLowerCase().contains(toSearch.toLowerCase()))
			{
				arrayList.add(index);
			}
		}
		return arrayList.stream().mapToInt(i -> i).toArray();
	}
	
	/**
	 * Fully populate the process list after a search is cleared.
	 *
	 * @param table the GUI table to populate
	 */
	private void fullPopulate(Table table)
	{
		table.setItemCount(0);
		tableItems = new TableItem[names.length];
		for(int index = 0;index < tableItems.length;index++)
		{
			try
			{
				tableItems[index] = new TableItem(table, SWT.NULL);
				tableItems[index].setText(0, getNames()[index]);
				tableItems[index].setText(1, Integer.toString(getPids()[index]));
				tableItems[index].setText(2, getMemory()[index]);
			}
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * Called when a search request is given by the GUI.
	 */
	private void searchEvent()
	{
		if(text.getText()=="")
		{
			fullPopulate(table);
		}
		else
		{
			int[] indexes = search(getNames(), text.getText());
			table.setItemCount(0);
			for(int index=0;index<indexes.length;index++)
			{
				tableItems[index] = new TableItem(table, SWT.NULL);
				tableItems[index].setText(0, getNames()[indexes[index]]);
				tableItems[index].setText(1, Integer.toString(getPids()[indexes[index]]));
				tableItems[index].setText(2, getMemory()[indexes[index]]);
			}
		}
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
			shell.setText("Processes");
			shell.setLocation(getX(), getY());
			shell.setLayout(new FormLayout());
			Button btnNewButton = new Button(shell, SWT.NONE);
			FormData fd_btnNewButton = new FormData();
			fd_btnNewButton.top = new FormAttachment(0, 5);
			fd_btnNewButton.left = new FormAttachment(0, 386);
			btnNewButton.setLayoutData(fd_btnNewButton);
			
			table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
			FormData fd_table = new FormData();
			fd_table.bottom = new FormAttachment(0, 256);
			fd_table.right = new FormAttachment(0, 429);
			fd_table.top = new FormAttachment(0, 35);
			fd_table.left = new FormAttachment(0, 5);
			table.setLayoutData(fd_table);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.addListener(SWT.SELECTED, new Listener() {
				public void handleEvent(Event e) {
					btnNewButton.setEnabled(true);
				}
			});
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
			
			TableColumn tableMemory = new TableColumn(table, SWT.CENTER);
			tableMemory.setWidth(width);
			tableMemory.setText("Memory");
			
			fullPopulate(table);
			
			System.out.println("table items: "+tableItems.length);
			
			btnNewButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
				}
			});
			btnNewButton.setText("Select");
			btnNewButton.setEnabled(false);
			
			Button btnSearch = new Button(shell, SWT.NONE);
			btnSearch.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					searchEvent();
				}
			});
			FormData fd_btnSearch = new FormData();
			fd_btnSearch.top = new FormAttachment(btnNewButton, 0, SWT.TOP);
			fd_btnSearch.right = new FormAttachment(btnNewButton, -232);
			btnSearch.setLayoutData(fd_btnSearch);
			btnSearch.setText("Search");
			
			Button button = new Button(shell, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					fullPopulate(table);
					text.setText("");
				}
			});
			fd_btnSearch.left = new FormAttachment(button, 6);
			button.setImage(SWTResourceManager.getImage(SelectProcess.class, "/icons/full/message_error.png"));
			FormData fd_button = new FormData();
			button.setLayoutData(fd_button);
			
			text = new Text(shell, SWT.BORDER);
			FormData fd_text = new FormData();
			fd_text.bottom = new FormAttachment(btnNewButton, 0, SWT.BOTTOM);
			fd_text.top = new FormAttachment(btnNewButton, 0, SWT.TOP);
			fd_text.right = new FormAttachment(btnNewButton, -281);
			fd_text.left = new FormAttachment(0, 5);
			text.setLayoutData(fd_text);
			fd_button.right = new FormAttachment(text, 0, SWT.RIGHT);
			fd_button.bottom = new FormAttachment(text, 0, SWT.BOTTOM);
			fd_button.left = new FormAttachment(text, -20);
			text.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e){	
					if(e.keyCode == SWT.CR) {
						searchEvent();	
					}	
				}	
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
