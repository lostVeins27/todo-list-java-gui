package main;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ArchiveTask extends JDialog implements ActionListener {
	
	// GUI COMPONENTS
	private final JButton btnRemoveSelected, btnRemoveAll, btnExit; // Buttons for task removal and exit.
	private final JPanel panelArchivedTask; // Panel displaying the list of archived task.
	private static List<SerializableCheckBox> archivedTask = new ArrayList<>(); // Stores serialized check box representing tasks archive from ToDo list.
	private static boolean hasAdd; // Flag indicating whether a task has been passed from main ToDo Frame to the ArchiveTask
	JFrame parentFrame; // the main frame that contains the ToDo list.
	JToolBar tb; // Tool bar for select all & de-select all buttons.
	
	// Constructor for the ArchiveTask window. It takes the parent frame as a parameter.
	ArchiveTask(JFrame parentFrame){
	
		this.parentFrame = parentFrame; // sets parent frame for positioning the window relative to it.
		
		setTitle("Archive"); // Sets title to the window "Archive".
		setResizable(false); // Disables resize-able window. If true enables window resizing.
		setSize(300, 200); // Sets window size (width, length).
		setLocationRelativeTo(parentFrame); // Centers the window relative to the parent frame.
		setModal(true); // Makes the window modal which blocks all other window interactions until closed.
		
		// Creating and configuring the panel to hold archive task, inside a scroll-able area.
		panelArchivedTask = new JPanel(); // Initialize a new panel for holding archived tasks.
		panelArchivedTask.setLayout(new BoxLayout(panelArchivedTask, BoxLayout.Y_AXIS)); // Sets the layout to display the task to goes under another.
		
		
		// If the has add is true, meaning an archived tasks were added, it will run the following code.
		if(hasAdd) {
			Collections.reverse(archivedTask); // Reverses the order of tasks in the list.
			hasAdd = false; // Sets 'hasAdd' to false because that only runs once if archived tasks were added.
		}
		
		// Call the method 'toLabel' that converts check boxes(the archive task) to a label 
		toLabel();
		
		// Create a JScrollPane to hold and make the panel that contains archived tasks scroll-able.
		JScrollPane sp = new JScrollPane(panelArchivedTask);
		add(sp, BorderLayout.CENTER); // Adds the scroll pane to the center of the window.
		
		//Create a panel that will hold the Buttons in the bottom of the window.
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		
		// Create and configure the "REMOVE SELECTED" Button;
		Font btnFont = new Font("Arial", Font.BOLD, 10); // Sets a font that will be shared to all the buttons.
		btnRemoveSelected = new JButton("REMOVE SELECTED"); // Initialize the "REMOVE SELECTED" button.
		btnRemoveSelected.setFont(btnFont); // Sets the font for the button.
		btnRemoveSelected.setPreferredSize(new Dimension(110, 23)); // Sets the preferred size for the button.
		btnRemoveSelected.setMargin(new Insets(0, 0, 0, 0)); // Sets margin to remove extra spaces around the button text.
		btnRemoveSelected.addActionListener(this); // Add action listener to handle button clicks.
		btnPanel.add(btnRemoveSelected); // Add the button to the button panel.
		
		btnRemoveAll = new JButton("REMOVE ALL"); // Initialize the "REMOVE ALL" button.
		btnRemoveAll.setFont(btnFont); // Sets the font for the button.
		btnRemoveAll.addActionListener(this); // Adds action listener for the button "REMOVE ALL".
		btnPanel.add(btnRemoveAll); // Adds the "REMOVE ALL" button to the button panel.
		
		btnExit = new JButton("EXIT"); // Initialize the "EXIT" button.
		btnExit.setFont(btnFont); // Sets the font for the button.
		btnExit.addActionListener(this); // Adds action listener for the "EXIT" button.
		btnPanel.add(btnExit); // Adds the "EXIT" button to the button panel.
		
		add(btnPanel, BorderLayout.SOUTH); // Adds the button panel to the south(bottom) border of the frame.
		
		
		setVisible(true); // Makes the window visible to the user.
		
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// runs whenever the "REMOVE SELECTED" button is clicked.
		if(e.getSource() == btnRemoveSelected) {
				
			// Calls 
			removeSelectedButtonSwitch();
			
		}
		
		if(e.getSource() == btnRemoveAll) {
			
			if(!archivedTask.isEmpty()) {
				if(JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Remove All?\nThis Cannot Be Undone.", "Remove All", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					
					archivedTask.clear();
					panelArchivedTask.removeAll();
					
					if(Boolean.TRUE.equals(btnRemoveSelected.getClientProperty("isCheckBox"))) {
						remove(tb);
						tb = null;
					}
					
					revalidate();
					repaint();
				}
			}
		}
		
		if(e.getSource() == btnExit) {
			dispose();
		}
		
		
	}
	
	private void removeSelectedButtonSwitch() {
		if(archivedTask.size() != 0) {
			

			//boolean for knowing if the label are converted to check box or vice versa
			boolean isCheckBox = Boolean.TRUE.equals(btnRemoveSelected.getClientProperty("isCheckBox"));
			
			//list to remove to the panel and to the archived task list
			List<JCheckBox> toRemove;
			
			
			//if structure for conversion of label to check box or vice versa, and removing of the selected check boxes
			if(!isCheckBox) {

				panelArchivedTask.removeAll();
				
				//if it is label this will convert it into a check box
				for(JCheckBox ar : archivedTask) {
					panelArchivedTask.add(ar);
				}

				tb = new JToolBar();
				tb.setFloatable(false);
				tb.setRollover(true);
				tb.setPreferredSize(new Dimension(20,20));
				
				JButton btnSelectAll = new JButton("SelectAll");
				btnSelectAll.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(JCheckBox cb : archivedTask) {
							cb.setSelected(true);
						}

					}
					
				});
				
				JButton btnDeSelectAll = new JButton("DeSelectAll");
				btnDeSelectAll.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						for(JCheckBox cb : archivedTask) {
							cb.setSelected(false);
						}
					}
				});
				
				
				tb.add(btnSelectAll);
				tb.addSeparator(new Dimension(10, 0));
				tb.add(btnDeSelectAll);
				

				add(tb, BorderLayout.NORTH);
				
				
				btnRemoveSelected.putClientProperty("isCheckBox", !isCheckBox);
				
				
			}else {
				
				boolean replied = false;
				int reply = JOptionPane.NO_OPTION;
				
				toRemove = new ArrayList<>();
				
				for(JCheckBox ar : archivedTask) {
					
					if(ar.isSelected()) {
						
						
						if(!replied){
							reply = JOptionPane.showConfirmDialog(null, "Are You Sure You Wan't To Remove Selected Archives?\nThis Cannot Be Undone.", "Removing Archive", JOptionPane.YES_NO_OPTION);
							replied = true;
							
						}
						
						if(reply == JOptionPane.YES_OPTION) {
							
							toRemove.add(ar);
						}
						
						
					}
				}
				
				if(reply == JOptionPane.YES_OPTION || toRemove.isEmpty()) {
					
					remove(tb);
					tb = null;
					
					panelArchivedTask.removeAll();
					
					unChecker();
					
					//for loop for removing to the archived task
					for(JCheckBox ar : toRemove) {
						
						archivedTask.remove(ar);
						
					}
					
					toLabel();
					
					btnRemoveSelected.putClientProperty("isCheckBox", !isCheckBox);
				}
				
				
				
			}
				
			revalidate();
			repaint();
		}
	}
	
	private void toLabel() {
		
		//if it is check box this will convert it into a label
		for(JCheckBox ar : archivedTask) {
			panelArchivedTask.add(new JLabel(ar.getText() + " - ✓"));
		}
		
	}
	
	public static void addedArchive(SerializableCheckBox archive, boolean hasAdd) {
		
		ArchiveTask.hasAdd = hasAdd;
		ArchiveTask.archivedTask.add(archive);
		
	}
	
	public static void addedArchive(List<SerializableCheckBox> archive) {
		
		ArchiveTask.archivedTask = archive;
		
	}
	
	public static List<SerializableCheckBox> getArchive() {
		return archivedTask;
	}
	
	public static void reverseCollection() {
		Collections.reverse(archivedTask);
	}
	
	public void unChecker() {
		
		for(JCheckBox ar : archivedTask) {
			ar.setSelected(false);
		}
	}
	
}
