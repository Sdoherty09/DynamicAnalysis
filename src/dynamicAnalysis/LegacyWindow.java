/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import javax.sound.sampled.*;

/**
 * The initial window used by the program. Now unused, included for demonstration purposes only.
 */
public class LegacyWindow {

	/** The shell. */
	protected Shell shell;
	
	/** The code. */
	private Table code;
	
	/** The details. */
	private Table details;
	
	/** The dll imports. */
	private Table dllImports;
	
	/** The file path. */
	private String filePath;
	
	/** The table items. */
	static TableItem tableItems[] = new TableItem[4];
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			LegacyWindow window = new LegacyWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Open.
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

	/** The sample rate. */
	public static float SAMPLE_RATE = 8000f;
	
	/**
	 * Tone.
	 *
	 * @param hz the hz
	 * @param msecs the msecs
	 * @throws LineUnavailableException the line unavailable exception
	 */
	public void tone(int hz, int msecs) throws LineUnavailableException 
	  {
	     tone(hz, msecs, 1.0);
	  }

	  /**
  	 * Tone.
  	 *
  	 * @param hz the hz
  	 * @param msecs the msecs
  	 * @param vol the vol
  	 * @throws LineUnavailableException the line unavailable exception
  	 */
  	public static void tone(int hz, int msecs, double vol)
	      throws LineUnavailableException 
	  {
	    byte[] buf = new byte[1];
	    AudioFormat af = 
	        new AudioFormat(
	            SAMPLE_RATE, // sampleRate
	            8,           // sampleSizeInBits
	            1,           // channels
	            true,        // signed
	            false);      // bigEndian
	    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	    sdl.open(af);
	    sdl.start();
	    for (int i=0; i < msecs*8; i++) {
	      double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
	      buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
	      sdl.write(buf,0,1);
	    }
	    sdl.drain();
	    sdl.stop();
	    sdl.close();
	  }
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(192, 192, 192));
		shell.setSize(881, 520);
		shell.setText("Dynamic Malware Analysis");
		
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
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");
		
		Menu menu_2 = new Menu(mntmView);
		mntmView.setMenu(menu_2);
		
		MenuItem mntmVirtualMemory = new MenuItem(menu_2, SWT.NONE);
		mntmVirtualMemory.setText("Virtual Memory");
		
		mntmVirtualMemory.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	System.out.println(tableItems[3].getText(1));
            	try
            	{
            		MemoryWindow memoryWindow = new MemoryWindow(Integer.parseInt(tableItems[3].getText(1)), shell.getLocation().x, shell.getLocation().y);
            		memoryWindow.open();
            	}
            	catch(NumberFormatException e1)
            	{
            		
            	}
            }
		});
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
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
            	SelectFile selectFile = new SelectFile(shell.getLocation().x, shell.getLocation().y, true);
            	code.clearAll();
            	if(!selectFile.isPidMode())
            	{
            		filePath = selectFile.getText();
                	try
                	{
                		tableItems[0].setText(1, filePath);
                    	try
                        {
                        	CodeExtract codeExtract = new CodeExtract(new File(filePath));
                            String[] codeArr = codeExtract.getCodeArr();
                           // byte[] resources = codeExtract.getResources();
                            if(codeExtract.getPeFile().getVersion() == Version.x32)
                            {
                            	tableItems[1].setText(1, "32-bit");
                            }
                            else
                            {
                            	tableItems[1].setText(1, "64-bit");
                            }               
                            code.setItemCount(0);
                            dllImports.clearAll();
                            for(int index=0;index<codeArr.length;index++)
                            {
                            	TableItem tableItem = new TableItem(code, SWT.NULL);
                            	tableItem.setText(codeArr[index]);
                            }
                            tableItems[0].setGrayed(false);
        	                tableItems[1].setGrayed(false);
                        }
                        catch (NullPointerException e1) {}
                	}
                    catch (IllegalArgumentException e1) {}
            	}
            	else
            	{
            		ProcessManager process = new ProcessManager(selectFile.getPid());
					tableItems[2].setText(1, process.getName());
					tableItems[3].setText(1, Integer.toString(selectFile.getPid()));
					String[] DLLs = process.getDLLs();
					dllImports.clearAll();
	                dllImports.setItemCount(0);
	                for(int index=0;index<DLLs.length;index++)
	                {
	                	TableItem tableItem = new TableItem(dllImports, SWT.NULL);
	                	tableItem.setText(DLLs[index]);
	                } 
	                tableItems[0].setGrayed(true);
	                tableItems[1].setGrayed(true);
            	}
               /* }
            }).start();*/
            }
		});
	}
}