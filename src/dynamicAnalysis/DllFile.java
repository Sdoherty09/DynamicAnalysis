package dynamicAnalysis;

import java.io.File;

public class DllFile
{
	private String path;
	private File file;

	public DllFile(String path)
	{
		setPath(path);
		file = createFile();
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}
	
	public File getFile()
	{
		return file;
	}

	private File createFile()
	{
		return new File(getPath());
	}

	@Override
	public String toString()
	{
		return "DllFile [path=" + path + ", file=" + file + "]";
	}
	
}
