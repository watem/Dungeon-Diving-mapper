package dungeonMapping.model.v1_2;

import java.util.HashSet;



@SuppressWarnings("serial")
public class Node extends GraphElement {
	private HashSet<String> edges;
	private Coords coords;

	public void addEdge(Edge e) {
		if (edges == null) {
			edges = new HashSet<>();
		}
		edges.add(e.getId());
	}

	public Coords getCoords() {
		return coords;
	}

	public Node(int x, int y, DungeonMap m, String id) {
		super(m);
		coords = new Coords(x, y);
		this.setId(id);
	}

	public HashSet<String> getEdges() {
		if (edges == null) {
			edges = new HashSet<String>();
		}
		return edges;
	}

	public void moveNode(int x, int y) {
		coords = new Coords(x, y);
	}

	@Override
	public String toString() {
		return getId()+coords;
	}

}
