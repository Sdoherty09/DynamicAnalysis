/*
 * 
 */
package dynamicAnalysis;

import java.io.File;
import java.util.Arrays;

/**
 * The Class ExecuteCode.
 */
public class ExecuteCode {
	
	/** The codes. */
	private byte[] codes;
	
	/** The code. */
	private byte code;
	
	/** The is arr. */
	private boolean isArr;
	
	/** The file. */
	private File file;
	
	/**
	 * Execute instruction.
	 *
	 * @param code the code
	 */
	private native void executeInstruction(byte code);
	
	/**
	 * Read registers.
	 *
	 * @return the int[]
	 */
	private native int[] readRegisters();
	
	static {System.load(getFile("execute.dll"));}
	
	/**
	 * Instantiates a new execute code.
	 *
	 * @param codes the codes
	 * @param file the file
	 */
	public ExecuteCode(byte[] codes, File file) {
		setCodes(codes);
		setFile(file);
		isArr=true;
	}
	
	/**
	 * Instantiates a new execute code.
	 *
	 * @param code the code
	 * @param file the file
	 */
	public ExecuteCode(byte code, File file) {
		setCode(code);
		setFile(file);
		isArr=false;
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}
	
	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file)
	{
		this.file = file;
	}
	
	/**
	 * Gets the codes.
	 *
	 * @return the codes
	 */
	public byte[] getCodes() {
		return codes;
	}
	
	/**
	 * Sets the codes.
	 *
	 * @param codes the new codes
	 */
	public void setCodes(byte[] codes) {
		this.codes = codes;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public byte getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(byte code) {
		this.code = code;
	}
	
	/**
	 * Gets the file.
	 *
	 * @param fileName the file name
	 * @return the file
	 */
	private static String getFile(String fileName)
	{
		return System.getProperty("user.dir")+"\\src\\dynamicAnalysis\\"+fileName;
	}
	
	/**
	 * Test.
	 *
	 * @return the string
	 */
	public native String test();
	
	/**
	 * Read.
	 *
	 * @return the int[]
	 */
	/*public ProcessManager execute()
	{
		ExecuteCode executeCode = new ExecuteCode(getCode(), getFile());
		executeCode.executeInstruction(getCode());
	}*/
	public int[] read()
	{
		int[] registers = new int[4];
		ExecuteCode executeCode = new ExecuteCode(getCode(), getFile());
		registers = executeCode.readRegisters();
		return registers;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return "ExecuteCode [codes=" + Arrays.toString(codes) + ", code=" + code + ", isArr=" + isArr + ", file=" + file
				+ "]";
	}
	
}
 //gcc ExecuteImpl.c -I I:/jre/jre6/include -I I:/jre/jre6/include/win32
//x86_64-w64-mingw32-gcc -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o execute.dll ExecuteImpl.c