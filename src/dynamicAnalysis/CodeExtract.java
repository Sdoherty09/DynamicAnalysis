package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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

	
	public CodeExtract(File file) {
		setFile(file);
		resources = loadPE(getFile());
		code = extract(getResources());
	}

	private byte[] loadPE(File file)
	{
		byte [] bytes = null;
		String output = "";
		try {
			PEData data = PELoader.loadPE(file);
			ResourceSection rsrc = new SectionLoader(data).loadResourceSection();
			List<Resource> resources = rsrc.getResources();
			Resource resource = resources.get(0);
			Location loc = resource.rawBytesLocation();
			long offset = loc.from();
			// this example only works for small resources
			assert loc.size() == (int) loc.size();
			int size = (int) loc.size();
			try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			    bytes = IOUtil.loadBytes(offset, size, raf);
			    // print as hex string
			
			    //System.out.println(ByteArrayUtil.byteToHex(bytes));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bytes;
	}
	
	private String extract(byte[] resources)
	{
		String code="";
		Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
		cs.setDetail(1);
	    Capstone.CsInsn[] allInsn = cs.disasm(resources, 0x1000);
	    for (int i=0; i<allInsn.length; i++) 
	    {
	    	code += String.format("0x%x:\t%s\t%s\n", allInsn[i].address,
	  	          allInsn[i].mnemonic, allInsn[i].opStr);
	    } 
	   return code;
	}
	
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

	@Override
	public String toString() {
		return "CodeExtract [file=" + file + ", resources=" + Arrays.toString(resources) + ", code=" + code + "]";
	}
}
