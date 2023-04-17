/*
 * 
 */
package dynamicAnalysis;

/**
 * The Enum for mnemonics used by the x86 instruction set.
 */
public enum Mnem {
	
	/** Jump Not Equal. */
	JNE, 
 /** Jump. */
 JMP, 
 /** Jump Less than. */
 JL, 
 /** Jump Before or Equal. */
 JBE, 
 /** Jump if Equal. */
 JE, 
 /** The jae. */
 JAE, 
 /** Jump Below. */
 JB, 
 /** Jump Above. */
 JA, 
 /** Jump if Less or Equal. */
 JLE;
	
	/**
	 * Gets the byte value of the jump instruction.
	 *
	 * @return the byte value of the jump instruction
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
