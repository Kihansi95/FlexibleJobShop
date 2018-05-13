package utility;

import jobshopflexible.Configuration;

public abstract class Verbose {
	
	private static final String VERBOSE = "data.verbose";
	protected boolean verbose;
	
	public Verbose(Configuration conf, String prefix) {
		boolean verbose = false;
		String v =  conf.getParam(prefix + "." +VERBOSE);
		verbose = v != null && !v.toUpperCase().equalsIgnoreCase("FALSE");
	}
}
