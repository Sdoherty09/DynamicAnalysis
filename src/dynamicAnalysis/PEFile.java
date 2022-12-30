package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PEFile {
	private File file;
	private int offset;
	private boolean x32;
	
	public PEFile(File file)
	{
		setFile(file);
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}
	
	public int getOffset()
	{
		return offset;
	}

	public boolean isX32()
	{
		return x32;
	}

	public void readFile()
	{
		byte[] bytes = null;
        try 
        {
        	bytes = Files.readAllBytes(Paths.get(file.toString()));
        	int offset = (((bytes[0x3c] & 0xff) | (bytes[0x3d] & 0xff) << 8 | (bytes[0x3e] & 0xff) << 16 | (bytes[0x3f] & 0xff) << 24)) + 24;
        	System.out.println("offset: "+offset);
        	if(bytes[offset+1]==0x01) x32 = true;
        	else if (bytes[offset+1]==0x02) x32 = false;
        	else System.out.println("ERROR finding version");
        	int sizeOfCode = (bytes[offset+4] & 0xff) | (bytes[offset+5] & 0xff) << 8 | (bytes[offset+6] & 0xff) << 16 | (bytes[offset+7] & 0xff) << 24;
        	int rvaOffset = offset + 92;
        	if(!x32) rvaOffset += 16;
        	int numberOfRvaAndSizes = (bytes[offset+4] & 0xff) | (bytes[offset+5] & 0xff) << 8 | (bytes[offset+6] & 0xff) << 16 | (bytes[offset+7] & 0xff) << 24;
        	System.out.println("rva: "+numberOfRvaAndSizes);
        	int importOffset;
        	if(x32) importOffset = offset + 104;
        	else importOffset = offset + 120;
        	System.out.println("imp offset: "+importOffset);
        	int importAddress = (bytes[importOffset] & 0xff) | (bytes[importOffset+1] & 0xff) << 8 | (bytes[importOffset+2] & 0xff) << 16 | (bytes[importOffset+3] & 0xff) << 24;
        	int importSize = (bytes[importOffset+4] & 0xff) | (bytes[importOffset+5] & 0xff) << 8 | (bytes[importOffset+6] & 0xff) << 16 | (bytes[importOffset+7] & 0xff) << 24;
        	System.out.println("size: "+importAddress);
        	int stringAddress;
        	String string="";
        	for(int index=0;index<importSize;index++)
        	{
        		importAddress+=12;
        		stringAddress = ((bytes[importAddress] & 0xff) | (bytes[importAddress+1] & 0xff) << 8 | (bytes[importAddress+2] & 0xff) << 16 | (bytes[importAddress+3] & 0xff) << 24) - numberOfRvaAndSizes;
        		while(bytes[stringAddress]!=0)
        		{
        			string+=bytes[stringAddress];
        			stringAddress++;
        		}
        		System.out.println(string);
        		string="";
        	}
        	System.out.println("address: "+importAddress);
        	
        } 
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
	}
}
