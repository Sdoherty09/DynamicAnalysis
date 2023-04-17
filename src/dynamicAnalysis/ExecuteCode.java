/*
 * 
 */
package dynamicAnalysis;

import java.io.File;
import java.util.Arrays;

/**
 * Unused implementation that would have allowed for registry view. Included for demonstration purposes only
 */
public class ExecuteCode {
	
	/** The registry byte values. */
	private byte[] codes;
	
	/** The registry byte value. */
	private byte code;
	
	/** The check for if an array is supplied to a method. */
	private boolean isArr;
	
	/** The file to be accessed. */
	private File file;
	
	/**
	 * JNI implementation for executing the instructions.
	 *
	 * @param code the registry byte
	 */
	private native void executeInstruction(byte code);
	
	/**
	 * Load the JNI method to read a file.
	 *
	 * @return registry integer array, containing four values
	 */
	private native int[] readRegisters();
	
	static {System.load(getFile("execute.dll"));}
	
	/**
	 * Instantiates the ExecuteCode method with an array
	 *
	 * @param codes the byte array of registry values
	 * @param file the file to be accessed
	 */
	public ExecuteCode(byte[] codes, File file) {
		setCodes(codes);
		setFile(file);
		isArr=true;
	}
	
	/**
	 * Instantiates the ExecuteCode method with single instruction
	 *
	 * @param codes the byte containing a registry value
	 * @param file the file to be accessed
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
	 * Gets the registry codes.
	 *
	 * @return the registry codes
	 */
	public byte[] getCodes() {
		return codes;
	}
	
	/**
	 * Sets the registry codes.
	 *
	 * @param codes the new registry codes
	 */
	public void setCodes(byte[] codes) {
		this.codes = codes;
	}
	
	/**
	 * Gets a single registry code.
	 *
	 * @return the registry code
	 */
	public byte getCode() {
		return code;
	}
	
	/**
	 * Sets the registry code.
	 *
	 * @param code the new registry code
	 */
	public void setCode(byte code) {
		this.code = code;
	}
	
	/**
	 * Gets the file path that was accessed.
	 *
	 * @param fileName the file name
	 * @return the full file path
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
	
	
	/*public ProcessManager execute()
	{
		ExecuteCode executeCode = new ExecuteCode(getCode(), getFile());
		executeCode.executeInstruction(getCode());
	}*/
	/**
	 * Read the current registry values.
	 *
	 * @return the array containing the four registry values
	 */
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
	 * @return the string describing values being used
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