package SwingUserInterface;

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
import javax.swing.UIManager;

import com.hp.hpl.jena.ontology.OntModel;


public class IndividualFrame extends JFrame implements ActionListener {
	
	/*
	 * Class to build one instance
	 * */

	private static final long serialVersionUID = 1L;
	
	private FactoryInstances factoryIndividuals;
	private DtoDefinitionClass dto;
	private OntModel model;
	private Search search;
	private String individualSource;
	private GraphIndividual graphIndividual;
	
	private JFrame indFrameSameDiferent;
	
	private String name;
	private ArrayList<String> ListInstances;	//List all instances
	private ArrayList<String> ListSame;
	private ArrayList<String> ListDiferent;
	private ArrayList<String> ListSameEmpty;
	private ArrayList<String> ListDiferentEmpty;
	
	private JButton btnCreate;
	private JButton btnNewInstance;
	private JButton btnSelectInstance;
	private JButton btnResetValues;
	private JButton btnAddSameDiferent;
	private JLabel lbLabelInstanceName;
	private JLabel lblInstanceNameValue;
	private JLabel lblUri;
	private JLabel lblUriValue;
	
	private JPanel contentPane;
	private JPanel panelInstance;	
	
	public IndividualFrame(DtoDefinitionClass dto, String individualSource, FactoryInstances factory, Search search) {
		
		this.setTitle("Instance " + individualSource );
		this.setLocationRelativeTo(null);	//Position relative to center
		
		this.factoryIndividuals = factory;
		this.individualSource = individualSource;
		this.search = search;
		this.dto = dto;
		
		this.ListInstances = this.search.GetAllInstances(Aplication.Model);
		this.ListInstances = this.removeInstance(individualSource, this.ListInstances);
		this.ListDiferent = new ArrayList<String>();
		this.ListSame = new ArrayList<String>();
		this.ListDiferentEmpty = new ArrayList<String>();
		this.ListSameEmpty = new ArrayList<String>();
		
		// In begining
		this.ListSame = new ArrayList<String>(this.ListInstances);
		this.ListDiferent = new ArrayList<String>(this.ListInstances);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 636, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelInstance = new JPanel();
		panelInstance.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Select new individual", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInstance.setToolTipText("");
		panelInstance.setBounds(20, 11, 597, 156);
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
		btnResetValues.setBounds(363, 178, 126, 37);
		contentPane.add(btnResetValues);
		
		btnAddSameDiferent = new JButton("Add Same and Diferent Properties");
		btnAddSameDiferent.addActionListener(this);
		btnAddSameDiferent.setBounds(20, 178, 308, 37);
		contentPane.add(btnAddSameDiferent);
		
		btnCreate = new JButton("Create");
		btnCreate.setBounds(492, 178, 125, 37);
		contentPane.add(btnCreate);
		btnCreate.addActionListener(this);
		
		this.setVisible(true);
	}

	private ArrayList<String> removeInstance(String individualSource,
			ArrayList<String> listInstances) {
		
		for (int i = 0; i < listInstances.size(); i++) {
			if(listInstances.get(i).equals(individualSource))
			{
				listInstances.remove(i);
				return listInstances;
			}
		}
		
		return listInstances;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnCreate)
		{
			//TODO:Check lists same and different individuals and create the instance
			
			// Create the instance
			
			Aplication.Model = factoryIndividuals.CreateIndividualsForSomeRelations_Single(individualSource, lblUriValue.getText()+ "#" + lblInstanceNameValue.getText(), this.dto.Relation, this.dto.Target, Aplication.Model);
			//Aplication.UpdateLists();
			//Aplication.SelectInstance(individualSource);
			
			//Close frame
			
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
				System.out.println("---" + Aplication.NS + name);
				for (String s : this.ListInstances) {
					if ((Aplication.NS + name).equals(s))
					{
						exist = true;
						break;
					}
				}
				
				if(exist == true)
				{
					JOptionPane.showMessageDialog(null, "This name is already exist. Choose another.");
					
				}else{
					
					name = Aplication.NS + name;
					
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
			
			//if( indFrameSameDiferent == null)
			//{
				indFrameSameDiferent = new IndividualFrameSameDifferentOptions(this.ListInstances, this.ListSame, this.ListDiferent, this.ListSameEmpty, this.ListDiferentEmpty);
			//}
			
			
		}
		
		
	}
}