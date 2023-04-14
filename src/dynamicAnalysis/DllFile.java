/*
 * 
 */
package dynamicAnalysis;

import java.io.File;

/**
 * Class used to identify DLL files.
 */
public class DllFile
{
	
	/** The path of the file. */
	private String path;
	
	/** The DLL file. */
	private File file;

	/**
	 * Instantiates a new dll file.
	 *
	 * @param path the path of the DLL file
	 */
	public DllFile(String path)
	{
		setPath(path);
		file = createFile();
	}

	/**
	 * Gets the path of the DLL file.
	 *
	 * @return the path of the DLL file
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path)
	{
		this.path = path;
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Creates the file.
	 *
	 * @return the file
	 */
	private File createFile()
	{
		return new File(getPath());
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return "DllFile [path=" + path + ", file=" + file + "]";
	}
	
}
