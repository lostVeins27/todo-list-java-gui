package main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

public class AddTask extends JDialog implements ActionListener, DocumentListener {

	
	private final JTextField tfTitle, tfTask;
	private final JButton btnConfirm, btnCancel;
	private final ToDoMain parent;
	
	AddTask(JFrame parentFrame, ToDoMain parent){
		
		this.parent = parent;
		setTitle("Add Task");
		setSize(300, 200);
		setLocationRelativeTo(parentFrame);
		setResizable(false);
		setModal(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(30, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		panel.add(new JLabel("Title:"), gbc);
		
		gbc.gridx = 1;
		tfTitle = new JTextField(10);
		tfTitle.addActionListener(this);
		tfTitle.getDocument().addDocumentListener(this);
		panel.add(tfTitle, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(new JLabel("Task:"), gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		tfTask = new JTextField(10);
		tfTask.addActionListener(this);
		tfTask.getDocument().addDocumentListener(this);
		panel.add(tfTask, gbc);

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
		Font btnFont = new Font("Arial", Font.BOLD, 25);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.setFont(btnFont);
		btnConfirm.setEnabled(false);
		btnConfirm.addActionListener(this);
		btnPanel.add(btnConfirm);
		
		
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(btnFont);
		btnCancel.addActionListener(this);
		btnPanel.add(btnCancel);
		
		add(panel, BorderLayout.NORTH);
		add(btnPanel, BorderLayout.SOUTH);
	
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == tfTitle) {
			
			tfTask.requestFocus();
			
		}
		
		if(e.getSource() == btnConfirm || (e.getSource() == tfTask && btnConfirm.isEnabled() == true)) {
			
			confirmTask();
			 
		}
		
		if(e.getSource() == btnCancel) {
			
			
			dispose();
			
		}
		
	}
	
	private void confirmTask() {
		
		int reply = JOptionPane.showConfirmDialog(null, "Title: " + tfTitle.getText() + "\nTask: " + tfTask.getText() + "\nAre you sure?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(reply == JOptionPane.YES_OPTION) {

			parent.addedTask(new SerializableCheckBox(tfTitle.getText() + " - " + tfTask.getText()));
			
			dispose();
			
		}
		
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		
		if(!tfTitle.getText().equals("") && !tfTask.getText().equals("")){
			btnConfirm.setEnabled(true);
		}
		
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		
		if(tfTitle.getText().equals("") || tfTask.getText().equals("")){
			
			btnConfirm.setEnabled(false);
			
		}
		
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
	
}
