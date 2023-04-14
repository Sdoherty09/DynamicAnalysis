/*
 * 
 */
package dynamicAnalysis;

/**
 * The Enum Mnem.
 */
public enum Mnem {
	
	/** The jne. */
	JNE, 
 /** The jmp. */
 JMP, 
 /** The jl. */
 JL, 
 /** The jbe. */
 JBE, 
 /** The je. */
 JE, 
 /** The jae. */
 JAE, 
 /** The jb. */
 JB, 
 /** The ja. */
 JA, 
 /** The jle. */
 JLE;
	
	/**
	 * Gets the byte.
	 *
	 * @return the byte
	 */
	public byte getByte()
	{
		switch(this)
		{
		case JAE:
			return 115;
		case JBE:
			return 118;
		case JE:
			return 15;
		case JL:
			return 124;
		case JMP:
			return -21;
		case JNE:
			return 117;
		case JB:
			return 114;
		case JA:
			return 119;
		case JLE:
			return 126;
		default:
			return 0;
			
		}
	}
}
