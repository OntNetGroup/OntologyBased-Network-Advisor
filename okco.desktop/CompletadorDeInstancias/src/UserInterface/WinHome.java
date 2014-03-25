package UserInterface;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Business.FactoryModel;
import Domain.IFactory;
import Domain.IRepository;

import com.hp.hpl.jena.ontology.OntModel;
import javax.swing.JButton;

public class WinHome extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JLabel lblResults;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem btnLoadFile;
	private JMenuItem btnSaveFile;
	private JMenuItem btnExitAction;
	private JMenuItem btnAbout;
	public static TextArea textArea;
	
	public static OntModel model;
	public static String NS;
	public static IFactory Factory;
	public static IRepository Repository;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Factory = new FactoryModel();
					Repository = Factory.GetRepository();
					
					WinHome frame = new WinHome();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public WinHome()  
    {     
		//Container
		
		this.setTitle("Completador de Conhecimento");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 696, 422);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		this.setResizable(false);			//Block the resize window property
		this.setLocationRelativeTo(null);	//Position relative to center
		contentPane.setLayout(null);
		
		//Labels
		
		lblResults = new JLabel("Results:");
		lblResults.setBounds(155, 42, 256, 14);
		contentPane.add(lblResults);
		
		//MenuBar
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 690, 21);
		
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
        
        contentPane.add(menuBar);
        
        //TextArea
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(155, 62, 380, 160);
		contentPane.add(textArea);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//WinOption frame = new WinOption();
				//frame.setVisible(true);
			}
		});
		btnOptions.setBounds(274, 242, 162, 23);
		contentPane.add(btnOptions);
		
		//Add Action Listeners
		
		btnLoadFile.addActionListener(this);
		btnSaveFile.addActionListener(this);
        btnAbout.addActionListener(this);
        btnExitAction.addActionListener(this);
        
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
	        	
	        	model = Repository.Open(inputFileName);
	        	NS = Repository.getNameSpace(model);
				
				textArea.setText(textArea.getText() + "- Arquivo carregado.\n");
	        }
				
		} else if(e.getSource() == btnSaveFile)
		{
			Repository.SaveWithDialog(model);
			
		} else if(e.getSource() == btnAbout)
		{
			//RemoverTodasRec();
			
		} else if(e.getSource() == btnExitAction)
		{
			System.exit(0);
		} 
	}
}
