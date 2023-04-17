/*
 * 
 */
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

/**
 * Composite to display the x86 instructions of a PE file
 */
public class InstructionsComposite extends Composite
{
	
	/** The table displaying the x86 instruction set. */
	private Table tableInstructions;
	
	/** The Capstone array containing x86 instructions. */
	private Capstone.CsInsn[] allInsn;
	
	/** The opcodes of the instructions. */
	private Text textOpcode;
	
	/** The virtual address space of the instructions. */
	private TableColumn tblclmnAddress;
	
	/** The mnemonics of the instructions. */
	private TableColumn tblclmnMnemonic;
	
	/** The table column to display the opcodes. */
	private TableColumn tblclmnOpcode;
	
	/** The updated instruction set when replacing instructions. */
	private byte[] updatedInstructions;
	
	/** The main window that acts as the composite parent. */
	private Composite parent;
	
	/** The values used by the GUI array. */
	private TableItem[] tableItems = null;
	
	/**
	 * Create the instructions composite.
	 *
	 * @param parent the parent that contains the composite
	 * @param style the SWT style
	 * @param file the file that contains the x86 instructions
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
		
		tableInstructions = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		FormData fd_tableInstructions = new FormData();
		fd_tableInstructions.top = new FormAttachment(0, 10);
		fd_tableInstructions.left = new FormAttachment(0, 10);
		fd_tableInstructions.right = new FormAttachment(0, 353);
		tableInstructions.setLayoutData(fd_tableInstructions);
		tableInstructions.setHeaderVisible(true);
		tableInstructions.setLinesVisible(true);
		tableInstructions.setItemCount(getAllInsn().length);
		
		Text comboMnemonic = new Text(this, SWT.BORDER);
		
		tableInstructions.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	Capstone.CsInsn instruction = getAllInsn()[tableInstructions.getSelectionIndex()];
		    	if(tableItems!=null) clearColors(tableItems);
		    	tableItems = null;
		    	if(instruction.groups.length == 2)
		    	{
		    		if(instruction.groups[0] == 7 && instruction.groups[1] == 1) //group for jump instructions, jmp works differently ?
		    		{
		    			long entryIndex = tableInstructions.getSelectionIndex();
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
	    				while(Long.decode(instruction.opStr) != ((Capstone.CsInsn)(tableInstructions.getItem((int) entryIndex).getData())).address)
	    				{
	    					System.out.println(entryIndex);
	    					try
	    					{
	    						tableItems = setGray(tableInstructions.getItem((int) entryIndex), tableItems, tableIndex);
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
	    				tableItems = setGreen(tableInstructions.getItem((int) entryIndex), tableItems, tableIndex);
		    		}
		    	}
	    	  	TableItem item = tableInstructions.getSelection()[0];
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
		
		tblclmnAddress = new TableColumn(tableInstructions, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setText("Address");
		
		tblclmnMnemonic = new TableColumn(tableInstructions, SWT.NONE);
		tblclmnMnemonic.setWidth(100);
		tblclmnMnemonic.setText("Mnemonic");
		
		tblclmnOpcode = new TableColumn(tableInstructions, SWT.NONE);
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
				for(int index=0;index<tableInstructions.getItemCount();index++)
				{
					Capstone.CsInsn instruction = (Capstone.CsInsn) tableInstructions.getItem(index).getData();
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
		fd_tableInstructions.bottom = new FormAttachment(100, -41);
		fd_tableInstructions.right = new FormAttachment(btnSave, -20);
		FormData fd_btnSave = new FormData();
		fd_btnSave.right = new FormAttachment(100, -10);
		fd_btnSave.top = new FormAttachment(0, 10);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");

		fillTable();
		tableInstructions.pack();
		
		
		FormData fd_comboMnemonic = new FormData();
		fd_comboMnemonic.top = new FormAttachment(tableInstructions, 6);
		fd_comboMnemonic.left = new FormAttachment(0, 109);
		fd_comboMnemonic.right = new FormAttachment(0, 207);
		comboMnemonic.setLayoutData(fd_comboMnemonic);
		
		textOpcode = new Text(this, SWT.BORDER);
		FormData fd_textOpcode = new FormData();
		fd_textOpcode.left = new FormAttachment(comboMnemonic, 6);
		fd_textOpcode.top = new FormAttachment(tableInstructions, 6);
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
				Capstone.CsInsn[] instruction = cs.disasm(updatedInstruction, ((Capstone.CsInsn)tableInstructions.getSelection()[0].getData()).address);
				tableInstructions.getSelection()[0].setText(0, "0x"+Long.toHexString(instruction[0].address));
			    tableInstructions.getSelection()[0].setText(1, instruction[0].mnemonic);
			    tableInstructions.getSelection()[0].setText(2, instruction[0].opStr);
			    tableInstructions.getSelection()[0].setData(instruction[0]);
			    System.out.println(((Capstone.CsInsn)tableInstructions.getSelection()[0].getData()).opStr);
			}
		});
		fd_textOpcode.right = new FormAttachment(100, -143);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(comboMnemonic, 0, SWT.BOTTOM);
		fd_btnNewButton.top = new FormAttachment(tableInstructions, 6);
		fd_btnNewButton.right = new FormAttachment(tableInstructions, 0, SWT.RIGHT);
		fd_btnNewButton.left = new FormAttachment(textOpcode, 28);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Replace");
		
	}
	
	/**
	 * Asynchronously populate the instruction table with x86 instructions, separated to increase performance.
	 */
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
                			tableInstructions.getItem(index).setText(0, "0x"+Long.toHexString(allInsn[index].address));
                			tableInstructions.getItem(index).setText(1, allInsn[index].mnemonic);
                			tableInstructions.getItem(index).setText(2, allInsn[index].opStr);
                			tableInstructions.getItem(index).setData(allInsn[index]);
                		}
                    }
                 });
            }
        }.start();
		
		
	}
	
	/**
	 * Updates the color of table items for jump instructions.
	 *
	 * @param item the table item to update color
	 * @param color the color to change to
	 * @param items the full array of table items
	 * @param index the index of table item to update color
	 * @return the updated table item array
	 */
	private TableItem[] setColor(TableItem item, Color color, TableItem[] items, int index)
	{
		items[index] = item;
		item.setBackground(color);
		return items;
	}
	
	/**
	 * Sets the color.
	 *
	 * @param item the item
	 * @param color the color
	 */
	private void setColor(TableItem item, Color color)
	{
		item.setBackground(color);
	}
	
	/**
	 * Sets table item to gray.
	 *
	 * @param item the item to update color of
	 * @param items the full table item array
	 * @param index the index of table item to update color
	 * @return the updated table item array
	 */
	private TableItem[] setGray(TableItem item, TableItem[] items, int index)
	{
		return setColor(item, new Color(parent.getDisplay(), 200, 200, 200), items, index);
	}
	
	/**
	 * Sets table item to green.
	 *
	 * @param item the item to update color of
	 * @param items the full table item array
	 * @param index the index of table item to update color
	 * @return the updated table item array
	 */
	private TableItem[] setGreen(TableItem item, TableItem[] items, int index)
	{
		return setColor(item, new Color(parent.getDisplay(), 0, 255, 0), items, index);
	}
	
	/**
	 * Set color of table items to default color.
	 *
	 * @param items the updated table item array
	 */
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
	
	/**
	 * Layout.
	 */
	@Override
	public void layout()
	{
		System.out.println("printing layout");
	}
	
	
	/**
	 * Gets the Capstone instruction array.
	 *
	 * @return the Capstone instruction array
	 */
	public Capstone.CsInsn[] getAllInsn()
	{
		return allInsn;
	}

	/**
	 * Sets the Capstone instruction array.
	 *
	 * @param allInsn the Capstone instruction array
	 */
	public void setAllInsn(Capstone.CsInsn[] allInsn)
	{
		this.allInsn = allInsn;
	}

	/**
	 * Check subclass.
	 */
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
