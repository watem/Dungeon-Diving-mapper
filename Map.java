package modelv2;

import java.io.Serializable;
import java.util.HashSet;

public class Map implements Serializable {
	private static final long serialVersionUID = -7953540481645760148L;
	String name;
	HashSet<Edge> edges;
	HashSet<Node> nodes;
	
	
	public Node addNode(int x, int y) {
		Node n = new Node(x,y);
		getNodes().add(n);
		return n;
	}
	
	public Edge connectNodes(Node n1, Node n2) {
		if (n1!=null&&n2!=null) {
			Edge e = new Edge(n1, n2);
			getEdges().add(e);
			n1.getEdges().add(e);
			n2.getEdges().add(e);
			return e;
		}
		return null;
	}
	
	public Map(String name) {
		this.name=name;
	}
	
	public HashSet<Edge> getEdges() {
		if (edges==null) {
			edges = new HashSet<Edge>();
		}
		return edges;
	}
	public HashSet<Node> getNodes() {
		if (edges==null) {
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
			e.node1.getEdges().remove(e);
			e.node2.getEdges().remove(e);
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

}

