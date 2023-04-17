/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.pcap4j.core.PcapNativeException;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * The main window that is loaded when the program is first run. Contains most functionality.
 */
public class Window
{

	/** The window's SWT shell. */
	protected Shell shlMaldive;
	
	/** The text field for either a file path or a process ID. */
	private Label text;
	
	/** The table to display information about a process. */
	private Table table;
	
	/** The file path. */
	private String filePath;
	
	/** The select file window. */
	private SelectFile selectFile;
	
	/** The process id. */
	public static int processId;
	
	/** The SWT display. */
	private Display display;
	
	/** The process manager for processes. */
	private ProcessManager process;
	
	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{
		Window window = null;
		try
		{
			window = new Window();
			window.open();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if(window.process!=null) window.process.destroyProcess();
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		display = Display.getDefault();
		createContents();
		shlMaldive.open();
		shlMaldive.layout();
		while (!shlMaldive.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		
	}
	
	/**
	 * Error alert.
	 *
	 * @param message the message
	 */
	private void errorAlert(String message)
	{
		MessageBox messageBox = new MessageBox(shlMaldive, SWT.ERROR);				        
        messageBox.setText("Error");
        messageBox.setMessage(message);
        messageBox.open();
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shlMaldive = new Shell();
		shlMaldive.setImage(new Image(display, System.getProperty("user.dir")+"\\lib\\Maldive.png"));
		shlMaldive.setSize(491, 573);
		shlMaldive.setText("Maldive");
		shlMaldive.setLayout(new FormLayout());
		shlMaldive.setMinimumSize(491, 573);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		shlMaldive.setLocation((dim.width/2)-200,(dim.height/2)-250);
		
		Button btnProcess = new Button(shlMaldive, SWT.CHECK);
		
		text = new Label(shlMaldive, SWT.BORDER);
		
		Button btnLaunch = new Button(shlMaldive, SWT.NONE);
		
		TableItem tableItems[] = new TableItem[4];
		
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 11);
		text.setLayoutData(fd_text);
		
		
		Button btnInstructions = new Button(shlMaldive, SWT.NONE);
		btnInstructions.setEnabled(false);
		
		FormData fd_btnInstructions = new FormData();
		btnInstructions.setLayoutData(fd_btnInstructions);
		btnInstructions.setText("x86 Instructions");
		
		Button btnMemory = new Button(shlMaldive, SWT.NONE);
		fd_btnInstructions.bottom = new FormAttachment(btnMemory, -6);
		btnMemory.setEnabled(false);
		
		btnMemory.setText("Virtual Memory");
		FormData fd_btnMemory = new FormData();
		fd_btnInstructions.bottom = new FormAttachment(btnMemory, -6);
		
		btnMemory.setLayoutData(fd_btnMemory);
		
		Button btnFiles = new Button(shlMaldive, SWT.NONE);
		fd_btnMemory.bottom = new FormAttachment(btnFiles, -6);
		btnFiles.setEnabled(false);
		btnFiles.setText("Files");
		FormData fd_btnFiles = new FormData();
		fd_btnFiles.bottom = new FormAttachment(0, 178);
		fd_btnFiles.top = new FormAttachment(0, 152);
		
		btnFiles.setLayoutData(fd_btnFiles);		
		table = new Table(shlMaldive, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL);
		fd_btnFiles.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnMemory.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnInstructions.right = new FormAttachment(table, 101, SWT.RIGHT);
		fd_btnInstructions.left = new FormAttachment(table, 6);
		fd_btnMemory.left = new FormAttachment(table, 6);
		fd_btnFiles.left = new FormAttachment(table, 6);
		fd_btnMemory.left = new FormAttachment(table, 6);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 177);
		fd_table.top = new FormAttachment(0, 90);
		fd_table.right = new FormAttachment(0, 331);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		
		Button btnSelect = new Button(shlMaldive, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selection: "+btnProcess.getSelection());
				selectFile = new SelectFile(shlMaldive.getLocation().x, shlMaldive.getLocation().y, btnProcess.getSelection());
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
		fd_btnSelect.right = new FormAttachment(btnInstructions, 0, SWT.RIGHT);
		fd_btnSelect.bottom = new FormAttachment(btnInstructions, -58);
		fd_btnSelect.left = new FormAttachment(text, 5, SWT.RIGHT);
		btnSelect.setLayoutData(fd_btnSelect);
		
		Label lblFilePath = new Label(shlMaldive, SWT.NONE);
		fd_text.right = new FormAttachment(table, 0, SWT.RIGHT);
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
		Menu menu = new Menu(shlMaldive, SWT.BAR);
		shlMaldive.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	selectFile = new SelectFile(shlMaldive.getLocation().x, shlMaldive.getLocation().y, btnProcess.getSelection());
            	filePath = selectFile.getText();
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
		});
		
		MenuItem mntmProcess = new MenuItem(menu, SWT.CASCADE);
		mntmProcess.setText("Process");
		
		Menu menu_2 = new Menu(mntmProcess);
		mntmProcess.setMenu(menu_2);
		
		MenuItem mntmDestroyProcess = new MenuItem(menu_2, SWT.NONE);
		mntmDestroyProcess.setText("Destroy Process");
		
		MenuItem mntmNetwork = new MenuItem(menu, SWT.NONE);
		mntmNetwork.setText("Network");
		
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
		fd_btnProcess.right = new FormAttachment(btnLaunch, 130);
		fd_btnProcess.left = new FormAttachment(btnLaunch, 10);
		fd_btnProcess.top = new FormAttachment(btnSelect, 6);
		btnProcess.setLayoutData(fd_btnProcess);
		btnProcess.setText("Process");
		btnLaunch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItems[0].setText(1, "");
				tableItems[1].setText(1, "");
				tableItems[2].setText(1, "");		
				tableItems[3].setText(1, "");
				btnMemory.setEnabled(true);
				btnFiles.setEnabled(true);
				tableItems[0].setGrayed(btnProcess.getSelection());
				tableItems[1].setGrayed(btnProcess.getSelection());
				btnInstructions.setEnabled(!btnProcess.getSelection());
				if(!btnProcess.getSelection())
				{
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
						e1.printStackTrace();
						errorAlert("Admin privileges are required to run the process.");
					}
					catch(ArrayIndexOutOfBoundsException e1)
					{
						e1.printStackTrace();
						errorAlert("Could not open the selected process.");
					}
				}
				else
				{
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
		
		CTabFolder tabFolder = new CTabFolder(shlMaldive, SWT.BORDER);
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
		
		btnFiles.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tbtmAdvanced = new CTabItem(tabFolder, SWT.CLOSE);
				tbtmAdvanced.setText("Files");
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
		
		mntmNetwork.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	CTabItem tbtmNetwork = new CTabItem(tabFolder, SWT.CLOSE);
            	try
            	{
            		processId = Integer.parseInt(tableItems[3].getText(1));
            	} catch(NumberFormatException e1)
            	{
            		processId = 0;
            	}
            	
            	tbtmNetwork.setText("Network");
            	try
				{
					NetworkComposite networkComposite = new NetworkComposite(tabFolder, SWT.NULL, processId);
					tbtmNetwork.setControl(networkComposite);
					tabFolder.setSelection(tbtmNetwork);
				} catch (PcapNativeException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
		});
		
		shlMaldive.addListener (SWT.Resize,  new Listener () {
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