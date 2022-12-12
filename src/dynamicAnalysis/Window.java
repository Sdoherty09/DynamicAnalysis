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

import java.io.File;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class Window {

	protected Shell shell;
	public static byte [] CODE = { 0x55, 0x48, (byte) 0x8b, 0x05, (byte) 0xb8,
		    0x13, 0x00, 0x00 };
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Open the window.
	 */
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
		shell.setSize(673, 482);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(3, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile_1 = new MenuItem(menu, SWT.CASCADE);
		mntmFile_1.setText("FIle");
		
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
		
		Label lblCode = new Label(shell, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCode.setAlignment(SWT.RIGHT);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		lblCode.setText("Code");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1);
		gd_text.widthHint = 194;
		gd_text.heightHint = 350;
		text.setLayoutData(gd_text);

		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		
		mntmOpen.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
            	String[] files = {
                        "*.exe",
                    };
                    fileDialog.setFilterExtensions(files);
                    String filePath = fileDialog.open();
                    CodeExtract codeExtract = new CodeExtract(new File(filePath));
                    text.setText(codeExtract.getCode());
                    
            }
		});
	}
}