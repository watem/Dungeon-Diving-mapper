package dungeonMapping.versionConversion;

import dungeonMapping.model.DungeonWrapper;

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
		return wrapper.dungeon;
	}

	private static dungeonMapping.model.v1_2.DungeonMap convert(dungeonMapping.model.v1_1.DungeonMap map) {
		dungeonMapping.model.v1_2.DungeonMap newMap = new dungeonMapping.model.v1_2.DungeonMap(map.getName());
		for (dungeonMapping.model.v1_1.Node n:map.getNodes()) {
			dungeonMapping.model.v1_2.Node newN = newMap.addNode(n.getCoords().x, n.getCoords().y);
			copyDescription(n.getDescription(), newN.getDescription());
		}
		for (dungeonMapping.model.v1_1.Edge e:map.getEdges()) {
			dungeonMapping.model.v1_1.Node n1 = map.getNode(e.getNode1());
			dungeonMapping.model.v1_1.Node n2 = map.getNode(e.getNode2());
			dungeonMapping.model.v1_2.Node newN1 = newMap.getNodeAt(n1.getCoords().x, n1.getCoords().y);
			dungeonMapping.model.v1_2.Node newN2 = newMap.getNodeAt(n2.getCoords().x, n2.getCoords().y);
			
			dungeonMapping.model.v1_2.Edge newE = newMap.connectNodes(newN1, newN2);
			
			copyDescription(e.getDescription(), newE.getDescription());
		}
		return newMap;
	}
	
	private static void copyDescription(dungeonMapping.model.v1_1.GraphElement.Description oldD, dungeonMapping.model.v1_2.Description newD) {
		newD.colour = oldD.colour;
		newD.features = oldD.features;
		newD.length = oldD.length;
		newD.name = oldD.name;
		newD.note = oldD.note;
		newD.treasure = oldD.treasure;
		newD.width = oldD.width;
	}

}
