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

public class SelectFile
{

	protected Shell shell;
	private Text text;
	private String filePath;
	private int x;
	private int y;
	private boolean pidMode = false;
	private int pid;
	/**
	 * Launch the application.
	 * @param args
	 */
	public SelectFile(int x, int y)
	{
		try
		{
			setX(x);
			setY(y);
			open();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
		shell.setText("Choose a file");
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
					int outputIndex = output.indexOf('\n');
					int arrayIndex = 0;
					while(outputIndex <= output.length())
					{
						try
						{
							names[arrayIndex]=output.substring(outputIndex+2, output.indexOf('\"', outputIndex+2));
							pids[arrayIndex]=Integer.parseInt(output.substring(output.indexOf(',', outputIndex)+2, output.indexOf('\"', output.indexOf(',', outputIndex)+2))); //stuck here
							outputIndex=output.indexOf('\n', outputIndex+1);
						}
						catch(ArrayIndexOutOfBoundsException e1)
						{
							break;
						}
						arrayIndex++;
					}
					SelectProcess selectProcess = new SelectProcess(names, pids, shell.getLocation().x, shell.getLocation().y);
					selectProcess.open();
					text.setText(Integer.toString(selectProcess.getPid()));
				}
			}
		});
		btnSelectFile.setBounds(235, 105, 74, 25);
		btnSelectFile.setText("Select File");
		
		Label lblFileLocation = new Label(shell, SWT.NONE);
		lblFileLocation.setBackground(SWTResourceManager.getColor(192, 192, 192));
		lblFileLocation.setBounds(161, 17, 67, 15);
		lblFileLocation.setText("File Location");
		
		Button btnEnterPid = new Button(shell, SWT.NONE);
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
	
	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	
	public boolean isPidMode()
	{
		return pidMode;
	}

	public void setPidMode(boolean pidMode)
	{
		this.pidMode = pidMode;
	}

	public String getText()
	{
		return filePath;
	}

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}
	
}


