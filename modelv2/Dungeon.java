package modelv2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dungeon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6204328942304442080L;
	List<DungeonMap> maps;
	
	public void addMap(DungeonMap m) {
		getMaps().add(m);
	}
	
	public boolean addMap(String mapName) {
		DungeonMap m = new DungeonMap(mapName);
		
		if (getMaps().contains(m)) {
			return false;
		} else {
			maps.add(m);
			return true;
		}
	}
	
	public List<DungeonMap> getMaps() {
		if (maps==null) {
			maps = new ArrayList<>();
		}
		return maps;
	}
}
