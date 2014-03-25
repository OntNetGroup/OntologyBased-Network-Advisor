package SwingUserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class IndividualFrameSameDifferentOptions extends JFrame implements ActionListener {

	/**
	 * Class for option individual. Here we build a frame
	 * for chose the individuals same and different
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JList listboxSame;
	private JList listboxDiferent;
	private JList listboxSameEmpty;
	private JList listboxDiferentEmpty;
	
	private ArrayList<String> ListInstances;	//List all instances
	private ArrayList<String> ListSame;
	private ArrayList<String> ListDiferent;
	private ArrayList<String> ListSameEmpty;
	private ArrayList<String> ListDiferentEmpty;
	
	private JButton buttonAddSame;
	private JButton buttonAddSameAll;
	private JButton buttonAddDiferent;
	private JButton buttonAddDiferentAll;
	private JButton buttonRemoveSame;
	private JButton buttonRemoveSameAll;
	private JButton buttonRemoveDiferent;
	private JButton buttonRemoveDiferentAll;
	private JButton btnOk;
	
	private JPanel panelSameIndividuals;
	private JPanel panelDiferentFrom;		
	
	public IndividualFrameSameDifferentOptions(ArrayList<String> ListInstances, ArrayList<String> ListSame, ArrayList<String> ListDiferent, ArrayList<String> ListSameEmpty, ArrayList<String> ListDiferentEmpty) {
		
		this.ListInstances = ListInstances;
		this.ListSame = ListSame;
		this.ListSameEmpty = ListSameEmpty;
		this.ListDiferent = ListDiferent;
		this.ListDiferentEmpty = ListDiferentEmpty;
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		
		setBounds(100, 100, 800, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setResizable(false);			//Block the resize window property
		this.setLocationRelativeTo(null);	//Position relative to center
		
		panelDiferentFrom = new JPanel();
		panelDiferentFrom.setBorder(new TitledBorder(null, "Different Individuals", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDiferentFrom.setBounds(10, 216, 759, 176);
		contentPane.add(panelDiferentFrom);
		panelDiferentFrom.setLayout(null);
		
		listboxDiferent = new JList(ListDiferent.toArray(new String[ListDiferent.size()]));
		listboxDiferent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listboxDiferent.setLocation(10, 25);
		listboxDiferent.setSize(new Dimension(328, 124));
		listboxDiferent.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDiferentFrom.add(listboxDiferent);
		
		listboxDiferentEmpty = new JList(ListDiferentEmpty.toArray(new String[ListDiferentEmpty.size()]));
		listboxDiferentEmpty.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listboxDiferentEmpty.setLocation(414, 25);
		listboxDiferentEmpty.setSize(new Dimension(335, 124));
		listboxDiferentEmpty.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDiferentFrom.add(listboxDiferentEmpty);
		
		buttonAddDiferent = new JButton(">");
		buttonAddDiferent.addActionListener(this);
		buttonAddDiferent.setBounds(348, 23, 56, 23);
		panelDiferentFrom.add(buttonAddDiferent);
		
		buttonAddDiferentAll = new JButton(">>");
		buttonAddDiferentAll.addActionListener(this);
		buttonAddDiferentAll.setBounds(348, 57, 56, 23);
		panelDiferentFrom.add(buttonAddDiferentAll);
		
		buttonRemoveDiferent = new JButton("<");
		buttonRemoveDiferent.addActionListener(this);
		buttonRemoveDiferent.setBounds(348, 91, 56, 23);
		panelDiferentFrom.add(buttonRemoveDiferent);
		
		buttonRemoveDiferentAll = new JButton("<<");
		buttonRemoveDiferentAll.addActionListener(this);
		buttonRemoveDiferentAll.setBounds(348, 126, 56, 23);
		panelDiferentFrom.add(buttonRemoveDiferentAll);
		
		panelSameIndividuals = new JPanel();
		panelSameIndividuals.setBounds(10, 11, 759, 176);
		contentPane.add(panelSameIndividuals);
		panelSameIndividuals.setBorder(new TitledBorder(null, "Same Individuals", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSameIndividuals.setLayout(null);
		
		listboxSame = new JList(ListSame.toArray(new String[ListSame.size()]));
		listboxSame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listboxSame.setBorder(new LineBorder(new Color(0, 0, 0)));
		listboxSame.setLocation(10, 21);
		listboxSame.setSize(new Dimension(324, 124));
		panelSameIndividuals.add(listboxSame);
		
		listboxSameEmpty = new JList(ListSameEmpty.toArray(new String[ListSameEmpty.size()]));
		listboxSameEmpty.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listboxSameEmpty.setBorder(new LineBorder(new Color(0, 0, 0)));
		listboxSameEmpty.setLocation(410, 21);
		listboxSameEmpty.setSize(new Dimension(339, 124));
		panelSameIndividuals.add(listboxSameEmpty);
		
		buttonAddSame = new JButton(">");
		buttonAddSame.addActionListener(this);
		buttonAddSame.setBounds(344, 19, 56, 23);
		panelSameIndividuals.add(buttonAddSame);
		
		buttonAddSameAll = new JButton(">>");
		buttonAddSameAll.addActionListener(this);
		buttonAddSameAll.setBounds(344, 55, 56, 23);
		panelSameIndividuals.add(buttonAddSameAll);
		
		buttonRemoveSame = new JButton("<");
		buttonRemoveSame.addActionListener(this);
		buttonRemoveSame.setBounds(344, 88, 56, 23);
		panelSameIndividuals.add(buttonRemoveSame);
		
		buttonRemoveSameAll = new JButton("<<");
		buttonRemoveSameAll.addActionListener(this);
		buttonRemoveSameAll.setBounds(344, 122, 56, 23);
		panelSameIndividuals.add(buttonRemoveSameAll);
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		btnOk.setBounds(643, 403, 126, 31);
		contentPane.add(btnOk);
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == buttonAddDiferent){		
			
			if(listboxDiferent.getSelectedIndex() > -1)
			{
				Object item = listboxDiferent.getModel().getElementAt(listboxDiferent.getSelectedIndex());
				
				//Update lists
				ListDiferentEmpty.add(item.toString());
				ListDiferent.remove(item.toString());
				
				//Update ListBox
				updateListBoxes();
			}
			
		} else if(e.getSource() == buttonRemoveDiferent){
			
			if(listboxDiferentEmpty.getSelectedIndex() > -1)
			{
				Object item = listboxDiferentEmpty.getModel().getElementAt(listboxDiferentEmpty.getSelectedIndex());
				
				//Update lists
				ListDiferentEmpty.remove(item.toString());
				ListDiferent.add(item.toString());
				
				//Update ListBox
				updateListBoxes();
			}
			
		} else if(e.getSource() == buttonAddDiferentAll){
			
			//Update Lists
			ListDiferent = new ArrayList<String>();
			ListDiferentEmpty = new ArrayList<String>(ListInstances);
			
			//Update ListBox
			updateListBoxes();
			
		} else if(e.getSource() == buttonRemoveDiferentAll){
			
			//Update Lists
			ListDiferent = new ArrayList<String>(ListInstances);
			ListDiferentEmpty = new ArrayList<String>();
			
			//Update ListBox
			updateListBoxes();			
			
		} else if(e.getSource() == buttonAddSame){
			
			if(listboxSame.getSelectedIndex() > -1)
			{
				Object item = listboxSame.getModel().getElementAt(listboxSame.getSelectedIndex());
				
				//Update lists
				ListSameEmpty.add(item.toString());
				ListSame.remove(item.toString());
				
				//Update ListBox
				updateListBoxes();
			}
			
		} else if(e.getSource() == buttonRemoveSame){
			
			if(listboxSameEmpty.getSelectedIndex() > -1)
			{
				Object item = listboxSameEmpty.getModel().getElementAt(listboxSameEmpty.getSelectedIndex());
				
				//Update lists
				ListSameEmpty.remove(item.toString());
				ListSame.add(item.toString());
				
				//Update ListBox
				updateListBoxes();
			}
			
		} else if(e.getSource() == buttonAddSameAll){
						
			//Update Lists
			ListSame = new ArrayList<String>();
			ListSameEmpty = new ArrayList<String>(ListInstances);
			
			//Update ListBox
			updateListBoxes();
			
		} else if(e.getSource() == buttonRemoveSameAll){
			
			//Update Lists
			ListSame = new ArrayList<String>(ListInstances);
			ListSameEmpty = new ArrayList<String>();
			
			//Update ListBox
			updateListBoxes();			
		} else if(e.getSource() == btnOk){
			
			this.dispose();
		}
		
	}
	
	void updateListBoxes()
	{
		this.listboxSameEmpty.setListData(ListSameEmpty.toArray(new String[ListSameEmpty.size()]));
		this.listboxSame.setListData(ListSame.toArray(new String[ListSame.size()]));
		this.listboxDiferentEmpty.setListData(ListDiferentEmpty.toArray(new String[ListDiferentEmpty.size()]));
		this.listboxDiferent.setListData(ListDiferent.toArray(new String[ListDiferent.size()]));
	}
}
