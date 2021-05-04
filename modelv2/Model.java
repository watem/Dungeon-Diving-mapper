package modelv2;

import java.util.HashSet;

public class Model {
	HashSet<DungeonMap> maps;
	
	public DungeonMap createNewMap(String name) {
		DungeonMap m = new DungeonMap(name);
		getMaps().add(m);
		return m;
	}
	
	private HashSet<DungeonMap> getMaps() {
		if (maps==null) {
			maps = new HashSet<>();
		}
		return maps;
	}
}
