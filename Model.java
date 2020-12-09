package modelv2;

import java.util.HashSet;

public class Model {
	HashSet<Map> maps;
	
	public Map createNewMap(String name) {
		Map m = new Map(name);
		getMaps().add(m);
		return m;
	}
	
	private HashSet<Map> getMaps() {
		if (maps==null) {
			maps = new HashSet<>();
		}
		return maps;
	}
}
