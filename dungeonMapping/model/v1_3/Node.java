package dungeonMapping.model.v1_3;

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

	public Node(int x, int y, int z, DungeonMap m, String id) {
		super(m);
		coords = new Coords(x, y, z);
		this.setId(id);
	}

	public HashSet<String> getEdges() {
		if (edges == null) {
			edges = new HashSet<String>();
		}
		return edges;
	}

	public void moveNode(int x, int y, int z) {
		coords = new Coords(x, y, z);
		for (String s: getEdges()) {
			Edge e = m.getEdge(s);
			if (getId().equals(e.getNode1())) {
				e.setNode1(this);
			} else if (getId().equals(e.getNode1())) {
				e.setNode2(this);
			}
		}
	}

	@Override
	public String toString() {
		return getId()+coords;
	}

}
