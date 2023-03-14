package dynamicAnalysis;

import capstone.Capstone;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.github.katjahahn.parser.IOUtil;
import com.github.katjahahn.parser.Location;
import com.github.katjahahn.parser.PEData;
import com.github.katjahahn.parser.PELoader;
import com.github.katjahahn.parser.sections.SectionLoader;
import com.github.katjahahn.parser.sections.rsrc.Resource;
import com.github.katjahahn.parser.sections.rsrc.ResourceSection;

public class CapstoneTest
{

	public static byte [] CODE = { (byte)0x8b, (byte)0x4c, (byte) 0x24, (byte) 0x04 };
	
	private static PEFile peFile;
	private static File file = new File("D:\\Downloads\\Everything-1.4.1.1022.x86-Setup\\$PLUGINSDIR\\Everything\\Everything.exe");
		  public static void main(String argv[]) {
			  peFile = new PEFile(file);
			  peFile.readFile();
			  byte[] bytes = peFile.getInstructions();
			  Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
			    Capstone.CsInsn[] allInsn = cs.disasm(bytes, 0x1000);
			    for (int i=0; i<allInsn.length; i++)
			      System.out.printf("0x%x:\t%s\t%s\n", allInsn[i].address,
			          allInsn[i].mnemonic, allInsn[i].opStr);
		  }

}
