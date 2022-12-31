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

public class Window {

	protected Shell shell;
	private Table table;
	private Table table_1;
	private Table table_2;
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
		shell.setSize(1045, 735);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(18, false));
		
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
		new Label(shell, SWT.NONE);
		
		Label lblCode = new Label(shell, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCode.setAlignment(SWT.RIGHT);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCode.setText("Code");

		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblDetails = new Label(shell, SWT.NONE);
		GridData gd_lblDetails = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblDetails.widthHint = 82;
		lblDetails.setLayoutData(gd_lblDetails);
		lblDetails.setText("Details");
		lblDetails.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDetails.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblDllImports = new Label(shell, SWT.NONE);
		lblDllImports.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblDllImports.setText("DLL Imports");
		lblDllImports.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblDllImports.setAlignment(SWT.CENTER);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_table.widthHint = 121;
		gd_table.heightHint = 349;
		table.setLayoutData(gd_table);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_table_1.widthHint = 223;
		table_1.setLayoutData(gd_table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn.setWidth(120);
		tblclmnNewColumn.setText("Labels");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(120);
		tblclmnNewColumn_1.setText("Values");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		table_2 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);
		
		
		
		TableItem tableItems[] = new TableItem[2];
		for(int index = 0;index<tableItems.length;index++)
		{
			tableItems[index] = new TableItem(table_1, SWT.NONE);
		}
		tableItems[0].setText(0, "Name");
		tableItems[1].setText(0, "PID");
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
            	String[] files = {
                        "*.exe",
                    };
                    fileDialog.setFilterExtensions(files);
                    String filePath = fileDialog.open();
                    CodeExtract codeExtract = new CodeExtract(new File(filePath));
                    String[] codeArr = codeExtract.getCodeArr();
                    byte[] resources = codeExtract.getResources();
                    ProcessManager process = new ProcessManager(new File(filePath));
                    tableItems[0].setText(1, process.getName());
                    tableItems[1].setText(1, process.getPidAsString());
                    String[] DLLs = process.getDLLs();
                    table.clearAll();
                    table.setItemCount(0);
                    for(int index=0;index<codeArr.length;index++)
                    {
                    	TableItem tableItem = new TableItem(table, SWT.NULL);
                    	tableItem.setText(codeArr[index]);
                    }
                    table_2.clearAll();
                    table_2.setItemCount(0);
                    for(int index=0;index<DLLs.length;index++)
                    {
                    	TableItem tableItem = new TableItem(table_2, SWT.NULL);
                    	tableItem.setText(DLLs[index]);
                    } 
            }
		});
	}
}