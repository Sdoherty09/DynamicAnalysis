package dynamicAnalysis;

public class Driver {

	private native void executeInstruction(byte code);
	
	static {System.loadLibrary("ExecuteImpl");}
	
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
