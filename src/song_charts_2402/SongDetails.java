package song_charts_2402;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class SongDetails {

	private JFrame frame;
	private JTextField titleText;
	private JTextField composerText;
	private JTextField musicalStyleText;
	private JTextField keyText;
	private JTextField tempoText;
	
	private Song song;
	private GUI parentForm;
	
	/**
	 * Create the application.
	 */
	public SongDetails(GUI callingWindow, Song _song) {
		parentForm = callingWindow;
		song = _song;
		initialize();
	}
	
	private void populateFieldsFromSong(){
		if (song != null){
			titleText.setText(song.getTitle());
			composerText.setText(song.getComposer());
			musicalStyleText.setText(song.getMusicalStyle());
			keyText.setText(song.getKey());
			tempoText.setText(song.getTempo());
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 262);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(6, 23, 61, 16);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblComposer = new JLabel("Composer:");
		lblComposer.setBounds(6, 61, 82, 16);
		frame.getContentPane().add(lblComposer);
		
		JLabel lblMusicalStyle = new JLabel("Musical Style:");
		lblMusicalStyle.setBounds(6, 101, 98, 16);
		frame.getContentPane().add(lblMusicalStyle);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setBounds(6, 135, 98, 16);
		frame.getContentPane().add(lblKey);
		
		titleText = new JTextField();
		titleText.setBounds(99, 17, 345, 28);
		frame.getContentPane().add(titleText);
		titleText.setColumns(10);
		
		composerText = new JTextField();
		composerText.setColumns(10);
		composerText.setBounds(100, 55, 345, 28);
		frame.getContentPane().add(composerText);
		
		musicalStyleText = new JTextField();
		musicalStyleText.setColumns(10);
		musicalStyleText.setBounds(99, 95, 345, 28);
		frame.getContentPane().add(musicalStyleText);
		
		keyText = new JTextField();
		keyText.setColumns(10);
		keyText.setBounds(99, 129, 345, 28);
		frame.getContentPane().add(keyText);
		
		JButton btnSaveButton = new JButton("Save");
		btnSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				song.setTitle(titleText.getText());
				song.setComposer(composerText.getText());
				song.setMusicalStyle(musicalStyleText.getText());
				song.setKey(keyText.getText());
				song.setTempo(tempoText.getText());
				
				// Update parent
				parentForm.updateSongFromChildForm();
				frame.dispose();
			}
		});
		btnSaveButton.setBounds(327, 203, 117, 29);
		frame.getContentPane().add(btnSaveButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnCancel.setBounds(211, 203, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		tempoText = new JTextField();
		tempoText.setColumns(10);
		tempoText.setBounds(99, 163, 345, 28);
		frame.getContentPane().add(tempoText);
		
		JLabel lblTempo = new JLabel("Tempo:");
		lblTempo.setBounds(6, 169, 98, 16);
		frame.getContentPane().add(lblTempo);
		
		populateFieldsFromSong();
		
		frame.setVisible(true);
	}
}
