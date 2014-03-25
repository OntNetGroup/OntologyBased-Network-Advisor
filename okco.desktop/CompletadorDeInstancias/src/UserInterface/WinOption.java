package UserInterface;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JButton;

import Business.FactoryInstances;
import Business.Search;
import Domain.DtoDefinitionClass;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WinOption extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private JCheckBox chckbxCreateIndAuto;
	private JCheckBox chckbxCompleteClasses;
	private JCheckBox chckbxSomeValuesFrom;
	private JCheckBox chckbxMinCardinality;
	private JCheckBox chckbxMaxCardinality;
	private JCheckBox chckbxExactlyCardinality;

	private Search search;
	private FactoryInstances factoryIndividuals;
	public static boolean nextWindow;
	public static Semaphore sem;
	
	public WinOption(String Namespace) {
		
		this.search = new Search(Namespace);
		this.factoryIndividuals = new FactoryInstances(this.search);
		
		setTitle("Options");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 414, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);			//Block the resize window property
		this.setLocationRelativeTo(null);	//Position relative to center
		
		final JLabel lblSelectTheActions = new JLabel("Select the actions:");
		lblSelectTheActions.setBounds(22, 22, 124, 14);
		contentPane.add(lblSelectTheActions);
		
		final JLabel lblCreateInd = new JLabel("Would you like to create individuals automatically?");
		lblCreateInd.setBounds(22, 228, 366, 14);
		contentPane.add(lblCreateInd);
		
		chckbxCreateIndAuto = new JCheckBox("Yes");
		chckbxCreateIndAuto.setBounds(22, 249, 97, 23);
		contentPane.add(chckbxCreateIndAuto);		
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(22, 47, 348, 103);
		contentPane.add(panel);
		
		chckbxMinCardinality = new JCheckBox("min cardinality");
		chckbxMinCardinality.setBackground(Color.WHITE);
		panel.add(chckbxMinCardinality);
		
		chckbxMaxCardinality = new JCheckBox("max cardinality");
		chckbxMaxCardinality.setBackground(Color.WHITE);
		panel.add(chckbxMaxCardinality);
		
		chckbxExactlyCardinality = new JCheckBox("exactly cardinality");
		chckbxExactlyCardinality.setBackground(Color.WHITE);
		panel.add(chckbxExactlyCardinality);
		
		chckbxSomeValuesFrom = new JCheckBox("some values from");
		chckbxSomeValuesFrom.setBackground(Color.WHITE);
		panel.add(chckbxSomeValuesFrom);
		
		chckbxCompleteClasses = new JCheckBox("complete classes");
		chckbxCompleteClasses.setBackground(Color.WHITE);
		panel.add(chckbxCompleteClasses);
		
		JButton btnAction = new JButton("Complete Knowledge");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(chckbxCreateIndAuto.isSelected()) // Create individuals automatically
				{
					if(chckbxMaxCardinality.isSelected())
					{
						//Doesn't need
					}
					
					if(chckbxMinCardinality.isSelected())
					{
						ArrayList<DtoDefinitionClass> dtoMinRelationsList = search.GetMinRelations(WinHome.model );
						WinHome.model = factoryIndividuals.CreateIndividualsForMinRestriction(dtoMinRelationsList, WinHome.model );
					}
					
					if(chckbxExactlyCardinality.isSelected())
					{
						ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = search.GetExactlyRelations(WinHome.model);
						WinHome.model = factoryIndividuals.CreateIndividualsForExactlyRestriction(dtoExactlyRelationsList, WinHome.model);
					}
					
					if(chckbxSomeValuesFrom.isSelected())
					{
						ArrayList<DtoDefinitionClass> dtoSomeRelationsList = search.GetSomeRelations(WinHome.model);		
						WinHome.model = factoryIndividuals.CreateIndividualsForSomeRelations(dtoSomeRelationsList, WinHome.model);
					}
					
					if(chckbxCompleteClasses.isSelected())
					{
						//ArrayList<DtoCompleteClass> dtoCompleteClasses = search.GetCompleteClasses(WinHome.model);
						//ArrayList<String> disjointClassOfClassD = search.GetDisjointClassesOf(NS + "D", model);
						/*
						 * Create the options
						 * */
					}					
					
				} else {							//  Create one by one
					
					//Initialize the semaphore
					//sem = new Semaphore(1, true);
					
					if(chckbxMinCardinality.isSelected())
					{
						ArrayList<DtoDefinitionClass> dtoMinRelationsList = search.GetMinRelations(WinHome.model);
						//
					}
					
					if(chckbxExactlyCardinality.isSelected())
					{
						ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = search.GetExactlyRelations(WinHome.model);
						//WinHome.model = factoryIndividuals.CreateIndividualsForExactlyRestriction(dtoExactlyRelationsList, WinHome.model);
					}
					
					if(chckbxSomeValuesFrom.isSelected())
					{
						System.out.println("Entrou 1");
						ArrayList<DtoDefinitionClass> dtoSomeRelationsList = search.GetSomeRelations(WinHome.model);
						
						for (DtoDefinitionClass dto : dtoSomeRelationsList) 
						{
							String source = dto.Source;
							String relation = dto.Relation;
							String target = dto.Target;
							
							ArrayList<String> listInstances = search.GetInstancesFromClass(WinHome.model, source);
							
							for (int i = 0; i < listInstances.size(); i++)							
							{	
								
							}
							//Create the processes
							/*ProcessadorThread[] process = new ProcessadorThread[listInstances.size()];
							
							for (int i = 0; i < listInstances.size(); i++)							
							{						        
								String individualSource = listInstances.get(i);
								boolean existInstanceTarget = search.CheckExistInstanceTarget(WinHome.model, individualSource, relation, target);
								if(existInstanceTarget){
									
									//Do nothing
									System.out.println("---- Faz nada ");
									
								} else {
									
									WinInstanceSomeValues frame;
								
									
									/*
									  Cada instancia vai ter a sua propria thread
									  
									  O acquire() sem nenhum parametro implica que você quer 1 permissão desse semáforo. Se o semáforo tiver menos que 1 permissão disponível,
									  a thread vai ficar parada nesse ponto até que o semaforo tenha o numero de permissões pedidas (1) 

									  O release() sem parametro implica que vc quer liberar 1 permissão desse semaforo. Ou seja, no momento que voce da release vc acorda as
									  threads que estão dormindo aguardando essa permissão
									  
									 *
									frame = new WinInstanceSomeValues(dto, listInstances);
									process[i] = new ProcessadorThread(i, sem, frame);
									process[i].start(); /* Call the method run() *
									
														
								}						
							}	*/					
						}					
					}
				}				
			}	// End function
		});
		btnAction.setBounds(91, 299, 205, 23);
		contentPane.add(btnAction);
		
		JButton btnSelectAll = new JButton("Select All");
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectAll();
			}
		});
		btnSelectAll.setBounds(160, 179, 101, 23);
		contentPane.add(btnSelectAll);
		
		JButton btnDeselectAll = new JButton("Desselect All");
		btnDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeselectAll();
			}
		});
		btnDeselectAll.setBounds(271, 179, 99, 23);
		contentPane.add(btnDeselectAll);
	}
	
	public void SelectAll()
	{
        chckbxCompleteClasses.setSelected(true);
        chckbxExactlyCardinality.setSelected(true);
        chckbxMaxCardinality.setSelected(true);
        chckbxMinCardinality.setSelected(true);
        chckbxSomeValuesFrom.setSelected(true);
	}
	
	public void DeselectAll()
	{
        chckbxCompleteClasses.setSelected(false);
        chckbxExactlyCardinality.setSelected(false);
        chckbxMaxCardinality.setSelected(false);
        chckbxMinCardinality.setSelected(false);
        chckbxSomeValuesFrom.setSelected(false);
	}
}
