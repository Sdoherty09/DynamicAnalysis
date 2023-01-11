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

import java.io.File;
import java.nio.file.Files;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Window {

	protected Shell shell;
	private Table table;
	private Table table_1;
	private Table table_2;
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
		shell.setSize(881, 520);
		shell.setText("SWT Application");
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
					table_2.clearAll();
	                table_2.setItemCount(0);
	                for(int index=0;index<DLLs.length;index++)
	                {
	                	TableItem tableItem = new TableItem(table_2, SWT.NULL);
	                	tableItem.setText(DLLs[index]);
	                } 
				}
				catch(NullPointerException e1) {}
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
		lblCode.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblCode.setAlignment(SWT.RIGHT);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCode.setText("Code");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblDetails = new Label(shell, SWT.NONE);
		GridData gd_lblDetails = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_lblDetails.widthHint = 82;
		lblDetails.setLayoutData(gd_lblDetails);
		lblDetails.setText("Details");
		lblDetails.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDetails.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		
		Label lblDllImports = new Label(shell, SWT.NONE);
		lblDllImports.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblDllImports.setText("DLL Imports");
		lblDllImports.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDllImports.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 121;
		gd_table.heightHint = 313;
		table.setLayoutData(gd_table);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table_1.widthHint = 316;
		table_1.setLayoutData(gd_table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn.setWidth(160);
		tblclmnNewColumn.setText("Labels");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table_1, SWT.CENTER | SWT.V_SCROLL);
		tblclmnNewColumn_1.setWidth(170);
		tblclmnNewColumn_1.setText("Values");
		new Label(shell, SWT.NONE);
		
		table_2 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table_2.widthHint = 200;
		table_2.setLayoutData(gd_table_2);
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);
		
		for(int index = 0;index<tableItems.length;index++)
		{
			tableItems[index] = new TableItem(table_1, SWT.NONE);
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
                    try
                    {
                    	CodeExtract codeExtract = new CodeExtract(new File(filePath));
                        String[] codeArr = codeExtract.getCodeArr();
                        byte[] resources = codeExtract.getResources();
                        
                        tableItems[0].setText(1, filePath);
                        
                        if(codeExtract.getPeFile().isX32())
                        {
                        	tableItems[1].setText(1, "32-bit");
                        }
                        else
                        {
                        	tableItems[1].setText(1, "64-bit");
                        }
                        
                        table.clearAll();
                        table.setItemCount(0);
                        table_2.clearAll();
                        for(int index=0;index<codeArr.length;index++)
                        {
                        	TableItem tableItem = new TableItem(table, SWT.NULL);
                        	tableItem.setText(codeArr[index]);
                        }
                        
                    }
                    catch (NullPointerException e1)
                    {
                    	e1.printStackTrace();
                    }
            }
		});
	}
}