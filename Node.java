package modelv2;

import java.util.HashSet;

public class Node extends GraphElement {
	HashSet<Edge> edges;
	Coords coords;
	
	public void addEdge(Edge e) {
		if (edges==null) {
			edges = new HashSet<>();
		}
		edges.add(e);
	}

	public Coords getCoords() {
		return coords;
	}

//	public Node(Edge edge, Coords coords) {
//		super();
//		edges = new HashSet<>();
//		edges.add(edge);
//		this.coords = coords;
//	}
//
//	public Node(Coords coords) {
//		super();
//		this.coords = coords;
//	}
	
	public Node(int x, int y) {
		super();
		coords = new Coords(x,y);
	}
	public HashSet<Edge> getEdges() {
		if (edges==null) {
			edges = new HashSet<Edge>();
		}
		return edges;
	}
	public void moveNode(int x, int y) {
		coords = new Coords(x,y);
	}
	
}
