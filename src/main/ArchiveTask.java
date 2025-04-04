package main;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ArchiveTask extends JDialog implements ActionListener {

	
	private final JButton btnRemoveSelected, btnRemoveAll, btnExit;
	private final JPanel panelArchivedTask;
	private static List<SerializableCheckBox> archivedTask = new ArrayList<>();
	private static boolean hasAdd;
	
	JFrame parentFrame;
	
	JToolBar tb;
	
	ArchiveTask(JFrame parentFrame){
		
		this.parentFrame = parentFrame;
		
		setTitle("Archive");
		setResizable(false);
		setSize(300, 200);
		setLocationRelativeTo(parentFrame);
		setModal(true);
		
		//panel layout inside JScrollPane
		panelArchivedTask = new JPanel();
		panelArchivedTask.setLayout(new BoxLayout(panelArchivedTask, BoxLayout.Y_AXIS));
		
		if(hasAdd) {
			Collections.reverse(archivedTask);
			hasAdd = false;
		}
		
		toLabel();
		
		//JScrollPane that will hold the panel
		JScrollPane sp = new JScrollPane(panelArchivedTask);
		//border layout centering the scroll pane
		add(sp, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		Font btnFont = new Font("Arial", Font.BOLD, 10);
		
		btnRemoveSelected = new JButton("REMOVE SELECTED");
		btnRemoveSelected.setFont(btnFont);
		btnRemoveSelected.setPreferredSize(new Dimension(110, 23));
		btnRemoveSelected.setMargin(new Insets(0, 0, 0, 0));
		btnRemoveSelected.addActionListener(this);
		btnPanel.add(btnRemoveSelected);
		
		btnRemoveAll = new JButton("REMOVE ALL");
		btnRemoveAll.setFont(btnFont);
		btnRemoveAll.addActionListener(this);
		btnPanel.add(btnRemoveAll);
		
		btnExit = new JButton("EXIT");
		btnExit.setFont(btnFont);
		btnExit.addActionListener(this);
		btnPanel.add(btnExit);
		
		add(btnPanel, BorderLayout.SOUTH);
		
		
		setVisible(true);
		
		
		
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnRemoveSelected) {
				
			
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
