package dungeonMapping.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;


public class DungeonMap implements Serializable {
	private static final long serialVersionUID = -7953540481645760148L;
	private String name;
	private HashSet<Edge> edges;
	private HashSet<Node> nodes;
	private int elemsCreated=0;
	private double distanceMultiplier=1.;
	
	
	public Node addNode(int x, int y) {
		Node n = new Node(x,y, this, (name+": type:node: elem:"+elemsCreated++));
		getNodes().add(n);
		return n;
	}
	
	public Edge connectNodes(Node n1, Node n2) {
		if (n1!=null&&n2!=null&&n1!=n2) {
			Edge e = new Edge(n1, n2, this, (name+": type:edge: elem:"+elemsCreated++));
			getEdges().add(e);
			n1.getEdges().add(e);
			n2.getEdges().add(e);
			return e;
		}
		return null;
	}
	
	public DungeonMap(String name) {
		this.name=name;
	}
	
	public HashSet<Edge> getEdges() {
		if (edges==null) {
			edges = new HashSet<Edge>();
		}
		return edges;
	}
	public HashSet<Node> getNodes() {
		if (nodes==null) {
			nodes = new HashSet<Node>();
		}
		return nodes;
	}
	public Node getNodeAt(int x, int y) {
		for(Node n:nodes) {
			Coords c = n.getCoords();
			if (c.x==x&&c.y==y) {
				return n;
			}
		}
		return null;
	}
	public void removeNode(Node n) {
		if(getNodes().remove(n)) {
			for(Edge e:n.getEdges()) {
				removeEdge(e);
			}
		}
	}
	

	public void removeEdge(Edge e) {
		if(getEdges().remove(e)) {
			e.getNode1().getEdges().remove(e);
			e.getNode2().getEdges().remove(e);
		}
	}
	
	public void removeItem(GraphElement e) {
		if (e instanceof Node) {
			removeNode((Node)e);
		} else if (e instanceof Edge) {
			removeEdge((Edge)e);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o==this) {
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
}

