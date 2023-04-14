/*
 * 
 */
package dynamicAnalysis;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

/**
 * The Class SelectFile.
 */
public class SelectFile
{

	/** The shell. */
	protected Shell shell;
	
	/** The text. */
	private Text text;
	
	/** The file path. */
	private String filePath;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
	
	/** The pid mode. */
	private boolean pidMode;
	
	/** The pid. */
	private int pid;
	
	/**
	 * Launch the application.
	 *
	 * @param x the x
	 * @param y the y
	 * @param pidMode the pid mode
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
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
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
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * Checks if is pid mode.
	 *
	 * @return true, if is pid mode
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
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText()
	{
		return filePath;
	}

	/**
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public int getPid()
	{
		return pid;
	}

	/**
	 * Sets the pid.
	 *
	 * @param pid the new pid
	 */
	public void setPid(int pid)
	{
		this.pid = pid;
	}
	
	/**
	 * Checks if is disposed.
	 *
	 * @return true, if is disposed
	 */
	public boolean isDisposed()
	{
		return shell.isDisposed();
	}
	
	/**
	 * Focus.
	 */
	public void focus()
	{
		shell.forceFocus();
	}
}


