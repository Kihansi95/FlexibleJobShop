package jobshopflexible;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	public String DataPath;
	public String pdfLatexPath;
	
	private  final Properties prop = new Properties();
		
	public Configuration(String filename) {
		
		InputStream input = null;
		
		try {
			input = new FileInputStream(filename);

			// load conf file
			prop.load(input);

		} catch (final IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * Return the value corresponding the parameter
	 * @param paramName
	 * @return found value or null
	 */
	public String getParam(String paramName) {
		return prop.getProperty(paramName);
	}
	
	/**
	 * Return the value corresponding the parameter
	 * @param paramName
	 * @param optional 
	 * @return found value or the optional
	 */
	public String getParam(String paramName, String optional) {
		return prop.getProperty(paramName, optional);
	}
}
