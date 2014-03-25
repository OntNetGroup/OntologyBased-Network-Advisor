package UserInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ComponentOrientation;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class FrameTeste extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameTeste frame = new FrameTeste();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameTeste() {
		setResizable(false);
		setMaximumSize(new Dimension(800, 600));
		setMinimumSize(new Dimension(530, 360));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 665, 374);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(300, 300));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panelExistRelations = new JPanel();
		panelExistRelations.setAutoscrolls(true);
		panelExistRelations.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Existent Relation with instance(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblNewLabel_1 = new JLabel("Ind -> hasRelation min 5 -> [?]");
		
		JLabel lblYouHave = new JLabel("You have 1/5 relations.");
		lblYouHave.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton btnAddRelation = new JButton("Add Relation");
		btnAddRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panelExistRelations, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnAddRelation, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblYouHave, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(20)
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addGap(18)
					.addComponent(panelExistRelations, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblYouHave, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnAddRelation)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		panelExistRelations.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panelExistRelations.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JList listExistInstances = new JList();
		listExistInstances.setModel(new AbstractListModel() {
			String[] values = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panel.add(listExistInstances);
		contentPane.setLayout(gl_contentPane);
		
	}
}
