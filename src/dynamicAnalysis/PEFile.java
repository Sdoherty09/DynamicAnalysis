/*
 * 
 */
package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The Class PEFile. Retrieves and stores information on a Portable Executable relating to the file format
 */
public class PEFile {
	
	/** The PE file to be accessed. */
	private File file;
	
	/** The offset used to fetch bytes from the file. */
	private int offset;
	
	/** The version enum, either x32 or x64. */
	private Version version;
	
	/** The pointer to where to begin reading the raw data from the file. */
	private int pointer;
	
	/** The raw byte array of the entire file. */
	private byte[] bytes = null;
	
	/** The pointer to raw data. */
	private int pointerToRawData;

	/**
	 * Instantiates a new PE file.
	 *
	 * @param file the PE file
	 */
	public PEFile(File file)
	{
		setFile(file);
	}

	/**
	 * Gets the PE file.
	 *
	 * @return the PE file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets the PE file.
	 *
	 * @param file the new PE file
	 */
	public void setFile(File file)
	{
		this.file = file;
	}
	
	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Gets the PE file's version.
	 *
	 * @return the version
	 */
	public Version getVersion()
	{
		return version;
	}
	
	/**
	 * Gets the raw x86 instruction bytes.
	 *
	 * @return the raw x86 instruction bytes
	 */
	public byte[] getInstructions()
	{
		int bytesIndex = offset;
    	while(true)
    	{
    		if(bytes[bytesIndex]==0x2e && bytes[bytesIndex+1] == 0x74 && bytes[bytesIndex+2] == 0x65 && bytes[bytesIndex+3] == 0x78 && bytes[bytesIndex+4] == 0x74) break;
    		bytesIndex++;
    	}
    	bytesIndex+=16;
    	int sizeOfRawData = ((bytes[bytesIndex] & 0xff) | (bytes[bytesIndex+1] & 0xff) << 8 | (bytes[bytesIndex+2] & 0xff) << 16 | (bytes[bytesIndex+3] & 0xff) << 24);
    	bytesIndex+=4;
    	pointerToRawData = ((bytes[bytesIndex] & 0xff) | (bytes[bytesIndex+1] & 0xff) << 8 | (bytes[bytesIndex+2] & 0xff) << 16 | (bytes[bytesIndex+3] & 0xff) << 24);
    	setPointer(pointerToRawData);
    	byte[] instructions = new byte[sizeOfRawData];
    	for(int index = pointerToRawData;index < sizeOfRawData + pointerToRawData;index++)
    	{
    		instructions[index-pointerToRawData] = (byte)(bytes[index] & 0xff);
    		if(index-pointerToRawData <100) System.out.print(instructions[index-pointerToRawData] + " "); 		
    	}
    	return instructions;
	}
	
	/**
	 * Gets the pointer.
	 *
	 * @return the pointer
	 */
	public int getPointer()
	{
		return pointer;
	}

	/**
	 * Sets the pointer.
	 *
	 * @param pointer the new pointer
	 */
	private void setPointer(int pointer)
	{
		this.pointer = pointer;
	}
	
	/**
	 * Gets all bytes contained in the PE file.
	 *
	 * @return the bytes contained in the PE file.
	 * @throws IOException Signals that the file cannot be read or is not found
	 */
	public byte[] getBytes() throws IOException
	{
		return Files.readAllBytes(Paths.get(file.toString()));
	}

	/**
	 * Sets the bytes.
	 *
	 * @param bytes the new bytes
	 */
	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	/**
	 * Read the PE file, populating fields that are stored
	 */
	public void readFile()
	{
		
        try 
        {
        	bytes = Files.readAllBytes(Paths.get(file.toString()));
        	offset = (((bytes[0x3c] & 0xff) | (bytes[0x3d] & 0xff) << 8 | (bytes[0x3e] & 0xff) << 16 | (bytes[0x3f] & 0xff) << 24)) + 24;
        	if(bytes[offset+1]==0x01) version = Version.x32;
        	else if (bytes[offset+1]==0x02) version = Version.x64;
        	else System.out.println("ERROR finding version");
        	int rvaOffset;
        	if(version == Version.x32) rvaOffset = offset + 92;
        	else rvaOffset = offset + 108;
        	int numberOfRvaAndSizes = ((bytes[rvaOffset] & 0xff) | (bytes[rvaOffset+1] & 0xff) << 8 | (bytes[rvaOffset+2] & 0xff) << 16 | (bytes[rvaOffset+3] & 0xff) << 24);
        	DataDirectory dataDirectories[] = new DataDirectory[numberOfRvaAndSizes];
        	int directoryOffset;
        	if(version == Version.x32) directoryOffset = offset + 96;
        	else directoryOffset = offset + 112;
        	int virtualAddress;
        	int size;
        	for(int index = 0; index < numberOfRvaAndSizes; index++)
        	{
        		virtualAddress = ((bytes[directoryOffset] & 0xff) | (bytes[directoryOffset+1] & 0xff) << 8 | (bytes[directoryOffset+2] & 0xff) << 16 | (bytes[directoryOffset+3] & 0xff) << 24);
        		size = ((bytes[directoryOffset+4] & 0xff) | (bytes[directoryOffset+5] & 0xff) << 8 | (bytes[directoryOffset+6] & 0xff) << 16 | (bytes[directoryOffset+7] & 0xff) << 24);
        		dataDirectories[index] = new DataDirectory(bytes, virtualAddress, size);
        	}     	
        } 
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 * To string.
	 *
	 * @return the string containing information on the PE file
	 */
	@Override
	public String toString()
	{
		return "PEFile [file=" + file + ", offset=" + offset + ", version=" + version + "]";
	}
	
}
