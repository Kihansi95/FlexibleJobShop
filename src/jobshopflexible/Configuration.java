package jobshopflexible;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	public String DataPath;
	public Mode mode;
	public String pdfLatexPath;
	
	private  final Properties prop = new Properties();
	
	private static final String CONF = "conf/fjs.conf";
	
	public Configuration() {
		
		InputStream input = null;
		
		try {
			input = new FileInputStream(CONF);

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
