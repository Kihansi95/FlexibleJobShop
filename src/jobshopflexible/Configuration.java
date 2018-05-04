package jobshopflexible;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	public String DataPath;
	public Mode mode;
	
	private static final String CONF = "conf/fjs.conf";
	
	public Configuration() {
		final Properties prop = new Properties();
		InputStream input = null;
		
		try {

			input = new FileInputStream(CONF);

			// load conf file
			prop.load(input);

			this.DataPath = prop.getProperty("dataPath");
			

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
}
