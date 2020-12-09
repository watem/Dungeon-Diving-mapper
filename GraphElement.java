package modelv2;

import java.io.Serializable;

public class GraphElement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2908245557754004004L;
	Description d;
	
	
	public class Description implements Serializable{
		private static final long serialVersionUID = -5645613373273383286L;
		String name;
		int width, length;
		String features, treasure, note;
	}
}
