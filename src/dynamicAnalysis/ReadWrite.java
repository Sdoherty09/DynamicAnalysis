/*
 * 
 */
package dynamicAnalysis;
import java.io.*;

/**
 * Helper class for reading and writing to a text file.
 */
public class ReadWrite
{
 	
	 /**
	  * Write to a file. Clears existing lines in the file.
	  *
	  * @param word the string to be written to the text file
	  * @param file the text file to write to
	  */
	 public static void write(String word, String file) //Method for clearing a text file and writing a line.
 	{
 		PrintWriter delete=null;
 		try
	 		{
 				delete = new PrintWriter(file);
		        FileWriter writer = new FileWriter(file);
		        delete.print("");
		        writer.write(word);
		        writer.close();
	        }
        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
 		finally
 		{
 			delete.close();
 		}
 	}
 	
 	/**
	  * Write to a file. Keeps existing lines in the file.
	  *
	  * @param word the string to be written to the text file
	  * @param file the text file to write to
	  */
	 public static void writeLine(String word, String file) //Method for adding a line to a text file.
 	{
 		try
	 		{
		        FileWriter writer = new FileWriter(file, true);
		        writer.write(word+"\n");
		        writer.close();
	        }
        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
 	}
 	
 	/**
	  * Delete.
	  *
	  * @param file the text file to write to
	  */
	 public static void delete(String file) //Method for clearing a text file.
 	{
 		write("", file);
 	}
 	
	 /**
	  * Gets the length.
	  *
	  * @param file the text file to write to
	  * @return the number of lines in the file
	  */
	 public static int getLength(String file) //Returns how many lines are in the text file.
 	{
 		int count=1;
 		@SuppressWarnings("unused")
		String dummy;
 		BufferedReader checkLine=null;
 		try
 		{
 		FileReader reader = new FileReader(file);
		checkLine=new BufferedReader(reader);
 		while (true)
			 	{
			 		dummy=checkLine.readLine().toString();
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 		else
			 		{
			 			count++;
			 		}
			 	}
		}
		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
	    return count;
 	}
 	
	 /**
	  * Gets the line at the specified index.
	  *
	  * @param number the index to retrieve
	  * @param file the file
	  * @return the line at the specified index
	  */
	 public static String getLine(int number, String file) //Method for returning a line from a text file.
 	{
 		String word="";
 		BufferedReader checkLine=null;
 		FileReader reader=null;
 		try
	 		{
 			try
 			{
 				reader = new FileReader(file);
 			}
		 		catch (FileNotFoundException e)
 			{
		 			return "";
 			}
		 		checkLine=new BufferedReader(reader);
		 		for (int index=-1;index<number;index++)
			 		{
			 			word=checkLine.readLine().toString();
			 			
			 		}
		 		reader.close();
	 		}
 		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
	    return word;
 	}
 	
 	/**
	  * First index that the specified string is found at. Returns -1 if it is not found.
	  *
	  * @param word the string to be searched in the text file
	  * @param file the text file to be searched
	  * @return the index that the string is located in the text file, -1 if it is not found
	  */
	 public static int indexOf(String word, String file) //Returns the index that the word is at.
 	{
 		int index=-1;
 		String temp;
 		BufferedReader checkLine=null;
 		try
	 		{
		 		FileReader reader = new FileReader(file);
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		if (!(checkLine.ready()))
			 		{
			 			index=-1;
			 			break;
			 		}
			 		index++;
			 		temp=checkLine.readLine().toString();
			 		if (temp.equals(word))
			 		{
			 			break;
			 		}
			 		
			 	}
	 		}
 		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
	    return index;
 	}
 	
 	/**
	  * Replace the first occurence of a line with another.
	  *
	  * @param oldWord the line to be replaces
	  * @param newWord the new line to replace the first occurence of oldWord
	  * @param file the text file
	  */
	 public static void replace(String oldWord, String newWord, String file) //Replaces the old word with the new word in a text file.
 	{
 		delete("temp.txt");
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
		 		FileReader reader = new FileReader(file);
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		temp=checkLine.readLine().toString();
			 		if (!(temp.equals(oldWord)))
			 		{
			 			writeLine(temp,"temp.txt");
			 		}
			 		else
			 		{
			 			writeLine(newWord,"temp.txt");
			 		}
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	delete(file);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), file);
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	
	 		}
 		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
 	}
 	
	 /**
	  * Replace the line at the specified index with a new line.
	  *
	  * @param index the index to be replaces
	  * @param newWord the new word to replace at the index
	  * @param file the text file
	  */
	 public static void replace(int index, String newWord, String file) //Replaces the word at specified index with the new word.
 	{
 		delete("temp.txt");
 		int increment=-1;
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
	 			
		 		FileReader reader = new FileReader(file);
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		increment++;
			 		temp=checkLine.readLine().toString();
			 		if (increment!=index)
			 		{
			 			writeLine(temp,"temp.txt");
			 		}
			 		else
			 		{
			 			writeLine(newWord,"temp.txt");
			 		}
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	delete(file);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), file);
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	
	 		}
 		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
 	}
 	
	 /**
	  * Checks if the text file is ready to be written and read from.
	  *
	  * @param file the text file
	  * @return true, if is ready to be read and wrote to
	  */
	 public static boolean isReady(String file) //Check if text file is ready to be written.
 	{
 		boolean check=true;
 		BufferedReader checkLine;
 		try
 		{
 			FileReader reader = new FileReader(file);
 			checkLine=new BufferedReader(reader);
 			check=checkLine.ready();
 		}
 		catch(IOException e)
 		{
 			e.printStackTrace();
 		}
 		return check;
 	}
 	
	 /**
	  * Delete line at the specified index.
	  *
	  * @param index the index to delete the line at
	  * @param file the text file
	  */
	 public static void deleteLine(int index, String file) //Deletes the line at a specified index.
 	{
 		delete("temp.txt");
 		int increment=-1;
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
	 			
		 		FileReader reader = new FileReader(file);
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		increment++;
			 		temp=checkLine.readLine().toString();
			 		if (increment!=index)
			 		{
			 			writeLine(temp,"temp.txt");
			 		}
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	delete(file);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), file);
			 		if (!(checkLine.ready()))
			 		{
			 			break;
			 		}
			 	}
			 	
	 		}
 		catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
 		
 	}
 	
	 /**
	  * Reads the entire text file and returns as a string
	  *
	  * @param file the text file
	  * @return the entire contents of the text file
	  */
	 public static String toString(String file)
 	{
 		BufferedReader checkLine;
 		String total="";
 		try
 		{
 		FileReader reader = new FileReader(file);
 		checkLine=new BufferedReader(reader);
 		while (true)
	 	{
	 		total+=checkLine.readLine().toString()+"\n";
	 		if (!(checkLine.ready()))
	 		{
	 			break;
	 		}
	 		
	 	}
	 	checkLine.close();
 		}
 		catch(IOException e)
 		{
 			e.printStackTrace();
 		}
	 	return total;
 	}
}