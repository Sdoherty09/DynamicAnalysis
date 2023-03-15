package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Table;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import capstone.Capstone;
import org.eclipse.swt.widgets.Button;

public class InstructionsComposite extends Composite
{
	private Table table;
	private Capstone.CsInsn[] allInsn;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InstructionsComposite(Composite parent, int style, File file)
	{
		super(parent, style);
		setLayout(new FormLayout());
		
		CodeExtract codeExtract = new CodeExtract(file);
		codeExtract.loadInstructions();
		allInsn = codeExtract.getAllInsn();
		setAllInsn(allInsn);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 238);
		fd_table.right = new FormAttachment(0, 313);
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setText("Address");
		
		TableColumn tblclmnMnemonic = new TableColumn(table, SWT.NONE);
		tblclmnMnemonic.setWidth(100);
		tblclmnMnemonic.setText("Mnemonic");
		
		TableColumn tblclmnOpcode = new TableColumn(table, SWT.NONE);
		tblclmnOpcode.setWidth(100);
		tblclmnOpcode.setText("Opcode");
		
		Button btnNewButton = new Button(this, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(table, 6);
		fd_btnNewButton.right = new FormAttachment(100, -186);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("New Button");

		fillTable();
		table.pack();
	}
	
	private void fillTable()
	{
		new Thread() {
            public void run() {
            	long start = System.currentTimeMillis();
            	Capstone.CsInsn[] allInsn = getAllInsn();
            	System.out.println("time for capstone load: "+(System.currentTimeMillis()-start));
            	TableItem[] tableItems = new TableItem[allInsn.length];
            	Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                    	
                		for(int index=0;index<100;index++)
                		{
                			tableItems[index] = new TableItem(table, SWT.NULL);
                			tableItems[index].setText(0, "0x"+Long.toHexString(allInsn[index].address));
                			tableItems[index].setText(1, allInsn[index].mnemonic);
                			tableItems[index].setText(2, allInsn[index].opStr);
                		}
                    }
                 });
            }
        }.start();
		
		
	}
	
	public Capstone.CsInsn[] getAllInsn()
	{
		return allInsn;
	}

	public void setAllInsn(Capstone.CsInsn[] allInsn)
	{
		this.allInsn = allInsn;
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
