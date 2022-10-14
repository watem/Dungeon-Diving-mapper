package dungeonMapping.serializing;

import java.io.File;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.model.DungeonWrapper;
import dungeonMapping.model.v1_3.DungeonMap;
import dungeonMapping.versionConversion.GenericConverter;

public class Persistence {

	public static final String folder = "./maps/";
	public static final String extension = "map";
	public static final String defaultFilename = "~dungeon";
	private static String filename = defaultFilename;
	
	public static final File defaultPath = new File(folder+defaultFilename+"."+extension);
	private static File path = defaultPath;

	

	public static void save(DungeonMap dungeon) {
		checkFolder();
		PersistenceObjectStream.serialize(new DungeonWrapper(dungeon), path);
	}

	public static void quickSave(DungeonMap dungeon) {
		checkFolder();
		PersistenceObjectStream.serialize(new DungeonWrapper(dungeon), defaultPath);
	}

	public static DungeonMap load() {
		checkFolder();
		
		DungeonWrapper wrapper = (DungeonWrapper) PersistenceObjectStream.deserialize(path);
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
	
	public static DungeonMap load(File p) {
		setPath(p);
		return load();
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

	public static void setPath(File p) {
		filename = p.getName();
		path = p;
	}

	public static File getPath() {
		return path;
	}
}