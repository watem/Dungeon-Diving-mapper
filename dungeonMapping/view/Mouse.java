package dungeonMapping.view;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import dungeonMapping.model.GraphElement;
import dungeonMapping.model.Node;

public class Mouse implements MouseListener, MouseMotionListener {
	public static final int SELECT = 0;
	public static final int ADD_NODE = 1;
	public static final int ADD_EDGE = 2;
	public static final int EDIT = 3;
	
	private Node draggedNode;
	
	private int dragScreenX, dragScreenY; 
	
	MapView parent;
	int mode = SELECT;
	
	public Mouse(MapView p) {
		parent = p;
	}
	public void setParent(MapView p) {
		parent = p;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(mode);
		System.out.println("Xl:"+parent.invert(e.getPoint()).x+" Yl:"+parent.invert(e.getPoint()).y);
		System.out.println("Xp:"+e.getX()+" Yp:"+e.getY());
		System.out.println(parent.getElementAt(parent.invert(e.getPoint())));
		
		if (mode==ADD_NODE) {
			Point p = parent.invert(e.getPoint());
			parent.m.addNode(p.x, p.y);
			parent.parent.refresh();
		} else if (mode==SELECT) {
			GraphElement ge = parent.getElementAt(parent.invert(e.getPoint()));
			if (ge==null) {
				return;
			}
			
			parent.parent.setLastSelected(ge);
			if (SwingUtilities.isRightMouseButton(e)) {
				DescriptionDialog d = new DescriptionDialog(ge, parent.parent);
				d.setVisible(true);
				parent.parent.getOpenDescriptions().add(d);
			}
		} else if (mode==ADD_EDGE) {
			System.out.println("edges mode");
			GraphElement lastSelected = parent.parent.getLastSelected();
			System.out.println("last "+lastSelected);
			if (lastSelected!=null&&lastSelected instanceof Node) {
				GraphElement newSelected = parent.getElementAt(parent.invert(e.getPoint()));
				if (newSelected==null) {
					parent.parent.setLastSelected(null);
				}
				System.out.println("new "+newSelected);
				if (newSelected instanceof Node) {
					System.out.println("new edge being created");
					parent.m.connectNodes((Node)lastSelected, (Node)newSelected);
					parent.parent.setLastSelected(newSelected);
					parent.parent.refresh();
				}
			} else {
				GraphElement newSelected = parent.getElementAt(parent.invert(e.getPoint()));
				if (newSelected instanceof Node) {
					parent.parent.setLastSelected(newSelected);
				}
			}
		} else if (mode==EDIT) {
			GraphElement ge = parent.getElementAt(parent.invert(e.getPoint()));
			if (ge==null) {
				return;
			}
			parent.parent.setLastSelected(ge);
			DescriptionDialog d = new DescriptionDialog(ge, parent.parent);
			d.setVisible(true);
			parent.parent.getOpenDescriptions().add(d);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (mode!=SELECT) {
				draggedNode = null;
				return;
			}
			GraphElement i = parent.getElementAt(parent.invert(e.getPoint()));
			if (!(i instanceof Node)) {
				draggedNode = null;
				return;
			}
			draggedNode=(Node)i;
//		System.out.println("Going to drag"+draggedNode);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Point p = parent.invert(e.getPoint());
			dragScreenX = p.x;
			dragScreenY = p.y;
			System.out.println("topleft: "+parent.topLeftX+" "+parent.topLeftY);
			System.out.println("dx:"+p.x+" dy:"+p.y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("topleft: "+parent.topLeftX+" "+parent.topLeftY);
		parent.refresh();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
	//		System.out.println("mouse dragged");
			if(mode!=SELECT||draggedNode == null) {
				return;
			}
	//		System.out.println("dragging "+draggedNode);
			Point p = parent.invert(e.getPoint());
			draggedNode.moveNode(p.x, p.y);
			parent.parent.refresh();
		} else if (SwingUtilities.isRightMouseButton(e)) {
			parent.topLeftX =(int) ((dragScreenX - e.getX())/parent.scale);
			parent.topLeftY =(int) ((dragScreenY - e.getY())/parent.scale);
			parent.refresh();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
