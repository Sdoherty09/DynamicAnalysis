package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import capstone.Capstone;

/**
 * Top level extraction of code from a PE file. Uses Capstone to translate a byte stream to x86 instructions.
 */
public class CodeExtract {
	
	/** The associated PE file. */
	private File file;
	
	/** Byte stream of instructions. */
	private byte[] instructions;
	
	/** String representation of x86 instructions. */
	private String code;
	
	/** Array of x86 instructions. */
	private String[] codeArr;
	
	/** A conversion of the file to a PE file. */
	private PEFile peFile;
	
	/** Capstone array containing all instructions. */
	private Capstone.CsInsn[] allInsn;
	
	/**
	 * Instantiates the CodeExtract class.
	 *
	 * @param file the file to extract the x86 instructions from
	 */
	public CodeExtract(File file) {
		setFile(file);
		loadPE(getFile());
	}

	/**
	 * Load PE.
	 *
	 * @param file the file to extract the x86 instructions from
	 */
	private void loadPE(File file)
	{
		peFile = new PEFile(file);
		peFile.readFile();
		
	}
	
	/**
	 * Load x86 instructions.
	 *
	 * @return byte array containing raw instruction bytes
	 */
	public byte[] loadInstructions()
	{
		long start = System.currentTimeMillis();
		byte[] bytes = peFile.getInstructions();
		System.out.println("time for loadInstruction: "+(System.currentTimeMillis()-start));
		setInstructions(bytes);
		try
		{
			codeArr = extractArr(getInstructions());
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * Load Capstone and convert instruction bytes to x86 instructions.
	 *
	 * @param instructions the raw byte instructions
	 * @return the x86 instructions
	 */
	private Capstone.CsInsn[] loadCapstone(byte[] instructions)
	{
		file = new File(file.getAbsolutePath());
        @SuppressWarnings("unused")
		byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(file.toString()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        Capstone cs = null;
		if(peFile.getVersion() == Version.x32)
		{
			cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
			System.out.println("Running x32 exe");
		}
		else
		{
			cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_64);
			System.out.println("Running x64 exe");
		}      	
		cs.setDetail(1);
		long start = System.currentTimeMillis();
	    Capstone.CsInsn[] allInsn = cs.disasm(instructions, peFile.getPointer());
	    System.out.println("dissasemble time: "+(System.currentTimeMillis()-start));
	    setAllInsn(allInsn);
	    return allInsn;
	}
	
	/**
	 * Extract x86 instructions as a string array.
	 *
	 * @param instructions the raw byte instructions
	 * @return the x86 instructions as a string array
	 */
	private String[] extractArr(byte[] instructions)
	{
		Capstone.CsInsn[] allInsn = loadCapstone(instructions);
	    String[] code = new String[allInsn.length];
	    for (int i=0; i<allInsn.length; i++) 
	    {
	    	code[i] = String.format("0x%x: %s %s\n", allInsn[i].address,
	  	          allInsn[i].mnemonic, allInsn[i].opStr);
	    } 
	   return code;
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Sets the instructions.
	 *
	 * @param instructions the new instructions
	 */
	private void setInstructions(byte[] instructions) {
		this.instructions = instructions;
	}
	
	/**
	 * Gets the instructions.
	 *
	 * @return the instructions
	 */
	public byte[] getInstructions() {
		return instructions;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Gets the code arr.
	 *
	 * @return the code arr
	 */
	public String[] getCodeArr() {
		return codeArr;
	}
	
	/**
	 * Gets the pe file.
	 *
	 * @return the pe file
	 */
	public PEFile getPeFile()
	{
		return peFile;
	}

	/**
	 * Sets the pe file.
	 *
	 * @param peFile the new pe file
	 */
	public void setPeFile(PEFile peFile)
	{
		this.peFile = peFile;
	}
	
	/**
	 * Gets the pointer.
	 *
	 * @return the pointer
	 */
	public int getPointer()
	{
		return peFile.getPointer();
	}
	
	/**
	 * Gets the Capstone instruction set.
	 *
	 * @return the Capstone instruction set
	 */
	public Capstone.CsInsn[] getAllInsn()
	{
		return allInsn;
	}

	/**
	 * Sets the Capstone instruction set.
	 *
	 * @param allInsn the new Capstone instruction set.
	 */
	public void setAllInsn(Capstone.CsInsn[] allInsn)
	{
		this.allInsn = allInsn;
	}

	/**
	 * Gets the raw instruction bytes.
	 *
	 * @return the raw instruction bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] getBytes() throws IOException
	{
		return peFile.getBytes();
	}
	
	/**
	 * Gets the PE version, either 32 bit or 64 bit.
	 *
	 * @return the PE version
	 */
	public Version getVersion()
	{
		return peFile.getVersion();
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "CodeExtract [file=" + file + ", instructions=" + Arrays.toString(instructions) + ", code=" + code + "]";
	}
}
