package dungeonMapping.view;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import dungeonMapping.application.MappingApplication;
import dungeonMapping.controller.MapController;
import dungeonMapping.model.v1_3.DungeonMap;
import dungeonMapping.model.v1_3.GraphElement;
import dungeonMapping.model.v1_3.Node;
import dungeonMapping.serializing.Persistence;
import findPath.AstarNode;

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
	private JRadioButton mousePaint = new JRadioButton("Paint", false);
	private JRadioButton mousePath = new JRadioButton("Path", false);
	private ButtonGroup mouseBehaviour = new ButtonGroup();

	private JLabel pathlength = new JLabel();
	
	private JLabel itemDetails = new JLabel();

	private JLabel disMulLabel = new JLabel("distance multiplier");
	private JTextField disMulField = new JTextField();

	private JButton saveButton = new JButton("save");
	private JButton saveAsButton = new JButton("save as");
	private JButton loadButton = new JButton("load");
	private JButton colourPickerButton = new JButton("");
	private Color currentColour = Color.black;

	private JButton zoomIn = new JButton("Zoom +");
	private JButton zoomOut = new JButton("Zoom -");

	private JCheckBox nodesShownButton = new JCheckBox("Show nodes?", true);
	private JCheckBox disconnectedNodes = new JCheckBox("Highlight disconnected nodes?", false);
	private JCheckBox showZLevel = new JCheckBox("Show Z Level?", true);
	private JCheckBox showColour = new JCheckBox("Show Colour?", true);

	private JLabel zLevelLabel = new JLabel("height level");
	private JTextField zLevelField = new JTextField();
	private JLabel heightMulLabel = new JLabel("height distance multiplier");
	private JTextField heightMulField = new JTextField();
	private int currentZLevel = 0;

	public MainPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mouseBehaviour.add(mouseDefault);
		mouseBehaviour.add(mouseAddNodes);
		mouseBehaviour.add(mouseAddEdges);
		mouseBehaviour.add(mouseEdit);
		mouseBehaviour.add(mousePaint);
		mouseBehaviour.add(mousePath);
		colourPickerButton.setBackground(currentColour);
		
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
		mousePaint.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setMode();
			}
		});
		mousePath.addActionListener(new java.awt.event.ActionListener() {
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
		TextListener.addChangeListener(heightMulField, e -> {
			DungeonMap m = MappingApplication.getDungeon();
			m.setHeightDistanceMultiplier(Double.parseDouble(heightMulField.getText()));
		});
		TextListener.addChangeListener(zLevelField, e -> {
			currentZLevel = Integer.parseInt(zLevelField.getText());
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
		colourPickerButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				currentColour = JColorChooser.showDialog(null, "Choose a color", currentColour);
				colourPickerButton.setBackground(currentColour);
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
		showZLevel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mapView.refresh();
			}
		});
		showColour.addActionListener(new java.awt.event.ActionListener() {
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
				.addGroup(layout.createSequentialGroup()
						.addComponent(newMapButton)
						.addComponent(deleteMapButton)
						.addComponent(saveButton)
						.addComponent(saveAsButton)
						.addComponent(loadButton))
				.addGroup(layout.createSequentialGroup()
						.addComponent(mapView)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(mouseDefault)
										.addComponent(mouseAddNodes)
										.addComponent(mouseAddEdges)
										.addComponent(mouseEdit)
										.addComponent(mousePaint)
										.addComponent(mousePath))
								.addGroup(layout.createSequentialGroup()
										.addComponent(editButton)
										.addComponent(deleteItemButton))
								.addGroup(layout.createSequentialGroup()
										.addComponent(nodesShownButton)
										.addComponent(disconnectedNodes))
								.addGroup(layout.createSequentialGroup()
										.addComponent(showZLevel)
										.addComponent(showColour))
								.addComponent(itemDetails)
								.addComponent(pathlength)
								.addGroup(layout.createSequentialGroup()
										.addComponent(disMulLabel)
										.addComponent(disMulField))
								.addGroup(layout.createSequentialGroup()
										.addComponent(heightMulLabel)
										.addComponent(heightMulField))
								.addGroup(layout.createSequentialGroup()
										.addComponent(zLevelLabel)
										.addComponent(zLevelField))
								.addComponent(colourPickerButton)
								.addGroup(layout.createSequentialGroup()
										.addComponent(zoomIn)
										.addComponent(zoomOut))
						)
				)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(newMapButton)
						.addComponent(deleteMapButton)
						.addComponent(saveButton)
						.addComponent(saveAsButton)
						.addComponent(loadButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(mapView)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(mouseDefault)
										.addComponent(mouseAddNodes)
										.addComponent(mouseAddEdges)
										.addComponent(mouseEdit)
										.addComponent(mousePaint)
										.addComponent(mousePath))
								.addGroup(layout.createParallelGroup()
										.addComponent(editButton)
										.addComponent(deleteItemButton))
								.addGroup(layout.createParallelGroup()
										.addComponent(nodesShownButton)
										.addComponent(disconnectedNodes))
								.addGroup(layout.createParallelGroup()
										.addComponent(showZLevel)
										.addComponent(showColour))
								.addComponent(itemDetails)
								.addComponent(pathlength)
								.addGroup(layout.createParallelGroup()
										.addComponent(disMulLabel)
										.addComponent(disMulField))
								.addGroup(layout.createParallelGroup()
										.addComponent(heightMulLabel)
										.addComponent(heightMulField))
								.addGroup(layout.createParallelGroup()
										.addComponent(zLevelLabel)
										.addComponent(zLevelField))
								.addComponent(colourPickerButton)
								.addGroup(layout.createParallelGroup()
										.addComponent(zoomIn)
										.addComponent(zoomOut)))));
		layout.linkSize(SwingConstants.HORIZONTAL,
				new java.awt.Component[] { mouseAddNodes, mouseDefault, mouseAddEdges });
