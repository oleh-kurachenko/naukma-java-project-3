/**
 * 
 */
package jcomponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

import fileAnalysis.AnalysedFile;

/**
 * @author Oleh Kurachenko
 *
 */
public class JWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9032665500296325383L;

	private static final int HEIGHT = 600, WIDTH = 850;

	private JPanel treePanel;
	private static final int TREE_PANEL_HEIGHT = 600, TREE_PANEL_WIDTH = 300;
	private static final int TREE_PANEL_MAX_WIDTH = 400, TREE_PANEL_MIN_WIDTH = 250;
	private JTree fileSystem;

	private JPanel eastPanel;
	private static final int EAST_PANEL_HEIGHT = 600, EAST_PANEL_WIDTH = WIDTH - TREE_PANEL_WIDTH;

	private static final int BUTTON_PANEL_WIDTH = 180;
	private static final int BUTTON_HEIGHT = 60;

	private JPanel northEastPanel;
	private static final int NORTH_EAST_PANEL_HEIGHT = 470,
			NORTH_EAST_PANEL_WIDTH = WIDTH - TREE_PANEL_WIDTH;
	private static final int NORTH_EAST_PANEL_MIN_HEIGHT = BUTTON_HEIGHT * 3,
			NORTH_EAST_PANEL_MIN_WIDTH = BUTTON_PANEL_WIDTH + 150;
	private JLabel choosenLabel;

	private JPanel finderPanel;
	private static final int FINDER_PANEL_HEIGHT = HEIGHT - NORTH_EAST_PANEL_HEIGHT,
			FINDER_PANEL_WIDTH = NORTH_EAST_PANEL_WIDTH;
	private static final int FINDER_PANEL_MIN_HEIGHT = 0, FINDER_PANEL_MIN_WIDTH = 0;
	private JTextField finderTextField;
	private JButton processFinderTextField;
	private JLabel finderLabel;

	private JPanel buttonPanel;
	private static final int BUTTON_PANEL_HEIGHT = NORTH_EAST_PANEL_HEIGHT;
	private JButton addButton;
	private JButton removeButton;
	private JButton removeAllButton;

	private JPanel choosenFilesPanel;
	private static final int CHOOSEN_FILES_PANEL_HEIGHT = NORTH_EAST_PANEL_HEIGHT,
			CHOOSEN_FILES_PANEL_WIDTH = NORTH_EAST_PANEL_WIDTH - BUTTON_PANEL_WIDTH;
	private DefaultListModel<AnalysedFile> lm;
	private JList<AnalysedFile> choosenFiles;

	private JResultDialogWindow jw;

	private JDialog loadingDialog;

	public static final Font bigFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	@SuppressWarnings("serial")
	public JWindow(String title) throws HeadlessException {
		super(title);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(WIDTH + 10, HEIGHT + 30);
		setLocation(
				(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2
						- this.getWidth() / 2,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
						- this.getHeight() / 2);

		getContentPane().setLayout(new BorderLayout());

		initiateLoadingDialog();

		initiateTreePanel();

		add(treePanel, BorderLayout.WEST);

		initiateChoosenFilesPanel();

		initiateButtonPanel();

		initiateNorthEastPanel();

		initiateFinderPanel();

		eastPanel = new JPanel(new BorderLayout()) {
			{
				setPreferredSize(new Dimension(EAST_PANEL_WIDTH, EAST_PANEL_HEIGHT));
				setMinimumSize(new Dimension(NORTH_EAST_PANEL_MIN_HEIGHT + FINDER_PANEL_MIN_HEIGHT,
						Math.max(NORTH_EAST_PANEL_MIN_WIDTH, FINDER_PANEL_MIN_WIDTH)));

				add(northEastPanel, BorderLayout.CENTER);
				add(finderPanel, BorderLayout.SOUTH);
			}
		};

		add(eastPanel, BorderLayout.CENTER);
		pack();
	}

	@SuppressWarnings("serial")
	private void initiateTreePanel() {
		fileSystem = new JTree(new FileSystemModel());
		fileSystem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		treePanel = new JPanel(new BorderLayout()) {
			{
				setPreferredSize(new Dimension(TREE_PANEL_WIDTH, TREE_PANEL_HEIGHT));
				setMinimumSize(new Dimension(TREE_PANEL_MIN_WIDTH, 10));
				setMaximumSize(new Dimension(TREE_PANEL_MAX_WIDTH, 10000));
				setBorder(BorderFactory.createLoweredBevelBorder());

				add(fileSystem, BorderLayout.CENTER);
				add(new JScrollPane(fileSystem));

				pack();
			}
		};
	}

	@SuppressWarnings("serial")
	private void initiateLoadingDialog() {
		loadingDialog = new JDialog(JWindow.this, "File is loading",
				ModalityType.APPLICATION_MODAL) {
			{
				setSize(500, 200);
				setLocation(
						(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2
								- this.getWidth() / 2,
						(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
								- this.getHeight() / 2);
				getContentPane().setLayout(new BorderLayout());

				JLabel label = new JLabel("File is loading...",
						new ImageIcon("resourses\\loading3.gif"), SwingConstants.CENTER);
				label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
				add(label, BorderLayout.CENTER);
				setResizable(false);
			}
		};

	}

	@SuppressWarnings("serial")
	private void initiateChoosenFilesPanel() {
		lm = new DefaultListModel<AnalysedFile>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.DefaultListModel#addElement(java.lang.Object)
			 */
			@Override
			public void addElement(AnalysedFile element) {
				if (!super.contains(element)) {
					super.addElement(element);

					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							doMySwingWorkerTask();
							return null;
						}
					};
					mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							if (evt.getPropertyName().equals("state")) {
								if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
									loadingDialog.dispose();
									choosenFilesPanel.repaint();
								}
							}
						}
					});
					mySwingWorker.execute();
					loadingDialog.setVisible(true);

				} else {
					JOptionPane.showMessageDialog(JWindow.this, "Element already exist!",
							"Already exist!", JOptionPane.WARNING_MESSAGE);
				}
			}

			private void doMySwingWorkerTask() {
				try {
					super.lastElement().prepare();
				} catch (Exception e) {
					loadingDialog.dispose();
					JOptionPane.showMessageDialog(JWindow.this, "Can't open file " + super.lastElement().getFileName(), "Can't load file", JOptionPane.ERROR_MESSAGE);
					super.removeElementAt(super.size()-1);
				}
			}

		};
		choosenFiles = new JList<AnalysedFile>(lm);
		choosenFiles.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		choosenFilesPanel = new JPanel(new BorderLayout()) {
			{
				setPreferredSize(
						new Dimension(CHOOSEN_FILES_PANEL_WIDTH, CHOOSEN_FILES_PANEL_HEIGHT));
				setBorder(BorderFactory.createLoweredBevelBorder());

				add(choosenFiles);
				add(new JScrollPane(choosenFiles));
				pack();
			}
		};
	}

	private void setJButtonForButtonPanel(JButton button) {
		button.setFont(bigFont);
		button.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_HEIGHT));
		button.setMinimumSize(button.getPreferredSize());
		button.setMaximumSize(button.getPreferredSize());
	}

	@SuppressWarnings("serial")
	private void initiateButtonPanel() {
		addButton = new JButton("Add", new ImageIcon("resourses\\addIcon.png"));
		setJButtonForButtonPanel(addButton);
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File tempFile = ((FileSystemModel.TreeFile) fileSystem
						.getLastSelectedPathComponent()).getFile();
				if (tempFile != null) {
					if (tempFile.isFile())
						lm.addElement(new AnalysedFile(tempFile));
					else {
						int isLoadAll = JOptionPane.showConfirmDialog(JWindow.this,
								"Do you want to load all the files in that directory?", "Load all?",
								JOptionPane.YES_NO_OPTION, JOptionPane.OK_CANCEL_OPTION);
						if (isLoadAll == JOptionPane.OK_OPTION) {
							for (File file : tempFile.listFiles())
								if (file.isFile())
									lm.addElement(new AnalysedFile(file));
						}
					}
				}

			}
		});
		removeButton = new JButton("Remove", new ImageIcon("resourses\\removeIcon.png"));
		setJButtonForButtonPanel(removeButton);
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lm.removeElement(choosenFiles.getSelectedValue());
			}
		});

		removeAllButton = new JButton("Clear", new ImageIcon("resourses\\clearAllIcon.png"));
		setJButtonForButtonPanel(removeAllButton);
		removeAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lm.clear();
			}
		});

		buttonPanel = new JPanel() {
			{
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));
				setBorder(BorderFactory.createEtchedBorder());

				add(addButton);
				add(removeButton);
				add(removeAllButton);
				add(Box.createVerticalGlue());
			}
		};

	}

	@SuppressWarnings("serial")
	private void initiateNorthEastPanel() {
		choosenLabel = new JLabel("Choosen files:");
		choosenLabel.setFont(bigFont);
		choosenLabel.setHorizontalAlignment(SwingConstants.CENTER);
		northEastPanel = new JPanel(new BorderLayout()) {
			{
				setPreferredSize(new Dimension(NORTH_EAST_PANEL_WIDTH, NORTH_EAST_PANEL_HEIGHT));
				setMinimumSize(new Dimension(BUTTON_PANEL_WIDTH, 3 * BUTTON_HEIGHT));
				add(buttonPanel, BorderLayout.WEST);
				add(choosenLabel, BorderLayout.NORTH);
				add(choosenFilesPanel, BorderLayout.CENTER);
				pack();
			}
		};

	}

	private void initiateFinderPanel() {
		finderPanel = new JPanel();

		finderPanel.setLayout(new BoxLayout(finderPanel, BoxLayout.Y_AXIS));

		finderPanel.setPreferredSize(new Dimension(FINDER_PANEL_WIDTH, FINDER_PANEL_HEIGHT));
		finderPanel.setBorder(BorderFactory.createEtchedBorder());
		finderPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		finderLabel = new JLabel("Enter word:");
		finderLabel.setFont(bigFont);
		finderPanel.add(finderLabel);
		finderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		finderPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		finderTextField = new JTextField();
		finderTextField.setFont(bigFont);
		finderTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		finderTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		finderTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processFinder();
			}
		});

		finderPanel.add(finderTextField);
		finderPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel processFinderTextFieldBox = new JPanel();
		processFinderTextFieldBox
				.setLayout(new BoxLayout(processFinderTextFieldBox, BoxLayout.X_AXIS));
		processFinderTextFieldBox.add(Box.createHorizontalGlue());

		processFinderTextField = new JButton("Process");
		processFinderTextField.setFont(bigFont);
		processFinderTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processFinder();
			}
		});

		processFinderTextFieldBox.add(processFinderTextField);
		processFinderTextFieldBox.add(Box.createRigidArea(new Dimension(5, 0)));
		processFinderTextFieldBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		finderPanel.add(processFinderTextFieldBox);

		finderPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	}

	private void processFinder() {
		String inquery = finderTextField.getText().toLowerCase();

		Map<Integer, List<String>> inclusions = new TreeMap<Integer, List<String>>();

		AnalysedFile aFile;
		Integer numberOfInclusions;

		for (int i = 0; i < lm.size(); i++) {
			aFile = lm.getElementAt(i);
			numberOfInclusions = aFile.get(inquery);
			if (!inclusions.containsKey(numberOfInclusions))
				inclusions.put(numberOfInclusions, new LinkedList<String>());

			inclusions.get(numberOfInclusions)
					.add(aFile.getFileName() + " - " + numberOfInclusions + " times");
		}

		LinkedList<String> resList = new LinkedList<String>();
		for (Integer i : inclusions.keySet()) {
			for (String s : inclusions.get(i)) {
				resList.add(s);
			}
		}

		jw = new JResultDialogWindow(JWindow.this, inquery, resList);
		jw.setVisible(true);
		finderTextField.requestFocus();
		finderTextField.selectAll();
	}
}
