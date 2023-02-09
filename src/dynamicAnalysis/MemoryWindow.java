package dynamicAnalysis;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import javax.swing.JTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MemoryWindow
{

	protected Shell shell;
	private int processId;
	private Table table;
	private Text text;
	public MemoryWindow(int processId)
	{
		try
		{
			setProcessId(processId);
			open();
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
	
	private String updateMemory()
	{
		byte[] chars = readMemory();
		String output = "";
		int sizeIndex = 0;
		int index = 0;
		while(sizeIndex!=25000)
		{
			if(chars[index]!=0)
			{
				output += (char)chars[index];
				sizeIndex++;
			}
			index++;
		}
		return output;
	}
	
	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(490, 342);
		shell.setLayout(new GridLayout(2, false));
		
		Button btnUpdate = new Button(shell, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMemory();
			}
		});
		btnUpdate.setText("Update");
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setFont(SWTResourceManager.getFont("Calibri", 9, SWT.NORMAL));
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		text.setText(updateMemory());
		new Label(shell, SWT.NONE);
		
		
		//VirtualMemory virtualMemory = new VirtualMemory(pr)
	}

}