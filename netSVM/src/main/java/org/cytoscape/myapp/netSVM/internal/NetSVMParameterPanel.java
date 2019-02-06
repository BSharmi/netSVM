package org.cytoscape.myapp.netSVM.internal;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;

public class NetSVMParameterPanel extends JPanel {

	public JLabel jLabel_top_nodes;
	public JLabel jLabel_cross_validation_fold;
	public JTextField jTextField_top_nodes;
	public JTextField jTextField_cross_validation_fold;
	public JPanel jPanel_Option;
	
	public NetSVMParameterPanel() {
		jLabel_top_nodes = new javax.swing.JLabel();
		jTextField_top_nodes = new javax.swing.JTextField();
		jLabel_cross_validation_fold = new javax.swing.JLabel();
		jTextField_cross_validation_fold = new javax.swing.JTextField();
    	

    	jPanel_Option = new javax.swing.JPanel();
    	jPanel_Option.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder
				(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 12), 
				new java.awt.Color(0, 102, 102)), "Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
				new java.awt.Font("Georgia", 1, 12), new java.awt.Color(0, 102, 102)));
    	
    	     
    	jLabel_top_nodes.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_top_nodes.setText("Top Num Genes to Select: ");    	
    	jLabel_cross_validation_fold.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_cross_validation_fold.setText("# of cross validation folds: ");    	
    	jTextField_top_nodes.setMaximumSize(new java.awt.Dimension(6, 20));
    	jTextField_top_nodes.setText("100");
    	jTextField_cross_validation_fold.setMaximumSize(new java.awt.Dimension(6, 20));
    	jTextField_cross_validation_fold.setText("5");
    	

    	
    	javax.swing.GroupLayout OptionLayout = new javax.swing.GroupLayout(jPanel_Option);
    	jPanel_Option.setLayout(OptionLayout);
    	OptionLayout.setHorizontalGroup(
    			OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)    				  				
    				.addGroup(OptionLayout.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_top_nodes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(200)
    						.addComponent(jTextField_top_nodes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))  
    				.addGroup(OptionLayout.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_cross_validation_fold, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(200)
    						.addComponent(jTextField_cross_validation_fold, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))		
    				.addGap(0, 21, Short.MAX_VALUE)
    	);
    	
    	OptionLayout.setVerticalGroup(
    			OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    			.addGroup(OptionLayout.createSequentialGroup()     									
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_top_nodes)
    							.addComponent(jTextField_top_nodes)) 
    			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    	    	.addGroup(OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    	    					.addComponent(jLabel_cross_validation_fold)
    	    					.addComponent(jTextField_cross_validation_fold))  				
    			)
    	);
    	
		this.add(jPanel_Option, BorderLayout.NORTH);
	}	
	
}
