package SwingUserInterface;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.hp.hpl.jena.ontology.OntModel;

import Business.FactoryInstances;
import Business.FactoryModel;
import Business.ManagerInstances;
import Business.Search;
import Domain.DtoDefinitionClass;
import Domain.IFactory;
import Domain.IRepository;
import Domain.Instance;
import Domain.EnumPropertyType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class Aplication extends JFrame implements ListSelectionListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static DefaultListModel listModelSome;
	
	public static JList list;
	
	public static JPanel paneDetails;
    private JScrollPane listScrollPane;
    private JScrollPane paneScrollDetails;
    
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem btnLoadFile;
	private JMenuItem btnSaveFile;
	private JMenuItem btnExitAction;
	private JMenuItem btnAbout;
	
	public static IFactory Factory;
	public static IRepository Repository;
	public static OntModel Model;	
	public static Search Search;
	public static String NS;
	public static FactoryInstances FactoryInstances;
	public static ManagerInstances ManagerInstances;	
	public static ArrayList<Instance> ListAllInstances;
	public static Instance instance; // Instance selected
	
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
				createGUI();
            }
        });
    }
	
    public Aplication() {
		
        ImageIcon icon = createImageIcon("./img/star.png");
		
       /*
        -----------------
        ----- Pane ------
        -----------------
       */
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 787, 480);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		this.setContentPane(topPanel);		
		
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(30, 10, 10, 10)); 
        
        /*
         -----------------
         ----- Menu ------
         -----------------
        */
        
        //MenuBar
		
        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 2000, 21);
	
		//MenuBar items
	
	  	fileMenu = new JMenu("File");
	  	helpMenu = new JMenu("Help");               
	  	menuBar.add(fileMenu);
	  	menuBar.add(helpMenu);
	
	  	//Menu items
	  
	  	btnLoadFile = new JMenuItem("Load file");
	  	btnSaveFile = new JMenuItem("Save As");
	  	btnExitAction = new JMenuItem("Close");
	  	fileMenu.add(btnLoadFile);
	  	fileMenu.add(btnSaveFile);
	  	fileMenu.add(btnExitAction);
	  	btnAbout = new JMenuItem("About");
	  	helpMenu.add(btnAbout);
	  
	    //Add MenuBar
	  
	  	topPanel.add(menuBar);
	  
	  	//Add Action Listeners
		
		btnLoadFile.addActionListener(this);
		btnSaveFile.addActionListener(this);
		btnAbout.addActionListener(this);
		btnExitAction.addActionListener(this);
        
        /*
        ------------------
        ------ Panel -----
        ------------------
        */        
        
        JPanel panelSome = new JPanel(new GridLayout(1, 0, 0, 0));//makeTextPanel("Panel Some");
        tabbedPane.addTab("Object Properties Tab", icon, panelSome,
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        //Create List
        
        listModelSome = new DefaultListModel();
        
        //Create the list and put it in a scroll pane.
        
        list = new JList(listModelSome);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        
        // Scroll list event
        
        list.addListSelectionListener(new ListSelectionListener() {
      	  public void valueChanged(ListSelectionEvent evt) {
      		  
      	    if (!evt.getValueIsAdjusting()) {
      	    	
      	    	System.out.println("Clicou para instancia " + list.getSelectedValue().toString());
      	    	paneDetails.removeAll();
      	    	String selectedInstance = list.getSelectedValue().toString();
      	    	Aplication.SelectInstance(selectedInstance);    
      	    }
      	  }
		
      	});
        
        // Panel with list
        
        listScrollPane = new JScrollPane(list);
        panelSome.add(listScrollPane);
        
        // Panel with details object
        
		paneDetails = new JPanel();
		paneDetails.setLayout(new BoxLayout(paneDetails, BoxLayout.Y_AXIS));
		paneScrollDetails = new JScrollPane(paneDetails);
		panelSome.add(paneScrollDetails);        
        
        // ------------------------- // ----------------------- //
        
      
        topPanel.add(tabbedPane, BorderLayout.CENTER);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected static void SelectInstance(String selectedInstance) {
			
  	    	instance = ManagerInstances.getInstance(ListAllInstances, selectedInstance);
  	    	
  	    	ImageIcon leftButtonIcon = new ImageIcon("img/search.png");   	    	
  	    	JButton btnViewInstance = new JButton("View instance", leftButtonIcon);
  	    	btnViewInstance.setVerticalTextPosition(AbstractButton.CENTER);
  	    	btnViewInstance.setHorizontalTextPosition(AbstractButton.RIGHT); //aka LEFT, for left-to-right locales
  			btnViewInstance.addActionListener(new ActionListener() {
  				public void actionPerformed(ActionEvent arg0) {
  					
  					//TODO: SHOW GRAPH
  					// Show the graph  
  					// graphIndividual = new GraphIndividual(Aplication.model, this.search, individualSource, dto.Source);
  	      			
  				}
  			});
  			paneDetails.add(btnViewInstance);
			
  			//SOME
			for (DtoDefinitionClass dto : instance.ListSome) 
			{	
				if(dto != null)
				{
					JPanel p1 = new JPanel();
  	      			p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
  	      			p1.setBorder(new LineBorder(new Color(0, 0, 0)));
  	      			paneDetails.add(p1);
  	      			
  	      			JLabel lblJ = new JLabel(dto.Relation.split("#")[1] + " -> [?] (" + dto.Target.split("#")[1] + ")");
  	      			p1.add(lblJ);
  	      			
  	      			JLabel lblRelationName = new JLabel("Relation name: " + dto.Relation.split("#")[1]);
	      			p1.add(lblRelationName);
	      			
	      			JLabel lblTargetName = new JLabel("Target name: [?]");
	      			p1.add(lblTargetName);
	      			
	      			JLabel lblTargetClass = new JLabel("Target Class: " + dto.Target.split("#")[1]);
	      			p1.add(lblTargetClass);
  	      			
	  	      		JButton btnComplete = new JButton("Complete");
	  	      		btnComplete.setName(dto.toString()); // Used to provide values to bottom click event
	  	      		
	  	      		// Bottom complete event
	  	      		
	  	      		btnComplete.setVerticalTextPosition(AbstractButton.CENTER);
	  	      		btnComplete.setHorizontalTextPosition(AbstractButton.RIGHT); //aka LEFT, for left-to-right locales
	  	      		btnComplete.addActionListener(new ActionListener() {
	      				public void actionPerformed(ActionEvent e) {
	      					
	      					Object source = e.getSource();  
	      					JButton btn = (JButton) source;  
	      					String dtoString = btn.getName();
	      					DtoDefinitionClass dto = DtoDefinitionClass.get(instance.ListSome, dtoString);	      					
	      					IndividualFrame frame = new IndividualFrame(dto, instance.URI, FactoryInstances, Search);
	      					//System.out.println("caralhoooooooooooooo");   						      	      			
	      				}
	      			});
	  	      		p1.add(btnComplete);		  	      		
				}
			}
			
			//MIN
			
			for (DtoDefinitionClass dto : instance.ListMin) 
			{	
				if(dto != null)
				{
					JPanel p1 = new JPanel();
  	      			p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
  	      			p1.setBorder(new LineBorder(new Color(0, 0, 0)));
  	      			paneDetails.add(p1);
  	      			
  	      			JLabel lblJ = new JLabel(dto.Relation.split("#")[1] + " MIN " + dto.Cardinality + " -> [?] (" + dto.Target.split("#")[1] + ")");
  	      			p1.add(lblJ);
  	      			
  	      			JLabel lblRelationName = new JLabel("Relation name: " + dto.Relation.split("#")[1]);
	      			p1.add(lblRelationName);
	      			
	      			JLabel lblTargetName = new JLabel("Target name: [?]");
	      			p1.add(lblTargetName);
	      			
	      			JLabel lblTargetClass = new JLabel("Target Class: " + dto.Target.split("#")[1]);
	      			p1.add(lblTargetClass);
  	      			
	  	      		JButton btnComplete = new JButton("Complete");
	  	      		btnComplete.setName(dto.toString()); // Used to provide values to bottom click event
	  	      		
	  	      		// Bottom complete event
	  	      		
	  	      		btnComplete.setVerticalTextPosition(AbstractButton.CENTER);
	  	      		btnComplete.setHorizontalTextPosition(AbstractButton.RIGHT); //aka LEFT, for left-to-right locales
	  	      		btnComplete.addActionListener(new ActionListener() {
	      				public void actionPerformed(ActionEvent e) {
	      					
	      					Object source = e.getSource();  
	      					JButton btn = (JButton) source;  
	      					String dtoString = btn.getName();
	      					DtoDefinitionClass dto = DtoDefinitionClass.get(instance.ListMin, dtoString);
	      					IndividualFrameCardinality frame = new IndividualFrameCardinality(Model, Search, dto, instance, EnumPropertyType.MIN);
	      					//System.out.println("caralhoooooooooooooo");   						      	      			
	      				}
	      			});
	  	      		p1.add(btnComplete);		  	      		
				}
			}
  			
  			//Refresh the panel
  			
  			paneDetails.validate();
  			paneDetails.repaint();  
			
		
		
	}

	protected JComponent makeTextPanel(String text) {
    	
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Aplication.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private static void createGUI() {
    	
		Factory = new FactoryModel();
		Repository = Factory.GetRepository();		
        JFrame frame = new Aplication();
        frame.pack();
        frame.setVisible(true);
    }

    public static void UpdateLists() {
    	
		// Gets
		
		ArrayList<DtoDefinitionClass> dtoSomeRelationsList = Search.GetSomeRelations(Model);
		ArrayList<DtoDefinitionClass> dtoMinRelationsList = Search.GetMinRelations(Model);
		//ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = search.GetExactlyRelations(Model);
		
		// Organize data
		
		ManagerInstances.AddInstanceAndRelations(ListAllInstances, dtoSomeRelationsList,EnumPropertyType.SOME);
		ManagerInstances.AddInstanceAndRelations(ListAllInstances, dtoMinRelationsList,EnumPropertyType.MIN);
		//managerInstances.AddInstanceAndRelations(ListAllInstances, dtoExactlyRelationsList,EnumPropertyType.EXACTLY);		
		
        //Create List
        
        listModelSome = new DefaultListModel();
        for (Instance instance : ListAllInstances) {
			if(instance.ListSome.size() > 0 || instance.ListMin.size() > 0 || instance.ListMax.size() > 0 || instance.ListExactly.size() > 0 || instance.ListComplete.size() > 0)
			{
				listModelSome.addElement(instance.URI);
			}
		}        
        list.removeAll();
        list.setModel(listModelSome);        
        list.setSelectedIndex(0);
        
        for (Instance i : ListAllInstances) {
			i.print();
		}
        
    }
    
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnLoadFile)
		{
			String inputFileName = "";
			
			JFileChooser file = new JFileChooser();
	        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        int i= file.showOpenDialog(null);
	        if (i==1){
	        	inputFileName = "";
	        } else {
	        	
	        	File arquivo = file.getSelectedFile();
	        	inputFileName = arquivo.getPath();
	        	
	        	this.Model = Repository.Open(inputFileName);
	        	this.NS = Repository.getNameSpace(Model);        	
	    		this.Search = new Search(NS);
	    		this.FactoryInstances = new FactoryInstances(this.Search);
	    		this.ManagerInstances = new ManagerInstances(Search, Model);
	    		this.ListAllInstances = new ArrayList<Instance>();
	    		
	    		UpdateLists();
	        	
	        }
				
		} else if(e.getSource() == btnSaveFile)
		{
			Repository.SaveWithDialog(Model);
			
		} else if(e.getSource() == btnAbout)
		{
			//RemoverTodasRec();
			
		} else if(e.getSource() == btnExitAction)
		{
			System.exit(0);
		} 
	}
}
