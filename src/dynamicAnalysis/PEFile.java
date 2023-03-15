package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

public class PEFile {
	private File file;
	private int offset;
	private Version version;
	private byte[] instructions;
	private int pointer;
	private byte[] bytes = null;
	private byte[][] bytesArr;
	private Thread[] threads;
	private int threadIndex;
	private Semaphore semaphore = new Semaphore(1);
	private int numOfThreads = 8;
	
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

	public byte[][] getBytesArr()
	{
		return bytesArr;
	}

	public void setBytesArr(byte[][] bytesArr)
	{
		this.bytesArr = bytesArr;
	}

	public int search(byte[] toSearch, byte[] search)
	{
		numOfThreads = 8;
		threads = new Thread[numOfThreads];
		bytesArr = new byte[numOfThreads][toSearch.length];
		for(int index = 0;index<bytesArr.length;index++)
		{
			for(int j=0;j<bytesArr[index].length;j++)
			{
				bytesArr[index][j] = toSearch[j+(index*numOfThreads)];
			}
		}
		for (int index = 0; index < threads.length; index++) {
			
		    threads[index] = new Thread(new Runnable() {
		        public void run() {
		        	try
					{
						PEFile.this.semaphore.acquire();
						byte[] toSearch = null;
						int threadID;
						for(int index=0;index<PEFile.this.numOfThreads;index++)
						{
							if(PEFile.this.getBytesArr()[index]!=null)
							{
								toSearch = PEFile.this.getBytesArr()[index];
								PEFile.this.getBytesArr()[index] = null;
								threadID = index;
								break;
							}
						}
						PEFile.this.semaphore.release();
						for(int index = 0; index < toSearch.length;index++)
						{
							// search for the search array here
						}
						
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	//if(PEFile.this.semaphore.)
		            //byte[] toSearch = PEFile.this.getBytesArr()[];
		        }
		    });
		   threads[index].setName(""+index);
		   threads[index].start();
		   
		}
		return 0; //first found index of search array
	}
	
	public byte[] getInstructions()
	{
		int bytesIndex = offset;
    	while(true)
    	{
    		if(bytes[bytesIndex]==0x2e && bytes[bytesIndex+1] == 0x74 && bytes[bytesIndex+2] == 0x65 && bytes[bytesIndex+3] == 0x78 && bytes[bytesIndex+4] == 0x74) break;
    		bytesIndex++;
    	}
    	System.out.println("bytes index: "+bytesIndex);
    	bytesIndex+=16;
    	int sizeOfRawData = ((bytes[bytesIndex] & 0xff) | (bytes[bytesIndex+1] & 0xff) << 8 | (bytes[bytesIndex+2] & 0xff) << 16 | (bytes[bytesIndex+3] & 0xff) << 24);
    	System.out.println("size of raw: "+sizeOfRawData);
    	bytesIndex+=4;
    	int pointerToRawData = ((bytes[bytesIndex] & 0xff) | (bytes[bytesIndex+1] & 0xff) << 8 | (bytes[bytesIndex+2] & 0xff) << 16 | (bytes[bytesIndex+3] & 0xff) << 24);
    	System.out.println("pointer to raw: "+pointerToRawData);
    	setPointer(pointerToRawData);
    	byte[] instructions = new byte[sizeOfRawData];
    	for(int index = pointerToRawData;index < sizeOfRawData + pointerToRawData;index++)
    	{
    		instructions[index-pointerToRawData] = (byte)(bytes[index] & 0xff);
    		if(index-pointerToRawData <100) System.out.print(instructions[index-pointerToRawData] + " "); 		
    	}
    	return instructions;
	}

	
	public int getPointer()
	{
		return pointer;
	}

	private void setPointer(int pointer)
	{
		this.pointer = pointer;
	}
	
	public Thread[] getThreads()
	{
		return threads;
	}

	public void setThreads(Thread[] threads)
	{
		this.threads = threads;
	}

	public void readFile()
	{
		
        try 
        {
        	bytes = Files.readAllBytes(Paths.get(file.toString()));
        	offset = (((bytes[0x3c] & 0xff) | (bytes[0x3d] & 0xff) << 8 | (bytes[0x3e] & 0xff) << 16 | (bytes[0x3f] & 0xff) << 24)) + 24;
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
        	System.out.println("rva: "+numberOfRvaAndSizes);
        	
        	/*for(int index = 0;index<numberOfRvaAndSizes;index++)
        	{
        		
        	}*/
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
