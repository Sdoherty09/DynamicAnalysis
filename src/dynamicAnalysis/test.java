/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * The Class test.
 */
public class test
{

	/** The shell. */
	protected Shell shell;
	
	/** The process id. */
	private int processId;
	
	/** The text. */
	private Text text;
	
	/** The txt length. */
	private Text txtLength;

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			test window = new test();
			window.open();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(490, 342);
		shell.setLayout(new GridLayout(3, false));
		
		Button btnUpdate = new Button(shell, SWT.NONE);
		btnUpdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		btnUpdate.setText("Update");
		
		txtLength = new Text(shell, SWT.BORDER);
		txtLength.setText("Length: ");
		txtLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setFont(SWTResourceManager.getFont("Calibri", 9, SWT.NORMAL));
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(shell, SWT.NONE);

		new Label(shell, SWT.NONE);

	}

}
