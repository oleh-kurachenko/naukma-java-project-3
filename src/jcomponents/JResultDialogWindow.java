/**
 * 
 */
package jcomponents;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Oleh Kurachenko
 *
 */
public class JResultDialogWindow extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6664982391638072342L;
	private JTextArea resultArea;
	private JLabel resultLabel;
	private JButton oKButton;
	
	/**
	 * @param owner
	 * @param title
	 */
	public JResultDialogWindow(JFrame owner, String inquery, LinkedList<String> listOfResults) {
		super(owner, owner.getTitle()+": result for \"" + inquery + "\"");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 600);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout());
		// TODO Style all down
		
		resultLabel = new JLabel("Result for inquery \"" + inquery + "\":");
		resultLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(resultLabel, BorderLayout.NORTH);
		
		StringBuilder sb = new StringBuilder();
		Iterator<String> i = listOfResults.descendingIterator();
		while (i.hasNext()) {
			sb.append(i.next());
			sb.append('\n');
		}
		
		resultArea = new JTextArea(sb.toString());
		resultArea.setEditable(false);
		resultArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		add(resultArea, BorderLayout.CENTER);
		add(new JScrollPane(resultArea));
		
		oKButton = new JButton("OK");
		oKButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();			
			}
		});
		oKButton.setFont(JWindow.bigFont);
		add(oKButton, BorderLayout.SOUTH);
	}
	
	
	
}
