package UserInterface;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.hp.hpl.jena.ontology.OntModel;
import Business.FactoryModel;
import Domain.IFactory;
import Domain.IRepository;

import java.awt.Point;
import java.awt.TextArea;
import java.awt.Color;
import java.awt.Toolkit;

public class AplicationOld extends JFrame implements ActionListener {
	/*
	 * 
	 * Program class
	 * 
	 * */

	private JPanel contentPane;
	private JDesktopPane jdPane;
	private static AplicationOld a;
	
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
	public static String Log = "";
	public static IFactory Factory;
	public static IRepository Repository;
	
	public static AplicationOld getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		if (a==null)
			a = new AplicationOld();
		return a;
	}
	
	public static JPanel getPanel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		return getInstance().contentPane;
	}
	
	public static JDesktopPane getDesktopPanel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		return getInstance().jdPane;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Factory = new FactoryModel();
					Repository = Factory.GetRepository();
					
					AplicationOld frame = getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public AplicationOld() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 787, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	    this.getContentPane().setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize());  
	    this.pack();  
	    this.setResizable(false);
	  
	    SwingUtilities.invokeLater(new Runnable() {  
	      public void run() {  
	        Point p = new Point(0, 0);  
	        SwingUtilities.convertPointToScreen(p, getContentPane());  
	        Point l = getLocation();  
	        l.x -= p.x;  
	        l.y -= p.y;  
	        setLocation(l);  
	      }  
	    });
		
		this.setResizable(false);			//Block the resize window property
		this.setLocationRelativeTo(null);	//Position relative to center
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.updateComponentTreeUI(this);
		
		//JDesktopPane
		jdPane = new JDesktopPane();
		
		//TextArea
		
        textArea = new TextArea();
        textArea.setEnabled(false);
        textArea.setBackground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setBounds(518, 52, 253, 366);
        //contentPane.add(textArea);
		
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
        
        contentPane.add(menuBar);
        
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
	        	
	        	//JInternalFrameOptions frameOption = new JInternalFrameOptions(NS,model);
	        	//try {
				//	getPanel().add(frameOption);
				//} catch (ClassNotFoundException | InstantiationException
				//		| IllegalAccessException
				//		| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
				//	e1.printStackTrace();
				//}
	        	//frameOption.setVisible(true);
				
				textArea.setText(textArea.getText() + "Loaded file: "+ inputFileName);
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
	
	private static final long serialVersionUID = 1L;
}
