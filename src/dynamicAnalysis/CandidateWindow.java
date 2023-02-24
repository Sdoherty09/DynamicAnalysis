package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class CandidateWindow
{

	protected Shell shell;
	private Label text;
	private String filePath;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			CandidateWindow window = new CandidateWindow();
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
		shell.setSize(324, 249);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		Button btnProcess = new Button(shell, SWT.CHECK);
		
		text = new Label(shell, SWT.BORDER);
		
		Button btnLaunch = new Button(shell, SWT.NONE);
		
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 28);
		text.setLayoutData(fd_text);
		
		
		Button btnInstructions = new Button(shell, SWT.NONE);
		btnInstructions.setEnabled(false);
		btnInstructions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		FormData fd_btnInstructions = new FormData();
		fd_btnInstructions.left = new FormAttachment(text, 120, SWT.LEFT);
		fd_btnInstructions.right = new FormAttachment(text, 0, SWT.RIGHT);
		btnInstructions.setLayoutData(fd_btnInstructions);
		btnInstructions.setText("x86 Instructions");
		
		Button btnMemory = new Button(shell, SWT.NONE);
		fd_btnInstructions.top = new FormAttachment(0, 117);
		btnMemory.setEnabled(false);
		btnMemory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnMemory.setText("Virtual Memory");
		FormData fd_btnMemory = new FormData();
		fd_btnMemory.bottom = new FormAttachment(btnInstructions, -6);
		fd_btnMemory.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_btnMemory.left = new FormAttachment(0, 203);
		btnMemory.setLayoutData(fd_btnMemory);
		
		Button btnAdvanced = new Button(shell, SWT.NONE);
		fd_btnInstructions.bottom = new FormAttachment(btnAdvanced, -6);
		btnAdvanced.setEnabled(false);
		btnAdvanced.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAdvanced.setText("Advanced");
		FormData fd_btnAdvanced = new FormData();
		fd_btnAdvanced.right = new FormAttachment(0, 298);
		fd_btnAdvanced.top = new FormAttachment(0, 148);
		fd_btnAdvanced.left = new FormAttachment(0, 203);
		btnAdvanced.setLayoutData(fd_btnAdvanced);
		
		Button btnSelect = new Button(shell, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selection: "+btnProcess.getSelection());
				SelectFile selectFile = new SelectFile(shell.getLocation().x, shell.getLocation().y, btnProcess.getSelection());
				filePath = selectFile.getText();
				btnLaunch.setEnabled(true);
				try
				{
					text.setText(filePath);
				}
				catch(IllegalArgumentException e1) {}
			}
		});
		btnSelect.setText("Select File");
		FormData fd_btnSelect = new FormData();
		fd_btnSelect.top = new FormAttachment(text, 6);
		fd_btnSelect.right = new FormAttachment(btnMemory, 0, SWT.RIGHT);
		fd_btnSelect.left = new FormAttachment(btnMemory, 0, SWT.LEFT);
		btnSelect.setLayoutData(fd_btnSelect);
		
		Label lblFilePath = new Label(shell, SWT.NONE);
		fd_text.top = new FormAttachment(lblFilePath, -1, SWT.TOP);
		fd_text.right = new FormAttachment(lblFilePath, 221, SWT.RIGHT);
		fd_text.left = new FormAttachment(lblFilePath, 6);
		lblFilePath.setAlignment(SWT.RIGHT);
		FormData fd_lblFilePath = new FormData();
		fd_lblFilePath.top = new FormAttachment(0, 12);
		fd_lblFilePath.left = new FormAttachment(0, 10);
		fd_lblFilePath.right = new FormAttachment(0, 77);
		lblFilePath.setLayoutData(fd_lblFilePath);
		lblFilePath.setText("File Path");

		/*TableItem tableItems[] = new TableItem[4];
		
		tableItems[0].setText(0, "Directory");
		tableItems[1].setText(0, "Version");
		tableItems[2].setText(0, "Name");		
		tableItems[3].setText(0, "PID");*/
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		
		MenuItem mntmProcess = new MenuItem(menu, SWT.CASCADE);
		mntmProcess.setText("Process");
		
		Menu menu_2 = new Menu(mntmProcess);
		mntmProcess.setMenu(menu_2);
		
		MenuItem mntmSelectProcess = new MenuItem(menu_2, SWT.NONE);
		mntmSelectProcess.setText("Select Process");
		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnProcess.getSelection())
				{
					lblFilePath.setText("Process ID");
					btnSelect.setText("Select Process");
				}
				else
				{
					lblFilePath.setText("File Path");
					btnSelect.setText("Select File");
				}
			}
		});
		FormData fd_btnProcess = new FormData();
		fd_btnProcess.top = new FormAttachment(btnSelect, 4, SWT.TOP);
		fd_btnProcess.right = new FormAttachment(btnSelect, -53);
		fd_btnProcess.left = new FormAttachment(text, 0, SWT.LEFT);
		btnProcess.setLayoutData(fd_btnProcess);
		btnProcess.setText("Process");
		btnLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnMemory.setEnabled(true);
				btnAdvanced.setEnabled(true);
				if(!btnProcess.getSelection())
				{
					btnInstructions.setEnabled(true);
					String filePath = text.getText();
					ProcessManager process = new ProcessManager(new File(filePath));
					CodeExtract codeExtract = new CodeExtract(new File(filePath));				
					//tableItems[0].setText(1, filePath);
					if(codeExtract.getPeFile().isX32())
	                {
						//tableItems[1].setText(1, "32-bit");
	                }
	                else
	                {
	                	//tableItems[1].setText(1, "64-bit");
	                }  
					//tableItems[2].setText(1, process.getName());
					//tableItems[3].setText(1, process.getPidAsString());
				}
				else
				{
					btnInstructions.setEnabled(false);
				}
			}
		});
		FormData fd_btnLaunch = new FormData();
		fd_btnLaunch.right = new FormAttachment(100, -345);
		fd_btnLaunch.left = new FormAttachment(0, 135);
		fd_btnLaunch.top = new FormAttachment(0, 34);
		fd_btnLaunch.bottom = new FormAttachment(100, -245);
		btnLaunch.setLayoutData(fd_btnLaunch);
		btnLaunch.setText("Launch");
		
	}
}
