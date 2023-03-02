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

public class FilesComposite extends Composite
{
	private Table dllsTable;
	private Table filesTable;
	private int processId;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
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

	
	
	public int getProcessId()
	{
		return processId;
	}

	public void setProcessId(int processId)
	{
		this.processId = processId;
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
