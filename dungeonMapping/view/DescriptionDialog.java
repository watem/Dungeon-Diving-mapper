package dungeonMapping.view;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dungeonMapping.model.v1_1.*;
import dungeonMapping.model.v1_1.GraphElement.Description;

@SuppressWarnings("serial")
public class DescriptionDialog extends JDialog {

	JButton colourPicker = new JButton("Colour");
	JTextField lengthValField = new JTextField();
	JLabel lengthValLabel = new JLabel();
	JLabel lengthLabel = new JLabel("length:");
	JComponent lengthVal;

	JLabel widthLabel = new JLabel("width:");
	JTextField widthField = new JTextField();

	JLabel nameLabel = new JLabel("name:");
	JTextField nameField = new JTextField();

	JLabel featuresLabel = new JLabel("features");
	JTextArea features = new JTextArea();
	JLabel treasureLabel = new JLabel("treasure");
	JTextArea treasure = new JTextArea();
	JLabel notesLabel = new JLabel("notes");
	JTextArea notes = new JTextArea();

	JButton update = new JButton("update");

	MainPage parent;
	GraphElement elem;
	Description d;

	public DescriptionDialog(GraphElement elem, MainPage parent) {
		this.elem = elem;
		this.parent = parent;
		d = elem.getDescription();

		refresh();
		menu();
		listeners();

	}

	public void refresh() {
		if (elem instanceof Edge) {
			lengthVal = lengthValLabel;
			lengthValLabel.setText("" + d.length);
		} else {
			lengthVal = lengthValField;
			lengthValField.setText("" + d.length);
		}
		widthField.setText("" + d.width);
		nameField.setText(d.name);

		features.setText(d.features);
		treasure.setText(d.treasure);
		notes.setText(d.note);

	}

	private void menu() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup().addComponent(nameLabel).addComponent(nameField)
						.addComponent(lengthLabel).addComponent(lengthVal).addComponent(widthLabel)
						.addComponent(widthField))
				.addComponent(featuresLabel).addComponent(features).addComponent(treasureLabel).addComponent(treasure)
				.addComponent(notesLabel).addComponent(notes)
				.addGroup(layout.createSequentialGroup().addComponent(colourPicker).addComponent(update))

		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(nameField)
						.addComponent(lengthLabel).addComponent(lengthVal).addComponent(widthLabel)
						.addComponent(widthField))
				.addComponent(featuresLabel).addComponent(features).addComponent(treasureLabel).addComponent(treasure)
				.addComponent(notesLabel).addComponent(notes)
				.addGroup(layout.createParallelGroup().addComponent(colourPicker).addComponent(update)));

		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] { widthField, lengthVal });
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] { nameField, widthField, lengthVal });
		nameField.setMinimumSize(new Dimension(20, 0));
		pack();
	}

	private void listeners() {
		colourPicker.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				d.colour = JColorChooser.showDialog(null, "Choose a color", d.colour);
				parent.refresh();
			}
		});
		update.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				parent.refresh();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				onClose();
//			System.out.println("jdialog window closed event received");
			}

			public void windowClosing(WindowEvent e) {
				onClose();
//		    System.out.println("jdialog window closing event received");
			}
		});
		TextListener.addChangeListener(nameField, e -> {
			nameField.setColumns(nameField.getText().length());
			pack();
		});
		TextListener.addChangeListener(widthField, e -> {
			widthField.setColumns(widthField.getText().length());
			pack();
		});
		TextListener.addChangeListener(lengthValField, e -> {
			lengthValField.setColumns(lengthValField.getText().length());
			pack();
		});
		TextListener.addChangeListener(features, e -> {
			pack();
		});
		TextListener.addChangeListener(treasure, e -> {
			pack();
		});
		TextListener.addChangeListener(notes, e -> {
			pack();
		});
	}

	private void onClose() {
		d.name = nameField.getText();
		d.width = Integer.parseInt(widthField.getText());
		if (elem instanceof Node) {
			try {
				d.length = Integer.parseInt(lengthValField.getText());
			} catch (NumberFormatException e) {
				if (e.getMessage().matches("For input string: \"\\D*\\d+\\D*\"")) {
					d.length = Integer.parseInt(e.getMessage().replaceAll("\\D", ""));
				} else if (e.getMessage().matches("For input string: \".*\"")) {
					d.length = 0;
				} else {
					throw e;
				}
			}
		}
		d.features = features.getText();
		d.treasure = treasure.getText();
		d.note = notes.getText();
		parent.getOpenDescriptions().remove(this);
		parent.refresh();
	}
}
