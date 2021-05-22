package dungeonMapping.versionConversion;

import dungeonMapping.model.DungeonWrapper;
import dungeonMapping.model.v1_1.DungeonMap;

public class GenericConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static Object convert(DungeonWrapper wrapper) {
		if (wrapper.version.equals(DungeonWrapper.currentVersion)) {
			return wrapper.dungeon;
		}
		int major = DungeonWrapper.getMajorVersion(wrapper.version);
		int minor = DungeonWrapper.getMinorVersion(wrapper.version);
		
		// TODO Auto-generated method stub
		return null;
	}

}
