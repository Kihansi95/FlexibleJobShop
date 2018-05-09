package output;

public class PdfWriter {

	private String directory;
	private String filename;
	private String content;
	
	private static final String endLine = System.getProperty("line.separator")+System.getProperty("line.separator"); // for \\
	
	public PdfWriter(String directory, String filename) {
		this.directory = directory;
		this.filename = filename;
	}
	
}
