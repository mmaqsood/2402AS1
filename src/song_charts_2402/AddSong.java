package song_charts_2402;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class AddSong {

	private JFrame frame;
	private JTextField titleText;
	private JTextField composerText;
	private JTextField musicalStyleText;
	private JTextField keyText;
	private JTextField tempoText;
	private JComboBox comboBox;
	private SongList masterSongList; 
	private GUI parentForm;

	/**
	 * Create the application.
	 */
	public AddSong(GUI callingWindow) {
		masterSongList = callingWindow.getMasterSongList();
		parentForm = callingWindow;
		initialize();
	}
	
	private void populateCombo(){
		
		// Helps us find out which song after
		int index = 1;
		for (Song s : masterSongList.getSongs()){
			comboBox.addItem(index + ". " + s.getTitle());
			index++;
		}
	}
	
	private void populateFieldsFromSelectedSong(){
		Object selectedItem = comboBox.getSelectedItem();
		
		if (selectedItem != null && selectedItem != ""){
			int songIndex = Character.getNumericValue(selectedItem.toString().charAt(0)) - 1;
			Song selectedSong = masterSongList.getSongs().get(songIndex);
			
			titleText.setText(selectedSong.getTitle());
			composerText.setText(selectedSong.getComposer());
			musicalStyleText.setText(selectedSong.getMusicalStyle());
			keyText.setText(selectedSong.getKey());
			tempoText.setText(selectedSong.getTempo());
		}
		else {
			titleText.setText("");
			composerText.setText("");
			musicalStyleText.setText("");
			keyText.setText("");
			tempoText.setText("");
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 303);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(6, 72, 61, 16);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblComposer = new JLabel("Composer:");
		lblComposer.setBounds(6, 110, 82, 16);
		frame.getContentPane().add(lblComposer);
		
		JLabel lblMusicalStyle = new JLabel("Musical Style:");
		lblMusicalStyle.setBounds(6, 150, 98, 16);
		frame.getContentPane().add(lblMusicalStyle);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setBounds(6, 184, 98, 16);
		frame.getContentPane().add(lblKey);
		
		titleText = new JTextField();
		titleText.setBounds(99, 66, 345, 28);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		
		composerText = new JTextField();
		composerText.setColumns(10);
		composerText.setBounds(100, 104, 345, 28);
		frame.getContentPane().add(composerText);
		
		musicalStyleText = new JTextField();
		musicalStyleText.setColumns(10);
		musicalStyleText.setBounds(99, 144, 345, 28);
		frame.getContentPane().add(musicalStyleText);
		
		keyText = new JTextField();
		keyText.setColumns(10);
		keyText.setBounds(99, 178, 345, 28);
		frame.getContentPane().add(keyText);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Song newSong = new Song(titleText.getText(),
										composerText.getText(),
										musicalStyleText.getText(),
										keyText.getText(),
										tempoText.getText());
				
				// Update the parent with the new song.
				parentForm.addSongFromChildForm(newSong);
				frame.dispose();
			}
		});
		btnNewButton.setBounds(327, 246, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnCancel.setBounds(211, 246, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		tempoText = new JTextField();
		tempoText.setColumns(10);
		tempoText.setBounds(99, 212, 345, 28);
		frame.getContentPane().add(tempoText);
		
		JLabel lblTempo = new JLabel("Tempo:");
		lblTempo.setBounds(6, 218, 98, 16);
		frame.getContentPane().add(lblTempo);
		
		JLabel lblNewLabel = new JLabel("Copy Fields From: ");
		lblNewLabel.setBounds(6, 21, 126, 16);
		frame.getContentPane().add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
	        @Override
			public void itemStateChanged(ItemEvent e) {
				populateFieldsFromSelectedSong();
			}
	    });
		comboBox.setBounds(127, 17, 317, 27);
		comboBox.addItem("");
		
		frame.getContentPane().add(comboBox);
		
		populateCombo();
		
		frame.setVisible(true);
	}
}
