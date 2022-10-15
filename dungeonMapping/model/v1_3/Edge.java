package dungeonMapping.model.v1_3;

@SuppressWarnings("serial")
public class Edge extends GraphElement {
	private String node1;
	private String node2;

	private Coords c1;
	private Coords c2;

	public double getLength() {
		double x = (c1.x - c2.x) * m.getDistanceMultiplier();
		double y = (c1.y - c2.y) * m.getDistanceMultiplier();
		double z = (c1.z - c2.z) * m.getHeightDistanceMultiplier();
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Edge(Node node1, Node node2, DungeonMap m, String id) {
		super(m);
		this.setNode1(node1);
		this.setNode2(node2);
		this.c1 = node1.getCoords();
		this.c2 = node2.getCoords();
		this.setId(id);
	}

	/**
	 * @return the node1
	 */
	public String getNode1() {
		return node1;
	}

	/**
	 * @param node1 the node1 to set
	 */
	public void setNode1(Node node1) {
		this.node1 = node1.getId();
		this.c1 = node1.getCoords();
	}

	/**
	 * @return the node2
	 */
	public String getNode2() {
		return node2;

	}

	/**
	 * @param node2 the node2 to set
	 */
	public void setNode2(Node node2) {
		this.node2 = node2.getId();
		this.c2 = node2.getCoords();
	}
	
	public Coords getC1() {
		return new Coords(c1.x, c1.y, c1.z);
	}
	public Coords getC2() {
		return new Coords(c2.x, c2.y, c2.z);
		
	}

	@Override
	public String toString() {
		return getId()+" [n1:" + c1 +", n2:" + c2 + "] (len: " + (int) getLength() + ")";
	}
}
