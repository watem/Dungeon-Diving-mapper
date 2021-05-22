package dungeonMapping.serializing;

import java.io.File;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.model.DungeonWrapper;
import dungeonMapping.model.v1_1.DungeonMap;
import dungeonMapping.versionConversion.GenericConverter;

public class Persistence {

	public static final String folder = "maps/";
	public static final String extension = ".map";

	public static final String defaultFilename = "~dungeon";
	private static String filename = defaultFilename;

	public static void save(DungeonMap dungeon) {
		checkFolder();
		PersistenceObjectStream.serialize(new DungeonWrapper(dungeon), folder + clean(filename) + extension);
	}

	public static String clean(String name) {
		if (name != null) {
			name = name.replace(' ', '_');
		}
		return name;
	}

	public static void quickSave(DungeonMap dungeon) {
		checkFolder();
		PersistenceObjectStream.serialize(new DungeonWrapper(dungeon), folder + clean(defaultFilename) + extension);
	}

	public static DungeonMap load() {
		checkFolder();
		DungeonWrapper wrapper = (DungeonWrapper) PersistenceObjectStream.deserialize(folder + clean(filename) + extension);
		DungeonMap dungeon;
		// model cannot be loaded - create empty map
		if (wrapper == null) {
			dungeon = new DungeonMap(filename);
		} else {
			dungeon = (DungeonMap) GenericConverter.convert(wrapper);
		}
		MappingApplication.setDungeon(dungeon);
		return dungeon;
	}

	public static void setFilename(String newFilename) {
		filename = newFilename;
	}

	public static DungeonMap reset() {
		checkFolder();
		filename = defaultFilename;
		DungeonMap dungeon = new DungeonMap("new dungeon");
		MappingApplication.setDungeon(dungeon);
		return dungeon;
	}

	private static void checkFolder() {
		File f = new File(folder);
		f.mkdir();
	}
}
