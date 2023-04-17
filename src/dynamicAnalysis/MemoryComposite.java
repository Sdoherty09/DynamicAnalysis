/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

/**
 * The composite to display the virtual memory in the GUI
 */
public class MemoryComposite extends Composite
{
	
	/** The GUI text field containing the raw memory space. */
	private Text memoryField;
	
	/** The unique identifier for the process. */
	private int processId;
	
	/** The bytes. */
	private byte[] bytes;
	
	/** The virtual table to display ASCII strings contained within the virtual memory. */
	private Table asciiTable;
	
	/** The text field to input search values. */
	private Text searchText;
	
	/** A temporary location to store the table when a search is performed. It is used while the contents are removed from the GUI. */
	private TableItem[] tableStore = null;
	
	/** The table items. */
	private TableItem[] tableItems;
	
	/** The array of ASCII sections found in the virtual memory. */
	private String[] asciiSections;
	
	/** SWT color for highlighting the table red. */
	private Color red;
	
	/** The string value containing the virtual memory. */
	private String memory;
	
	/** the amount of characters to display in the memory text field. Used to increase performance. */
	private final int memoryCount = 10000;
	
	/**
	 * Create the memory composite.
	 *
	 * @param parent the main window that acts as the parent
	 * @param style the SWT style applied to the composite
	 * @param green the SWT color green
	 */
	public MemoryComposite(Composite parent, int style, Color green)
	{
		
		super(parent, style);
		setProcessId(Window.processId);
		setRed(green);
		setLayout(new GridLayout(4, false));
		Button btnUpdate = new Button(this, SWT.NONE);
		btnUpdate.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		btnUpdate.setText("Update");
		
		Label txtLength = new Label(this, SWT.BORDER);
		GridData gd_txtLength = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtLength.widthHint = 109;
		txtLength.setLayoutData(gd_txtLength);
		txtLength.setText("Length: ");
		
		searchText = new Text(this, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_searchText.widthHint = 157;
		searchText.setLayoutData(gd_searchText);	
		
		Button btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchEvent();
			}
		});
		btnNewButton_1.setText("Search");
		
		memoryField = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		memoryField.setEditable(false);
		GridData gd_memoryField = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gd_memoryField.widthHint = 154;
		gd_memoryField.heightHint = 217;
		memoryField.setLayoutData(gd_memoryField);
		 
			
		asciiTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		
		GridData gd_asciiTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_asciiTable.heightHint = 225;
		gd_asciiTable.widthHint = 197;
		asciiTable.setLayoutData(gd_asciiTable);
		asciiTable.setHeaderVisible(true);
		asciiTable.setLinesVisible(true);	
		
		new Thread() {
            public void run() {
            	String memoryText = updateMemory();
        		asciiSections = findAsciiSections();
            	Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                    	System.out.println(memoryText);
                    	memoryField.setText(memoryText);
                    	txtLength.setText("Length: "+getBytes().length);
                    	tableItems = new TableItem[asciiSections.length];
                    	asciiTable.setItemCount(asciiSections.length);
                    	for(int index=0;index<asciiSections.length;index++)
                		{
                    		asciiTable.getItem(index).setText(asciiSections[index].substring(asciiSections[index].indexOf('}')+1));
                    		asciiTable.getItem(index).setData(Integer.parseInt(asciiSections[index].substring(1, asciiSections[index].indexOf('}'))));
                		}
                    }
                 });
            }
        }.start();
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new Thread() {
		            public void run() {
		            	updateMemory();
		            	Display.getDefault().asyncExec(new Runnable() {
		                    public void run() {
		                    	txtLength.setText("Length: "+getBytes().length);
		                    }
		                });
		            }
		        }.start();	
			}
		});
		
		searchText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){	
				if(e.keyCode == SWT.CR) {
					searchEvent();	
				}	
			}	
		});
		
		asciiTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				int index=asciiTable.getSelectionIndex();
				
				int startIndex=(int)asciiTable.getItem(index).getData();
				System.out.println("index: "+startIndex);
				String tableText = asciiTable.getItem(index).getText();
				//System.out.println(tableText);
				populateMemory(memoryField, startIndex, tableText);
			}
		});
	}

	/**
	 * Gets the process id.
	 *
	 * @return the process id
	 */
	public int getProcessId()
	{
		return processId;
	}

	/**
	 * Sets the process id.
	 *
	 * @param processId the new process id
	 */
	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	/**
	 * Calls the method to retrieve the virtual memory space of an application from its process ID.
	 *
	 * @return the byte array containing the entire virtual memory space
	 */
	private byte[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		setBytes(virtualMemory.readMemory());
		return getBytes();
	}
	
	/**
	 * Gets the virtual memory space.
	 *
	 * @return the virtual memory space byte array
	 */
	public byte[] getBytes()
	{
		return bytes;
	}

	/**
	 * Sets the virtual memory space.
	 *
	 * @param bytes the new virtual memory space.
	 */
	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	/**
	 * Gets the SWT color red.
	 *
	 * @return the SWT color red
	 */
	public Color getRed() {
		return red;
	}

	/**
	 * Sets the SWT color red.
	 *
	 * @param red the new SWT color red
	 */
	public void setRed(Color red) {
		this.red = red;
	}

	/**
	 * Retrieve the memory text field with the current values stored within.
	 *
	 * @return the updated memory space
	 */
	private String updateMemory()
	{
		long start = System.currentTimeMillis();
		byte[] chars = readMemory();
		String output = "";
		String update = "";
		int sizeIndex = 0;
		int index = 0;
		while(sizeIndex!=memoryCount)
		{
			try
			{
				if(chars[index]!=0)
				{
					output += (char)chars[index];
					sizeIndex++;
				}
				index++;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				e.printStackTrace();
				//MessageDialog.openError(shell, "Error", "Process was closed.");
				break;
			}
		}
		setBytes(chars);
		System.out.println("memory time: "+(System.currentTimeMillis()-start));
		return output;
	}
	
	/**
	 * Looks through the virtual memory space for sections containing ASCII values. The threshhold is at least 8 readable characters in a row. The complexity of this algorithm is O(N).
	 *
	 * @return the string array containing sections of readable data from the virtual memory space
	 */
	private String[] findAsciiSections()
	{
		long startTime = System.currentTimeMillis();
		int startIndex = 0;
		boolean start = true;
		ArrayList<String> asciiSections = new ArrayList<String>();
		String current="";
		for(int index=0;index<getBytes().length;index++)
		{
			while(isAscii((char)getBytes()[index]))
			{
				if(start)
				{
					startIndex=index;
					start=false;
				}
				current+=(char)getBytes()[index];
				index++;
			}
			if(current.length()>8)
			{
				asciiSections.add("{"+startIndex+"}"+current);
			}
			if(!start) start=true;
			current="";
		}
		return asciiSections.toArray(new String[0]);
	}
	
	/**
	 * Checks if a character is ASCII.
	 *
	 * @param character the character to check
	 * @return true, if the given character is considered ASCII
	 */
	private boolean isAscii(char character)
	{
		return character>=32&&character<=126;
	}
	
	/**
	 * The search algorithm that filters the readable data.
	 *
	 * @param entries the ASCII string array
	 * @param toSearch the value to search for
	 * @return the filtered ASCII string array that match the search
	 */
	private String[] search(String[] entries, String toSearch)
	{
		ArrayList<String> filtered = new ArrayList<String>();
		for(int index = 0; index < entries.length; index++)
		{
			if(toSearch.endsWith("*"))
			{
				if(entries[index].toLowerCase().substring(entries[index].indexOf('}')+1).startsWith(toSearch.toLowerCase().substring(0, toSearch.length()-1)))
				{
					filtered.add(entries[index]);
				}
			}
			else if(toSearch.startsWith("*"))
			{
				if(entries[index].toLowerCase().substring(entries[index].indexOf('}')+1).endsWith(toSearch.toLowerCase().substring(1)))
				{
					filtered.add(entries[index]);
				}
			}
			else
			{
				if(entries[index].toLowerCase().substring(entries[index].indexOf('}')+1).contains(toSearch.toLowerCase()))
				{
					filtered.add(entries[index]);
				}
			}
			
		}
		String[] filteredArray = filtered.toArray(new String[0]);
		return filteredArray;
	}
	
	/**
	 * When a search request is supplied by the GUI, this method gets called first.
	 */
	private void searchEvent()
	{
		if(searchText.getText()=="")
		{
			asciiTable.setItemCount(asciiSections.length);
			for(int index=0;index<asciiSections.length;index++)
			{
				asciiTable.getItem(index).setText(asciiSections[index].substring(asciiSections[index].indexOf('}')+1));
				asciiTable.getItem(index).setData(Integer.parseInt(asciiSections[index].substring(1, asciiSections[index].indexOf('}'))));
			}
		}
		else
		{
			if(tableStore==null)
			{
				tableStore = tableItems;
			}
			String[] filtered = search(asciiSections, searchText.getText());
			asciiTable.setItemCount(filtered.length);
			for(int index=0;index<filtered.length;index++)
			{
				asciiTable.getItem(index).setText(filtered[index].substring(filtered[index].indexOf('}')+1));
				asciiTable.getItem(index).setData(Integer.parseInt(filtered[index].substring(1, filtered[index].indexOf('}'))));
			}
		}
	}
	
	/**
	 * Populate the memory text space. Split from main process to increase performance.
	 *
	 * @param text the GUI text field for the memory
	 * @param memoryIndex the memory index
	 * @param tableText the table text
	 * @return the new memory
	 */
	private String populateMemory(Text text, int memoryIndex, String tableText)
	{
		byte[] fullMemory = getBytes();
		memory = "";
		int startIndex;
		if(memoryIndex<memoryCount/2) startIndex = 0;
		else startIndex=memoryIndex-(memoryCount/2);
		for(int index = startIndex; index<startIndex+memoryCount; index++)
		{
			if(fullMemory[index]>0)
			{
				memory+=(char)fullMemory[index];
			}
			
		}
		text.setText(memory);
		System.out.println(memory);
		System.out.println("startindex: "+memory.indexOf(tableText));
		System.out.println("endindex: "+(memory.indexOf(tableText)+tableText.length()));
		text.setFocus();
		text.setSelection( new Point( text.getText().indexOf(tableText), text.getText().indexOf(tableText)+tableText.length() ) );
		return memory;
	}
	
	/**
	 * Check subclass.
	 */
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
