package dynamicAnalysis;

import java.io.File;
import java.util.Arrays;

public class ExecuteCode {
	private byte[] codes;
	private byte code;
	private boolean isArr;
	private File file;
	
	private native void executeInstruction(byte code);
	private native int[] readRegisters();
	
	static {System.load(getFile("execute.dll"));}
	
	public ExecuteCode(byte[] codes, File file) {
		setCodes(codes);
		setFile(file);
		isArr=true;
	}
	public ExecuteCode(byte code, File file) {
		setCode(code);
		setFile(file);
		isArr=false;
	}
	
	public File getFile()
	{
		return file;
	}
	public void setFile(File file)
	{
		this.file = file;
	}
	public byte[] getCodes() {
		return codes;
	}
	public void setCodes(byte[] codes) {
		this.codes = codes;
	}
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	private static String getFile(String fileName)
	{
		return System.getProperty("user.dir")+"\\src\\dynamicAnalysis\\"+fileName;
	}
	public native String test();
	
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
	@Override
	public String toString()
	{
		return "ExecuteCode [codes=" + Arrays.toString(codes) + ", code=" + code + ", isArr=" + isArr + ", file=" + file
				+ "]";
	}
	
}
 //gcc ExecuteImpl.c -I I:/jre/jre6/include -I I:/jre/jre6/include/win32
//x86_64-w64-mingw32-gcc -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o execute.dll ExecuteImpl.c