package song_charts_2402;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ApplicationMain {
	public static void main(String[] args) {
		GUI frame = null;
		
		//Create the frame with a static name for now.
		frame =  new GUI("Fake Book Songs");
		
		//Show the window.
		frame.setVisible(true);
	}
}
