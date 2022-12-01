package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import capstone.*;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

public class Window {

	protected Shell shell;
	public static byte [] CODE = { 0x55, 0x48, (byte) 0x8b, 0x05, (byte) 0xb8,
		    0x13, 0x00, 0x00 };
	private Table table;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_64);
	    Capstone.CsInsn[] allInsn = cs.disasm(CODE, 0x1000);
	    for (int i=0; i<allInsn.length; i++) {
	      System.out.printf("0x%x:\t%s\t%s\n", allInsn[i].address,
	          allInsn[i].mnemonic, allInsn[i].opStr);
	  } 
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
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.NONE);
		mntmEdit.setText("Edit");
		
		MenuItem mntmView = new MenuItem(menu, SWT.NONE);
		mntmView.setText("View");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		
		StyledText styledText = new StyledText(shell, SWT.BORDER);
		styledText.setBounds(304, 50, 117, 152);
		
		Label lblRegistry = new Label(shell, SWT.NONE);
		lblRegistry.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblRegistry.setBounds(326, 23, 65, 21);
		lblRegistry.setText("Registry");
		
		StyledText styledText_1 = new StyledText(shell, SWT.BORDER);
		styledText_1.setBounds(123, 50, 175, 152);
		
		Label lblCode = new Label(shell, SWT.NONE);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblCode.setBounds(192, 23, 39, 21);
		lblCode.setText("Code");
		
		TableViewer tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setBounds(10, 10, 93, 218);

	}
}
