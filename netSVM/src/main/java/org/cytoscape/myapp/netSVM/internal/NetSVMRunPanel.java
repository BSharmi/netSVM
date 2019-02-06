package org.cytoscape.myapp.netSVM.internal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

public class NetSVMRunPanel extends JPanel {
	// drawing
	private CyNetworkManager cyNetworkManagerServiceRef = null;
	private CyNetworkNaming cyNetworkNamingServiceRef = null;
	private CyNetworkFactory cyNetworkFactoryServiceRef = null;
	private CyNetworkViewFactory networkViewFactory = null;
	private CyNetworkViewManager networkViewManager = null;
	
	private DialogTaskManager dialogTaskManager = null;
	
	private VisualMappingManager vmmServiceRef = null;
	private VisualStyleFactory vsfServiceRef = null;
	private VisualMappingFunctionFactory vmfFactoryC = null;
	private VisualMappingFunctionFactory vmfFactoryD = null;
	private VisualMappingFunctionFactory vmfFactoryP = null;
	private CyLayoutAlgorithmManager clamRef = null;

    // panel references
    private NetSVMParameterPanel parameterPanel = null;
    private NetSVMDataPanel dataPanel = null;
    private NetSVMConfigurePanel NetSVMConfigurePanel = null;
    
    // cv fields
    public JPanel jPanel_Output_cv;
    public JLabel jLabel_output_directory;
    public JTextField jTextField_output_directory;
    public JButton jButton_output_directory;    
    public JLabel jLabel_spe;
    public JTextField jTextField_spe;
    public JLabel jLabel_sen;
    public JTextField jTextField_sen;
    public JLabel jLabel_auc;
    public JTextField jTextField_auc;
    public JButton jButton_start;    
    
    // independent fields
    public JPanel jPanel_Output_indeptest;
    public JLabel jLabel_spe_test;
    public JTextField jTextField_spe_test;
    public JLabel jLabel_sen_test;
    public JTextField jTextField_sen_test;
       
    
    public File currentDir;
    
