package song_charts_2402;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class GUI extends JFrame implements ActionListener{
	
	/*
	 * List gets populated with songs from the XML once
	 * and is never modified again.
	 * 
	 * This list is used by the filteredSongList to filter songs
	 * based on user input.
	 * 
	 * Reason this is needed for now is we need two lists; one that
	 * we don't touch that we can always read from and another that
	 * contains the filtered results that we can show to the view.
	 */
	private SongList masterSongList; 
	
	/*
	 * List contains songs from masterSongList that have a 
	 * song title that contains whatever the user types in, case insensitive.
	 */
	private SongList filteredSongList;
	private Song    selectedSong; //song currently selected in the GUI list
	
    private Timer timer; //can used for animation 

	private	JMenuBar		aMenuBar = new JMenuBar();
	private	JMenu			fileMenu = new JMenu("File");
	private	JMenu			playMenu = new JMenu("Play");
	private	JMenu			tempoMenu = new JMenu("Tempo");
	private	JMenu			songMenu = new JMenu("Song");
	private	JMenu			barMenu = new JMenu("Bar");
    //FILE MENU ITEMS
	private JMenuItem		openFileItem = new JMenuItem("Open XML File");    
	private JMenuItem		exportXMLItem = new JMenuItem("Export XML");
	
	private JMenuItem		playItem = new JMenuItem("Play");    
	private JMenuItem		pauseItem = new JMenuItem("Pause");    
	private JMenuItem		stopItem = new JMenuItem("Stop");    
	
	private JMenuItem		t100Item = new JMenuItem("100 bpm");    
	private JMenuItem		t120Item = new JMenuItem("120 bpm");    
	private JMenuItem		t140Item = new JMenuItem("140 bpm");   
	
	//Song items
	private JMenuItem		newSongItem = new JMenuItem("New Song");  
	private JMenuItem		updateSongItem = new JMenuItem("Update Song");  
	
	//Bar items
	private JMenuItem		newBarItem = new JMenuItem("Add Bar");  
	

	// Store the view that contains the components
	ListPanel 		view; //panel of GUI components for the main window
	ChartView chartView; //panel to view PDF charts
	
	GUI thisFrame;

	// Here are the component listeners
	ActionListener			theSearchButtonListener;
	ActionListener			timerListener;
	ListSelectionListener	songListSelectionListener;
	KeyListener             keyListener;

	// Here is the default constructor
	public GUI(String title) {
		super(title);

        masterSongList = new SongList();
        filteredSongList = new SongList();

        //add some sample songs for now
        //songList.add(new Song("All The Things You Are"));
        //songList.add(new Song("The Girl From Ipanema"));
        //songList.add(new Song("My One And Only Love"));
        //songList.add(new Song("Footprints"));
      
        
 		selectedSong = null;
		thisFrame = this;
		
		setJMenuBar(aMenuBar);
		//FILE MENU
		aMenuBar.add(fileMenu);
		fileMenu.add(openFileItem);
		fileMenu.add(exportXMLItem);
		
		openFileItem.addActionListener(this);
		exportXMLItem.addActionListener(this);
		
		//PLAY MENU
		aMenuBar.add(playMenu);
		playMenu.add(playItem);
		playMenu.add(pauseItem);
		playMenu.add(stopItem);

		playItem.addActionListener(this);
		pauseItem.addActionListener(this);
		stopItem.addActionListener(this);
		
		//PLAY MENU
		aMenuBar.add(tempoMenu);
		tempoMenu.add(t100Item);
		tempoMenu.add(t120Item);
		tempoMenu.add(t140Item);

		t100Item.addActionListener(this);
		t120Item.addActionListener(this);
		t140Item.addActionListener(this);


		// Song menu
		aMenuBar.add(songMenu);
		songMenu.add(newSongItem);
		songMenu.add(updateSongItem);
		
		newSongItem.addActionListener(this);
		updateSongItem.addActionListener(this);
		
		// Bar menu
		aMenuBar.add(barMenu);
		barMenu.add(newBarItem);
		
		newBarItem.addActionListener(this);
		
		addWindowListener(
				new WindowAdapter() {
	 				public void windowClosing(WindowEvent e) {
	 					if(timer != null) timer.stop(); //stop animation
						System.exit(0);
					}
				}
			);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		setLayout(layout);

		// Make the main window view panel
		view = new ListPanel(filteredSongList);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
		layoutConstraints.weightx = 1.0;
		layoutConstraints.weighty = 1.0;
		layout.setConstraints(view, layoutConstraints);
		add(view);
		
		// Make the main window view panel
		chartView = new ChartView();
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
		layoutConstraints.weightx = 5.0;
		layoutConstraints.weighty = 1.0;
		layout.setConstraints(chartView, layoutConstraints);
		add(chartView);


			
		theSearchButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					search();
				}};



		// Add a listener to allow selection of buddies from the list
		songListSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				selectSong();
			}};


			
		keyListener = new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) {
						
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {

					int keyChar = arg0.getKeyChar();

			        if (keyChar == KeyEvent.VK_ENTER)  search();
				
				}};


        setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900,700);
		
		int millisecondsBetweenEvents = 1000; 
		timer = new Timer(millisecondsBetweenEvents, this); 
		timer.start(); //start the timer


		// Start off with everything updated properly to reflect the model state
		search();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == timer){
		    tick();
		}
		
		else if(e.getSource() == openFileItem){
			getSongDataFromFile();		    
		}
		else if(e.getSource() == exportXMLItem){
			exportXMLSongDataToFile();		    
		}
		else if(e.getSource() == playItem){
			chartView.startPlayback();		     
		}
		else if(e.getSource() == pauseItem){
			chartView.pausePlayback();		    
		}
		else if(e.getSource() == stopItem){
			chartView.stopPlayback();		    
		}
		else if(e.getSource() == t100Item){
			chartView.setTempo(100);		    
		}
		else if(e.getSource() == t120Item){
			chartView.setTempo(120);		    
		}
		else if(e.getSource() == t140Item){
			chartView.setTempo(140);		    
		}
		else if(e.getSource() == newSongItem){
			if (masterSongList.getSongs().size() > 0){
				new AddSong(this);
			}
		}
		else if (e.getSource() == updateSongItem){
			if (selectedSong != null){
				new SongDetails(this, selectedSong);
			}
		}
		else if (e.getSource() == newBarItem){
			if (selectedSong != null){
				new AddBar(selectedSong);
			}
		}

	}

	
	private void tick(){
		if (selectedSong != null && selectedSong.getBars().size() > 0){
			//Handle a timer event
			chartView.advanceCurrentBar();
			update();
		}
	}
	
	public SongList getMasterSongList(){
		return masterSongList;
	}
	
	private void getSongDataFromFile(){
		
	    File dataFile = getInputFile();
	    SongList theSongs = SongList.parseFromFile(dataFile);
	    if(theSongs != null){
	      masterSongList = theSongs;
	      view.setSongListData(masterSongList);
	      selectedSong = null;
	    }
	    update();
	    
		
	}
	
	private  File getInputFile(){
		
		File dataFile =null;
		
		//Open file dialog to find the data file
   	    String currentDirectoryProperty = System.getProperty("user.dir");
   	    
        JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty); 
        
        
        chooser.setCurrentDirectory(currentDirectory);
        
         
        int returnVal = chooser.showOpenDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION) { 
    
        	dataFile = chooser.getSelectedFile();
        }
        return dataFile;
	}
	
	private  void exportXMLSongDataToFile(){
		
  	    String currentDirectoryProperty = System.getProperty("user.dir");
	    JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty); 

        chooser.setCurrentDirectory(currentDirectory);
        
        int returnVal = chooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) { 
            
        	File file = chooser.getSelectedFile();
        	
            try{ 
                PrintWriter    outputFile = new PrintWriter(new FileWriter(file));

                String indent = "";
                        	
                String XMLDocTypeHeader = "<?xml version = \"1.0\"?>";
                String fakeBookXMLStartTag = "<fakebookXML>";
                String fakeBookXMLEndTag = "</fakebookXML>";

               	outputFile.println(XMLDocTypeHeader);
               	outputFile.println(indent + fakeBookXMLStartTag);

               	filteredSongList.exportXMLToFile(indent+"  ", outputFile);

            	outputFile.println(indent + fakeBookXMLEndTag);

                outputFile.close();
                
                } catch (FileNotFoundException e) { 
                    System.out.println("Error: Cannot open file" + file.getName() + " for writing.");
                    
                } catch (IOException e) { 
                    System.out.println("Error: Cannot write to file: " + file.getName());
                    
                }
            }

	}

	// Enable all listeners
	private void enableListeners() {
		view.getSearchButton().addActionListener(theSearchButtonListener);
		view.getSongJList().addListSelectionListener(songListSelectionListener);
		view.getSearchText().addKeyListener(keyListener);
	}

	// Disable all listeners
	private void disableListeners() {
		view.getSearchButton().removeActionListener(theSearchButtonListener);
		view.getSongJList().removeListSelectionListener(songListSelectionListener);
		view.getSearchText().removeKeyListener(keyListener);
	}


	// This is called when the user clicks the add button
	private void search() {
		
		String searchPrototype = view.getSearchText().getText().trim();

		// Search
		filteredSongList = masterSongList.searchForSongs(masterSongList, searchPrototype);
		
		// Set
		view.setSongListData(filteredSongList);
		
		update();
	}


	// This is called when the user clicks the edit button


	// This is called when the user selects a book from the list

	// This is called when the user selects a song from the list
	private void selectSong() {
		
		//select songs or toggle it off
		selectedSong = (Song)(view.getSongJList().getSelectedValue());
		
		chartView.showSong(selectedSong);
	
		update();
	}


	// Update the remove button
	private void updateSearchButton() {
		view.getSearchButton().setEnabled(true);
	}


	
	// This is used so that the child form (Add Song for example)
	// can tell the presentation layer to update itself.
	public void addSongFromChildForm(Song newSong){
		if (newSong != null){
			// Update both lists
			masterSongList.add(newSong);
			
			// Update the UI
			search();
		}
	}
	
	public void updateSongFromChildForm(){
		// Update the UI
		search();
	}
	
	// Update the list
	private void updateList() {        
		
		if (selectedSong != null)
			view.getSongJList().setSelectedValue(selectedSong, true);
	}
	// Update chart view
	private void updateChartView() {
        
		chartView.update();
	}


	// Update the components
	private void update() {
		disableListeners();
		updateList();
		updateChartView();
		updateSearchButton();
		enableListeners();
	}


}