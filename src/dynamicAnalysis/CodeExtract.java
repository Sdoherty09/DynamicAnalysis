package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import capstone.Capstone;

public class CodeExtract {
	private File file;
	private byte[] instructions;
	private String code;
	private String[] codeArr;
	private PEFile peFile;
	private Capstone.CsInsn[] allInsn;
	
	public CodeExtract(File file) {
		setFile(file);
		loadPE(getFile());
	}

	private void loadPE(File file)
	{
		peFile = new PEFile(file);
		peFile.readFile();
		
	}
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
			//TODO: handle wrong arch
		}
		return bytes;
	}
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
	/*
	private String extract(byte[] instructions)
	{
		String code = "";
		Capstone.CsInsn[] allInsn = loadCapstone(instructions);
	    for (int i=0; i<allInsn.length; i++) 
	    {
	    	code += String.format("0x%x: %s %s\n", allInsn[i].address,
	  	          allInsn[i].mnemonic, allInsn[i].opStr);
	    } 
	   return code;
	}*/
	
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
	
	/*public Process execute(byte code, File file)
	{
		ExecuteCode executeCode = new ExecuteCode(code, file);
		return executeCode.execute();
	}*/
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	private void setInstructions(byte[] instructions) {
		this.instructions = instructions;
	}
	public byte[] getInstructions() {
		return instructions;
	}
	
	public String getCode() {
		return code;
	}
	
	public String[] getCodeArr() {
		return codeArr;
	}
	
	public PEFile getPeFile()
	{
		return peFile;
	}

	public void setPeFile(PEFile peFile)
	{
		this.peFile = peFile;
	}
	
	public Capstone.CsInsn[] getAllInsn()
	{
		return allInsn;
	}

	public void setAllInsn(Capstone.CsInsn[] allInsn)
	{
		this.allInsn = allInsn;
	}

	public byte[] getBytes() throws IOException
	{
		return peFile.getBytes();
	}
	
	public byte[] writeInstructions(byte[] bytes)
	{
		return peFile.writeInstructions(bytes);
	}
	
	public Version getVersion()
	{
		return peFile.getVersion();
	}
	
	@Override
	public String toString() {
		return "CodeExtract [file=" + file + ", instructions=" + Arrays.toString(instructions) + ", code=" + code + "]";
	}
}
