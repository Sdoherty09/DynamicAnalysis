package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class MemoryComposite extends Composite
{
	private Text text;
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

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MemoryComposite(Composite parent, int style)
	{
		
		super(parent, style);
		setLayout(new FormLayout());
		
		Button btnUpdate = new Button(this, SWT.NONE);
		FormData fd_btnUpdate = new FormData();
		fd_btnUpdate.top = new FormAttachment(0, 10);
		fd_btnUpdate.left = new FormAttachment(0, 10);
		btnUpdate.setLayoutData(fd_btnUpdate);
		btnUpdate.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		btnUpdate.setText("Update");
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMemory(text);
				txtLength.setText("Length: "+getBytes().length);
			}
		});
		
		Label txtLength = new Label(this, SWT.BORDER);
		FormData fd_txtLength = new FormData();
		fd_txtLength.right = new FormAttachment(0, 245);
		fd_txtLength.top = new FormAttachment(0, 14);
		fd_txtLength.left = new FormAttachment(0, 66);
		txtLength.setLayoutData(fd_txtLength);
		txtLength.setText("Length: ");
		
		text = new Text(this, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 254);
		fd_text.right = new FormAttachment(0, 245);
		fd_text.top = new FormAttachment(0, 41);
		fd_text.left = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);
		text.setText(updateMemory(text));
		
		asciiTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_asciiTable = new FormData();
		fd_asciiTable.bottom = new FormAttachment(0, 254);
		fd_asciiTable.right = new FormAttachment(0, 396);
		fd_asciiTable.top = new FormAttachment(0, 41);
		fd_asciiTable.left = new FormAttachment(0, 251);
		asciiTable.setLayoutData(fd_asciiTable);
		asciiTable.setHeaderVisible(true);
		asciiTable.setLinesVisible(true);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.right = new FormAttachment(0, 396);
		fd_btnNewButton.top = new FormAttachment(0, 10);
		fd_btnNewButton.left = new FormAttachment(0, 346);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Search");
		
		searchText = new Text(this, SWT.BORDER);
		FormData fd_searchText = new FormData();
		fd_searchText.bottom = new FormAttachment(0, 31);
		fd_searchText.right = new FormAttachment(0, 340);
		fd_searchText.top = new FormAttachment(0, 14);
		fd_searchText.left = new FormAttachment(0, 251);
		searchText.setLayoutData(fd_searchText);
		searchText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){	
				if(e.keyCode == SWT.CR) {
					searchEvent();	
				}	
			}	
		});
		
		
		txtLength.setText("Length: "+getBytes().length);
		asciiSections = findAsciiSections();
		tableItems = new TableItem[asciiSections.length];
		for(int index=0;index<asciiSections.length;index++)
		{
			if(index>6000)
			{
				break;
			}
			tableItems[index] = new TableItem(asciiTable, SWT.NULL);
			tableItems[index].setText(asciiSections[index]);
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
			if(asciiSections.size()>100000)
			{
				break;
			}
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
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
