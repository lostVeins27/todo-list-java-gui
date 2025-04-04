package main;

import javax.swing.*;
import java.io.*;

public class SerializableCheckBox extends JCheckBox implements Serializable {

	SerializableCheckBox(String text) {
		super(text);
	}
	
}
