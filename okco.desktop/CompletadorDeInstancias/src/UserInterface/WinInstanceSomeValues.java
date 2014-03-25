package UserInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;

import Business.FactoryInstances;
import Business.Search;
import Domain.DtoDefinitionClass;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WinInstanceSomeValues extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textInstaceName;
	
	private JLabel lblInstanceNewName;
	private JLabel lblInstance;
	private JLabel lblinstancename;
	private JLabel lblRelation;
	private JLabel lblrelationname;
	private JLabel lblNewInstance;
	
	public DtoDefinitionClass dto;
	private Search Search;
	private FactoryInstances factoryIndividuals;
	
	public WinInstanceSomeValues(final DtoDefinitionClass dto, ArrayList<String> listInstance){
				
		System.out.println("Entrou 3");
		
		this.dto = dto;
		this.Search = new Search(WinHome.NS);
		
		setTitle("Individuals for some relation");
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);			//Block the resize window property
		this.setLocationRelativeTo(null);	//Position relative to center
		this.setBounds(100, 100, 494, 194);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textInstaceName = new JTextField();
		textInstaceName.setBounds(147, 77, 311, 20);
		contentPane.add(textInstaceName);
		textInstaceName.setColumns(10);
		
		lblInstanceNewName = new JLabel("New Instance name:");
		lblInstanceNewName.setBounds(10, 80, 154, 14);
		contentPane.add(lblInstanceNewName);
		
		lblInstance = new JLabel("Instance");
		lblInstance.setBounds(21, 11, 93, 14);
		contentPane.add(lblInstance);
		
		lblinstancename = new JLabel("[instanceName]");
		lblinstancename.setBounds(10, 36, 154, 14);
		contentPane.add(lblinstancename);
		
		lblRelation = new JLabel("Relation");
		lblRelation.setBounds(229, 11, 46, 14);
		contentPane.add(lblRelation);
		
		lblrelationname = new JLabel("[relationName]");
		lblrelationname.setBounds(206, 36, 111, 14);
		contentPane.add(lblrelationname);
		
		lblNewInstance = new JLabel("New instance");
		lblNewInstance.setBounds(389, 11, 111, 14);
		contentPane.add(lblNewInstance);
		
		JLabel label = new JLabel("[?]");
		label.setBounds(412, 36, 46, 14);
		contentPane.add(label);
		
		// Update the values //
		
		String newInstanceName = dto.Target + "-" + (Search.GetInstancesFromClass(WinHome.model, dto.Target).size() + 1);
		
		// Instance Source
		lblinstancename.setText(listInstance.get(0));
		
		// Relation
		lblrelationname.setText(dto.Relation.toString());
		
		// New instance
		textInstaceName.setText(newInstanceName);
		
		JButton btnCreateInstance = new JButton("Create Instance");
		btnCreateInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					
				System.out.println("---- Criando instância");				
				
				//Get instance source, class target, property
				Individual indInstance = WinHome.model.getIndividual(lblinstancename.getText());
				OntClass ClassImage = WinHome.model.getOntClass(dto.Target);
				Property relation = WinHome.model.getProperty(dto.Relation);				
				
				//Create individual
				String instanceName = textInstaceName.getText();
				Individual newInstance = ClassImage.createIndividual(instanceName);
				
				//Add relation
				System.out.println("--------" + indInstance.getURI() + " -> "+ relation + " -> " + newInstance.getURI());
				indInstance.addProperty(relation, newInstance);
				System.out.println("---- Individuo " + newInstance.getURI());
				
				//Close window
				setVisible(false); //you can't see me!
				dispose(); //Destroy the JFrame object
				
				//Unblock semaphore
				WinOption.sem.release();
			}
		});
		btnCreateInstance.setBounds(304, 114, 154, 23);
		contentPane.add(btnCreateInstance);
		
	}
}
