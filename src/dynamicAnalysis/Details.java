package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

public class Details extends Composite
{
	private Table table;
	private TableItem tableItems[] = new TableItem[4];
	private boolean selection;
	private Button btnInstructions = new Button(this, SWT.NONE);
	private Button btnMemory = new Button(this, SWT.NONE);
	private Button btnAdvanced = new Button(this, SWT.NONE);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Details(Composite parent, int style, boolean selection)
	{
		super(parent, style);
		setLayout(new FormLayout());
		
		
		FormData fd_btnInstructions = new FormData();
		fd_btnInstructions.right = new FormAttachment(100, -27);
		fd_btnInstructions.bottom = new FormAttachment(table, 25, SWT.TOP);
		fd_btnInstructions.top = new FormAttachment(table, 0, SWT.TOP);
		btnInstructions.setLayoutData(fd_btnInstructions);
		btnInstructions.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnInstructions.setText("x86 Instructions");
		
		
		FormData fd_btnMemory = new FormData();
		fd_btnMemory.right = new FormAttachment(100, -27);
		fd_btnMemory.left = new FormAttachment(0, 191);
		fd_btnMemory.top = new FormAttachment(btnInstructions, 10);

		btnMemory.setLayoutData(fd_btnMemory);
		btnMemory.setText("Virtual Memory");
		
		
		FormData fd_btnAdvanced = new FormData();
		
		fd_btnAdvanced.left = new FormAttachment(0, 191);
		fd_btnAdvanced.top = new FormAttachment(table, -25, SWT.BOTTOM);
		fd_btnAdvanced.bottom = new FormAttachment(table, 0, SWT.BOTTOM);
		btnAdvanced.setLayoutData(fd_btnAdvanced);
		btnAdvanced.setText("Advanced");
		
		table = new Table(this, SWT.FULL_SELECTION | SWT.NO_SCROLL);
		fd_btnAdvanced.bottom = new FormAttachment(table, 0, SWT.BOTTOM);
		fd_btnAdvanced.top = new FormAttachment(btnMemory, 10, SWT.BOTTOM);
		FormData fd_table = new FormData();
		fd_table.left = new FormAttachment(btnInstructions, -191, SWT.LEFT);
		fd_table.right = new FormAttachment(50, 20);
		fd_table.top = new FormAttachment(0);
		fd_table.bottom = new FormAttachment(100, -20);
		table.setLayoutData(fd_table);
		table.setLinesVisible(true);
		table.setHeaderVisible(false);
		
		fd_btnAdvanced.top = new FormAttachment(table, -25, SWT.BOTTOM);
		fd_btnAdvanced.right = new FormAttachment(btnInstructions, 0, SWT.RIGHT);
		fd_btnMemory.right = new FormAttachment(btnInstructions, 0, SWT.RIGHT);
		fd_btnInstructions.right = new FormAttachment(table, 100, SWT.RIGHT);
		fd_btnInstructions.left = new FormAttachment(table, 5, SWT.RIGHT);
		
		TableColumn labels = new TableColumn(table, SWT.NONE);
		labels.setWidth(100);
		
		TableColumn values = new TableColumn(table, SWT.CENTER | SWT.V_SCROLL);
		values.setWidth(150);

		
		
		for(int index = 0;index<tableItems.length;index++)
		{
			tableItems[index] = new TableItem(table, SWT.NONE);
		}
		
		tableItems[0].setText(0, "Directory");
		tableItems[1].setText(0, "Version");
		tableItems[2].setText(0, "Name");		
		tableItems[3].setText(0, "PID");

	}
	
	public void clearData()
	{
		tableItems[0].setText(1, "");
		tableItems[1].setText(1, "");
		tableItems[2].setText(1, "");		
		tableItems[3].setText(1, "");
		btnMemory.setEnabled(true);
		btnAdvanced.setEnabled(true);
		tableItems[0].setGrayed(isSelection());
		tableItems[1].setGrayed(isSelection());
		btnInstructions.setEnabled(!isSelection());
	}
	
	public boolean isSelection()
	{
		return selection;
	}

	public void setSelection(boolean selection)
	{
		this.selection = selection;
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
