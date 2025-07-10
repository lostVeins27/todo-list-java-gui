package main;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.awt.font.TextAttribute;

public class ToDoMain extends JFrame implements ActionListener, ItemListener {
	
	private final JButton btnAdd, btnArchive, btnClearTask;
	JPanel panelTask;
	private final JScrollPane sp;
	
	private final JMenuItem menuItemArchive, menuItemQuit, menuItemProgrammer;
	
	List<SerializableCheckBox> taskCheckBox = new ArrayList<>();

	
	ToDoMain(){

		JMenu menuFile = new JMenu("File");
		menuItemArchive = new JMenuItem("Archive");
		menuItemArchive.addActionListener(this);
		menuItemQuit = new JMenuItem("Quit");
		menuItemQuit.addActionListener(this);
		menuFile.add(menuItemArchive);
		menuFile.add(menuItemQuit);
		
		JMenu menuAbout = new JMenu("About");
		menuItemProgrammer = new JMenuItem("Programmer");
		menuItemProgrammer.addActionListener(this);
		menuAbout.add(menuItemProgrammer);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuAbout);
		setJMenuBar(menuBar);
		
		JLabel title = new JLabel("TO-DO LISTS:", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);
		
		panelTask = new JPanel();
		panelTask.setLayout(new BoxLayout(panelTask, BoxLayout.Y_AXIS));
		
		loadTasks();
		
		sp = new JScrollPane(panelTask);
		add(sp, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel();
		
		btnPanel.add(btnAdd = new JButton("ADD TASK"));
		btnAdd.addActionListener(this);
		
		btnArchive = new JButton("ARCHIVE TASK");
		btnArchive.addActionListener(this);
		btnPanel.add(btnArchive);
		
		btnClearTask = new JButton("CLEAR TASK");
		btnClearTask.addActionListener(this);
		btnPanel.add(btnClearTask);
		
		setButtonsState(taskCheckBox);
		
		add(btnPanel, BorderLayout.SOUTH);
		
		setTitle("To-Do List");
		setSize(400, 400);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveTasks();
			}
		});
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnAdd) {
			new AddTask(this, this);
		}
		
		if(e.getSource() == btnArchive) {
			archiveTasks();
		}
		
		if(e.getSource() == btnClearTask) {
			
			clearTasks();
		}
		
		
		if(e.getSource() == menuItemArchive) {

			new ArchiveTask(this);
			
		}
		
		setButtonsState(taskCheckBox);
		
		if(e.getSource() == menuItemQuit) {
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
			
			if(reply == JOptionPane.YES_OPTION) {
				saveTasks();
				System.exit(0);
			}
			
		}
		
		if(e.getSource() == menuItemProgrammer) {
			JOptionPane.showMessageDialog(null, "Programmed by Daniel", "About Programmer", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void setButtonsState(List<SerializableCheckBox> taskslist) {
		if (taskslist != null) {
			btnArchive.setEnabled(!taskslist.isEmpty());
			btnClearTask.setEnabled(!taskslist.isEmpty());
		}
	}
	
	private void archiveTasks() {
		int reply = JOptionPane.NO_OPTION;
		boolean replied = false;

		List<SerializableCheckBox> toArchive = new ArrayList<>();
		
		for(SerializableCheckBox checkBox : taskCheckBox) {
			
			if(checkBox.isSelected()) {
				
				if(!replied) {
					reply = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Archive Finished Tasks?\nThis Cannot Be Undone.", "Archiving", JOptionPane.YES_NO_OPTION);
					replied = true;
				}
				
				if(reply == JOptionPane.YES_OPTION) {

					toArchive.add(checkBox);
					
					
				}
				
			}
			
			
		}
		
		
		if(reply == JOptionPane.YES_OPTION && !toArchive.isEmpty()) {
			

			ArchiveTask.reverseCollection();
			Collections.reverse(toArchive);
			
			for(SerializableCheckBox checkBox : toArchive) {

				checkBox.setSelected(false);
				ArchiveTask.addedArchive(checkBox, true);
				panelTask.remove(checkBox);
				taskCheckBox.remove(checkBox);
				
			}
			
			revalidate();
			repaint();
			
			
		}
	}
	
	private void clearTasks() {
		
		if(taskCheckBox.size() != 0 ) {
			
			int reply = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Delete All The Task?", "Confirm Delete All", JOptionPane.YES_NO_OPTION);
			
			if(reply == JOptionPane.YES_OPTION) {
				taskCheckBox.clear();
				panelTask.removeAll();
				revalidate();
				repaint();
				
			}
		}
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		Font fontCheckBox = new Font("Arial", Font.PLAIN, 12);
		Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) fontCheckBox.getAttributes();
		
		for(SerializableCheckBox cb : taskCheckBox) {
			if(cb.isSelected()) {
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				
			}else{
				attributes.remove(TextAttribute.STRIKETHROUGH);
				
			}
			
			cb.setFont(fontCheckBox.deriveFont(attributes));
			
			
		}
		
	}
	
	public void addedTask(SerializableCheckBox task) {
		task.setFont(new Font("Arial", Font.PLAIN, 12));
		taskCheckBox.add(task);
		panelTask.add(task);
		task.addItemListener(this);
		revalidate();
		repaint();
	}
	
	private String getFilePath(String fileName) {
		String folderPath = System.getProperty("user.home") + "/Documents/ToDoList Archive";
		
		File folder = new File(folderPath);
		if(!folder.exists() && !folder.mkdirs()) {
			System.err.println("No Folder is created");
		}
		
		return folderPath + "/" + fileName;
	}
	
	
	private void saveTasks() {
		
		saveToFile("tasks.dat", taskCheckBox);
		saveToFile("archive.dat", ArchiveTask.getArchive());
		
	}
	
	private void saveToFile(String fileName, Object data) {
		
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFilePath(fileName)))){
			
			oos.writeObject(data);
			
		}catch(IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void loadTasks() {
		
		taskCheckBox = loadFromFile("tasks.dat");
		ArchiveTask.addedArchive(loadFromFile("archive.dat"));
		taskCheckBox.forEach(panelTask::add);
		
		revalidate();
		repaint();
	}
	
	private List<SerializableCheckBox> loadFromFile(String fileName) {
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFilePath(fileName)))) {
			
	        return (List<SerializableCheckBox>) ois.readObject();
	        
	    } catch (IOException | ClassNotFoundException e) {
	        return new ArrayList<>();
	    }
		
	}
	
	public static void main(String[] args) {
		new ToDoMain();
	}
	
}
