package UserInterface;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JTextArea;

import Business.FactoryInstances;
import Business.Search;
import Domain.DtoDefinitionClass;
import SwingUserInterface.GraphIndividual;
import SwingUserInterface.IndividualFrameSameDifferentOptions;
//import SwingUserInterface.JInternalFrameOptions;

import javax.swing.UIManager;


public class CopyOfIndividualFrame extends JFrame implements ActionListener {
	
	/*
	 * Class to build the frame for individual
	 * - Some Relation
	 * */

	private static final long serialVersionUID = 1L;
	
	private FactoryInstances factoryIndividuals;
	private DtoDefinitionClass dto;
	private String individualSource;
	private Search search;
	private GraphIndividual graphIndividual;
	
	private String name;
	public static ArrayList<String> ListInstances;	//List all instances
	public static ArrayList<String> ListSame;
	public static ArrayList<String> ListDiferent;
	public static ArrayList<String> ListSameEmpty;
	public static ArrayList<String> ListDiferentEmpty;
	
	private JButton btnCreate;
	private JButton btnNewInstance;
	private JButton btnSelectInstance;
	private JButton btnResetValues;
	private JButton btnAddSameDiferent;
	private JButton btnGraph;
	
	private JLabel lblIndividualsourcename;
	private JLabel lblRelationName;
	private JLabel labelNewInstanceQuestion;
	private JLabel lblNewIndividual;
	private JLabel lblLblindividualsourcenamevalue;
	private JLabel lblLblrelationnamevalue;
	private JLabel lbLabelInstanceName;
	private JLabel lblInstanceNameValue;
	private JLabel lblUri;
	private JLabel lblUriValue;
	private JLabel lblIndividualClassValue;
	private JLabel lblNewIndividualClassValue;
	private JLabel lblIndividualClass;
	private JLabel lblNewIndividualClass;
	
	private JTextArea txtLog;
	
	private JPanel contentPane;
	private JPanel panelInstance;	
	private JPanel panelRelation;
	private JLabel lblTopInvidualname;
	private JLabel lblTopRelationName;
	private JLabel label;
	
	public CopyOfIndividualFrame(DtoDefinitionClass dto, String individualSource, FactoryInstances factory, Search search) {
		
		this.setTitle("Individual");
		this.setLocationRelativeTo(null);	//Position relative to center
		
		this.factoryIndividuals = this.factoryIndividuals;
		this.individualSource = individualSource;
		this.search = search;
		this.dto = dto;
		
		this.ListInstances = search.GetAllInstances(AplicationOld.model);		
		this.ListDiferent = new ArrayList<String>();
		this.ListSame = new ArrayList<String>();
		this.ListDiferentEmpty = new ArrayList<String>();
		this.ListSameEmpty = new ArrayList<String>();
		
		// In begining
		this.ListSame = new ArrayList<String>(this.ListInstances);
		this.ListDiferent = new ArrayList<String>(this.ListInstances);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 884, 728);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnCreate = new JButton("Create");
		btnCreate.setBounds(774, 637, 71, 37);
		btnCreate.addActionListener(this);
		contentPane.setLayout(null);
		contentPane.add(btnCreate);
		
		panelInstance = new JPanel();
		panelInstance.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select new individual", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInstance.setToolTipText("");
		panelInstance.setBounds(23, 357, 597, 156);
		contentPane.add(panelInstance);
		panelInstance.setLayout(null);
		
		lbLabelInstanceName = new JLabel("Instance name:");
		lbLabelInstanceName.setBounds(10, 108, 103, 14);
		panelInstance.add(lbLabelInstanceName);
		
		lblInstanceNameValue = new JLabel("Instance name value");
		lblInstanceNameValue.setForeground(Color.BLUE);
		lblInstanceNameValue.setBounds(123, 108, 453, 14);
		panelInstance.add(lblInstanceNameValue);
		
		btnNewInstance = new JButton("New instance");
		btnNewInstance.addActionListener(this);
		btnNewInstance.setBounds(18, 29, 157, 23);
		panelInstance.add(btnNewInstance);
		
		btnSelectInstance = new JButton("Select instance");
		btnSelectInstance.addActionListener(this);
		btnSelectInstance.setBounds(214, 29, 157, 23);
		panelInstance.add(btnSelectInstance);
		
		lblUri = new JLabel("Uri:");
		lblUri.setBounds(10, 83, 46, 14);
		panelInstance.add(lblUri);
		
		lblUriValue = new JLabel("Uri value");
		lblUriValue.setForeground(Color.BLUE);
		lblUriValue.setBounds(123, 83, 453, 14);
		panelInstance.add(lblUriValue);
		
