package dungeonMapping.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;

import dungeonMapping.model.v1_3.*;

@SuppressWarnings("serial")
public class MapView extends JPanel {
//	private List<Ellipse2D> nodes;
//	private List<Line2D> edges;
//	private HashMap<Ellipse2D, Node> nodeMap;
//	private HashMap<Line2D, Node> edgeMap;
	public static final int INSIDE = 0; // 0000
	public static final int LEFT = 1;   // 0001
	public static final int RIGHT = 2;  // 0010
	public static final int BOTTOM = 4; // 0100
	public static final int TOP = 8;    // 1000

	MainPage parent;
	DungeonMap m;
	double r = 4.;
	double scale = 1;
	int epsilon = 2;
	
	List<Node> path = null;

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
	
	private boolean onScreen(Node n) {
		Point pos = coordToScreen(n.getCoords());
		return (pos.x + r > 0 && pos.y + r > 0 && pos.x - r < this.getWidth() && pos.y - r < this.getHeight());
	}
	
	
	private int outCode(double x, double y) {
		int code = INSIDE;
		if (x < 0)           // to the left of clip window
			code |= LEFT;
		else if (x > this.getWidth())      // to the right of clip window
			code |= RIGHT;
		if (y < 0)           // below the clip window
			code |= BOTTOM;
		else if (y > this.getHeight())      // above the clip window
			code |= TOP;

		return code;
	}
	
	// Cohen–Sutherland clipping algorithm clips a line from
	// P0 = (x0, y0) to P1 = (x1, y1) against a rectangle with 
	// diagonal from (xmin, ymin) to (xmax, ymax).
	private boolean CohenSutherlandLineClip(Point pos1, Point pos2) {
		// compute outcodes for P0, P1, and whatever point lies outside the clip rectangle
		int outcode0 = outCode(pos1.x, pos1.y);
		int outcode1 = outCode(pos2.x, pos2.y);
		boolean accept = false;
		double ymax = this.getHeight();
		double xmax = this.getWidth();
		double x0 = pos1.x;
		double y0 = pos1.y;
		double x1 = pos2.x;
		double y1 = pos2.y;

		while (true) {
			if ((outcode0 | outcode1) == 0) {
				// bitwise OR is 0: both points inside window; trivially accept and exit loop
				accept = true;
				break;
			} else if ((outcode0 & outcode1) != 0) {
				// bitwise AND is not 0: both points share an outside zone (LEFT, RIGHT, TOP,
				// or BOTTOM), so both must be outside window; exit loop (accept is false)
				break;
			} else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge
				double x = 0, y = 0;

				// At least one endpoint is outside the clip rectangle; pick it.
				int outcodeOut = outcode1 > outcode0 ? outcode1 : outcode0;

				// Now find the intersection point;
				// use formulas:
				//   slope = (y1 - y0) / (x1 - x0)
				//   x = x0 + (1 / slope) * (ym - y0), where ym is ymin or ymax
				//   y = y0 + slope * (xm - x0), where xm is xmin or xmax
				// No need to worry about divide-by-zero because, in each case, the
				// outcode bit being tested guarantees the denominator is non-zero
				if ((outcodeOut & TOP) != 0) {           // point is above the clip window
					x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
					y = ymax;
				} else if ((outcodeOut & BOTTOM) != 0) { // point is below the clip window
					x = x0 + (x1 - x0) * (-y0) / (y1 - y0);
					y = 0;
				} else if ((outcodeOut & RIGHT) != 0) {  // point is to the right of clip window
					y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
					x = xmax;
				} else if ((outcodeOut & LEFT) != 0) {   // point is to the left of clip window
					y = y0 + (y1 - y0) * (-x0) / (x1 - x0);
					x = 0;
				}

				// Now we move outside point to intersection point to clip
				// and get ready for next pass.
				if (outcodeOut == outcode0) {
					x0 = x;
					y0 = y;
					outcode0 = outCode(x0, y0);
				} else {
					x1 = x;
					y1 = y;
					outcode1 = outCode(x1, y1);
				}
			}
		}
		return accept;
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
					if (CohenSutherlandLineClip(pos1, pos2)) {
						if (parent.areColoursShown()) {
							g2d.setColor(e.getDescription().colour);
						}
						g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
					}
				}
			}
			if (nodeSet != null && parent.areNodesShown()) {
				g2d.setFont(getFont().deriveFont(3));
				for (Node n : m.getNodes()) {
					if (onScreen(n)) {
						Point pos = coordToScreen(n.getCoords());
						if (parent.areColoursShown()) {
							g2d.setColor(n.getDescription().colour);
						}
						if (parent.areDisconnectedNodesHighlighted() && n.getEdges().size() < 1) {
							g2d.setColor(Color.CYAN);
							g2d.fill(new Ellipse2D.Double(pos.x - 3 * r, pos.y - 3 * r, 6 * r, 6 * r));
						} else {
							g2d.fill(new Ellipse2D.Double(pos.x - r, pos.y - r, 2 * r, 2 * r));
						}
						if (parent.areZLevelsShown()) {
							g2d.setColor(Color.LIGHT_GRAY);
							
							g2d.drawString(String.valueOf(n.getCoords().z),(float)(pos.x - r), (float)(pos.y - r));
							
							g2d.setColor(Color.BLACK);
						}

					}
				}
			}
		}
		Node previous = null;
		if (path==null) {
			return;
		}
		for(Node n:path) {
			g2d.setColor(Color.RED);
			Point pos = coordToScreen(n.getCoords());
			if (pos.x + r > 0 && pos.y + r > 0 && pos.x - r < this.getWidth() && pos.y - r < this.getHeight()) {
				g2d.fill(new Ellipse2D.Double(pos.x - r, pos.y - r, 2 * r, 2 * r));
			}
			if (previous!=null) {
				Edge e = m.getEdge(previous, n);
				Point pos1 = coordToScreen(m.getNode(e.getNode1()).getCoords());
				Point pos2 = coordToScreen(m.getNode(e.getNode2()).getCoords());
				if ((pos1.x + r > 0 && pos1.y + r > 0 && pos1.x - r < this.getWidth()
						&& pos1.y - r < this.getHeight())
						|| (pos2.x + r > 0 && pos2.y + r > 0 && pos2.x - r < this.getWidth()
								&& pos2.y - r < this.getHeight())) {
					g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
				}
			}
			
			previous = n;
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
				
				double d = pos1.distance(pos2);
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

	Coords centre = new Coords(0, 0, 0);

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
		return new Coords(x, y, parent.getCurrentZLevel());
	}

}
