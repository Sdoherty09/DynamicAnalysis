/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

/**
 * The Class SelectFile. Displays a window to choose a file from. Can also select a process.
 */
public class SelectFile
{

	/** The SWT shell for the window. */
	protected Shell shell;
	
	/** The text field for a file or process ID. */
	private Text text;
	
	/** The file path. */
	private String filePath;
	
	/** The X value to position the window horizontally. */
	private int x;
	
	/** The Y value to position the window vertically. */
	private int y;
	
	/** Determines whether the window was opened to read a file or a process. */
	private boolean pidMode;
	
	/** The unique process identifier. */
	private int pid;
	
	/**
	 * Launch the file selection window.
	 *
	 * @param x the X value to position at
	 * @param y the Y value to position at
	 * @param pidMode whether to read a file or a process
	 */
	public SelectFile(int x, int y, boolean pidMode)
	{
		setX(x);
		setY(y);
		setPidMode(pidMode);
		open();
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
			try
			{
				if (!display.readAndDispatch())
				{
					display.sleep();
				}
			}
			catch(IllegalArgumentException e) {}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		System.out.println(getX());
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(192, 192, 192));
		shell.setSize(400, 180);
		
		if(!isPidMode()) shell.setText("Choose a file");
		else shell.setText("Choose a Process");
		
		shell.setLocation(getX()+200,getY()+90);
		
		text = new Text(shell, SWT.BORDER);
		text.setBackground(SWTResourceManager.getColor(255, 255, 255));
		text.setBounds(69, 38, 250, 25);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!isPidMode())
				{
					filePath = text.getText();
					shell.dispose();
				}
				else
				{
					setPid(Integer.parseInt(text.getText()));
					System.out.println("pid: "+getPid());
					shell.dispose();
				}
			}
		});
		btnOk.setBounds(315, 105, 46, 25);
		btnOk.setText("OK");
		
		Button btnSelectFile = new Button(shell, SWT.NONE);
		btnSelectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!isPidMode())
				{
					FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
	            	String[] files = {
	                        "*.exe",
	                    };
	                    fileDialog.setFilterExtensions(files);
	                    text.setText(fileDialog.open());
				}
				else
				{
					CommandLine commandLine = new CommandLine();
					String output = commandLine.getAll();
					int breakCount = 0;
					
					for(int index = 0;index<output.length();index++)
					{
						if(output.charAt(index)=='\n')
						{
							breakCount++;
						}
					}
					
					String[] names = new String[breakCount-2];
					int[] pids = new int[breakCount-2];
					String[] memory = new String[breakCount-2];
					int outputIndex = output.indexOf('\n');
					int arrayIndex = 0;
					while(outputIndex <= output.length())
					{
						try
						{
							names[arrayIndex]=output.substring(outputIndex+2, output.indexOf('\"', outputIndex+2));
							pids[arrayIndex]=Integer.parseInt(output.substring(output.indexOf(',', outputIndex)+2, output.indexOf('\"', output.indexOf(',', outputIndex)+2))); //stuck here
							memory[arrayIndex]=output.substring(output.lastIndexOf("\"", output.indexOf("\n", outputIndex+1)-2)+1, output.lastIndexOf("\"", output.indexOf("\n", outputIndex+1)));
							outputIndex=output.indexOf('\n', outputIndex+1);
						}
						catch(ArrayIndexOutOfBoundsException e1)
						{
							break;
						}
						arrayIndex++;
					}
					SelectProcess selectProcess = new SelectProcess(names, pids, memory, shell.getLocation().x, shell.getLocation().y);
					selectProcess.open();
					if(selectProcess.getPid() == 0) text.setText("");
					else text.setText(Integer.toString(selectProcess.getPid()));
				}
			}
		});
		btnSelectFile.setBounds(235, 105, 74, 25);
		btnSelectFile.setText("Select File");
		
		Label lblFileLocation = new Label(shell, SWT.NONE);
		lblFileLocation.setBackground(SWTResourceManager.getColor(192, 192, 192));
		lblFileLocation.setBounds(161, 17, 67, 15);
		lblFileLocation.setText("File Location");
		
		if(isPidMode())
		{
			setPidMode(true);
			lblFileLocation.setText("Process ID");
			//btnEnterPid.setText("Enter File Location");
			btnSelectFile.setText("Select Process");
			//btnEnterPid.setBounds(116, 105, 103, 25);
			btnSelectFile.setBounds(223, 105, 86, 25);
			
		}
		else
		{
			setPidMode(false);
			lblFileLocation.setText("File Location");
			//btnEnterPid.setText("Enter PID");
			btnSelectFile.setText("Select File");
			lblFileLocation.setBounds(161, 17, 67, 15);
			btnSelectFile.setBounds(235, 105, 74, 25);
			//btnEnterPid.setBounds(162, 105, 67, 25);
			
		}
		
		Button btnEnterPid = new Button(shell, SWT.NONE);
		btnEnterPid.setVisible(false);
		btnEnterPid.setSelection(true);
		btnEnterPid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!isPidMode())
				{
					setPidMode(true);
					lblFileLocation.setText("Process ID");
					btnEnterPid.setText("Enter File Location");
					btnSelectFile.setText("Select Process");
					btnEnterPid.setBounds(116, 105, 103, 25);
					btnSelectFile.setBounds(223, 105, 86, 25);
					
				}
				else
				{
					setPidMode(false);
					lblFileLocation.setText("File Location");
					btnEnterPid.setText("Enter PID");
					btnSelectFile.setText("Select File");
					lblFileLocation.setBounds(161, 17, 67, 15);
					btnSelectFile.setBounds(235, 105, 74, 25);
					btnEnterPid.setBounds(162, 105, 67, 25);
					
				}
			}
		});
		btnEnterPid.setBounds(162, 105, 67, 25);
		btnEnterPid.setText("Enter PID");

	}
	
	/**
	 * Gets the X value.
	 *
	 * @return the X value
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Sets the X value.
	 *
	 * @param x the new X value
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Gets the Y value.
	 *
	 * @return the Y value
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Sets the Y value.
	 *
	 * @param y the new Y value
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * Checks if the window should read a process or a file.
	 *
	 * @return true, if is in pid mode
	 */
	public boolean isPidMode()
	{
		return pidMode;
	}

	/**
	 * Sets the pid mode.
	 *
	 * @param pidMode the new pid mode
	 */
	public void setPidMode(boolean pidMode)
	{
		this.pidMode = pidMode;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getText()
	{
		return filePath;
	}

	/**
	 * Gets the PID.
	 *
	 * @return the PID
	 */
	public int getPid()
	{
		return pid;
	}

	/**
	 * Sets the PID.
	 *
	 * @param pid the new PID
	 */
	public void setPid(int pid)
	{
		this.pid = pid;
	}
	
	/**
	 * Checks if the window is disposed.
	 *
	 * @return true, if window is disposed
	 */
	public boolean isDisposed()
	{
		return shell.isDisposed();
	}
	
	/**
	 * Force focus on the file selection window.
	 */
	public void focus()
	{
		shell.forceFocus();
	}
}


