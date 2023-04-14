/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * Composite to display the files used by the process
 */
public class FilesComposite extends Composite
{
	
	/** The GUI table displaying DLL files. */
	private Table dllsTable;
	
	/** The GUI table displaying the files used by the process. */
	private Table filesTable;
	
	/** The process ID of the process. */
	private int processId;

	/**
	 * Create the composite.
	 *
	 * @param parent the parent that contains this composite
	 * @param style the SWT style
	 */
	public FilesComposite(Composite parent, int style)
	{
		super(parent, style);
		setProcessId(Window.processId);
		setLayout(new GridLayout(2, false));
		
		dllsTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_dllsTable = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_dllsTable.widthHint = 222;
		dllsTable.setLayoutData(gd_dllsTable);
		dllsTable.setHeaderVisible(true);
		dllsTable.setLinesVisible(true);
		
		filesTable = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		filesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		filesTable.setHeaderVisible(true);
		filesTable.setLinesVisible(true);
		
		ProcessManager processManager = new ProcessManager(getProcessId());
		String[] DLLs = processManager.getDLLs();
		for(int index=0;index<DLLs.length;index++)
		{
			TableItem tableItem = new TableItem(dllsTable, SWT.NULL);
			tableItem.setText(DLLs[index]);
		}
		String files[] = processManager.getFiles();
		System.out.println("files: "+files.length);
		for(int index=0;index<files.length;index++)
		{
			TableItem tableItem = new TableItem(filesTable, SWT.NULL);
			System.out.println("files: "+files[index]);
			tableItem.setText(files[index]);
		}
		
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
	 * Check subclass.
	 */
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
