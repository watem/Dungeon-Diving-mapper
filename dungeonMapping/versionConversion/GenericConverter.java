package dungeonMapping.versionConversion;

import dungeonMapping.model.DungeonWrapper;
import dungeonMapping.model.v1_1.DungeonMap;

public class GenericConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static Object convert(DungeonWrapper wrapper) {
		if (wrapper.getVersion().equals(DungeonWrapper.currentVersion)) {
			return wrapper.dungeon;
		}

		return convert(wrapper, DungeonWrapper.currentVersion);
	}

	public static Object convert(DungeonWrapper wrapper, String updatedVersion) {
		int major = DungeonWrapper.getMajorVersion(wrapper.getVersion());
		int minor = DungeonWrapper.getMinorVersion(wrapper.getVersion());
		int updatedMajor = DungeonWrapper.getMajorVersion(updatedVersion);
		int updatedMinor = DungeonWrapper.getMinorVersion(updatedVersion);

		if (major >= updatedMajor && minor > updatedMinor) {
			throw new RuntimeException("cannot downgrade version");
		}
		switch (wrapper.getVersion()) {
		case "v1.1":
			wrapper = new DungeonWrapper(convert((dungeonMapping.model.v1_1.DungeonMap) wrapper.dungeon), "v1.2");
		}
		return wrapper;
	}

	private static dungeonMapping.model.v1_1.DungeonMap convert(dungeonMapping.model.v1_1.DungeonMap map) {
		return map;
	}

}
