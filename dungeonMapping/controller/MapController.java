package dungeonMapping.controller;

import java.io.File;
import java.util.HashSet;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.model.v1_3.*;
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
	
	public static void updateHeight(Description d, int height) {
		HashSet<Node> nodes = MappingApplication.getDungeon().getNodes();
		for (Node node: nodes) {
			if (node.getDescription() == d) {
				Coords c = node.getCoords();
				node.moveNode(c.x, c.y, height);
				break;
			}
		}
	}
	

}
