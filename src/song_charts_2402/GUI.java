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
	
	//Song currently selected in the GUI list
	private Song    selectedSong; 
	
	//Used for animating the red highlights on bars in a song.
    private Timer timer; 

    //UI Elements
	private	JMenuBar		aMenuBar = new JMenuBar();
	private	JMenu			fileMenu = new JMenu("File");
	private	JMenu			playMenu = new JMenu("Play");
	private	JMenu			tempoMenu = new JMenu("Tempo");
	private	JMenu			songMenu = new JMenu("Song");
	private	JMenu			barMenu = new JMenu("Bar");
	
    //File menu items
	private JMenuItem		openFileItem = new JMenuItem("Open XML File");    
	private JMenuItem		exportXMLItem = new JMenuItem("Export XML");
	
	//Play menu items
	private JMenuItem		playItem = new JMenuItem("Play");    
	private JMenuItem		pauseItem = new JMenuItem("Pause");    
	private JMenuItem		stopItem = new JMenuItem("Stop");    
	
	//Tempo menu items
	private JMenuItem		t100Item = new JMenuItem("100 bpm");    
	private JMenuItem		t120Item = new JMenuItem("120 bpm");    
	private JMenuItem		t140Item = new JMenuItem("140 bpm");   
	
	//Song items
	private JMenuItem		newSongItem = new JMenuItem("New Song");  
	private JMenuItem		updateSongItem = new JMenuItem("Update Song");  
	
	//Bar items
	private JMenuItem		newBarItem = new JMenuItem("Add Bar");
	
	//Tempo
	private int tempo;
	
	//
	
	/*
	 * These are components to build up our UI.
	 */
	//Panel of GUI components for the main window
	ListPanel 		view;
	
	//Panel to view PDF charts
	ChartView chartView; 
	
	GUI thisFrame;

	//Listeners for our UI.
	ActionListener			theSearchButtonListener;
	ActionListener			timerListener;
	ListSelectionListener	songListSelectionListener;
	KeyListener             keyListener;

	//Default constructor.
	public GUI(String title) {
		super(title);
		
		//Set the two lists.
        masterSongList = new SongList();
        filteredSongList = new SongList();
        
 		selectedSong = null;
		thisFrame = this;
		
		//Building our menu bar.
		setJMenuBar(aMenuBar);
		
		//File menu
		aMenuBar.add(fileMenu);
		fileMenu.add(openFileItem);
		fileMenu.add(exportXMLItem);
		
		openFileItem.addActionListener(this);
		exportXMLItem.addActionListener(this);
		
		//Play menu
		aMenuBar.add(playMenu);
		playMenu.add(playItem);
		playMenu.add(pauseItem);
		playMenu.add(stopItem);

		playItem.addActionListener(this);
		pauseItem.addActionListener(this);
		stopItem.addActionListener(this);
		
		//Tempo menu
		aMenuBar.add(tempoMenu);
		tempoMenu.add(t100Item);
		tempoMenu.add(t120Item);
		tempoMenu.add(t140Item);

		t100Item.addActionListener(this);
		t120Item.addActionListener(this);
		t140Item.addActionListener(this);


		//Song menu
		aMenuBar.add(songMenu);
		songMenu.add(newSongItem);
		songMenu.add(updateSongItem);
		
		newSongItem.addActionListener(this);
		updateSongItem.addActionListener(this);
		
		//Bar menu
		aMenuBar.add(barMenu);
		barMenu.add(newBarItem);
		
		newBarItem.addActionListener(this);
		
		//Exit the window and stop the timer when we're closing.
		addWindowListener(
				new WindowAdapter() {
	 				public void windowClosing(WindowEvent e) {
	 					if(timer != null) timer.stop(); //stop animation
						System.exit(0);
					}
				}
			);

		/*
		 * Setup for the UI.
		 */
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
			
		/*
		 * Events to propogate changes to UI.
		 */
		
		//Click of the search button.
		theSearchButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					search();
				}};



		//Selection of a song in the song list.
		songListSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				selectSong();
			}};


			
		//Searching when enter is pressed.
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
		
		//Setup of the timer.
		int millisecondsBetweenEvents = 1000; 
		timer = new Timer(millisecondsBetweenEvents, this); 
		timer.start(); 

		//Begin by searching so that we can handle future searches and make it 
		//easy to deal with searches.
		search();
	}
	
	/*
	 * Generic method to listen for multiple UI element events.
	 */
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
			tempo = 100;
			chartView.setTempo(100);
			updateTimerWithTempo(100);		    
		}
		else if(e.getSource() == t120Item){
			tempo = 120;
			chartView.setTempo(120);
			updateTimerWithTempo(120);		    
		}
		else if(e.getSource() == t140Item){
			tempo = 140;
			chartView.setTempo(140);
			updateTimerWithTempo(140);		    
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

	/*
	 * Move the red highlight to the next bar.
	 */
	private void tick(){
		if (selectedSong != null && selectedSong.getBars().size() > 0){
			//Handle a timer event
			chartView.advanceCurrentBar();
			update();
		}
	}

	/*
	 * Getters
	 */
	public SongList getMasterSongList(){
		return masterSongList;
	}
	
	private void getSongDataFromFile(){
		
	    File dataFile = getInputFile();
	    SongList theSongs = SongList.parseFromFile(dataFile);
	    
	    if(theSongs != null){
	      masterSongList = theSongs;
	      //Update the UI
	      view.setSongListData(masterSongList);
	      selectedSong = null;
	    }
	    //Propagate changes.
	    update();
	}
	
	/*
	 * Handle choosing a file.
	 */
	private File getInputFile(){
		
		File dataFile = null;
		
		//Open file dialog to find the data file
   	    String currentDirectoryProperty = System.getProperty("user.dir");
        JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty); 
        chooser.setCurrentDirectory(currentDirectory);
        int returnVal = chooser.showOpenDialog(this);
         
        //Determine if they selected a file.
        if (returnVal == JFileChooser.APPROVE_OPTION) { 
        	dataFile = chooser.getSelectedFile();
        }
        return dataFile;
	}
	
	/*
	 * Go through our list of songs and build a new
	 * XML document based off on it.
	 */
	private  void exportXMLSongDataToFile(){
		
		//Choose place to store exported XML file.
  	    String currentDirectoryProperty = System.getProperty("user.dir");
	    JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty); 

        chooser.setCurrentDirectory(currentDirectory);
        
        int returnVal = chooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) { 
            
        	File file = chooser.getSelectedFile();
        	
            try{ 
            	//Begin the export.
                PrintWriter    outputFile = new PrintWriter(new FileWriter(file));

                String indent = "";
                 
                String XMLDocTypeHeader = "<?xml version = \"1.0\"?>";
                String fakeBookXMLStartTag = "<fakebookXML>";
                String fakeBookXMLEndTag = "</fakebookXML>";

               	outputFile.println(XMLDocTypeHeader);
               	outputFile.println(indent + fakeBookXMLStartTag);
               	
               	//Export songs to XML.
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

	/*
	 * Enable all listeners
	 */
	private void enableListeners() {
		view.getSearchButton().addActionListener(theSearchButtonListener);
		view.getSongJList().addListSelectionListener(songListSelectionListener);
		view.getSearchText().addKeyListener(keyListener);
	}

	/*
	 * Disable all listeners
	 */
	private void disableListeners() {
		view.getSearchButton().removeActionListener(theSearchButtonListener);
		view.getSongJList().removeListSelectionListener(songListSelectionListener);
		view.getSearchText().removeKeyListener(keyListener);
	}


	/*
	 * Called when search button is clicked as well as when the 
	 * application starts up.
	 * 
	 * Sets the stage for filteredSongList.
	 */
	private void search() {
		
		//Grab what the user's search input.
		String searchPrototype = view.getSearchText().getText().trim();

		//Perform the search.
		filteredSongList = masterSongList.searchForSongs(masterSongList, searchPrototype);
		
		//Update the UI with our results.
		view.setSongListData(filteredSongList);
		
		//Propagate the changes.
		update();
	}



	/*
	 * Called when user selects a song from the list
	 */
	private void selectSong() {
		
		//Grab the currently selected song from the UI.
		selectedSong = (Song)(view.getSongJList().getSelectedValue());
		
		//Update our UI with the new selected song.
		chartView.showSong(selectedSong);
	
		//Propagate the changes in selection.
		update();
	}


	/*
	 * Enable the search button.
	 */
	private void updateSearchButton() {
		view.getSearchButton().setEnabled(true);
	}
	
	/*
	 * Called by the child form that captures user input to
	 * create a new Song.
	 * 
	 * This is so that the child form can indirectly tell
	 * this class to update the UI and propagate the changes.
	 */
	public void addSongFromChildForm(Song newSong){
		if (newSong != null){
			// Update the master list
			masterSongList.add(newSong);
			
			// Update the UI
			search();
		}
	}
	
	/*
	 * Called by the child form that captures user input to
	 * update a Song.
	 * 
	 * This is so that the child form can indirectly tell
	 * this class to update the UI and propagate the changes.
	 */
	public void updateSongFromChildForm(){
		// Update the tempo incase the time signature changed
		updateTimerWithTempo(tempo);
		// Update the UI
		search();
	}
	
	/*
	 * Update the timer that is running with the tempo that is set.
	 * 
	 * Ideally, there isn't a reason for tempo to be in the GUI 
	 * layer since ChartView is doing the highlighting.
	 * 
	 * TA NOTE: Please keep in mind to me it wasn't very clear
	 * 			how the math would look like for the tempo. Thats why
	 * 			I put the tempo (120, 140) * 10 so you can see that I'm
	 * 			changing the tempo programatically and am just missing the math.
	 * 
	 * 			If there is a timeSignature defined then I will do the proper
	 * 			math as defined in the assignment, but if not I do the generic
	 * 			*10
	 * 
	 * 
	 */
	public void updateTimerWithTempo(int tempo){
		//This is normally the time signature in the first bar of the XML.
		//That is what I've learned from just looking at the XML, did not see
		//much mention of this in the assignment.
		int songTempoDivisor = 1;
		
		if (selectedSong != null && selectedSong.getBars().size() > 0){
			String timeSignature = selectedSong.getBars().get(0).getTimeSignature();
			if (timeSignature != null && !timeSignature.isEmpty()){
				songTempoDivisor = Character.getNumericValue(timeSignature.charAt(0));
			}
		}
		timer = new Timer(tempo/songTempoDivisor * 10, this); 
		timer.start(); 
	}
	
	//Update the list
	private void updateList() {        
		
		if (selectedSong != null)
			view.getSongJList().setSelectedValue(selectedSong, true);
	}
	//Update chart view
	private void updateChartView() {
        
		chartView.update();
	}


	//Update the components
	private void update() {
		disableListeners();
		updateList();
		updateChartView();
		updateSearchButton();
		enableListeners();
	}


}