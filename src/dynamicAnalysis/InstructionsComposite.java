package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import capstone.Capstone;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;

public class InstructionsComposite extends Composite
{
	private Table table;
	private Capstone.CsInsn[] allInsn;
	private Text textOpcode;
	private TableColumn tblclmnAddress;
	private TableColumn tblclmnMnemonic;
	private TableColumn tblclmnOpcode;
	private byte[] updatedInstructions;
	private Composite parent;
	private TableItem[] tableItems = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InstructionsComposite(Composite parent, int style, File file)
	{
		super(parent, style);
		this.parent = parent;
		setLayout(new FormLayout());
		
		CodeExtract codeExtract = new CodeExtract(file);
		codeExtract.loadInstructions();
		allInsn = codeExtract.getAllInsn();
		setAllInsn(allInsn);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		fd_table.right = new FormAttachment(0, 353);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(getAllInsn().length);
		
		Text comboMnemonic = new Text(this, SWT.BORDER);
		
		table.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	Capstone.CsInsn instruction = getAllInsn()[table.getSelectionIndex()];
		    	if(tableItems!=null) clearColors(tableItems);
		    	tableItems = null;
		    	if(instruction.groups.length == 2)
		    	{
		    		if(instruction.groups[0] == 7 && instruction.groups[1] == 1) //group for jump instructions, jmp works differently ?
		    		{
		    			long entryIndex = table.getSelectionIndex();
		    			long operandIndex = instruction.bytes[1] + entryIndex;
		    			boolean lowerOperand = instruction.bytes[1] < 0;
		    			System.out.println("lower operand: "+lowerOperand);
		    			System.out.println("entry index" + entryIndex);
		    			System.out.println("operand index " + operandIndex );
	    				if(lowerOperand) entryIndex--;
	    				else entryIndex++;
	    				System.out.println("instruction addr: "+Long.decode(instruction.opStr));
	    				tableItems = new TableItem[Math.abs(instruction.bytes[1])];
	    				int tableIndex = 0;
	    				while(Long.decode(instruction.opStr) != ((Capstone.CsInsn)(table.getItem((int) entryIndex).getData())).address)
	    				{
	    					System.out.println(entryIndex);
	    					try
	    					{
	    						tableItems = setGray(table.getItem((int) entryIndex), tableItems, tableIndex);
		    					tableIndex++;
	    					}
	    					catch(ArrayIndexOutOfBoundsException e1)
	    					{
	    						e1.printStackTrace();
	    						clearColors(tableItems);
	    					}
	    					if(lowerOperand) entryIndex--;
	    					else entryIndex++;
	    				}    				
	    				tableItems = setGreen(table.getItem((int) entryIndex), tableItems, tableIndex);
		    		}
		    	}
	    	  	TableItem item = table.getSelection()[0];
				System.out.println(item.getText());
				//comboMnemonic.setText(Integer.toString(instruction.bytes[0] & 0xff));
				comboMnemonic.setText(Byte.toString(instruction.bytes[0]));
				String opcode = "";
				for(int index = 1;index<instruction.bytes.length;index++)
				{
					//opcode+=Integer.toString(instruction.bytes[index] & 0xff)+" ";
					opcode+=Byte.toString(instruction.bytes[index])+" ";
				}
				if(opcode.length()!=0) textOpcode.setText(opcode.substring(0, opcode.length()-1));
				else textOpcode.setText(opcode);
		    	  
		      }
		});
		
		tblclmnAddress = new TableColumn(table, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setText("Address");
		
		tblclmnMnemonic = new TableColumn(table, SWT.NONE);
		tblclmnMnemonic.setWidth(100);
		tblclmnMnemonic.setText("Mnemonic");
		
		tblclmnOpcode = new TableColumn(table, SWT.NONE);
		tblclmnOpcode.setWidth(5000);	
		tblclmnOpcode.setText("Opcode");
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				byte[] bytes = null;
				try
				{
					bytes = codeExtract.getBytes();
				} catch (IOException e2)
				{
					e2.printStackTrace();
				}
				int bytesIndex = codeExtract.getPointer();
				for(int index=0;index<table.getItemCount();index++)
				{
					Capstone.CsInsn instruction = (Capstone.CsInsn) table.getItem(index).getData();
					for(int j = 0; j < instruction.bytes.length; j++)
					{
						bytes[bytesIndex] = instruction.bytes[j];
						bytesIndex++;
					}
				}
				FileDialog fileDialog = new FileDialog(parent.getShell(), SWT.MULTI);
            	String[] files = {
                        "*.exe",
                    };
                    fileDialog.setFilterExtensions(files);
                    fileDialog.setFilterPath(file.getPath());
                    fileDialog.setFileName(codeExtract.getFile().getName().substring(0, codeExtract.getFile().getName().length()-4)+"_1.exe");
				try (FileOutputStream fos = new FileOutputStream(fileDialog.open())) {
					   fos.write(bytes);
					} catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
			}
		});
		fd_table.bottom = new FormAttachment(100, -41);
		fd_table.right = new FormAttachment(btnSave, -20);
		FormData fd_btnSave = new FormData();
		fd_btnSave.right = new FormAttachment(100, -10);
		fd_btnSave.top = new FormAttachment(0, 10);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");

		fillTable();
		table.pack();
		
		
		FormData fd_comboMnemonic = new FormData();
		fd_comboMnemonic.top = new FormAttachment(table, 6);
		fd_comboMnemonic.left = new FormAttachment(0, 109);
		fd_comboMnemonic.right = new FormAttachment(0, 207);
		comboMnemonic.setLayoutData(fd_comboMnemonic);
		
		textOpcode = new Text(this, SWT.BORDER);
		FormData fd_textOpcode = new FormData();
		fd_textOpcode.left = new FormAttachment(comboMnemonic, 6);
		fd_textOpcode.top = new FormAttachment(table, 6);
		textOpcode.setLayoutData(fd_textOpcode);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String opcodeInstructions = textOpcode.getText();
				int count = 2;
				for(int index = 0;index<opcodeInstructions.length();index++)
				{
					if(opcodeInstructions.charAt(index)==' ')
					{
						count++;
					}
				}
				if(opcodeInstructions.equals("")) count=1;
				byte[] updatedInstruction = new byte[count];
				updatedInstruction[0]=Byte.parseByte(comboMnemonic.getText());
				Capstone cs;
				if(codeExtract.getVersion() == Version.x32)
				{
					cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
				}
				else
				{
					cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_64);
				}
				if(!opcodeInstructions.equals(""))
				{
					int instructionIndex = 1;
					while(opcodeInstructions.contains(" "))
					{
						updatedInstruction[instructionIndex]=Byte.parseByte(opcodeInstructions.substring(0, opcodeInstructions.indexOf(' ')));
						opcodeInstructions = opcodeInstructions.substring(opcodeInstructions.indexOf(' ')+1);
						instructionIndex++;
					}
					updatedInstruction[instructionIndex]=Byte.parseByte(opcodeInstructions);
				}
				Capstone.CsInsn[] instruction = cs.disasm(updatedInstruction, ((Capstone.CsInsn)table.getSelection()[0].getData()).address);
				table.getSelection()[0].setText(0, "0x"+Long.toHexString(instruction[0].address));
			    table.getSelection()[0].setText(1, instruction[0].mnemonic);
			    table.getSelection()[0].setText(2, instruction[0].opStr);
			    table.getSelection()[0].setData(instruction[0]);
			    System.out.println(((Capstone.CsInsn)table.getSelection()[0].getData()).opStr);
			}
		});
		fd_textOpcode.right = new FormAttachment(100, -143);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(comboMnemonic, 0, SWT.BOTTOM);
		fd_btnNewButton.top = new FormAttachment(table, 6);
		fd_btnNewButton.right = new FormAttachment(table, 0, SWT.RIGHT);
		fd_btnNewButton.left = new FormAttachment(textOpcode, 28);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Replace");
		
	}
	
	private void fillTable()
	{
		new Thread() {
            public void run() {
            	long start = System.currentTimeMillis();
            	Capstone.CsInsn[] allInsn = getAllInsn();
            	System.out.println("time for capstone load: "+(System.currentTimeMillis()-start));
            	TableItem[] tableItems = new TableItem[allInsn.length];
            	Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                    	
                		for(int index=0;index<allInsn.length;index++)
                		{
                			table.getItem(index).setText(0, "0x"+Long.toHexString(allInsn[index].address));
                			table.getItem(index).setText(1, allInsn[index].mnemonic);
                			table.getItem(index).setText(2, allInsn[index].opStr);
                			table.getItem(index).setData(allInsn[index]);
                		}
                    }
                 });
            }
        }.start();
		
		
	}
	
	private TableItem[] setColor(TableItem item, Color color, TableItem[] items, int index)
	{
		items[index] = item;
		item.setBackground(color);
		return items;
	}
	
	private void setColor(TableItem item, Color color)
	{
		item.setBackground(color);
	}
	
	private TableItem[] setGray(TableItem item, TableItem[] items, int index)
	{
		return setColor(item, new Color(parent.getDisplay(), 200, 200, 200), items, index);
	}
	
	private TableItem[] setGreen(TableItem item, TableItem[] items, int index)
	{
		return setColor(item, new Color(parent.getDisplay(), 0, 255, 0), items, index);
	}
	
	private void clearColors(TableItem[] items)
	{
		for(int index = 0;index < items.length;index++)
		{
			try
			{
				System.out.println("data: "+items[index]);
				setColor(items[index], new Color(parent.getDisplay(), 255, 255, 255));
			}
			catch(NullPointerException e)
			{
				break;
			}
		}
	}
	
	@Override
	public void layout()
	{
		System.out.println("printing layout");
	}
	
	
	public Capstone.CsInsn[] getAllInsn()
	{
		return allInsn;
	}

	public void setAllInsn(Capstone.CsInsn[] allInsn)
	{
		this.allInsn = allInsn;
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
