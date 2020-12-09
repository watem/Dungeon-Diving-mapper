package modelv2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;


public class Model2View extends JPanel {
//	private List<Ellipse2D> nodes;
//	private List<Line2D> edges;
//	private HashMap<Ellipse2D, Node> nodeMap;
//	private HashMap<Line2D, Node> edgeMap;
	
	
	Model2Page parent;
	Map m;
	double r = 4.;
	int scale = 1;
	int epsilon = 2;
	
	Mouse ml = new Mouse(this);
	public Model2View(Model2Page parent) {
		this.parent = parent;
	}
	public void set(Map m) {
		this.m = m;
		addMouseListener(ml);
		addMouseMotionListener(ml);
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
		    if (nodeSet!=null) {
			    for(Node n:m.getNodes()) {
			    	Coords pos = n.getCoords();
			    	if (pos.x+r>0 && pos.y+r>0 && pos.x-r<this.getWidth() && pos.y-r<this.getHeight()) {
			    		g2d.fill(new Ellipse2D.Double(scale*pos.x-r, scale*pos.y-r, 2*r, 2*r));
			    	}
			    	
			    }
		    }
		    if (edgeSet!=null) {
			    for(Edge e:m.getEdges()) {
			    	Coords pos1 = e.node1.getCoords();
			    	Coords pos2 = e.node2.getCoords();
			    	if ((pos1.x+r>0 && pos1.y+r>0 && pos1.x-r<this.getWidth() && pos1.y-r<this.getHeight())||(pos2.x+r>0 && pos2.y+r>0 && pos2.x-r<this.getWidth() && pos2.y-r<this.getHeight())) {
			    		g2d.drawLine(scale*pos1.x,scale*pos1.y,scale*pos2.x,scale*pos2.y);
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
			    Coords pos = n.getCoords();
			    if(distance(p,pos)<=r) {
			    	return n;
			    }
			    	
			}
			for(Edge e:m.getEdges()) {
			    	Coords pos1 = e.node1.getCoords();
			    	Coords pos2 = e.node2.getCoords();
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
}
