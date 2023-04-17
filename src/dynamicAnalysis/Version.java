/*
 * 
 */
package dynamicAnalysis;

/**
 * The Version enum. Translates a boolean value into either x32 or x64 for a PE file
 */
public enum Version {
    
    /** The x32 value, set to false. */
    x32(false),
	
	/** The x64 value, set to true. */
	x64(true);
    
    /** The boolean to translate into either x32 or x64. */
    private final boolean value;

    /**
     * Instantiates a new version enum.
     *
     * @param value the boolean value to translate into a PE version
     */
    private Version(boolean value) {
        this.value = value;
    }

    /**
     * Gets the boolean value from the PE version.
     *
     * @return the boolean value
     */
    public boolean getValue() {
    	switch(Boolean.hashCode(value)) {
    	case 1: return true;
    	case 0: return false;
    	}
        return value;
    }
    
}