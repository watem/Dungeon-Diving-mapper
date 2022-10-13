package dungeonMapping.controller;

import java.io.File;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.serializing.Persistence;

public class MapController {
	public static boolean newMap(File path, boolean save) {
		if (save) {
			MappingApplication.save();
		}
		Persistence.setPath(path);
		MappingApplication.resetDungeon();

		if (path.exists()) {
			return false;
		}
		MappingApplication.save();
		return true;
	}

	public static boolean deleteMap(File path) {
		boolean isCurrentMap = MappingApplication.getDungeon().getName().equals(path.getName());
		boolean deleted = path.delete();
		if (isCurrentMap) {
			Persistence.reset();
		}
		return deleted;
	}
	

}
