package dungeonMapping.old;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

public class GraphElement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2908245557754004004L;
	Description d;
	String id;
	DungeonMap m;
	private static HashMap<String, GraphElement> elementsByName = new HashMap<String, GraphElement>();
	
	public GraphElement(DungeonMap m) {
		this.m = m;
	}
//	public GraphElement(String id) {
//		this.id = id;
//		elementsByName.put(id, this);
//	}
	public GraphElement getElementByid(String id) {
		return elementsByName.get(id);
	}
	
	public class Description implements Serializable{
		private static final long serialVersionUID = -5645613373273383286L;
		public String name;
		public int width, length;
		public String features, treasure, note;
		public Color colour = Color.BLACK;
	}
	
	public Description getDescription() {
		if (d==null) {
			d = new Description();
		}
		if (this instanceof Edge) {
			d.length=(int)(((Edge)this).getLength()*m.distanceMultiplier);
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
