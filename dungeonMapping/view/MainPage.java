package dungeonMapping.view;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.controller.MapController;
import dungeonMapping.model.v1_2.DungeonMap;
import dungeonMapping.model.v1_2.GraphElement;
import dungeonMapping.serializing.Persistence;

@SuppressWarnings("serial")
public class MainPage extends JFrame {
	private MapView mapView = new MapView(this);

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

	private JButton saveButton = new JButton("save");
	private JButton saveAsButton = new JButton("save as");
	private JButton loadButton = new JButton("load");

	private JButton zoomIn = new JButton("Zoom +");
	private JButton zoomOut = new JButton("Zoom -");

	private JCheckBox nodesShownButton = new JCheckBox("Show nodes?", true);
	private JCheckBox disconnectedNodes = new JCheckBox("Highlight disconnected nodes?", false);

	public MainPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mouseBehaviour.add(mouseDefault);
		mouseBehaviour.add(mouseAddNodes);
		mouseBehaviour.add(mouseAddEdges);
		mouseBehaviour.add(mouseEdit);

		listeners();
		menu();

		DungeonMap m = MappingApplication.getDungeon();
		refreshMap(m);
	}

	public void listeners() {
		newMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newMap();
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
		deleteMapButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteMap();
			}
		});
		deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DungeonMap m = MappingApplication.getDungeon();
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
		TextListener.addChangeListener(disMulField, e -> {
			DungeonMap m = MappingApplication.getDungeon();
			m.setDistanceMultiplier(Double.parseDouble(disMulField.getText()));
		});
		saveAsButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAs();
			}
		});
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MappingApplication.save();
			}
		});
		loadButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadAs();
			}
		});

		zoomIn.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mapView.zoomIn();
			}
		});
		zoomOut.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mapView.zoomOut();
			}
		});

		nodesShownButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mapView.refresh();
			}
		});
		disconnectedNodes.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mapView.refresh();
			}
		});
	}

	public void menu() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addComponent(newMapButton).addComponent(deleteMapButton)
						.addComponent(saveButton).addComponent(saveAsButton).addComponent(loadButton))
				.addGroup(layout.createSequentialGroup().addComponent(mapView).addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup().addComponent(mouseDefault).addComponent(mouseAddNodes)
								.addComponent(mouseAddEdges).addComponent(mouseEdit))
						.addGroup(
								layout.createSequentialGroup().addComponent(editButton).addComponent(deleteItemButton))
						.addGroup(layout.createSequentialGroup().addComponent(nodesShownButton)
								.addComponent(disconnectedNodes))
						.addComponent(itemDetails)
						.addGroup(layout.createSequentialGroup().addComponent(disMulLabel).addComponent(disMulField))
						.addGroup(layout.createSequentialGroup().addComponent(zoomIn).addComponent(zoomOut)))));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(newMapButton).addComponent(deleteMapButton)
						.addComponent(saveButton).addComponent(saveAsButton).addComponent(loadButton))
				.addGroup(layout.createParallelGroup().addComponent(mapView).addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup().addComponent(mouseDefault).addComponent(mouseAddNodes)
								.addComponent(mouseAddEdges).addComponent(mouseEdit))
						.addGroup(layout.createParallelGroup().addComponent(editButton).addComponent(deleteItemButton))
						.addGroup(layout.createParallelGroup().addComponent(nodesShownButton)
								.addComponent(disconnectedNodes))

						.addComponent(itemDetails)
						.addGroup(layout.createParallelGroup().addComponent(disMulLabel).addComponent(disMulField))
						.addGroup(layout.createParallelGroup().addComponent(zoomIn).addComponent(zoomOut)))));
		layout.linkSize(SwingConstants.HORIZONTAL,
				new java.awt.Component[] { mouseAddNodes, mouseDefault, mouseAddEdges });
//		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {});
//		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newMapButton});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] { disMulLabel, disMulField });
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] { disMulLabel, disMulField });

		mapView.setMinimumSize(new Dimension(100, 100));
		mapView.setPreferredSize(new Dimension(700, 700));
		pack();

	}

	void refreshMap(DungeonMap m) {
		if (mapView.m != m) {
			mapView.set(m);
			lastSelectedItem = null;
			refresh();
		}
	}

	private void setMode() {
		if (mouseDefault.isSelected()) {
			mapView.ml.mode = Mouse.SELECT;
		} else if (mouseAddNodes.isSelected()) {
			mapView.ml.mode = Mouse.ADD_NODE;
		} else if (mouseAddEdges.isSelected()) {
			lastSelectedItem = null;
			mapView.ml.mode = Mouse.ADD_EDGE;
		} else if (mouseEdit.isSelected()) {
			mapView.ml.mode = Mouse.EDIT;
		}
	}

	void refresh() {
		mapView.refresh();
		for (DescriptionDialog d : getOpenDescriptions()) {
			d.refresh();
		}
		DungeonMap m = MappingApplication.getDungeon();
		if (m != null) {
			disMulField.setText("" + m.getDistanceMultiplier());
		}
		this.setTitle("Mapping tool: " + m.getName());
		MappingApplication.quickSave();
		// show information of selected item
	}

	void setLastSelected(GraphElement e) {
		this.lastSelectedItem = e;
		if (lastSelectedItem == null) {
			itemDetails.setText("");
		} else {
			itemDetails.setText(lastSelectedItem.toString());
		}
		refresh();
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

	private void saveAs() {
		String s = (String) JOptionPane.showInputDialog(this, "name of save file", "save as", JOptionPane.PLAIN_MESSAGE,
				null, null, "dungeon");
		DungeonMap m = MappingApplication.getDungeon();
		m.setName(s);
		Persistence.setFilename(s);
		MappingApplication.save();
		MappingApplication.quickSave();
		refreshMap(m);
	}

	private void loadAs() {
		String s = (String) JOptionPane.showInputDialog(this, "name of save file", "load", JOptionPane.PLAIN_MESSAGE,
				null, null, "dungeon");
		if (s == null) {
			return;
		}
		Persistence.setFilename(s);
		refreshMap(MappingApplication.resetDungeon());
	}

	public boolean areNodesShown() {
		return nodesShownButton.isSelected();
	}

	public boolean areDisconnectedNodesHighlighted() {
		return disconnectedNodes.isSelected();
	}

	private void newMap() {
		String s = (String) JOptionPane.showInputDialog(this, "name of new map", "load", JOptionPane.PLAIN_MESSAGE,
				null, null, "dungeon");
		if (s == null) {
			return;
		}
		MapController.newMap(s, true);
		refresh();
	}

	private void deleteMap() {
		int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this map?",
				"Delete " + MappingApplication.getDungeon().getName() + "?", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			System.out.print("test");
			DungeonMap m = MappingApplication.getDungeon();
			MapController.deleteMap(m.getName());
			refreshMap(MappingApplication.getDungeon());
		}
	}
}
