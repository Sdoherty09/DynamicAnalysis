package dynamicAnalysis;

public class ExecuteCode {
	private byte[] codes;
	private byte code;
	private boolean isArr;
	
	private native void executeInstruction(byte code);
	
	static {System.loadLibrary("ExecuteImpl");}
	
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
	public void execute() {
		ExecuteCode executeCode = new ExecuteCode(getCode());
		executeCode.execute();
	}
}
