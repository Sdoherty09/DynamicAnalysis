package dynamicAnalysis;

public enum Version {
    x32(false),
	x64(true);
    private final boolean value;

    private Version(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
    	switch(Boolean.hashCode(value)) {
    	case 1: return true;
    	case 0: return false;
    	}
        return value;
    }
    
}