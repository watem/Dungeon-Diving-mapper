package dungeonMapping.versionConversion;

import java.util.ArrayList;

import dungeonMapping.old.EdgeGetter;

public class Converter {
	
	public static void main(String[] args) {
		convert(dungeonMapping.old.serializing.Persistence.load());
	}
	public static void convert(dungeonMapping.old.Dungeon d) {
		ArrayList<dungeonMapping.model.DungeonMap> maps = new ArrayList<>();
		for(dungeonMapping.old.DungeonMap m:d.getMaps()) {
			maps.add(convert(m));
		}
		for(dungeonMapping.model.DungeonMap m:maps) {
			dungeonMapping.serializing.Persistence.setFilename(m.getName());
			dungeonMapping.serializing.Persistence.save(m);
		}
	}
	
	public static dungeonMapping.model.DungeonMap convert(dungeonMapping.old.DungeonMap m) {
		dungeonMapping.model.DungeonMap map = new dungeonMapping.model.DungeonMap(m.getName());
		for(dungeonMapping.old.Node n:m.getNodes()) {
			dungeonMapping.model.Node node = map.addNode(n.getCoords().x, n.getCoords().y);
			updateDescription(node, n.getDescription());
		}
		for(dungeonMapping.old.Edge e:m.getEdges()) {
			dungeonMapping.old.Node n1 = EdgeGetter.n1(e);
			dungeonMapping.old.Node n2 = EdgeGetter.n2(e);
			dungeonMapping.model.Edge edge = map.connectNodes(map.getNodeAt(n1.getCoords().x, n1.getCoords().y), map.getNodeAt(n2.getCoords().x, n2.getCoords().y));
			updateDescription(edge, e.getDescription());
		}
		return map;
	}
	
	public static void updateDescription(dungeonMapping.model.GraphElement ge, dungeonMapping.old.GraphElement.Description d) {
		dungeonMapping.model.GraphElement.Description de = ge.getDescription();
		de.colour = d.colour;
		de.features = d.features;
		de.length = d.length;
		de.name = d.name;
		de.note = d.note;
		de.treasure = d.treasure;
		de.width = d.width;
	}

}