    public NetSVMRunPanel(CyNetworkManager cyNetworkManagerServiceRef, 
			CyNetworkNaming cyNetworkNamingServiceRef, 
			CyNetworkFactory cyNetworkFactoryServiceRef,
			DialogTaskManager dialogTaskManager, 
			CyNetworkViewFactory networkViewFactory,
			CyNetworkViewManager networkViewManager, 
			VisualMappingManager vmmServiceRef,
			VisualStyleFactory vsfServiceRef, 
			VisualMappingFunctionFactory vmfFactoryC, 
			VisualMappingFunctionFactory vmfFactoryD, 
			VisualMappingFunctionFactory vmfFactoryP,
			CyLayoutAlgorithmManager clamRef, 
			NetSVMParameterPanel parameterPanel, NetSVMDataPanel dataPanel, 
			NetSVMConfigurePanel NetSVMConfigurePanel) {
		
		this.cyNetworkFactoryServiceRef = cyNetworkFactoryServiceRef;
		this.cyNetworkManagerServiceRef = cyNetworkManagerServiceRef;
		this.cyNetworkNamingServiceRef = cyNetworkNamingServiceRef;
		this.networkViewFactory = networkViewFactory;
		this.dialogTaskManager = dialogTaskManager;
		this.networkViewManager = networkViewManager;
		this.vmmServiceRef = vmmServiceRef;
		this.vsfServiceRef = vsfServiceRef;
		this.vmfFactoryC = vmfFactoryC;
		this.vmfFactoryD = vmfFactoryD;
		this.vmfFactoryP = vmfFactoryP;
		this.clamRef = clamRef;
		this.parameterPanel = parameterPanel;
		this.dataPanel = dataPanel;
		this.NetSVMConfigurePanel = NetSVMConfigurePanel;
		
		// cv fields
		jPanel_Output_cv = new javax.swing.JPanel();
		jLabel_spe = new javax.swing.JLabel();
		jTextField_spe = new javax.swing.JTextField();
		jLabel_sen = new javax.swing.JLabel();
		jTextField_sen = new javax.swing.JTextField();
		jLabel_auc = new javax.swing.JLabel();
		jTextField_auc = new javax.swing.JTextField();
		jButton_start = new javax.swing.JButton(); 
		
		// independent fields
		jPanel_Output_indeptest = new javax.swing.JPanel();
		jLabel_spe_test = new javax.swing.JLabel();
		jTextField_spe_test = new javax.swing.JTextField();
		jLabel_sen_test = new javax.swing.JLabel();
		jTextField_sen_test = new javax.swing.JTextField();
		jLabel_output_directory = new javax.swing.JLabel();
		jTextField_output_directory = new javax.swing.JTextField();
		jButton_output_directory = new javax.swing.JButton();
    	
		
    	
		jPanel_Output_cv.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder
				(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 12), 
				new java.awt.Color(0, 102, 102)), "Cross validation Output", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
				new java.awt.Font("Georgia", 1, 12), new java.awt.Color(0, 102, 102)));
    	
    	jPanel_Output_indeptest.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder
				(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Georgia", 1, 12), 
				new java.awt.Color(0, 102, 102)), " Independent test Output", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, 
				new java.awt.Font("Georgia", 1, 12), new java.awt.Color(0, 102, 102)));
    	
    	
    	jLabel_output_directory.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_output_directory.setText("Output directory:");
    	jTextField_output_directory.setMaximumSize(new java.awt.Dimension(6, 20));    	
    	    	
    	jButton_output_directory.setText("Select");
    	jButton_output_directory.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(java.awt.event.ActionEvent evt) {
    			jButton_OutputActionPerformed(evt);
    		}
    	});
    	
    	Color readonlyfield_color = Color.GRAY;
    	
    	// cv fields 
    	jLabel_spe.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_spe.setText("Specificity:");
    	jTextField_spe.setEditable(false);
    	jTextField_spe.setBackground(readonlyfield_color);
    	jTextField_spe.setMaximumSize(new java.awt.Dimension(6, 20));    	
    	
    	jLabel_sen.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_sen.setText("Sensitivity:");
    	jTextField_sen.setEditable(false);
    	jTextField_sen.setBackground(readonlyfield_color);
    	jTextField_sen.setMaximumSize(new java.awt.Dimension(6, 20));
    	    	
    	jLabel_auc.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_auc.setText("AUC:");
    	jTextField_auc.setEditable(false);
    	jTextField_auc.setBackground(readonlyfield_color);
    	jTextField_auc.setMaximumSize(new java.awt.Dimension(6, 20));
    	
    	
    	// independent fields
    	jLabel_spe_test.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_spe_test.setText("Specificity:");
    	jTextField_spe_test.setEditable(false);
    	jTextField_spe_test.setBackground(readonlyfield_color);
    	jTextField_spe_test.setMaximumSize(new java.awt.Dimension(6, 20));
    	
    	jLabel_sen_test.setFont(new java.awt.Font("Tahoma", 0, 12));
    	jLabel_sen_test.setText("Sensitivity:");
    	jTextField_sen_test.setEditable(false);
    	jTextField_sen_test.setBackground(readonlyfield_color);
    	jTextField_sen_test.setMaximumSize(new java.awt.Dimension(6, 20));
    	
    	// start button
    	jButton_start.setText("Start netSVM");
    	jButton_start.addActionListener(new java.awt.event.ActionListener() {
    		public void actionPerformed(java.awt.event.ActionEvent evt) {
    			jButton_StartActionPerformed(evt);
    		}
    	});    	 	
    	
    	// cv group
    	javax.swing.GroupLayout OutputLayout_cv = new javax.swing.GroupLayout(jPanel_Output_cv);
    	jPanel_Output_cv.setLayout(OutputLayout_cv);
    	OutputLayout_cv.setHorizontalGroup(
    			OutputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    				.addGroup(OutputLayout_cv.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_spe, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(150)
    						.addComponent(jTextField_spe, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				.addGroup(OutputLayout_cv.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_sen, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(150)
    						.addComponent(jTextField_sen, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				.addGroup(OutputLayout_cv.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_auc, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(150)
    						.addComponent(jTextField_auc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				  						  				    								
    	);
    	
    	// independent group
    	javax.swing.GroupLayout OutputLayout_indeptest = new javax.swing.GroupLayout(jPanel_Output_indeptest);
    	jPanel_Output_indeptest.setLayout(OutputLayout_indeptest);
    	OutputLayout_indeptest.setHorizontalGroup(
    			OutputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    				.addGroup(OutputLayout_indeptest.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_output_directory, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(18, 18, 18)
    						.addComponent(jTextField_output_directory, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(27, 27, 27)
    						.addComponent(jButton_output_directory,javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				.addGroup(OutputLayout_indeptest.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_spe_test, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(18, 18, 18)
    						.addComponent(jTextField_spe_test, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				.addGroup(OutputLayout_indeptest.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jLabel_sen_test, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addGap(18, 18, 18)
    						.addComponent(jTextField_sen_test, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
    				.addGroup(OutputLayout_indeptest.createSequentialGroup()
    						.addContainerGap()
    						.addComponent(jButton_start, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
    				  		 				    								
    	);
    	
    	
    	// cv group
    	OutputLayout_cv.setVerticalGroup(
    			OutputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    			.addGroup(OutputLayout_cv.createSequentialGroup()
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_spe)
    							.addComponent(jTextField_spe))	
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_sen)
    							.addComponent(jTextField_sen))	
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_cv.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_auc)
    							.addComponent(jTextField_auc))		    					 					   								
    			)
    	);
    	
		//this.add(jPanel_Output_cv, BorderLayout.WEST);
		
		// independent test
		OutputLayout_indeptest.setVerticalGroup(
				OutputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    			.addGroup(OutputLayout_indeptest.createSequentialGroup()
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_output_directory)
    							.addComponent(jTextField_output_directory)
    							.addComponent(jButton_output_directory))
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_spe_test)
    							.addComponent(jTextField_spe_test))	
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addGroup(OutputLayout_indeptest.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    							.addComponent(jLabel_sen_test)
    							.addComponent(jTextField_sen_test))	
    					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    					.addComponent(jButton_start)   
    									   								
    			)
    	);
    	
		//this.add(jPanel_Output_indeptest, BorderLayout.EAST);
		Box right = Box.createVerticalBox();
		right.add(jPanel_Output_cv);
		right.add(jPanel_Output_indeptest);
		this.add(right, BorderLayout.NORTH);
	}

	private void jButton_OutputActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setCurrentDirectory(currentDir);
		int returnVal = jFileChooser.showOpenDialog(NetSVMRunPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = jFileChooser.getSelectedFile();
			currentDir = jFileChooser.getCurrentDirectory();
			jTextField_output_directory.setText(selectedFile.getPath());
		}
	}
	
	private void jButton_StartActionPerformed(java.awt.event.ActionEvent evt) {
		NetSVM_Experiment NetInstance = null;
		String outputfile = jTextField_output_directory.getText() + "/network.txt";
		String outputdir = jTextField_output_directory.getText() ;
		
		// run NetSVM task
		try {
			NetInstance = new NetSVM_Experiment(cyNetworkManagerServiceRef,
					cyNetworkNamingServiceRef,cyNetworkFactoryServiceRef,
					networkViewFactory, networkViewManager,
					vmmServiceRef, vsfServiceRef, vmfFactoryC,
					vmfFactoryD, vmfFactoryP, clamRef,
					outputfile, 
					dataPanel.jTextField_file_early_idx, 
					dataPanel.jTextField_file_late_idx, 
					dataPanel.jTextField_file_exp_data, 
					dataPanel.jTextField_file_ppi, 
					dataPanel.jTextField_file_gid,
					dataPanel.jTextField_file_early_idx_indeptest,
					dataPanel.jTextField_file_late_idx_indeptest,
					dataPanel.jTextField_file_exp_data_indeptest,
					jTextField_output_directory, 
				    parameterPanel.jTextField_top_nodes, parameterPanel.jTextField_cross_validation_fold,
					dataPanel, parameterPanel,jTextField_spe,jTextField_sen,jTextField_auc,outputdir,dataPanel.jTextField_file_network, jTextField_spe_test, jTextField_sen_test);	
			
			dialogTaskManager.execute(new TaskIterator(NetInstance));			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}	

}
