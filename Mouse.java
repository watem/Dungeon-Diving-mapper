package modelv2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
	public static final int SELECT = 0;
	public static final int ADD_NODE = 1;
	public static final int ADD_EDGE = 2;
	
	private Node draggedNode;
	
	Model2View parent;
	int mode = SELECT;
	
	public Mouse(Model2View p) {
		parent = p;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("X:"+e.getX()+" Y:"+e.getY());
		System.out.println(parent.getElementAt(e.getPoint()));
		
		if (mode==ADD_NODE) {
			parent.m.addNode(e.getX(), e.getY());
			parent.refresh();
		} else if (mode==SELECT) {
			parent.parent.setLastSelected(parent.getElementAt(e.getPoint()));
		} else if (mode==ADD_EDGE) {
			System.out.println("edges mode");
			GraphElement lastSelected = parent.parent.getLastSelected();
			System.out.println("last "+lastSelected);
			if (lastSelected!=null&&lastSelected instanceof Node) {
				GraphElement newSelected = parent.getElementAt(e.getPoint());
				System.out.println("new "+newSelected);
				if (newSelected instanceof Node) {
					System.out.println("new edge being created");
					parent.m.connectNodes((Node)lastSelected, (Node)newSelected);
					parent.parent.setLastSelected(null);
				}
			} else {
				GraphElement newSelected = parent.getElementAt(e.getPoint());
				if (newSelected instanceof Node) {
					parent.parent.setLastSelected(newSelected);
					parent.refresh();
				}
			}
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
		if (mode!=SELECT) {
			draggedNode = null;
			return;
		}
		GraphElement i = parent.getElementAt(e.getPoint());
		if (!(i instanceof Node)) {
			draggedNode = null;
			return;
		}
		draggedNode=(Node)i;
//		System.out.println("Going to drag"+draggedNode);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println("mouse dragged");
		if(mode!=SELECT||draggedNode == null) {
			return;
		}
//		System.out.println("dragging "+draggedNode);
		draggedNode.moveNode(e.getX(), e.getY());
		parent.refresh();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
