package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.github.katjahahn.parser.IOUtil;
import com.github.katjahahn.parser.Location;
import com.github.katjahahn.parser.PEData;
import com.github.katjahahn.parser.PELoader;
import com.github.katjahahn.parser.sections.SectionLoader;
import com.github.katjahahn.parser.sections.rsrc.Resource;
import com.github.katjahahn.parser.sections.rsrc.ResourceSection;

import capstone.Capstone;

public class CodeExtract {
	private File file;
	private byte[] resources;
	private String code;
	private String[] codeArr;
	private PEFile peFile;
	
	public CodeExtract(File file) {
		setFile(file);
		resources = loadPE(getFile());
		code = extract(getResources());
		codeArr = extractArr(getResources());
	}

	private byte[] loadPE(File file)
	{
		byte [] bytes = null;
		try {
			peFile = new PEFile(file);
			PEData data = PELoader.loadPE(peFile.getFile());
			peFile.readFile();
			ResourceSection rsrc = new SectionLoader(data).loadResourceSection();
			List<Resource> resources = rsrc.getResources();
			Resource resource = resources.get(0);
			Location loc = resource.rawBytesLocation();
			long offset = loc.from();
			assert loc.size() == (int) loc.size();
			int size = (int) loc.size();
			try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			    bytes = IOUtil.loadBytes(offset, size, raf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	private Capstone.CsInsn[] loadCapstone(byte[] resources)
	{
		file = new File(file.getAbsolutePath());
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(file.toString()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        Capstone cs = null;
		if(peFile.isX32())
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
	    Capstone.CsInsn[] allInsn = cs.disasm(resources, 0x1000);
	    return allInsn;
	}
	private String extract(byte[] resources)
	{
		String code = "";
		Capstone.CsInsn[] allInsn = loadCapstone(resources);
	    for (int i=0; i<allInsn.length; i++) 
	    {
	    	code += String.format("0x%x:\t%s\t%s\n", allInsn[i].address,
	  	          allInsn[i].mnemonic, allInsn[i].opStr);
	    } 
	   return code;
	}
	
	private String[] extractArr(byte[] resources)
	{
		Capstone.CsInsn[] allInsn = loadCapstone(resources);
	    String[] code = new String[allInsn.length];
	    for (int i=0; i<allInsn.length; i++) 
	    {
	    	code[i] = String.format("0x%x: %s\t%s\n", allInsn[i].address,
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
	
	public byte[] getResources() {
		return resources;
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

	@Override
	public String toString() {
		return "CodeExtract [file=" + file + ", resources=" + Arrays.toString(resources) + ", code=" + code + "]";
	}
}
