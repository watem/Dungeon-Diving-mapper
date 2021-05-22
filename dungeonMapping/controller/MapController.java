package dungeonMapping.controller;

import java.io.File;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.model.v1_1.DungeonMap;
import dungeonMapping.serializing.Persistence;

public class MapController {
	public static boolean newMap(String name, boolean save) {
		if(save) {
			MappingApplication.save();
		}
		Persistence.setFilename(name);
		MappingApplication.resetDungeon();
		
		File newMap = new File(name);
		if(newMap.exists()) {
			return false;
		}
		MappingApplication.save();
		return true;
	}
	
	public static boolean deleteMap(String name) {
		boolean isCurrentMap = MappingApplication.getDungeon().getName().equals(name);
		File map = new File(Persistence.folder+Persistence.clean(name)+Persistence.extension);
		boolean deleted = map.delete();
		if (isCurrentMap) {
			Persistence.reset();
		}
		return deleted;
	}

}
