/*
 * 
 */
package dynamicAnalysis;

/**
 * The Enum Version.
 */
public enum Version {
    
    /** The x 32. */
    x32(false),
	
	/** The x 64. */
	x64(true);
    
    /** The value. */
    private final boolean value;

    /**
     * Instantiates a new version.
     *
     * @param value the value
     */
    private Version(boolean value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public boolean getValue() {
    	switch(Boolean.hashCode(value)) {
    	case 1: return true;
    	case 0: return false;
    	}
        return value;
    }
    
}