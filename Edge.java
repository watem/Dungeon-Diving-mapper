package modelv2;

import java.io.Serializable;
import java.util.HashSet;

public class Edge extends GraphElement {
	Node node1;
	Node node2;
	
	
	public double getLength() {
		Coords c1 = node1.getCoords();
		Coords c2 = node2.getCoords();
		int x = c1.x-c2.x;
		int y = c1.y-c2.y;
		return Math.sqrt(x*x+y*y);
	}


	public Edge(Node node1, Node node2, DungeonMap m) {
		super(m);
		this.node1 = node1;
		this.node2 = node2;
	}
}