//		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {});
//		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newMapButton});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] { disMulLabel, disMulField, heightMulLabel, heightMulField, zLevelLabel, zLevelField });
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] { disMulLabel, disMulField, heightMulLabel, heightMulField, zLevelLabel, zLevelField });

		int size = 2*colourPickerButton.getPreferredSize().height;
		colourPickerButton.setSize(size, size);
		colourPickerButton.setMaximumSize(colourPickerButton.getSize());
		colourPickerButton.setMinimumSize(colourPickerButton.getSize());
		zLevelField.setText("0");
		
		mapView.setMinimumSize(new Dimension(100, 100));
		mapView.setPreferredSize(new Dimension(700, 700));
		pack();

	}

	void refreshMap(DungeonMap m) {
		if (mapView.m != m) {
			mapView.set(m);
			lastSelectedItem = null;
			refresh();
			mapView.refresh();
		}
	}

	private void setMode() {
		pathlength.setText("");
		mapView.path = null;
		if (mouseDefault.isSelected()) {
			mapView.ml.mode = Mouse.SELECT;
		} else if (mouseAddNodes.isSelected()) {
			mapView.ml.mode = Mouse.ADD_NODE;
		} else if (mouseAddEdges.isSelected()) {
			lastSelectedItem = null;
			mapView.ml.mode = Mouse.ADD_EDGE;
		} else if (mouseEdit.isSelected()) {
			mapView.ml.mode = Mouse.EDIT;
		} else if (mousePaint.isSelected()) {
			mapView.ml.mode = Mouse.PAINT;
		} else if (mousePath.isSelected()) {
			mapView.ml.mode = Mouse.PATH;
			pathlength.setText("Length: ");
		}
	}

	void refresh() {
		for (DescriptionDialog d : getOpenDescriptions()) {
			d.refresh();
		}
		DungeonMap m = MappingApplication.getDungeon();
		if (m != null) {
			disMulField.setText("" + m.getDistanceMultiplier());
			heightMulField.setText("" + m.getHeightDistanceMultiplier());
		}
		mapView.refresh();
		this.setTitle("Mapping tool: " + m.getName());
		MappingApplication.quickSave();
		// show information of selected item
	}

	void setLastSelected(GraphElement e) {
//		if(lastSelectedItem!=null) {
//			mapView.path = AstarNode.findBestPath(mapView.m, (dungeonMapping.model.v1_2.Node)lastSelectedItem, (dungeonMapping.model.v1_2.Node)e);
//		}
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

	public Color getCurrentColour() {
		return new Color(currentColour.getRed(), currentColour.getGreen(), currentColour.getBlue(), currentColour.getAlpha());
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
		File p = getPath("Save As.");
		if (p != null) {
			DungeonMap m = MappingApplication.getDungeon();
			m.setName(p.getName());
			Persistence.setPath(p);
			MappingApplication.save();
			MappingApplication.quickSave();
			refreshMap(m);
		}
	}

	private void loadAs() {
		File p = getPath("Load As.");
		if (p != null) {
			Persistence.setPath(p);
			refreshMap(MappingApplication.resetDungeon());
		}
	}
	
	private File getPath(String title) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(Persistence.folder));
		chooser.setDialogTitle(title);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Map file", Persistence.extension);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File f = chooser.getSelectedFile();
	    	if (f == null) {
	    		return null;
	    	}
	    	String filename = f.getName();
	    	if (filename.length() < Persistence.extension.length() + 1 || !filename.substring(
	    			filename.length() - Persistence.extension.length() - 1).equals("." + Persistence.extension)) {
	    		f = new File(f.toPath() + "." + Persistence.extension);
	    	}
	    	return f;
	    }
	    return null;
	}

	public boolean areNodesShown() {
		return nodesShownButton.isSelected();
	}

	public boolean areDisconnectedNodesHighlighted() {
		return disconnectedNodes.isSelected();
	}
	
	public boolean areZLevelsShown() {
		return showZLevel.isSelected();
	}
	
	public boolean areColoursShown() {
		return showColour.isSelected();
	}

	private void newMap() {
		File p = getPath("New Map");
		if (p != null) {
			MapController.newMap(p, true);
			refresh();
			mapView.set(MappingApplication.getDungeon());
		}
	}

	private void deleteMap() {
		int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this map?",
				"Delete " + MappingApplication.getDungeon().getName() + "?", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			System.out.print("test");
			MapController.deleteMap(Persistence.getPath());
			refreshMap(MappingApplication.getDungeon());
		}
	}
	
	public int getCurrentZLevel() {
		return currentZLevel;
	}

	public void updatePath(List<Node> path) {
		if (path != null) {
			double distance = 0;
			Node previous = null;
			for(Node n: path) {
				if (previous != null) {
					distance += AstarNode.distance(previous, n, mapView.m);
				}
				previous = n;
			}
			pathlength.setText("Length: " + (int) distance);
		} else {
			pathlength.setText("Length: ");
		}
		
		// TODO Auto-generated method stub
		
	}
}
