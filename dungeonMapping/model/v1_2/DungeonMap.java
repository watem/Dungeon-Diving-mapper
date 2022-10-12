package dungeonMapping.model.v1_2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class DungeonMap implements Serializable {
	private static final long serialVersionUID = -7953540481645760148L;
	private String name;
	private HashSet<Edge> edges;
	private HashSet<Node> nodes;
	private transient HashMap<String, Edge> edgesById;
	private transient HashMap<String, Node> nodesById;
//	private transient HashMap<Coords, Node> nodesByCoord;
	private int elemsCreated = 0;
	private double distanceMultiplier = 1.;

	public Node addNode(int x, int y) {
		Node n = new Node(x, y, this, (name + ": type:node: elem:" + elemsCreated++));
		getNodes().add(n);
		if (nodesById==null) {
			resetHashMaps();
		}
		nodesById.put(n.getId(), n);
		return n;
	}

	public Edge connectNodes(Node n1, Node n2) {
		if (n1 != null && n2 != null && n1 != n2) {
			Edge e = new Edge(n1, n2, this, (name + ": type:edge: elem:" + elemsCreated++));
			HashSet<Edge> pEdges = getEdges();
			if (pEdges.contains(e)) {
				return null;
			}
			pEdges.add(e);
			if (edgesById==null) {
				resetHashMaps();
			}
			edgesById.put(e.getId(), e);
			n1.getEdges().add(e.getId());
			n2.getEdges().add(e.getId());
			return e;
		}
		return null;
	}

	public DungeonMap(String name) {
		this.name = name;
	}

	public HashSet<Edge> getEdges() {
		if (edges == null) {
			edges = new HashSet<Edge>();
		}
		return edges;
	}

	public HashSet<Node> getNodes() {
		if (nodes == null) {
			nodes = new HashSet<Node>();
		}
		return nodes;
	}

	public Edge getEdge(String id) {
		if (edgesById == null) {
			resetHashMaps();
		}
		return edgesById.get(id);
	}
	public Edge getEdge(Node n1, Node n2) {
		if (n1==null||n2==null||n1==n2) {
			return null;
		}
		for(String s:n1.getEdges()) {
			Edge e = getEdge(s);
			if (getNode(e.getNode1()).equals(n2)||getNode(e.getNode2()).equals(n2)) {
				return e;
			}
		}
		return null;
	}

	public Node getNode(String id) {
		if (nodesById == null) {
			resetHashMaps();
		}
		return nodesById.get(id);
	}

	public Node getNodeAt(int x, int y) {
//		if(nodesByCoord==null) {
//			resetHashMaps();
//		}
//		return nodesByCoord.get(new Coords(x,y));
		Coords c = new Coords(x, y);
		for (Node n : getNodes()) {
			if (n.getCoords().equals(c)) {
				return n;
			}
		}
		return null;
	}

	public Node getNodeAt(Coords c) {
//		if(nodesByCoord==null) {
//			resetHashMaps();
//		}
//		return nodesByCoord.get(c);
		for (Node n : getNodes()) {
			if (n.getCoords().equals(c)) {
				return n;
			}
		}
		return null;
	}

	public void removeNode(Node n) {
		if (n==null) {
			return;
		}
		if (nodesById!=null) {
			nodesById.remove(n.getId());
		}
		getNodes().remove(n);
		for (Edge e : edges(n.getEdges())) {
			removeEdge(e);
		}
	}

	public HashSet<Edge> edges(HashSet<String> ids) {
		HashSet<Edge> edgeSet = new HashSet<>();
		for (String id : ids) {
			edgeSet.add(getEdge(id));
		}
		return edgeSet;
	}

	public HashSet<Node> nodes(HashSet<String> ids) {
		HashSet<Node> nodeSet = new HashSet<>();
		for (String id : ids) {
			nodeSet.add(getNode(id));
		}
		return nodeSet;
	}

	public void removeEdge(Edge e) {
		if (e==null) {
			return;
		}
		Node n1 = getNode(e.getNode1());
		Node n2 = getNode(e.getNode2());
		if (edgesById!=null) {
			edgesById.remove(e.getId());
		}
		getEdges().remove(e);
		if (n1!=null) {
			n1.getEdges().remove(e.getId());
		}
		if (n2!=null) {
			n2.getEdges().remove(e.getId());
		}
	}

	public void removeItem(GraphElement e) {
		if (e instanceof Node) {
			removeNode((Node) e);
		} else if (e instanceof Edge) {
			removeEdge((Edge) e);
		}
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DungeonMap)) {
			return false;
		}
		DungeonMap m = (DungeonMap) o;
		return m.name.equals(this.name);
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the distanceMultiplier
	 */
	public double getDistanceMultiplier() {
		return distanceMultiplier;
	}

	/**
	 * @param distanceMultiplier the distanceMultiplier to set
	 */
	public void setDistanceMultiplier(double distanceMultiplier) {
		this.distanceMultiplier = distanceMultiplier;
	}

	public void setName(String s) {
		name = s;

	}

	public void resetHashMaps() {
		edgesById = new HashMap<>();
		nodesById = new HashMap<>();
//		nodesByCoord = new HashMap<>();

		for (Edge e : getEdges()) {
			edgesById.put(e.getId(), e);
		}
		for (Node n : getNodes()) {
			nodesById.put(n.getId(), n);
//			nodesByCoord.put(n.getCoords(), n);
		}
	}
}
