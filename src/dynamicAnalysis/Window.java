package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;


import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Window {

	protected Shell shell;
	private Table code;
	private Table details;
	private Table dllImports;
	private String filePath;
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(192, 192, 192));
		shell.setSize(881, 520);
		shell.setText("SWT Application");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		shell.setLocation((dim.width/2)-400,(dim.height/2)-200);
		
		GridLayout gl_shell = new GridLayout(9, false);
		gl_shell.marginBottom = 15;
		shell.setLayout(gl_shell);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile_1 = new MenuItem(menu, SWT.CASCADE);
		mntmFile_1.setText("File");
		
		Menu menu_1 = new Menu(mntmFile_1);
		mntmFile_1.setMenu(menu_1);
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.NONE);
		mntmEdit.setText("Edit");
		
		MenuItem mntmView = new MenuItem(menu, SWT.NONE);
		mntmView.setText("View");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		TableItem tableItems[] = new TableItem[4];

		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");		
		
		MenuItem mntmRun = new MenuItem(menu, SWT.NONE);
		mntmRun.setText("Run");
		mntmRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try
				{
					ProcessManager process = new ProcessManager(new File(filePath));
					tableItems[2].setText(1, process.getName());
					tableItems[3].setText(1, process.getPidAsString());
					String[] DLLs = process.getDLLs();
					dllImports.clearAll();
	                dllImports.setItemCount(0);
	                for(int index=0;index<DLLs.length;index++)
	                {
	                	TableItem tableItem = new TableItem(dllImports, SWT.NULL);
	                	tableItem.setText(DLLs[index]);
	                } 
				}
				catch(NullPointerException e1) 
				{
					System.out.println("No file selected");
				}
			}
		});
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblCode = new Label(shell, SWT.NONE);
		lblCode.setBackground(SWTResourceManager.getColor(192, 192, 192));
		lblCode.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblCode.setAlignment(SWT.RIGHT);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCode.setText("Code");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblDetails = new Label(shell, SWT.NONE);
		lblDetails.setBackground(SWTResourceManager.getColor(192, 192, 192));
		GridData gd_lblDetails = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_lblDetails.widthHint = 82;
		lblDetails.setLayoutData(gd_lblDetails);
		lblDetails.setText("Details");
		lblDetails.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDetails.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		
		Label lblDllImports = new Label(shell, SWT.NONE);
		lblDllImports.setBackground(SWTResourceManager.getColor(192, 192, 192));
		lblDllImports.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblDllImports.setText("DLL Imports");
		lblDllImports.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDllImports.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		code = tableViewer.getTable();
		code.setToolTipText("x86 instructions");
		code.setBackground(SWTResourceManager.getColor(192, 192, 192));
		code.setHeaderBackground(SWTResourceManager.getColor(192, 192, 192));
		GridData gd_code = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_code.widthHint = 121;
		gd_code.heightHint = 313;
		code.setLayoutData(gd_code);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		details = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		details.setToolTipText("File information");
		details.setHeaderBackground(SWTResourceManager.getColor(192, 192, 192));
		details.setBackground(SWTResourceManager.getColor(192, 192, 192));
		GridData gd_details = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_details.widthHint = 316;
		details.setLayoutData(gd_details);
		details.setHeaderVisible(true);
		details.pack();
		
		TableColumn labels = new TableColumn(details, SWT.CENTER);
		labels.setWidth(160);
		labels.setText("Labels");
		
		TableColumn values = new TableColumn(details, SWT.CENTER | SWT.V_SCROLL);
		values.setWidth(170);
		values.setText("Values");
		new Label(shell, SWT.NONE);
		
		dllImports = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		dllImports.setToolTipText("DLL file paths");
		dllImports.setHeaderBackground(SWTResourceManager.getColor(192, 192, 192));
		dllImports.setBackground(SWTResourceManager.getColor(192, 192, 192));
		GridData gd_dllImports = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_dllImports.widthHint = 200;
		dllImports.setLayoutData(gd_dllImports);
		new Label(shell, SWT.NONE);
		
		for(int index = 0;index<tableItems.length;index++)
		{
			tableItems[index] = new TableItem(details, SWT.NONE);
		}
		tableItems[0].setText(0, "Directory");
		tableItems[1].setText(0, "Version");
		tableItems[2].setText(0, "Name");		
		tableItems[3].setText(0, "PID");
		
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
            	String[] files = {
                        "*.exe",
                    };
                    fileDialog.setFilterExtensions(files);
                    filePath = fileDialog.open();
                    tableItems[0].setText(1, filePath);
                   /* new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {*/
                        	try
                            {
                            	CodeExtract codeExtract = new CodeExtract(new File(filePath));
                                String[] codeArr = codeExtract.getCodeArr();
                               // byte[] resources = codeExtract.getResources();
                                
                                
                                
                                if(codeExtract.getPeFile().isX32())
                                {
                                	tableItems[1].setText(1, "32-bit");
                                }
                                else
                                {
                                	tableItems[1].setText(1, "64-bit");
                                }
                                
                                code.clearAll();
                                code.setItemCount(0);
                                dllImports.clearAll();
                                for(int index=0;index<codeArr.length;index++)
                                {
                                	TableItem tableItem = new TableItem(code, SWT.NULL);
                                	tableItem.setText(codeArr[index]);
                                }
                                
                            }
                            catch (NullPointerException e1)
                            {
                            	System.out.println("Explorer closed");
                            }
                       /* }
                    }).start();*/
            }
		});
	}
}