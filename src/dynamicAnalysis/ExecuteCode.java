package dynamicAnalysis;

public class ExecuteCode {
	private byte[] codes;
	private byte code;
	private boolean isArr;
	
	private native void executeInstruction(byte code);
	private native int[] readRegisters();
	
	static {System.load(getFile("execute.dll"));}
	
	public ExecuteCode(byte[] codes) {
		setCodes(codes);
		isArr=true;
	}
	public ExecuteCode(byte code) {
		setCode(code);
		isArr=false;
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
	public void execute()
	{
		ExecuteCode executeCode = new ExecuteCode(getCode());
		executeCode.executeInstruction(getCode());
	}
	public int[] read()
	{
		int[] registers = new int[4];
		ExecuteCode executeCode = new ExecuteCode(getCode());
		registers = executeCode.readRegisters();
		return registers;
	}
	
}
 //gcc ExecuteImpl.c -I I:/jre/jre6/include -I I:/jre/jre6/include/win32
//x86_64-w64-mingw32-gcc -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o execute.dll ExecuteImpl.c