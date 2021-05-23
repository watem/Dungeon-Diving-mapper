package dungeonMapping.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import javax.swing.JPanel;

import dungeonMapping.model.v1_1.*;

@SuppressWarnings("serial")
public class MapView extends JPanel {
//	private List<Ellipse2D> nodes;
//	private List<Line2D> edges;
//	private HashMap<Ellipse2D, Node> nodeMap;
//	private HashMap<Line2D, Node> edgeMap;

	MainPage parent;
	DungeonMap m;
	double r = 4.;
	double scale = 1;
	int epsilon = 2;

	Mouse ml = new Mouse(this);

	public MapView(MainPage parent) {
		this.parent = parent;
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}

	public void set(DungeonMap m) {
		this.m = m;
		scale = 1;
		refresh();
	}

	public void refresh() {
		repaint();
	}

	private void doDrawing(Graphics g) {
		// bodyLocations = SpatialController.getAllLocations(system.getId());
		Graphics2D background = (Graphics2D) g.create();
		background.setColor(Color.WHITE);
		Rectangle2D background_1 = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
		background.draw(background_1);
		background.fill(background_1);

		Graphics2D g2d = (Graphics2D) g.create();
		BasicStroke aStroke = new BasicStroke(2);
		g2d.setStroke(aStroke);
		g2d.setColor(Color.BLACK);
		Rectangle2D boundingBox = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
		g2d.draw(boundingBox);
		if (m != null) {
			HashSet<Node> nodeSet = m.getNodes();
			HashSet<Edge> edgeSet = m.getEdges();
			if (edgeSet != null) {
				for (Edge e : m.getEdges()) {

					Point pos1 = coordToScreen(m.getNode(e.getNode1()).getCoords());
					Point pos2 = coordToScreen(m.getNode(e.getNode2()).getCoords());
					if ((pos1.x + r > 0 && pos1.y + r > 0 && pos1.x - r < this.getWidth()
							&& pos1.y - r < this.getHeight())
							|| (pos2.x + r > 0 && pos2.y + r > 0 && pos2.x - r < this.getWidth()
									&& pos2.y - r < this.getHeight())) {
						g2d.setColor(e.getDescription().colour);
						g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
					}
				}
			}
			if (nodeSet != null && parent.areNodesShown()) {
				for (Node n : m.getNodes()) {
//			    	Coords pos = transform(n.getCoords());
					Point pos = coordToScreen(n.getCoords());
					if (pos.x + r > 0 && pos.y + r > 0 && pos.x - r < this.getWidth() && pos.y - r < this.getHeight()) {
						g2d.setColor(n.getDescription().colour);
						if (parent.areDisconnectedNodesHighlighted() && n.getEdges().size() < 1) {
							g2d.setColor(Color.CYAN);
							g2d.fill(new Ellipse2D.Double(pos.x - 3 * r, pos.y - 3 * r, 6 * r, 6 * r));
						} else {
							g2d.fill(new Ellipse2D.Double(pos.x - r, pos.y - r, 2 * r, 2 * r));
						}

					}
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	public GraphElement getElementAt(Point p) {
		if (m != null) {
			for (Node n : m.getNodes()) {
				Point pos = coordToScreen(n.getCoords());
				if (parent.areDisconnectedNodesHighlighted() && n.getEdges().size() < 1) {
					if (p.distance(pos) <= 3 * r) {
						return n;
					}
				}
				if (p.distance(pos) <= r) {
					return n;
				}

			}
			for (Edge e : m.getEdges()) {
				Point pos1 = coordToScreen(m.getNode(e.getNode1()).getCoords());
				Point pos2 = coordToScreen(m.getNode(e.getNode2()).getCoords());
				double dx = scale * pos1.x - scale * pos2.x;
				double dy = scale * pos1.y - scale * pos2.y;
				double d = Math.sqrt(dy * dy + dx * dx);
				double dTotal = p.distance(pos1) + p.distance(pos2);
				if (dTotal + epsilon >= d && dTotal - epsilon <= d) {
					return e;
				}
			}
		}
		return null;
	}

	public void zoomIn() {
		System.out.println("centre:" + centre.x + " " + centre.y);
		scale *= 1.5;
		refresh();

	}

	public void zoomOut() {
		System.out.println("centre:" + centre.x + " " + centre.y);
		scale /= 1.5;
		refresh();
	}

	Coords centre = new Coords(0, 0);

	public Point coordToScreen(Coords location) {
		int x = location.x - centre.x;
		int y = -location.y + centre.y;
		x *= scale;
		y *= scale;
		x += this.getWidth() / 2;
		y += this.getHeight() / 2;

		return new Point(x, y);
	}

	public Coords screenToCoord(Point screen) {
		int x = screen.x - this.getWidth() / 2;
		int y = screen.y - this.getHeight() / 2;

		x /= scale;
		y /= scale;

		x += centre.x;
		y = centre.y - y;
		return new Coords(x, y);
	}

}
