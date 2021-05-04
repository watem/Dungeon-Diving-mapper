package dungeonMapping.versionConversion;

import java.util.ArrayList;

import modelv2.EdgeGetter;

public class Converter {
	
	public static void main(String[] args) {
		convert(modelv2.serializing.Persistence.load());
	}
	public static void convert(modelv2.Dungeon d) {
		ArrayList<dungeonMapping.model.DungeonMap> maps = new ArrayList<>();
		for(modelv2.DungeonMap m:d.getMaps()) {
			maps.add(convert(m));
		}
		for(dungeonMapping.model.DungeonMap m:maps) {
			dungeonMapping.serializing.Persistence.setFilename(m.getName());
			dungeonMapping.serializing.Persistence.save(m);
		}
	}
	
	public static dungeonMapping.model.DungeonMap convert(modelv2.DungeonMap m) {
		dungeonMapping.model.DungeonMap map = new dungeonMapping.model.DungeonMap(m.getName());
		for(modelv2.Node n:m.getNodes()) {
			dungeonMapping.model.Node node = map.addNode(n.getCoords().x, n.getCoords().y);
			updateDescription(node, n.getDescription());
		}
		for(modelv2.Edge e:m.getEdges()) {
			modelv2.Node n1 = EdgeGetter.n1(e);
			modelv2.Node n2 = EdgeGetter.n2(e);
			dungeonMapping.model.Edge edge = map.connectNodes(map.getNodeAt(n1.getCoords().x, n1.getCoords().y), map.getNodeAt(n2.getCoords().x, n2.getCoords().y));
			updateDescription(edge, e.getDescription());
		}
		return map;
	}
	
	public static void updateDescription(dungeonMapping.model.GraphElement ge, modelv2.GraphElement.Description d) {
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
