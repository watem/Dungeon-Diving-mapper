package dungeonMapping;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Model2View extends JPanel {
//	private List<Ellipse2D> nodes;
//	private List<Line2D> edges;
//	private HashMap<Ellipse2D, Node> nodeMap;
//	private HashMap<Line2D, Node> edgeMap;


	Model2Page parent;
	DungeonMap m;
	double r = 4.;
	double scale = 1;
	int epsilon = 2;
	int topLeftX = 0, topLeftY = 0;


	Mouse ml = new Mouse(this);
	public Model2View(Model2Page parent) {
		this.parent = parent;
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}
	public void set(DungeonMap m) {
		this.m = m;
		refresh();
	}
	public void refresh() {
		repaint();
	}

	private void doDrawing(Graphics g){
	//	bodyLocations = SpatialController.getAllLocations(system.getId());
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
	//    for(TOBody body:bodies) {
	//    	Color c = body.getC();
	//    	if (c==null) {
	//
	//    	}
	//    	g2d.setColor(c);
	//    	double r = baseSize/(body.getDepth()+1);
	//    	Coordinates co = bodyLocations.get(body);
	//    	Pixels pos = toPixel(bodyLocations.get(body));
	//    	if (pos.x+r>0 && pos.y+r>0 && pos.x-r<this.getWidth() && pos.y-r<this.getHeight()) {
	//    		g2d.fill(new Ellipse2D.Double(pos.x-r, pos.y-r, 2*r, 2*r));
	//    	}
	//    }
	    if (m!=null) {
		    HashSet<Node> nodeSet = m.getNodes();
		    HashSet<Edge> edgeSet = m.getEdges();
		    if (edgeSet!=null) {
			    for(Edge e:m.getEdges()) {
			    	Coords pos1 = transform(e.node1.getCoords());
			    	Coords pos2 = transform(e.node2.getCoords());
			    	if ((pos1.x+r>0 && pos1.y+r>0 && pos1.x-r<this.getWidth() && pos1.y-r<this.getHeight())||(pos2.x+r>0 && pos2.y+r>0 && pos2.x-r<this.getWidth() && pos2.y-r<this.getHeight())) {
			    		g2d.setColor(e.getDescription().colour);
			    		g2d.drawLine(pos1.x,pos1.y,pos2.x,pos2.y);
			    	}
			    }
		    }
		    if (nodeSet!=null && parent.areNodesShown()) {
			    for(Node n:m.getNodes()) {
			    	Coords pos = transform(n.getCoords());
			    	if (pos.x+r>0 && pos.y+r>0 && pos.x-r<this.getWidth() && pos.y-r<this.getHeight()) {
			    		g2d.setColor(n.getDescription().colour);
			    		g2d.fill(new Ellipse2D.Double(pos.x-r, pos.y-r, 2*r, 2*r));
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
		if (m!=null) {
			for(Node n:m.getNodes()) {
			    Coords pos = transform(n.getCoords());
			    if(distance(p,pos)<=r) {
			    	return n;
			    }

			}
			for(Edge e:m.getEdges()) {
			    	Coords pos1 = transform(e.node1.getCoords());
			    	Coords pos2 = transform(e.node2.getCoords());
			    	double dx = scale*pos1.x-scale*pos2.x;
						double dy = scale*pos1.y-scale*pos2.y;
			    	double d = Math.sqrt(dy*dy+dx*dx);
			    	double dTotal = distance(p,pos1)+distance(p,pos2);
			    	if(dTotal+epsilon>=d && dTotal-epsilon<=d) {
			    		return e;
			    	}
			    }
		    }
		return null;
	}

	private double distance(Point p, Coords c) {
		double dx = p.getX()-scale*c.x;
		double dy = p.getY()-scale*c.y;


		return Math.sqrt(dy*dy+dx*dx);
	}
	
	/**
	 * turns "physical" locations to their relative position on screen 
	 * @param location
	 * @return
	 */
	Coords transform (Coords location) {
		int x =(int) (scale*(location.x-topLeftX));
		int y =(int) (scale*(location.y-topLeftY));
		return new Coords(x,y);
	}

	/**
	 * turns positions on screen to their relative "physical" location
	 * @param p
	 * @return
	 */
	public Point invert(Point p) {
		int x =(int) ((p.x/scale + topLeftX));
		int y =(int) ((p.y/scale + topLeftY));
		return new Point(x,y);
	}
	public void zoomIn() {
		Point centre = centreLocation();
		System.out.println("centre:"+centre.x+" "+centre.y);
		scale *=2.;
		moveToLocation(centre);
		System.out.println("top:"+topLeftX+" "+topLeftY);
		refresh();
		
	}
	public void zoomOut() {
		Point centre = centreLocation();
		System.out.println("centre:"+centre.x+" "+centre.y);
		scale /=1.5;
		moveToLocation(centre);
		refresh();
	}
	private Point centreLocation() {
		Point p = new Point(this.getWidth()/2, this.getHeight()/2);
		return invert(p);
	}
	private void moveToLocation(Point location) {
		Coords c = transform(new Coords(location.x, location.y));
		System.out.println("centre loc:"+c.x+" "+c.y);
		topLeftX =(int) ((c.x-this.getWidth()/2));
		topLeftY =(int) ((c.y-this.getHeight()/2));
	}

}
