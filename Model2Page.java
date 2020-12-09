package modelv2;

import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Model2Page extends JFrame {
	private JComboBox<Map> maps = new JComboBox<>();
	int x1,x2,y1,y2;
	private JLabel x1Label = new JLabel("x1");
	private JLabel x2Label = new JLabel("x2");
	private JLabel y1Label = new JLabel("y1");
	private JLabel y2Label = new JLabel("y2");
	private JTextField x1Text = new JTextField();
	private JTextField x2Text = new JTextField();
	private JTextField y1Text = new JTextField();
	private JTextField y2Text = new JTextField();
	private JTextField mapName = new JTextField();
	private Model2View mapView = new Model2View(this);
	
	private JButton newMapButton = new JButton("New Map");
	private JButton newNodeButton = new JButton("New Node");
	private JButton newEdgeButton = new JButton("New Edge");
	private JButton moveNodeButton = new JButton("Move Node");
	private JButton deleteMapButton = new JButton("Delete Map");
	private JButton deleteItemButton = new JButton("Delete SelectedItem");
	
	private GraphElement lastSelectedItem;
	
	
	private JRadioButton mouseDefault = new JRadioButton("Select Items", true);
	private JRadioButton mouseAddNodes = new JRadioButton("Add Nodes", false);
	private JRadioButton mouseAddEdges = new JRadioButton("Add Edges", false);
	private ButtonGroup mouseBehaviour = new ButtonGroup();
	
	
	
	public Model2Page() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mouseBehaviour.add(mouseDefault);
		mouseBehaviour.add(mouseAddNodes);
		mouseBehaviour.add(mouseAddEdges);
		
		
		listeners();
		menu();
		
		refreshMaps();
		Map m = maps.getItemAt(maps.getSelectedIndex());
		refreshMap(m);
	}
	
	public void listeners() {
		newMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Modelv2Mapping.getDungeon().addMap((new Map(mapName.getText())));
				refreshMaps();
			}
		});
		newNodeButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (maps == null) {
					return;
				}
				maps.getItemAt(maps.getSelectedIndex()).addNode(Integer.parseInt(x1Text.getText()), Integer.parseInt(y1Text.getText()));
				refresh();
			}
		});
		newEdgeButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Map m = maps.getItemAt(maps.getSelectedIndex());
				Node n1 = m.getNodeAt(Integer.parseInt(x1Text.getText()), Integer.parseInt(y1Text.getText()));
				Node n2 = m.getNodeAt(Integer.parseInt(x2Text.getText()), Integer.parseInt(y2Text.getText()));
				m.connectNodes(n1,n2);
				refresh();
			}
		});
		moveNodeButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Map m = maps.getItemAt(maps.getSelectedIndex());
				Node n = m.getNodeAt(Integer.parseInt(x1Text.getText()), Integer.parseInt(y1Text.getText()));
				n.moveNode(Integer.parseInt(x2Text.getText()), Integer.parseInt(y2Text.getText()));
				refresh();
			}
		});
		mouseAddNodes.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setMode();
			}
		});
		mouseDefault.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setMode();
			}
		});
		mouseAddEdges.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setMode();
			}
		});
		maps.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Map m = maps.getItemAt(maps.getSelectedIndex());
				refreshMap(m);
			}
		});
		deleteMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Map m = maps.getItemAt(maps.getSelectedIndex());
				Modelv2Mapping.getDungeon().getMaps().remove(m);
				refreshMaps();
				m = maps.getItemAt(maps.getSelectedIndex());
				refreshMap(m);
			}
		});
		deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Map m = maps.getItemAt(maps.getSelectedIndex());
				m.removeItem(lastSelectedItem);
				lastSelectedItem = null;
			}
		});
	}
	

	public void menu() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(maps)
						.addComponent(mapName)
						.addComponent(newMapButton)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(mapView)
						.addGroup(layout.createParallelGroup()
								.addComponent(x1Label)
								.addComponent(y1Label)
						)
						.addGroup(layout.createParallelGroup()
								.addComponent(x1Text)
								.addComponent(y1Text)
								.addComponent(newNodeButton)
								.addComponent(mouseDefault)
								.addComponent(deleteItemButton)
						)
						.addGroup(layout.createParallelGroup()
								.addComponent(x2Label)
								.addComponent(y2Label)
								.addComponent(moveNodeButton)
								.addComponent(mouseAddNodes)
						)
						.addGroup(layout.createParallelGroup()
								.addComponent(x2Text)
								.addComponent(y2Text)
								.addComponent(newEdgeButton)
								.addComponent(mouseAddEdges)
								.addComponent(deleteMapButton)
						)
				)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(maps)
						.addComponent(mapName)
						.addComponent(newMapButton)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(mapView)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(x1Label)
										.addComponent(x1Text)
										.addComponent(x2Label)
										.addComponent(x2Text)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(y1Label)
										.addComponent(y1Text)
										.addComponent(y2Label)
										.addComponent(y2Text)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(newNodeButton)
										.addComponent(moveNodeButton)
										.addComponent(newEdgeButton)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(mouseDefault)
										.addComponent(mouseAddNodes)
										.addComponent(mouseAddEdges)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(deleteItemButton)
										.addComponent(deleteMapButton)
								)
						)
				)
		);
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {newNodeButton, moveNodeButton, newEdgeButton, x1Text, x2Text, y1Text, y2Text, mouseAddNodes, mouseDefault, mouseAddEdges});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {x1Label, x1Text, x2Label, x2Text, y1Label, y1Text, y2Label, y2Text});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {maps,mapName,newMapButton});
		
		mapView.setMinimumSize(new Dimension(100, 100));
		mapView.setPreferredSize(new Dimension(700,700));
		pack();
				
	}
	
	
	void refreshMap(Map m) {
		if (mapView.m!=m) {
			mapView.set(m);
			lastSelectedItem=null;
			refresh();
		}
	}
	
	private void setMode() {
		int m;
		if (mouseDefault.isSelected()) {
			mapView.ml.mode=Mouse.SELECT;
		} else if (mouseAddNodes.isSelected()) {
			mapView.ml.mode=Mouse.ADD_NODE;
		} else if (mouseAddEdges.isSelected()) {
			lastSelectedItem = null;
			mapView.ml.mode=Mouse.ADD_EDGE;
		}
	}
	
	private void refresh() {
		mapView.refresh();
		Modelv2Mapping.save();
		// show information of selected item
	}
	
	private void refreshMaps() {
		maps.removeAllItems();
		for(Map m:Modelv2Mapping.getDungeon().getMaps()) {
			maps.addItem(m);
		}
		refresh();
	}
	
	void setLastSelected(GraphElement e) {
		this.lastSelectedItem = e;
	}
	GraphElement getLastSelected() {
		return lastSelectedItem;
	}
	
	
}
