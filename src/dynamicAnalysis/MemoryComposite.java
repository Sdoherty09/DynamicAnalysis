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

public class MemoryComposite extends Composite
{
	private Text memoryField;
	private int processId;
	private Label txtLength;
	private byte[] bytes;
	private int x;
	private int y;
	private Table asciiTable;
	private Button btnNewButton;
	private Text searchText;
	private TableItem[] tableStore = null;
	private TableItem[] tableItems;
	private Button btnUpdate;
	private GridData gd_asciiTable;
	private ProgressBar progressBar;
	private String[] asciiSections;
	private Color red;
	private String memory;
	private final int memoryCount = 10000;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
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

	public int getProcessId()
	{
		return processId;
	}

	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	private byte[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		setBytes(virtualMemory.readMemory());
		return getBytes();
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public Color getRed() {
		return red;
	}

	public void setRed(Color red) {
		this.red = red;
	}

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
	
	private boolean isAscii(char character)
	{
		return character>=32&&character<=126;
	}
	
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
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
