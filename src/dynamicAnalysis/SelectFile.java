package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectFile
{

	protected Shell shell;
	private Text text;
	private String filePath;
	/**
	 * Launch the application.
	 * @param args
	 */
	public SelectFile()
	{
		try
		{
			open();
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
		shell.setBackground(SWTResourceManager.getColor(192, 192, 192));
		shell.setSize(400, 180);
		shell.setText("Choose a file");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		shell.setLocation((dim.width/2)-400,(dim.height/2)-200);
		
		text = new Text(shell, SWT.BORDER);
		text.setBackground(SWTResourceManager.getColor(255, 255, 255));
		text.setBounds(69, 38, 250, 25);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filePath = text.getText();
				shell.dispose();
			}
		});
		btnOk.setBounds(315, 105, 46, 25);
		btnOk.setText("OK");
		
		Button btnSelectFile = new Button(shell, SWT.NONE);
		btnSelectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filePath;
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
            	String[] files = {
                        "*.exe",
                    };
                    fileDialog.setFilterExtensions(files);
                    text.setText(fileDialog.open());
			}
		});
		btnSelectFile.setBounds(234, 105, 75, 25);
		btnSelectFile.setText("Select File");

	}
	
	public String getText()
	{
		return filePath;
	}
}


