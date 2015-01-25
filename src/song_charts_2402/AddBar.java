package song_charts_2402;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class AddBar {

	private JFrame frame;
	private JTextField chordsText;
	private JLabel lblTimeSignature;
	private JTextField timeSignatureText;
	private JLabel lblRehearsalLetter;
	private JTextField rehearsalLetterText;
	private JLabel lblPhraseAbreviation;
	private JTextField phraseAbreviationText;
	private JLabel lblEndings;
	private JTextField endingsText;
	private JLabel lblDalSegno;
	private JTextField dalSegnoText;
	private JLabel lblDalCapo;
	private JTextField daCapoText;
	private JLabel lblText;
	private JTextField textText;
	private JButton btnAddBar;
	private JButton btnCancel;
	private JCheckBox signCheckBox;
	private JCheckBox codaCheckbox;
	
	private Song song;
	
	/**
	 * Create the application.
	 */
	public AddBar(Song _song) {
		song = _song;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 486, 379);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		chordsText = new JTextField();
		chordsText.setColumns(10);
		chordsText.setBounds(126, 27, 345, 28);
		frame.getContentPane().add(chordsText);
		
		JLabel lblChords = new JLabel("Chords:");
		lblChords.setBounds(6, 27, 61, 16);
		frame.getContentPane().add(lblChords);
		
		lblTimeSignature = new JLabel("Time Signature:");
		lblTimeSignature.setBounds(6, 61, 108, 16);
		frame.getContentPane().add(lblTimeSignature);
		
		timeSignatureText = new JTextField();
		timeSignatureText.setColumns(10);
		timeSignatureText.setBounds(126, 61, 345, 28);
		frame.getContentPane().add(timeSignatureText);
		
		lblRehearsalLetter = new JLabel("Rehearsal Letter:");
		lblRehearsalLetter.setBounds(6, 95, 108, 16);
		frame.getContentPane().add(lblRehearsalLetter);
		
		rehearsalLetterText = new JTextField();
		rehearsalLetterText.setColumns(10);
		rehearsalLetterText.setBounds(126, 95, 345, 28);
		frame.getContentPane().add(rehearsalLetterText);
		
		lblPhraseAbreviation = new JLabel("Phrase Abreviation:");
		lblPhraseAbreviation.setBounds(6, 131, 132, 16);
		frame.getContentPane().add(lblPhraseAbreviation);
		
		phraseAbreviationText = new JTextField();
		phraseAbreviationText.setColumns(10);
		phraseAbreviationText.setBounds(126, 131, 345, 28);
		frame.getContentPane().add(phraseAbreviationText);
		
		lblEndings = new JLabel("Endings:");
		lblEndings.setBounds(6, 165, 61, 16);
		frame.getContentPane().add(lblEndings);
		
		endingsText = new JTextField();
		endingsText.setColumns(10);
		endingsText.setBounds(126, 165, 345, 28);
		frame.getContentPane().add(endingsText);
		
		lblDalSegno = new JLabel("Dal Segno:");
		lblDalSegno.setBounds(6, 199, 108, 16);
		frame.getContentPane().add(lblDalSegno);
		
		dalSegnoText = new JTextField();
		dalSegnoText.setColumns(10);
		dalSegnoText.setBounds(126, 199, 345, 28);
		frame.getContentPane().add(dalSegnoText);
		
		lblDalCapo = new JLabel("Da Capo: ");
		lblDalCapo.setBounds(6, 233, 108, 16);
		frame.getContentPane().add(lblDalCapo);
		
		daCapoText = new JTextField();
		daCapoText.setColumns(10);
		daCapoText.setBounds(126, 233, 345, 28);
		frame.getContentPane().add(daCapoText);
		
		lblText = new JLabel("Text:");
		lblText.setBounds(6, 267, 61, 16);
		frame.getContentPane().add(lblText);
		
		textText = new JTextField();
		textText.setColumns(10);
		textText.setBounds(126, 267, 345, 28);
		frame.getContentPane().add(textText);
		
		signCheckBox = new JCheckBox("Sign");
		signCheckBox.setBounds(6, 307, 61, 23);
		frame.getContentPane().add(signCheckBox);
		
		codaCheckbox = new JCheckBox("Coda");
		codaCheckbox.setBounds(66, 307, 91, 23);
		frame.getContentPane().add(codaCheckbox);
		
		btnAddBar = new JButton("Add Bar");
		btnAddBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Bar newBar = new Bar();
				
				newBar.setChords(chordsText.getText());
				newBar.setTimeSignature(timeSignatureText.getText());
				newBar.setPhraseAbreviation(phraseAbreviationText.getText());
				newBar.setEndings(endingsText.getText());
				newBar.setText(textText.getText());
				newBar.setRehearsalLetter(rehearsalLetterText.getText());
				newBar.setSign(signCheckBox.isSelected());
				newBar.setCoda(codaCheckbox.isSelected());
				newBar.setDaCapo(daCapoText.getText());
				newBar.setDalSegno(dalSegnoText.getText());
				
				song.addBar(newBar);
				frame.dispose();
			}
		});
		btnAddBar.setBounds(354, 328, 117, 29);
		frame.getContentPane().add(btnAddBar);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel.setBounds(233, 328, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		frame.setVisible(true);
	}
}
