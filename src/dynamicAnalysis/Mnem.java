package dynamicAnalysis;

public enum Mnem {
	JNE, JMP, JL, JBE, JE, JAE, JB, JA, JLE;
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
