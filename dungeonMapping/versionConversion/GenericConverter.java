package dungeonMapping.versionConversion;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import dungeonMapping.model.DungeonWrapper;
import dungeonMapping.model.v1_3.DungeonMap;
import dungeonMapping.serializing.Persistence;
import dungeonMapping.serializing.PersistenceObjectStream;

public class GenericConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jframe = new JFrame();
		while (true) {
			File toLoad = getPath("Load:", jframe);
			if (toLoad == null) {
				break;
			}
			DungeonMap d = Persistence.load(toLoad);
			dungeonMapping.model.v1_2.DungeonMap dm = downgrade(d);
			PersistenceObjectStream.serialize(new DungeonWrapper(dm, "v1.2"), toLoad);
		}
		System.exit(0);
	}
	
	private static File getPath(String title, JFrame jframe) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(Persistence.folder));
		chooser.setDialogTitle(title);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Map file", Persistence.extension);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(jframe);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File f = chooser.getSelectedFile();
	    	if (f == null) {
	    		return null;
	    	}
	    	String filename = f.getName();
	    	if (filename.length() < Persistence.extension.length() + 1 || !filename.substring(
	    			filename.length() - Persistence.extension.length() - 1).equals("." + Persistence.extension)) {
	    		f = new File(f.toPath() + "." + Persistence.extension);
	    	}
	    	return f;
	    }
	    return null;
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
		case "v1.2":
			wrapper = new DungeonWrapper(convert((dungeonMapping.model.v1_2.DungeonMap) wrapper.dungeon), "v1.3");
		}
		return wrapper.dungeon;
	}

	private static dungeonMapping.model.v1_2.DungeonMap convert(dungeonMapping.model.v1_1.DungeonMap map) {
		dungeonMapping.model.v1_2.DungeonMap newMap = new dungeonMapping.model.v1_2.DungeonMap(map.getName());
		newMap.setDistanceMultiplier(map.getDistanceMultiplier());
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
	
	private static dungeonMapping.model.v1_3.DungeonMap convert(dungeonMapping.model.v1_2.DungeonMap map) {
		dungeonMapping.model.v1_3.DungeonMap newMap = new dungeonMapping.model.v1_3.DungeonMap(map.getName());
		newMap.setDistanceMultiplier(map.getDistanceMultiplier());
		for (dungeonMapping.model.v1_2.Node n:map.getNodes()) {
			dungeonMapping.model.v1_3.Node newN = newMap.addNode(n.getCoords().x, n.getCoords().y, 0);
			copyDescription(n.getDescription(), newN.getDescription());
		}
		for (dungeonMapping.model.v1_2.Edge e:map.getEdges()) {
			dungeonMapping.model.v1_2.Node n1 = map.getNode(e.getNode1());
			dungeonMapping.model.v1_2.Node n2 = map.getNode(e.getNode2());
			dungeonMapping.model.v1_3.Node newN1 = newMap.getNodeAt(n1.getCoords().x, n1.getCoords().y);
			dungeonMapping.model.v1_3.Node newN2 = newMap.getNodeAt(n2.getCoords().x, n2.getCoords().y);
			
			dungeonMapping.model.v1_3.Edge newE = newMap.connectNodes(newN1, newN2);
			
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
	private static void copyDescription(dungeonMapping.model.v1_2.Description oldD, dungeonMapping.model.v1_3.Description newD) {
		newD.colour = oldD.colour;
		newD.features = oldD.features;
		newD.length = oldD.length;
		newD.name = oldD.name;
		newD.note = oldD.note;
		newD.treasure = oldD.treasure;
		newD.width = oldD.width;
		newD.height = 0;
	}
	private static void copyDescription(dungeonMapping.model.v1_3.Description oldD, dungeonMapping.model.v1_2.Description newD) {
		newD.colour = oldD.colour;
		newD.features = oldD.features;
		newD.length = oldD.length;
		newD.name = oldD.name;
		newD.note = oldD.note;
		newD.treasure = oldD.treasure;
		newD.width = oldD.width;
	}
	
	private static dungeonMapping.model.v1_2.DungeonMap downgrade(dungeonMapping.model.v1_3.DungeonMap map) {
		dungeonMapping.model.v1_2.DungeonMap newMap = new dungeonMapping.model.v1_2.DungeonMap(map.getName());
		newMap.setDistanceMultiplier(map.getDistanceMultiplier());
		for (dungeonMapping.model.v1_3.Node n:map.getNodes()) {
			dungeonMapping.model.v1_2.Node newN = newMap.addNode(n.getCoords().x, n.getCoords().y);
			copyDescription(n.getDescription(), newN.getDescription());
		}
		for (dungeonMapping.model.v1_3.Edge e:map.getEdges()) {
			dungeonMapping.model.v1_3.Node n1 = map.getNode(e.getNode1());
			dungeonMapping.model.v1_3.Node n2 = map.getNode(e.getNode2());
			dungeonMapping.model.v1_2.Node newN1 = newMap.getNodeAt(n1.getCoords().x, n1.getCoords().y);
			dungeonMapping.model.v1_2.Node newN2 = newMap.getNodeAt(n2.getCoords().x, n2.getCoords().y);
			
			dungeonMapping.model.v1_2.Edge newE = newMap.connectNodes(newN1, newN2);
			
			copyDescription(e.getDescription(), newE.getDescription());
		}
		return newMap;
	}

}
