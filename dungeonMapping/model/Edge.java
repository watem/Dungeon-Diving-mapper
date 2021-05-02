package dungeonMapping.model;

public class Edge extends GraphElement {
	private Node node1;
	private Node node2;
	
	
	public double getLength() {
		Coords c1 = getNode1().getCoords();
		Coords c2 = getNode2().getCoords();
		int x = c1.x-c2.x;
		int y = c1.y-c2.y;
		return Math.sqrt(x*x+y*y);
	}


	public Edge(Node node1, Node node2, DungeonMap m, String id) {
		super(m);
		this.setNode1(node1);
		this.setNode2(node2);
		this.setId(id);
	}


	/**
	 * @return the node1
	 */
	public Node getNode1() {
		return node1;
	}


	/**
	 * @param node1 the node1 to set
	 */
	public void setNode1(Node node1) {
		this.node1 = node1;
	}


	/**
	 * @return the node2
	 */
	public Node getNode2() {
		return node2;
	}


	/**
	 * @param node2 the node2 to set
	 */
	public void setNode2(Node node2) {
		this.node2 = node2;
	}
}
