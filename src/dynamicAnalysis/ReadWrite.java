/*
 * 
 */
package dynamicAnalysis;
import java.io.*;

/**
 * The Class ReadWrite.
 */
public class ReadWrite
{
 	
	 /**
	  * Write.
	  *
	  * @param word the word
	  * @param name the name
	  */
	 public static void write(String word, String name) //Method for clearing a text file and writing a line.
 	{
 		PrintWriter delete=null;
 		try
	 		{
 				delete = new PrintWriter(name);
		        FileWriter writer = new FileWriter(name);
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
	  * Write line.
	  *
	  * @param word the word
	  * @param name the name
	  */
	 public static void writeLine(String word, String name) //Method for adding a line to a text file.
 	{
 		try
	 		{
		        FileWriter writer = new FileWriter(name, true);
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
	  * @param name the name
	  */
	 public static void delete(String name) //Method for clearing a text file.
 	{
 		write("", name);
 	}
 	
	 /**
	  * Gets the length.
	  *
	  * @param name the name
	  * @return the length
	  */
	 public static int getLength(String name) //Returns how many lines are in the text file.
 	{
 		
 	//	writeLine("", name);
 		int count=1;
 		@SuppressWarnings("unused")
		String dummy;
 		BufferedReader checkLine=null;
 		try
 		{
 		FileReader reader = new FileReader(name);
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
	  * Gets the line.
	  *
	  * @param number the number
	  * @param name the name
	  * @return the line
	  */
	 public static String getLine(int number, String name) //Method for returning a line from a text file.
 	{
 		String word="";
 		BufferedReader checkLine=null;
 		FileReader reader=null;
 		try
	 		{
 			try
 			{
 				reader = new FileReader(name);
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
	  * Index of.
	  *
	  * @param word the word
	  * @param name the name
	  * @return the int
	  */
	 public static int indexOf(String word, String name) //Returns the index that the word is at.
 	{
 		int index=-1;
 		String temp;
 		BufferedReader checkLine=null;
 		try
	 		{
		 		FileReader reader = new FileReader(name);
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
	  * Replace.
	  *
	  * @param oldWord the old word
	  * @param newWord the new word
	  * @param name the name
	  */
	 public static void replace(String oldWord, String newWord, String name) //Replaces the old word with the new word in a text file.
 	{
 		delete("temp.txt");
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
		 		FileReader reader = new FileReader(name);
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
			 	delete(name);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), name);
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
	  * Replace.
	  *
	  * @param index the index
	  * @param newWord the new word
	  * @param name the name
	  */
	 public static void replace(int index, String newWord, String name) //Replaces the word at specified index with the new word.
 	{
 		delete("temp.txt");
 		int increment=-1;
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
	 			
		 		FileReader reader = new FileReader(name);
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
			 	delete(name);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), name);
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
	  * Checks if is ready.
	  *
	  * @param name the name
	  * @return true, if is ready
	  */
	 public static boolean isReady(String name) //Check if text file is ready to be written.
 	{
 		boolean check=true;
 		BufferedReader checkLine;
 		try
 		{
 			FileReader reader = new FileReader(name);
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
	  * Delete line.
	  *
	  * @param index the index
	  * @param name the name
	  */
	 public static void deleteLine(int index,String name) //Deletes the line at a specified index.
 	{
 		delete("temp.txt");
 		int increment=-1;
 		String temp="";
 		BufferedReader checkLine;
 		try
	 		{
	 			
		 		FileReader reader = new FileReader(name);
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
			 	delete(name);
			 	reader = new FileReader("temp.txt");
		 		checkLine=new BufferedReader(reader);
		 		while (true)
			 	{
			 		writeLine(checkLine.readLine().toString(), name);
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
	  * To string.
	  *
	  * @param name the name
	  * @return the string
	  */
	 public static String toString(String name)
 	{
 		BufferedReader checkLine;
 		String total="";
 		try
 		{
 		FileReader reader = new FileReader(name);
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