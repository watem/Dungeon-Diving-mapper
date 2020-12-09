package modelv2;

import java.awt.Dimension;
import java.util.ArrayList;

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
	private JComboBox<DungeonMap> maps = new JComboBox<>();
	private JTextField mapName = new JTextField();
	private Model2View mapView = new Model2View(this);
	
	private JButton newMapButton = new JButton("New Map");
	private JButton deleteMapButton = new JButton("Delete Map");
	private JButton deleteItemButton = new JButton("Delete SelectedItem");
	private JButton editButton = new JButton("Edit SelectedItem");
	
	private GraphElement lastSelectedItem;
	
	private ArrayList<DescriptionDialog> openDescriptions = new ArrayList<>();
	
	private JRadioButton mouseDefault = new JRadioButton("Select Items", true);
	private JRadioButton mouseAddNodes = new JRadioButton("Add Nodes", false);
	private JRadioButton mouseAddEdges = new JRadioButton("Add Edges", false);
	private JRadioButton mouseEdit = new JRadioButton("Edit", false);
	private ButtonGroup mouseBehaviour = new ButtonGroup();
	
	private JLabel itemDetails = new JLabel();
	
	private JLabel disMulLabel = new JLabel("distance multiplier");
	private JTextField disMulField = new JTextField();
	
	public Model2Page() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mouseBehaviour.add(mouseDefault);
		mouseBehaviour.add(mouseAddNodes);
		mouseBehaviour.add(mouseAddEdges);
		mouseBehaviour.add(mouseEdit);
		
		
		listeners();
		menu();
		
		refreshMaps();
		DungeonMap m = maps.getItemAt(maps.getSelectedIndex());
		refreshMap(m);
	}
	
	public void listeners() {
		newMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Modelv2Mapping.getDungeon().addMap((mapName.getText()));
				refreshMaps();
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
		mouseEdit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setMode();
			}
		});
		maps.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DungeonMap m = maps.getItemAt(maps.getSelectedIndex());
				refreshMap(m);
			}
		});
		deleteMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DungeonMap m = maps.getItemAt(maps.getSelectedIndex());
				Modelv2Mapping.getDungeon().getMaps().remove(m);
				refreshMaps();
				m = maps.getItemAt(maps.getSelectedIndex());
				refreshMap(m);
			}
		});
		deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DungeonMap m = maps.getItemAt(maps.getSelectedIndex());
				m.removeItem(lastSelectedItem);
				lastSelectedItem = null;
				refresh();
			}
		});
		editButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				edit();
			}
		});
		TextListener.addChangeListener(disMulField, e->{DungeonMap m = maps.getItemAt(maps.getSelectedIndex());m.distanceMultiplier = Double.parseDouble(disMulField.getText()) ;pack();});
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
						.addComponent(deleteMapButton)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(mapView)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(mouseDefault)
										.addComponent(mouseAddNodes)
										.addComponent(mouseAddEdges)
										.addComponent(mouseEdit)
								)
								.addGroup(layout.createSequentialGroup()
										.addComponent(editButton)
										.addComponent(deleteItemButton)
								)
								.addComponent(itemDetails)
								.addGroup(layout.createSequentialGroup()
										.addComponent(disMulLabel)
										.addComponent(disMulField)
								)
						)
				)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(maps)
						.addComponent(mapName)
						.addComponent(newMapButton)
						.addComponent(deleteMapButton)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(mapView)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(mouseDefault)
										.addComponent(mouseAddNodes)
										.addComponent(mouseAddEdges)
										.addComponent(mouseEdit)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(editButton)
										.addComponent(deleteItemButton)
								)
								.addComponent(itemDetails)
								.addGroup(layout.createParallelGroup()
										.addComponent(disMulLabel)
										.addComponent(disMulField)
								)
						)
				)
		);
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {mouseAddNodes, mouseDefault, mouseAddEdges});
//		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {maps,mapName,newMapButton});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {disMulLabel,disMulField});
		mapView.setMinimumSize(new Dimension(100, 100));
		mapView.setPreferredSize(new Dimension(700,700));
		pack();
				
	}
	
	
	void refreshMap(DungeonMap m) {
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
		} else if (mouseEdit.isSelected()) {
			mapView.ml.mode=Mouse.EDIT;
		}
	}
	
	void refresh() {
		mapView.refresh();
		for(DescriptionDialog d: getOpenDescriptions()) {
			d.refresh();
		}
		DungeonMap m = maps.getItemAt(maps.getSelectedIndex());
		if (m!=null) {
			disMulField.setText(""+m.distanceMultiplier);
		}
		
		Modelv2Mapping.save();
		// show information of selected item
	}
	
	private void refreshMaps() {
		maps.removeAllItems();
		for(DungeonMap m:Modelv2Mapping.getDungeon().getMaps()) {
			maps.addItem(m);
		}
		refresh();
	}
	
	void setLastSelected(GraphElement e) {
		this.lastSelectedItem = e;
		if (lastSelectedItem==null) {
			itemDetails.setText("");
		} else {
			itemDetails.setText(lastSelectedItem.toString());
		}
	}
	GraphElement getLastSelected() {
		return lastSelectedItem;
	}
	
	public ArrayList<DescriptionDialog> getOpenDescriptions() {
		if (openDescriptions == null) {
			openDescriptions = new ArrayList<>();
		}
		return openDescriptions;
	}
	
	private void edit() {
		DescriptionDialog d = new DescriptionDialog(lastSelectedItem, this);
		d.setVisible(true);
		getOpenDescriptions().add(d);
	}
}
