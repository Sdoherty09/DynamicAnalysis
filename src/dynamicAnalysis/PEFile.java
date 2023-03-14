package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PEFile {
	private File file;
	private int offset;
	private Version version;
	
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

	public Version getVersion()
	{
		return version;
	}

	public void readFile()
	{
		byte[] bytes = null;
        try 
        {
        	bytes = Files.readAllBytes(Paths.get(file.toString()));
        	int offset = (((bytes[0x3c] & 0xff) | (bytes[0x3d] & 0xff) << 8 | (bytes[0x3e] & 0xff) << 16 | (bytes[0x3f] & 0xff) << 24)) + 24;
        	System.out.println("offset: "+offset);
        	if(bytes[offset+1]==0x01) version = Version.x32;
        	else if (bytes[offset+1]==0x02) version = Version.x64;
        	else System.out.println("ERROR finding version");
        	int sizeOfCode = (bytes[offset+4] & 0xff) | (bytes[offset+5] & 0xff) << 8 | (bytes[offset+6] & 0xff) << 16 | (bytes[offset+7] & 0xff) << 24;
        	System.out.println("size of code: "+sizeOfCode);
        	int baseOfCode = (bytes[offset+20] & 0xff) | (bytes[offset+21] & 0xff) << 8 | (bytes[offset+22] & 0xff) << 16 | (bytes[offset+23] & 0xff) << 24;
        	System.out.println("base of code: "+(baseOfCode));
        	int imageBaseOffset = offset + 24;
        	int imageBase;
        	if(version == Version.x32)
    		{
    			imageBaseOffset += 4;
    			imageBase = (bytes[imageBaseOffset] & 0xff) | (bytes[imageBaseOffset+1] & 0xff) << 8 | (bytes[imageBaseOffset+2] & 0xff) << 16 | (bytes[imageBaseOffset+3] & 0xff) << 24;
    		}
        	else
        	{
        		imageBase = (bytes[imageBaseOffset] & 0xff) | (bytes[imageBaseOffset+1] & 0xff) << 8 | (bytes[imageBaseOffset+2] & 0xff) << 16 | (bytes[imageBaseOffset+3] & 0xff) << 24 | (bytes[imageBaseOffset+4] & 0xff) << 32 | (bytes[imageBaseOffset+5] & 0xff) << 40 | (bytes[imageBaseOffset+6] & 0xff) << 48 | (bytes[imageBaseOffset+7] & 0xff) << 56;
        	}
        	System.out.println("image base: "+imageBaseOffset);
        	int importOffset;
        	if(version == Version.x32) importOffset = offset + 104;
        	else importOffset = offset + 120;
        	int importAddress = ((bytes[importOffset] & 0xff) | (bytes[importOffset+1] & 0xff) << 8 | (bytes[importOffset+2] & 0xff) << 16 | (bytes[importOffset+3] & 0xff) << 24); //uses virtual address
        	int importSize = (bytes[importOffset+4] & 0xff) | (bytes[importOffset+5] & 0xff) << 8 | (bytes[importOffset+6] & 0xff) << 16 | (bytes[importOffset+7] & 0xff) << 24; //should still be correct pointer
        	int rvaOffset;
        	if(version == Version.x32) rvaOffset = offset + 92;
        	else rvaOffset = offset + 108;
        	int numberOfRvaAndSizes = ((bytes[rvaOffset] & 0xff) | (bytes[rvaOffset+1] & 0xff) << 8 | (bytes[rvaOffset+2] & 0xff) << 16 | (bytes[rvaOffset+3] & 0xff) << 24);
        	System.out.println("rva: "+numberOfRvaAndSizes);
        	System.out.println("import address: "+importAddress);
        	int stringAddress;
        	String string="";
        	for(int index=0;index<importSize;index++)
        	{
        		importAddress+=12;
        		stringAddress = ((bytes[importAddress] & 0xff) | (bytes[importAddress+1] & 0xff) << 8 | (bytes[importAddress+2] & 0xff) << 16 | (bytes[importAddress+3] & 0xff) << 24)+offset;
        		/*while(bytes[stringAddress]!=0)
        		{
        			string+=bytes[stringAddress];
        			stringAddress++;
        		}
        		System.out.println(string);
        		string="";*/
        	}
        	System.out.println("address: "+importAddress);
        	
        } 
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
        //rawDataOffset, rawDataSize
	}

	@Override
	public String toString()
	{
		return "PEFile [file=" + file + ", offset=" + offset + ", version=" + version + "]";
	}
	
}
