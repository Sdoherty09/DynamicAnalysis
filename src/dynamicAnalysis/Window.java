package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;


import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import java.io.File;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class Window {

	protected Shell shell;
	private Table table;
	
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
		shell.setLayout(new GridLayout(4, false));
		
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
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_table.heightHint = 571;
		gd_table.widthHint = 243;
		table.setLayoutData(gd_table);
		
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
                    codeExtract.execute(resources[0]);
                    for(int index=0;index<codeArr.length;index++)
                    {
                    	TableItem tableItem = new TableItem(table, SWT.NULL);
                    	tableItem.setText(codeArr[index]);
                    }  
            }
		});
	}
}