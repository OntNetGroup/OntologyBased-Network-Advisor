package SwingUserInterface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import com.hp.hpl.jena.ontology.OntModel;

import Business.ManagerInstances;
import Business.Search;
import Domain.DtoDefinitionClass;
import Domain.EnumPropertyType;
import Domain.Instance;

public class IndividualFrameCardinality extends JFrame
                      implements ListSelectionListener, ActionListener {

	/*
	 * Class to create frame for instances with cardinality restrictions
	 */
	
    private static final String addString = "Add";
    private static final String editString = "Edit";
    private static final String removeString = "Remove";
    private static final String closeString = "Close";   
    
    private JList list;
    private DefaultListModel listModel;
    private JButton removeButton;
    private JButton addButton;
    private JButton editButton;
    private JButton closeButton; 
    private JTextField instanceName;
    
    /* List of instances to create on close button click*/
    private ArrayList<Instance> listInstancesInRelation;
    private DtoDefinitionClass dto;
	private Search search;
	private OntModel model;
	private ManagerInstances managerInstances;

    public IndividualFrameCardinality(OntModel model, Search search, DtoDefinitionClass dto, Instance instance, EnumPropertyType propertyType) {
    	
    	this.dto = dto;
    	this.search = search;
    	this.model = model;
    	this.managerInstances = new ManagerInstances(search, model);
    	ArrayList<String> listInstancesName = this.search.GetInstancesOfTargetWithRelation(model, instance.URI, dto.Relation, dto.Target);
    	
    	//populate the list of instances
    	this.listInstancesInRelation = managerInstances.getIntersectionOf(Aplication.ListAllInstances, listInstancesName);
    	
    	JPanel panelContent = new JPanel(new BorderLayout());
    	this.setContentPane(panelContent);
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel();
        for (Instance i : this.listInstancesInRelation) {
        	listModel.addElement(i.URI);
		}
        
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());
        
        editButton = new JButton(editString);
        editButton.setActionCommand(editString);
        editButton.addActionListener(new EditListener());
        
        closeButton = new JButton(closeString);
        closeButton.addActionListener(this);

        instanceName = new JTextField(20);
        instanceName.addActionListener(addListener);
        instanceName.getDocument().addDocumentListener(addListener);
        String name = listModel.getElementAt(
                              list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(editButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(instanceName);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(addButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(100));
        buttonPane.add(closeButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        panelContent.add(listScrollPane, BorderLayout.CENTER);
        panelContent.add(buttonPane, BorderLayout.PAGE_END);
        
        //Display the window.
        this.pack();
        this.setVisible(true);
    }

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable removing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }
    
    class EditListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {        	

            //This method can be called only if
            //there's a valid selection
            //so go ahead and edit whatever's selected.
            int index = list.getSelectedIndex();
            System.out.println("Clicou no edit " + index);
            //listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable editing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    //index--;
                	System.out.println("Clicou no edit");
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = instanceName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                instanceName.requestFocusInWindow();
                instanceName.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(instanceName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            instanceName.requestFocusInWindow();
            instanceName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable remove button.
                removeButton.setEnabled(false);

            } else {
            //Selection, enable the remove button.
                removeButton.setEnabled(true);
            }
        }
    }
    
    @Override
	public void actionPerformed(ActionEvent e) 
    {
		if(e.getSource() == closeButton)
		{
			this.dispose();
		}
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        //JFrame newContentPane = new IndividualFrameCardinality();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
