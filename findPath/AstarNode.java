package findPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import dungeonMapping.model.v1_3.DungeonMap;
import dungeonMapping.model.v1_3.Edge;
import dungeonMapping.model.v1_3.Node;

public class AstarNode {
	double distanceTraveled;
	double distanceRemaining;
	List<Node> path;
	Node currentNode;
	Node destination;
	DungeonMap map;
	
	
	public AstarNode(DungeonMap map, Node source, Node sink) {
		distanceTraveled = 0;
		path = new ArrayList<>();
		path.add(source);
		currentNode = source;
		destination = sink;
		distanceRemaining = distance(source, sink, map);
		this.map = map;
	}
	public AstarNode(AstarNode parent, Node next) {
		map = parent.map;
		path = new ArrayList<>(parent.path);
		Node previous = parent.currentNode;
		path.add(next);
		distanceTraveled = parent.distanceTraveled + distance(previous, next, map);
		destination = parent.destination;
		distanceRemaining = distance(next, destination, map);
		currentNode = next;
	}
	
	public static List<Node> findBestPath(DungeonMap map, Node source, Node sink) {
		Comparator<AstarNode> c = new Comparator<AstarNode>() {
			@Override
			public int compare(AstarNode o1, AstarNode o2) {
				if (o1==null||o2==null) {
					return 0;
				}
				return (int) (o1.eval()-o2.eval());
			}
		};
		PriorityQueue<AstarNode> pq = new PriorityQueue<>(c);
		AstarNode start = new AstarNode(map, source, sink);
		pq.add(start);
		while (!pq.isEmpty()) {
			AstarNode current = pq.poll();
			if (current.currentNode.equals(current.destination)) {
				return current.path;
			}
			for(Node n:current.adjacentNodes()) {
				if (!current.path.contains(n)) {
					pq.add(new AstarNode(current, n));
				}
			}
		}
		
		return null;
	}

	
	public static double distance(Node n1, Node n2, DungeonMap m) {
		if (n1==null||n2==null) {
			return Double.MAX_VALUE;
		}
		double dx = (n1.getCoords().x - n2.getCoords().x) * m.getDistanceMultiplier();
		double dy = (n1.getCoords().y - n2.getCoords().y) * m.getDistanceMultiplier();
		double dz = (n1.getCoords().z - n2.getCoords().z) * m.getHeightDistanceMultiplier();
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}
	private double eval() {
		return distanceTraveled+distanceRemaining;
	}
	private List<Node> adjacentNodes() {
		List<Node> l = new ArrayList<>();
		for(String s:currentNode.getEdges()) {
			Edge e = map.getEdge(s);
			Node n1 = map.getNode(e.getNode1());
			Node n2 = map.getNode(e.getNode2());
			if (n1.equals(currentNode)) {
				l.add(n2);
			} else {
				l.add(n1);
			}
		}
		
		return l;
	}
}
