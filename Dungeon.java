package modelv2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dungeon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6204328942304442080L;
	List<Map> maps;
	
	public void addMap(Map m) {
		getMaps().add(m);
	}
	
	public List<Map> getMaps() {
		if (maps==null) {
			maps = new ArrayList<>();
		}
		return maps;
	}
}
