package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class Window
{

	protected Shell shell;
	private Label text;
	private Table table;
	private String filePath;
	private SelectFile selectFile;
	public static int processId;
	private Display display;
	private ProcessManager process;
	
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
		display = Display.getDefault();
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
		shell.setSize(491, 573);
		shell.setText("Dynamic Malware Analyzer");
		shell.setLayout(new FormLayout());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		shell.setLocation((dim.width/2)-200,(dim.height/2)-250);
		
		Button btnProcess = new Button(shell, SWT.CHECK);
		
		text = new Label(shell, SWT.BORDER);
		
		Button btnLaunch = new Button(shell, SWT.NONE);
		
		TableItem tableItems[] = new TableItem[4];
		
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 11);
		text.setLayoutData(fd_text);
		
		
		Button btnInstructions = new Button(shell, SWT.NONE);
		btnInstructions.setEnabled(false);
		
		FormData fd_btnInstructions = new FormData();
		btnInstructions.setLayoutData(fd_btnInstructions);
		btnInstructions.setText("x86 Instructions");
		
		Button btnMemory = new Button(shell, SWT.NONE);
		fd_btnInstructions.bottom = new FormAttachment(btnMemory, -6);
		btnMemory.setEnabled(false);
		
		btnMemory.setText("Virtual Memory");
		FormData fd_btnMemory = new FormData();
		fd_btnInstructions.bottom = new FormAttachment(btnMemory, -6);
		
		btnMemory.setLayoutData(fd_btnMemory);
		
		Button btnAdvanced = new Button(shell, SWT.NONE);
		fd_btnMemory.bottom = new FormAttachment(btnAdvanced, -6);
		btnAdvanced.setEnabled(false);
		btnAdvanced.setText("Advanced");
		FormData fd_btnAdvanced = new FormData();
		fd_btnAdvanced.bottom = new FormAttachment(0, 178);
		fd_btnAdvanced.top = new FormAttachment(0, 152);
		
		btnAdvanced.setLayoutData(fd_btnAdvanced);		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		fd_btnAdvanced.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnMemory.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnInstructions.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnInstructions.left = new FormAttachment(table, 6);
		fd_btnMemory.left = new FormAttachment(table, 6);
		fd_btnAdvanced.left = new FormAttachment(table, 6);
		fd_btnMemory.left = new FormAttachment(table, 6);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 177);
		fd_table.top = new FormAttachment(0, 90);
		fd_table.right = new FormAttachment(0, 331);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		
		Button btnSelect = new Button(shell, SWT.NONE);
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
		fd_btnSelect.right = new FormAttachment(0, 367);
		fd_btnSelect.bottom = new FormAttachment(btnInstructions, -58);
		fd_btnSelect.left = new FormAttachment(0, 271);
		btnSelect.setLayoutData(fd_btnSelect);
		
		Label lblFilePath = new Label(shell, SWT.NONE);
		fd_text.right = new FormAttachment(lblFilePath, 181, SWT.RIGHT);
		fd_text.left = new FormAttachment(lblFilePath, 13);
		lblFilePath.setAlignment(SWT.RIGHT);
		fd_table.left = new FormAttachment(0, 26);
		FormData fd_lblFilePath = new FormData();
		fd_lblFilePath.right = new FormAttachment(0, 79);
		fd_lblFilePath.left = new FormAttachment(0, 26);
		fd_lblFilePath.top = new FormAttachment(0, 11);
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
		
		MenuItem mntmDestroyProcess = new MenuItem(menu_2, SWT.NONE);
		mntmDestroyProcess.setText("Destroy Process");
		
		mntmDestroyProcess.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	if(process!=null) process.destroyProcess();
            }
		});
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
		fd_btnProcess.right = new FormAttachment(100, -129);
		fd_btnProcess.left = new FormAttachment(btnLaunch, 54);
		fd_btnProcess.top = new FormAttachment(btnSelect, 6);
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
					try
					{
						process = new ProcessManager(new File(filePath));
						CodeExtract codeExtract = new CodeExtract(new File(filePath));				
						tableItems[0].setText(1, filePath);
						if(codeExtract.getPeFile().getVersion() == Version.x32)
		                {
							tableItems[1].setText(1, "32-bit");
		                }
		                else
		                {
		                	tableItems[1].setText(1, "64-bit");
		                }  
						tableItems[2].setText(1, process.getName());
						tableItems[3].setText(1, process.getPidAsString());
						System.out.println("version: "+codeExtract.getPeFile().getVersion());
					}
					catch(NullPointerException e1)
					{
						MessageBox messageBox = new MessageBox(shell, SWT.ERROR);				        
				        messageBox.setText("Error");
				        messageBox.setMessage("Admin privileges are required to run the process.");
				        messageBox.open();
					}
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
		fd_btnLaunch.top = new FormAttachment(btnProcess, -4, SWT.TOP);
		fd_btnLaunch.right = new FormAttachment(lblFilePath, 66, SWT.RIGHT);
		fd_btnLaunch.left = new FormAttachment(text, 0, SWT.LEFT);
		btnLaunch.setLayoutData(fd_btnLaunch);
		btnLaunch.setText("Launch");
		btnLaunch.setEnabled(false);
		
		CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.right = new FormAttachment(100, -45);
		fd_tabFolder.top = new FormAttachment(table, 30);
		fd_tabFolder.bottom = new FormAttachment(100, -23);
		fd_tabFolder.left = new FormAttachment(table, 0, SWT.LEFT);
		tabFolder.setLayoutData(fd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		
		btnMemory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
        		CTabItem tbtmMemory = new CTabItem(tabFolder, SWT.CLOSE);
        		tbtmMemory.setText("Memory");
        		processId = Integer.parseInt(tableItems[3].getText(1));
        		Color red = display.getSystemColor(SWT.COLOR_RED);
        		MemoryComposite memoryComposite = new MemoryComposite(tabFolder, SWT.NULL, red);
        		memoryComposite.layout();
        		memoryComposite.setFocus();
        		System.out.println(memoryComposite.getProcessId());
        		tbtmMemory.setControl(memoryComposite);
        		tabFolder.setSelection(tbtmMemory);
			}
		});
		
		btnAdvanced.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tbtmAdvanced = new CTabItem(tabFolder, SWT.CLOSE);
				tbtmAdvanced.setText("Advanced");
        		processId = Integer.parseInt(tableItems[3].getText(1));
        		FilesComposite filesComposite = new FilesComposite(tabFolder, SWT.NULL);
        		filesComposite.layout();
        		tbtmAdvanced.setControl(filesComposite);
        		tabFolder.setSelection(tbtmAdvanced);
			}
		});
		
		btnInstructions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tbtmInstructions = new CTabItem(tabFolder, SWT.CLOSE);
				tbtmInstructions.setText("Instructions");
        		InstructionsComposite instructionsComposite = new InstructionsComposite(tabFolder, SWT.NULL, new File(filePath));
        		instructionsComposite.layout();
        		tbtmInstructions.setControl(instructionsComposite);
        		tabFolder.setSelection(tbtmInstructions);
			}
		});
		
		shell.addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	Control[] composites = tabFolder.getChildren();
		    	for(int index = 0; index < composites.length; index++)
		    	{
		    		composites[index].requestLayout();
		    		System.out.println("modified "+index);
		    	}
		    }
		  });
	}
}

//TODO: Implement import and export by automatic logging
