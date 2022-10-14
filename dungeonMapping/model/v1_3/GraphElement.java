package dungeonMapping.model.v1_3;

import java.io.Serializable;
import java.util.HashMap;

public class GraphElement implements Serializable {

	private static final long serialVersionUID = 2908245557754004004L;
	private Description d;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	DungeonMap m;
	private static HashMap<String, GraphElement> elementsByName = new HashMap<String, GraphElement>();

	public GraphElement(DungeonMap m) {
		this.m = m;
	}

	public GraphElement getElementByid(String id) {
		return elementsByName.get(id);
	}

	public Description getDescription() {
		if (d == null) {
			d = new Description();
		}
		if (this instanceof Edge) {
			d.length = (int) ((Edge) this).getLength();
		} else if (this instanceof Node) {
			d.height = ((Node) this).getCoords().z;
		}
		
		if (d.name == null) {
			d.name = "";
		}
		if (d.features == null) {
			d.features = "";
		}
		if (d.treasure == null) {
			d.treasure = "";
		}
		if (d.note == null) {
			d.note = "";
		}
		return d;
	}

	
}
