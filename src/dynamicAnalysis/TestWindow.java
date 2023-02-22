package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StyledText;

public class TestWindow
{

	protected Shell shell;
	private StyledText text;
	private StyledText text_1;
	private Table table;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			TestWindow window = new TestWindow();
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
		shell.setSize(422, 295);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		text = new StyledText(shell, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 28);
		fd_text.left = new FormAttachment(0, 86);
		fd_text.right = new FormAttachment(0, 171);
		text.setLayoutData(fd_text);
		
		text_1 = new StyledText(shell, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(0, 58);
		fd_text_1.left = new FormAttachment(0, 86);
		fd_text_1.right = new FormAttachment(0, 171);
		text_1.setLayoutData(fd_text_1);
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.top = new FormAttachment(0, 56);
		fd_btnNewButton_1.left = new FormAttachment(0, 176);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
		btnNewButton_1.setText("Select Process");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		FormData fd_btnNewButton_2 = new FormData();
		fd_btnNewButton_2.right = new FormAttachment(100, -33);
		fd_btnNewButton_2.top = new FormAttachment(0, 90);
		btnNewButton_2.setLayoutData(fd_btnNewButton_2);
		btnNewButton_2.setText("x86 Instructions");
		
		Button btnNewButton_2_1 = new Button(shell, SWT.NONE);
		fd_btnNewButton_2.bottom = new FormAttachment(100, -132);
		btnNewButton_2_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_2_1.setText("Virtual Memory");
		FormData fd_btnNewButton_2_1 = new FormData();
		fd_btnNewButton_2_1.top = new FormAttachment(btnNewButton_2, 18);
		fd_btnNewButton_2_1.right = new FormAttachment(100, -33);
		btnNewButton_2_1.setLayoutData(fd_btnNewButton_2_1);
		
		Button btnNewButton_2_1_1 = new Button(shell, SWT.NONE);
		fd_btnNewButton_2_1.bottom = new FormAttachment(btnNewButton_2_1_1, -19);
		btnNewButton_2_1_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_2_1_1.setText("Advanced");
		FormData fd_btnNewButton_2_1_1 = new FormData();
		fd_btnNewButton_2_1_1.right = new FormAttachment(100, -33);
		fd_btnNewButton_2_1_1.bottom = new FormAttachment(100, -27);
		fd_btnNewButton_2_1_1.top = new FormAttachment(0, 195);
		btnNewButton_2_1_1.setLayoutData(fd_btnNewButton_2_1_1);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		fd_btnNewButton_2_1_1.left = new FormAttachment(table, 6);
		fd_btnNewButton_2_1.left = new FormAttachment(table, 6);
		fd_btnNewButton_2.left = new FormAttachment(table, 6);
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(btnNewButton_1, 0, SWT.RIGHT);
		fd_table.top = new FormAttachment(0, 87);
		fd_table.bottom = new FormAttachment(100, -26);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		
		Button btnNewButton_1_1 = new Button(shell, SWT.NONE);
		btnNewButton_1_1.setText("Select File");
		FormData fd_btnNewButton_1_1 = new FormData();
		fd_btnNewButton_1_1.right = new FormAttachment(btnNewButton_1, 0, SWT.RIGHT);
		fd_btnNewButton_1_1.top = new FormAttachment(text, -2, SWT.TOP);
		fd_btnNewButton_1_1.left = new FormAttachment(text, 6);
		btnNewButton_1_1.setLayoutData(fd_btnNewButton_1_1);
		
		Label lblFilePath = new Label(shell, SWT.NONE);
		FormData fd_lblFilePath = new FormData();
		fd_lblFilePath.top = new FormAttachment(text, 0, SWT.TOP);
		fd_lblFilePath.left = new FormAttachment(table, 0, SWT.LEFT);
		lblFilePath.setLayoutData(fd_lblFilePath);
		lblFilePath.setText("File Path");
		
		Label lblProcessId = new Label(shell, SWT.NONE);
		fd_table.left = new FormAttachment(lblProcessId, 0, SWT.LEFT);
		FormData fd_lblProcessId = new FormData();
		fd_lblProcessId.bottom = new FormAttachment(text_1, 0, SWT.BOTTOM);
		fd_lblProcessId.right = new FormAttachment(text_1, -6);
		lblProcessId.setLayoutData(fd_lblProcessId);
		lblProcessId.setText("Process ID");

	}
}
