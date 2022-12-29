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
        	int offset=bytes[0x0c]+bytes[0x0d]*8+bytes[0x0d]*64+bytes[0x0d]*256;
        	if(bytes[offset+4]==0x4c) x32 = true;
        	else x32 = false;
        } 
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
	}
}