		btnResetValues = new JButton("Reset values");
		btnResetValues.setBounds(627, 637, 95, 37);
		contentPane.add(btnResetValues);
		
		
		panelRelation = new JPanel();
		panelRelation.setBorder(new TitledBorder(null, "Relation informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRelation.setBounds(23, 11, 594, 335);
		contentPane.add(panelRelation);
		panelRelation.setLayout(null);
		
		lblIndividualsourcename = new JLabel("Individual");
		lblIndividualsourcename.setBounds(10, 98, 106, 14);
		panelRelation.add(lblIndividualsourcename);
		
		lblRelationName = new JLabel("Relation name");
		lblRelationName.setBounds(10, 173, 106, 14);
		panelRelation.add(lblRelationName);
		
		labelNewInstanceQuestion = new JLabel("[?]");
		labelNewInstanceQuestion.setBounds(138, 240, 46, 14);
		panelRelation.add(labelNewInstanceQuestion);
		
		lblNewIndividual = new JLabel("New individual");
		lblNewIndividual.setBounds(10, 240, 106, 14);
		panelRelation.add(lblNewIndividual);
		
		lblLblindividualsourcenamevalue = new JLabel(individualSource);
		lblLblindividualsourcenamevalue.setBounds(138, 98, 446, 14);
		panelRelation.add(lblLblindividualsourcenamevalue);
		
		lblLblrelationnamevalue = new JLabel(dto.Relation);
		lblLblrelationnamevalue.setBounds(138, 173, 446, 14);
		panelRelation.add(lblLblrelationnamevalue);
		
		lblIndividualClass = new JLabel("Individual Class");
		lblIndividualClass.setBounds(10, 123, 106, 14);
		panelRelation.add(lblIndividualClass);
		
		lblNewIndividualClass = new JLabel("New individual Class");
		lblNewIndividualClass.setBounds(10, 265, 118, 14);
		panelRelation.add(lblNewIndividualClass);
		
		lblIndividualClassValue = new JLabel(dto.Source);
		lblIndividualClassValue.setBounds(138, 123, 446, 14);
		panelRelation.add(lblIndividualClassValue);
		
		lblNewIndividualClassValue = new JLabel(dto.Target);
		lblNewIndividualClassValue.setBounds(138, 265, 446, 14);
		panelRelation.add(lblNewIndividualClassValue);
		
		lblTopInvidualname = new JLabel(individualSource.replace(AplicationOld.NS,""));
		lblTopInvidualname.setBounds(39, 36, 177, 14);
		panelRelation.add(lblTopInvidualname);
		
		lblTopRelationName = new JLabel(dto.Relation.replace(AplicationOld.NS,""));
		lblTopRelationName.setBounds(237, 36, 177, 14);
		panelRelation.add(lblTopRelationName);
		
		label = new JLabel("[?]");
		label.setBounds(424, 36, 46, 14);
		panelRelation.add(label);
		
		btnGraph = new JButton("Graph");
		btnGraph.addActionListener(this);
		btnGraph.setBounds(495, 32, 89, 23);
		panelRelation.add(btnGraph);
		
		txtLog = new JTextArea();
		txtLog.setBounds(627, 21, 218, 605);
		contentPane.add(txtLog);
		this.txtLog.setText(AplicationOld.Log);
		txtLog.setRows(10);
		txtLog.setEditable(false);
		
		btnAddSameDiferent = new JButton("Add Same and Diferent Properties");
		btnAddSameDiferent.addActionListener(this);
		btnAddSameDiferent.setBounds(154, 524, 324, 37);
		contentPane.add(btnAddSameDiferent);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnCreate)
		{
			//Check lists same and different individuals
			//TODO
			
			AplicationOld.Log = AplicationOld.Log + "Individual " + lblInstanceNameValue.getText() + " was created." + "\n";
			txtLog.setText(AplicationOld.Log);
			
			//Go to next
			//JInternalFrameOptions.semaphore.release();
			
			//remove the previous JFrame
            this.setVisible(false);
            this.dispose();
            
            //close graph individual frame
            if(graphIndividual != null)
            {
            	graphIndividual.finalizeFrame();
            }
            
            //Clean the lists
            this.ListDiferent = new ArrayList<String>();
			this.ListSame = new ArrayList<String>();;
			this.ListDiferentEmpty = new ArrayList<String>();;
			this.ListSameEmpty = new ArrayList<String>();;
			this.ListInstances = new ArrayList<String>();;
            
		} else if(e.getSource() == btnNewInstance){
			
			name = JOptionPane.showInputDialog("What is the instance name?");
			if(name == null || name == "")
			{
				// Do nothing
			} else {
				
				// Check if already exist
				boolean exist = false;
				System.out.println("---" + AplicationOld.NS + name);
				for (String s : this.ListInstances) {
					if ((AplicationOld.NS + name).equals(s))
					{
						exist = true;
						break;
					}
				}
				
				if(exist == true)
				{
					JOptionPane.showMessageDialog(null, "This name is already exist. Choose another.");
					
				}else{
					
					name = AplicationOld.NS + name;
					
					// Update label name and URI name
					lblInstanceNameValue.setText(name.split("#")[1]);
					lblUriValue.setText(name.split("#")[0]);
				}
				
			}
		} else if(e.getSource() == btnSelectInstance){
			
			Object[] possibilities = this.ListInstances.toArray(new String[ListInstances.size()]);
			String s = (String)JOptionPane.showInputDialog(
			                    null,
			                    "Select an instance:\n",
			                    "Select instance",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    ListInstances.get(0));

			//If a string was returned, say so.
			if ((s != null) && (s.length() > 0)) {

				name = s;
				
				// Update label name and URI name
				lblInstanceNameValue.setText(name.split("#")[1]);
				lblUriValue.setText(name.split("#")[0]);
			}
			
		} else if (e.getSource() == btnAddSameDiferent){
			
			//JFrame indFrameSameDiferent = new IndividualFrameSameDifferentOptions();
			
		} else if (e.getSource() == btnGraph){
			
			System.out.println("----------------------------------ENTROUUU--------------------------------");
			graphIndividual = new GraphIndividual(AplicationOld.model, this.search, individualSource, dto.Source);
		}
		
		
	}
}