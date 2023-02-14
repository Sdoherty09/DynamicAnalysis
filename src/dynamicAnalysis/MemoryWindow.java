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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MemoryWindow
{

	protected Shell shell;
	private int processId;
	private Text text;
	private Text txtLength;
	private byte[] bytes;
	private int x;
	private int y;
	private Table asciiTable;
	
	/*public static void main(String[] args)
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
		createContents();
		shell.open();
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


	private String updateMemory()
	{
		byte[] chars = readMemory();
		String output = "";
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
				MessageDialog.openError(shell, "Error", "Process was closed.");
				break;
			}
		}
		setBytes(chars);
		return output;
	}
	
	private String[] findAsciiSections()
	{
		ArrayList<String> asciiSections = new ArrayList<String>();
		String current="";
		for(int index=0;index<getBytes().length;index++)
		{
			if(asciiSections.size()>=6000)
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
		return asciiSections.toArray(new String[0]);
	}
	
	private boolean isAscii(char character)
	{
		return character>=32&&character<=126;
	}
	
	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(673, 432);
		shell.setLayout(new GridLayout(3, false));
		shell.setLocation(x+490/2, y+342/2);
		
		Button btnUpdate = new Button(shell, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMemory();
				txtLength.setText("Length: "+getBytes().length);
			}
		});
		btnUpdate.setText("Update");
		
		txtLength = new Text(shell, SWT.BORDER);
		txtLength.setText("Length: ");
		txtLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(shell, SWT.NONE);
		
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setFont(SWTResourceManager.getFont("Calibri", 9, SWT.NORMAL));
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_text.widthHint = 399;
		text.setLayoutData(gd_text);
		
		updateMemory();
		text.setText(updateMemory());
		txtLength.setText("Length: "+getBytes().length);
		
		asciiTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_asciiTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_asciiTable.widthHint = 186;
		asciiTable.setLayoutData(gd_asciiTable);
		asciiTable.setHeaderVisible(true);
		asciiTable.setLinesVisible(true);
		String[] asciiSections = findAsciiSections();
		TableItem[] tableItems = new TableItem[asciiSections.length];
		for(int index=0;index<asciiSections.length;index++)
		{
			tableItems[index] = new TableItem(asciiTable, SWT.NULL);
			tableItems[index].setText(asciiSections[index]);
		}
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		
		//VirtualMemory virtualMemory = new VirtualMemory(pr)
	}

}