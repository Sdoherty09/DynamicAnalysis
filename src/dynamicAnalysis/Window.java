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

public class Window
{

	protected Shell shell;
	private Label text;
	private Table table;
	private String filePath;
	private SelectFile selectFile;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			Window window = new Window();
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
		shell.setSize(567, 295);
		shell.setText("Dynamic Malware Analyzer");
		shell.setLayout(new FormLayout());
		
		Button btnProcess = new Button(shell, SWT.CHECK);
		
		text = new Label(shell, SWT.BORDER);
		
		Button btnLaunch = new Button(shell, SWT.NONE);
		
		TableItem tableItems[] = new TableItem[4];
		
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 11);
		text.setLayoutData(fd_text);
		
		
		Button btnInstructions = new Button(shell, SWT.NONE);
		btnInstructions.setEnabled(false);
		btnInstructions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		FormData fd_btnInstructions = new FormData();
		fd_btnInstructions.top = new FormAttachment(0, 90);
		fd_btnInstructions.left = new FormAttachment(100, -157);
		fd_btnInstructions.right = new FormAttachment(100, -32);
		btnInstructions.setLayoutData(fd_btnInstructions);
		btnInstructions.setText("x86 Instructions");
		
		Button btnMemory = new Button(shell, SWT.NONE);
		btnMemory.setEnabled(false);
		fd_btnInstructions.bottom = new FormAttachment(btnMemory, -6);
		btnMemory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MemoryWindow memoryWindow = new MemoryWindow(Integer.parseInt(tableItems[3].getText(1)), shell.getLocation().x, shell.getLocation().y);
        		memoryWindow.open();
			}
		});
		btnMemory.setText("Virtual Memory");
		FormData fd_btnMemory = new FormData();
		fd_btnMemory.top = new FormAttachment(0, 121);
		fd_btnMemory.left = new FormAttachment(100, -157);
		fd_btnMemory.right = new FormAttachment(100, -32);
		btnMemory.setLayoutData(fd_btnMemory);
		
		Button btnAdvanced = new Button(shell, SWT.NONE);
		btnAdvanced.setEnabled(false);
		fd_btnMemory.bottom = new FormAttachment(btnAdvanced, -6);
		btnAdvanced.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAdvanced.setText("Advanced");
		FormData fd_btnAdvanced = new FormData();
		fd_btnAdvanced.left = new FormAttachment(100, -157);
		fd_btnAdvanced.right = new FormAttachment(100, -32);
		fd_btnAdvanced.top = new FormAttachment(0, 152);
		fd_btnAdvanced.bottom = new FormAttachment(100, -59);
		btnAdvanced.setLayoutData(fd_btnAdvanced);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(btnAdvanced, 0, SWT.BOTTOM);
		fd_table.top = new FormAttachment(btnAdvanced, -87);
		fd_table.right = new FormAttachment(0, 331);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		
		Button btnSelect = new Button(shell, SWT.NONE);
		fd_text.right = new FormAttachment(100, -249);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selection: "+btnProcess.getSelection());
				selectFile = new SelectFile(shell.getLocation().x, shell.getLocation().y, btnProcess.getSelection());
				filePath = selectFile.getText();	
				if(!selectFile.isPidMode())
				{
					try
					{
						text.setText(filePath);
						btnLaunch.setEnabled(true);
					}
					catch(IllegalArgumentException e1) 
					{
						btnLaunch.setEnabled(false);
					}
				}
				else
				{
					try
					{
						text.setText(Integer.toString(selectFile.getPid()));
						btnLaunch.setEnabled(true);
					}
					catch(IllegalArgumentException e1) 
					{
						btnLaunch.setEnabled(false);
					}
				}
			}
		});
		btnSelect.setText("Select File");
		FormData fd_btnSelect = new FormData();
		fd_btnSelect.top = new FormAttachment(text, -5, SWT.TOP);
		fd_btnSelect.left = new FormAttachment(text, 6);
		btnSelect.setLayoutData(fd_btnSelect);
		
		Label lblFilePath = new Label(shell, SWT.NONE);
		fd_text.left = new FormAttachment(lblFilePath, 6);
		lblFilePath.setAlignment(SWT.RIGHT);
		fd_table.left = new FormAttachment(0, 26);
		FormData fd_lblFilePath = new FormData();
		fd_lblFilePath.top = new FormAttachment(0, 11);
		fd_lblFilePath.left = new FormAttachment(table, 0, SWT.LEFT);
		fd_lblFilePath.right = new FormAttachment(100, -465);
		lblFilePath.setLayoutData(fd_lblFilePath);
		lblFilePath.setText("File Path");
		
		TableColumn labels = new TableColumn(table, SWT.NONE);
		labels.setWidth(100);
		
		TableColumn values = new TableColumn(table, SWT.CENTER | SWT.V_SCROLL);
		values.setWidth(200);

		
		for(int index = 0;index<tableItems.length;index++)
		{
			tableItems[index] = new TableItem(table, SWT.NONE);
		}
		
		tableItems[0].setText(0, "Directory");
		tableItems[1].setText(0, "Version");
		tableItems[2].setText(0, "Name");		
		tableItems[3].setText(0, "PID");
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
		
		fd_btnSelect.right = new FormAttachment(100, -142);
		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				btnLaunch.setEnabled(false);
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
		fd_btnProcess.top = new FormAttachment(text, 6);
		fd_btnProcess.left = new FormAttachment(btnSelect, 0, SWT.LEFT);
		fd_btnProcess.right = new FormAttachment(100, -129);
		btnProcess.setLayoutData(fd_btnProcess);
		btnProcess.setText("Process");
		
		
		fd_btnProcess.bottom = new FormAttachment(btnLaunch, 0, SWT.BOTTOM);
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
					tableItems[0].setText(1, filePath);
					if(codeExtract.getPeFile().isX32())
	                {
						tableItems[1].setText(1, "32-bit");
	                }
	                else
	                {
	                	tableItems[1].setText(1, "64-bit");
	                }  
					tableItems[2].setText(1, process.getName());
					tableItems[3].setText(1, process.getPidAsString());
				}
				else
				{
					btnInstructions.setEnabled(false);
					ProcessManager process = new ProcessManager(Integer.parseInt(text.getText()));
					tableItems[2].setText(1, process.getName());
					tableItems[3].setText(1, Integer.toString(selectFile.getPid()));
				}
			}
		});
		FormData fd_btnLaunch = new FormData();
		fd_btnLaunch.bottom = new FormAttachment(btnInstructions, -31);
		fd_btnLaunch.top = new FormAttachment(text, 6);
		fd_btnLaunch.left = new FormAttachment(text, 0, SWT.LEFT);
		fd_btnLaunch.right = new FormAttachment(100, -388);
		btnLaunch.setLayoutData(fd_btnLaunch);
		btnLaunch.setText("Launch");
		btnLaunch.setEnabled(false);
		
	}
}
