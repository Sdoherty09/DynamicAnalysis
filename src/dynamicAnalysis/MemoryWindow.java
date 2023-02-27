package dynamicAnalysis;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ProgressBar;

public class MemoryWindow
{

	protected Shell shell;
	private int processId;
	private Text text;
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
	
	/*
	public static void main(String[] args)
	{
		MemoryWindow memoryWindow = new MemoryWindow();
		memoryWindow.open();
	}*/
	public MemoryWindow(int processId, int x, int y)
	{
		try
		{
			setProcessId(processId);
			setX(x);
			setY(y);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public int getProcessId()
	{
		return processId;
	}

	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}


	public void setY(int y)
	{
		this.y = y;
	}

	public void open()
	{
		Display display = Display.getDefault();
		createBaseContents();
		shell.open();
		createContents();		
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	private byte[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		return virtualMemory.readMemory();
	}
	
	
	
	public byte[] getBytes()
	{
		return bytes;
	}


	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}


	private String updateMemory(Text text)
	{
		long start = System.currentTimeMillis();
		byte[] chars = readMemory();
		String output = "";
		String update = "";
		int sizeIndex = 0;
		int index = 0;
		while(sizeIndex!=10000)
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
		long start = System.currentTimeMillis();
		ArrayList<String> asciiSections = new ArrayList<String>();
		String current="";
		for(int index=0;index<getBytes().length;index++)
		{
			while(isAscii((char)getBytes()[index]))
			{
				current+=(char)getBytes()[index];
				index++;
			}
			if(current.length()>8)
			{
				asciiSections.add(current);
			}
			current="";
		}
		System.out.println("ascii time: "+(System.currentTimeMillis()-start));
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
				if(entries[index].toLowerCase().startsWith(toSearch.toLowerCase().substring(0, toSearch.length()-1)))
				{
					filtered.add(entries[index]);
				}
			}
			else if(toSearch.startsWith("*"))
			{
				if(entries[index].toLowerCase().endsWith(toSearch.toLowerCase().substring(1)))
				{
					filtered.add(entries[index]);
				}
			}
			else
			{
				if(entries[index].toLowerCase().contains(toSearch.toLowerCase()))
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
			asciiTable.setItemCount(0);
			for(int index=0;index<asciiSections.length;index++)
			{
				if(index>6000)
				{
					break;
				}
				tableItems = new TableItem[asciiSections.length];
				tableItems[index]=new TableItem(asciiTable, SWT.NULL);
				tableItems[index].setText(asciiSections[index]);
			}
		}
		else
		{
			if(tableStore==null)
			{
				tableStore = tableItems;
			}
			String[] filtered = search(asciiSections, searchText.getText());
			asciiTable.setItemCount(0);
			tableItems = new TableItem[filtered.length];
			for(int index=0;index<filtered.length;index++)
			{
				tableItems[index]=new TableItem(asciiTable, SWT.NULL);
				tableItems[index].setText(filtered[index]);
			}
		}
	}
	
	protected void createBaseContents()
	{
		shell = new Shell();
		shell.setSize(865, 432);
		shell.setLayout(new GridLayout(5, false));
		shell.setLocation(x+490/2, y+342/2);
		
		btnUpdate = new Button(shell, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMemory(text);
				txtLength.setText("Length: "+getBytes().length);
			}
		});
		btnUpdate.setText("Update");
		txtLength = new Label(shell, SWT.BORDER);
		txtLength.setText("Length: ");
		txtLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		progressBar = new ProgressBar(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setFont(SWTResourceManager.getFont("Calibri", 9, SWT.NORMAL));
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.widthHint = 399;
		text.setLayoutData(gd_text);
		
		asciiTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		gd_asciiTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_asciiTable.widthHint = 186;
		asciiTable.setLayoutData(gd_asciiTable);
		asciiTable.setHeaderVisible(true);
		asciiTable.setLinesVisible(true);
		
		searchText = new Text(shell, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		searchText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){	
				if(e.keyCode == SWT.CR) {
					searchEvent();	
				}	
			}	
		});
		
		
		btnNewButton = new Button(shell, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnNewButton.widthHint = 62;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("Search");
	}
	/*
	private void updateBar(ProgressBar progresBar, int selection)
	{
		progressBar.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                progressBar.setSelection(selection);
            }});
	}*/
	
	protected synchronized void createContents()
	{
		progressBar.setMaximum(100);
		progressBar.setVisible(true);
		long start = System.currentTimeMillis();
		text.setText(updateMemory(text));
		progressBar.setSelection(33);
		System.out.println("set memory text time: "+(System.currentTimeMillis()-start));
		txtLength.setText("Length: "+getBytes().length);		
		progressBar.setSelection(66);
		asciiSections = findAsciiSections();
		tableItems = new TableItem[asciiSections.length];
		start = System.currentTimeMillis();
		for(int index=0;index<asciiSections.length;index++)
		{
			if(index>6000)
			{
				break;
			}
			tableItems[index] = new TableItem(asciiTable, SWT.NULL);
			tableItems[index].setText(asciiSections[index]);
		}
		System.out.println("set table time: "+(System.currentTimeMillis()-start));
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchEvent();
			}
		});
		
		
		
		progressBar.setVisible(false);
	}

}